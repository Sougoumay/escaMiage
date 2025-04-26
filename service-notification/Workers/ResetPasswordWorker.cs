using RabbitMQ.Client.Events;
using RabbitMQ.Client;
using service_notification.Services;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization;
using service_notification.Modeles.dto;
using service_notification.Repositories;
using service_notification.Modeles;

namespace service_notification.Workers
{
    internal class ResetPasswordWorker : BackgroundService
    {
        private readonly ILogger<ResetPasswordWorker> _logger;
        private readonly IEmailService _emailService;
        private readonly IConfiguration _configuration;
        private IConnection _connection;
        private IChannel _channel;
        private bool _connected = false;
        private IConfigurationSection _rabbitMqSettings;
        private readonly IServiceScopeFactory _serviceScopeFactory;

        public ResetPasswordWorker(IConfiguration configuration, ILogger<ResetPasswordWorker> logger, IEmailService emailService, 
            IServiceScopeFactory serviceScopeFactory)
        {
            _configuration = configuration;
            _rabbitMqSettings = _configuration.GetSection("RabbitMq");
            _logger = logger;
            _emailService = emailService;
            _serviceScopeFactory = serviceScopeFactory;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            _logger.LogInformation("Reset Password Worker Service démarré");

            // Boucle de tentative de connexion
            while (!stoppingToken.IsCancellationRequested && !_connected)
            {
                try
                {
                    await ConfigureRabbitMQAsync();
                    _connected = true;
                    _logger.LogInformation("Connexion à RabbitMQ établie avec succès");
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Échec de connexion à RabbitMQ. Nouvelle tentative dans 10 secondes...");
                    await Task.Delay(TimeSpan.FromSeconds(10), stoppingToken);
                }
            }

            if (_connected)
            {
                try
                {
                    await ConsumeMessagesAsync(stoppingToken);
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Erreur pendant la consommation des messages");
                    _connected = false;
                }
            }
        }

        private async Task ConfigureRabbitMQAsync()
        {
            var hostName = _rabbitMqSettings["Hostname"];
            var userName = _rabbitMqSettings["Username"];
            var password = _rabbitMqSettings["Password"];
            var port = int.Parse(_rabbitMqSettings["Port"]);
            var type = _rabbitMqSettings["Type"];
            var exchange = _rabbitMqSettings["Exchange"];
            var queue = _rabbitMqSettings["ResetPasswordQueue"];
            var routingkey = _rabbitMqSettings["ResetPasswordRoutingkey"];

            var factory = new ConnectionFactory
            {
                HostName = hostName,
                UserName = userName,
                Password = password,
                Port = port
            };



            _connection = await factory.CreateConnectionAsync();
            _channel = await _connection.CreateChannelAsync();

            // Déclarer l'exchange
            await _channel.ExchangeDeclareAsync(
                exchange: exchange,
                type: type,
                durable: true,
                autoDelete: false);

            // Déclarer la queue
            await _channel.QueueDeclareAsync(
                queue: queue,
                durable: true,
                exclusive: false,
                autoDelete: false,
                arguments: null);

            // Lier la queue à l'exchange avec la routing key
            await _channel.QueueBindAsync(
                queue: queue,
                exchange: exchange,
                routingKey: routingkey
                );

            // Configuration de QoS (préfetch)
            await _channel.BasicQosAsync(0, 1, false);

            _logger.LogInformation("RabbitMQ configuration terminée");
        }

        private async Task ConsumeMessagesAsync(CancellationToken stoppingToken)
        {
            _logger.LogInformation("Démarrage de la consommation des messages");

            var consumer = new AsyncEventingBasicConsumer(_channel);

            consumer.ReceivedAsync += async (model, ea) =>
            {
                string message = null;

                try
                {
                    var body = ea.Body.ToArray();
                    message = Encoding.UTF8.GetString(body);
                    _logger.LogInformation($"Message reçu: {message}");

                    // Options de désérialisation spéciales pour gérer différents formats
                    var options = new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true,
                        DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull
                    };

                    // Essayer de désérialiser avec différents formats
                    ResetPasswordDTO resetPasswordDTO = null;

                    try
                    {

                        // Créer un scope pour accéder aux repositories
                        using (var scope = _serviceScopeFactory.CreateScope())
                        {
                            // Obtenir les repositories du scope
                            var _utilisateurRepository = scope.ServiceProvider.GetRequiredService<IUtilisateurRepository>();
                            var _notificationRepository = scope.ServiceProvider.GetRequiredService<INotificationRepository>();


                            // Essayer de désérialiser comme ResetPasswordDto
                            var resetPasswordDto = JsonSerializer.Deserialize<ResetPasswordDTO>(message, options);
                            if (resetPasswordDto != null && resetPasswordDto.Id > 0)
                            {
                                // Récupérer les données de l'utilisateur à partir du repository
                                var utilisateur = await _utilisateurRepository.GetByIdAsync(resetPasswordDto.Id);

                                if (utilisateur != null)
                                {
                                    var contenu = $@"<h1>Bonjour {utilisateur.Prenom} {utilisateur.Nom},</h1>
                                        <p>Vous avez demandé une réinitialisation de votre mot de passe.</p>
                                        <p>Utilisez le code suivant {resetPasswordDto.Token} pour confirmer que vous êtes bien le propriétaire du compte</p>
                                        <p>Ce code est valide pendant 15 minutes</p>
                                        <p>Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet email.</p>";
                                    // Envoyer l'email
                                    await _emailService.SendEmailAsync(
                                        utilisateur.Email,
                                        "Réinitialisation de votre mot de passe",
                                        contenu,
                                        true);

                                    _logger.LogInformation($"Email de réinitialisation envoyé à {utilisateur.Email}");

                                    var nouvelleNotification = new Notification
                                    {
                                        DateEnvoi = DateTime.Now,
                                        Contenu = contenu,
                                        TypeNotif = TypeNotificationEnum.MailReinitMDP,
                                        UtilisateurId = utilisateur.Id,
                                        Envoye = true
                                    };

                                    await _notificationRepository.AddAsync(nouvelleNotification);
                                    _logger.LogInformation("Notification ajoutée dans la BDD avec succès");
                                }
                                else
                                {
                                    _logger.LogWarning($"Utilisateur avec ID {resetPasswordDto.Id} non trouvé");
                                }
                            }
                        }
                    }
                    catch (JsonException)
                    {
                        _logger.LogWarning("Impossible de désérialiser directement le message, tentative avec format alternatif");                      
                    }

                    // Confirmer que le message a été traité
                    await _channel.BasicAckAsync(ea.DeliveryTag, false);
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, $"Erreur lors du traitement du message: {message}");

                    // Rejeter le message en cas d'erreur
                    await _channel.BasicNackAsync(ea.DeliveryTag, false, true);
                }
            };

            var queue = _rabbitMqSettings["ResetPasswordQueue"];

            // Commencer à consommer les messages
            string consumerTag = await _channel.BasicConsumeAsync(
                queue: queue,
                autoAck: false,
                consumer: consumer);

            _logger.LogInformation($"Consommateur enregistré avec tag: {consumerTag}");

            // Garde le service en cours d'exécution jusqu'à l'arrêt
            while (!stoppingToken.IsCancellationRequested)
            {
                await Task.Delay(1000, stoppingToken);
            }
        }

        public override async Task StopAsync(CancellationToken cancellationToken)
        {
            _logger.LogInformation("Arrêt du service Email Worker");

            try
            {
                // Fermer proprement la connexion à RabbitMQ
                _channel?.CloseAsync();
                _connection?.CloseAsync();
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors de la fermeture des connexions RabbitMQ");
            }

            await base.StopAsync(cancellationToken);
        }
    }
}
