using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using service_notification.Modeles;
using service_notification.Repositories;
using service_notification.Services;
using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace service_notification.Workers
{
    internal class BirthdayWorker : BackgroundService
    {
        private readonly ILogger<BirthdayWorker> _logger;
        private readonly IEmailService _emailService;
        private readonly IConfiguration _configuration;
        private readonly IServiceScopeFactory _serviceScopeFactory;
        private Timer _timer;

        public BirthdayWorker(IConfiguration configuration, ILogger<BirthdayWorker> logger,
            IEmailService emailService, IServiceScopeFactory serviceScopeFactory)
        {
            _configuration = configuration;
            _logger = logger;
            _emailService = emailService;
            _serviceScopeFactory = serviceScopeFactory;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            _logger.LogInformation("Service d'envoi d'emails d'anniversaire démarré");

            // Déterminer le délai initial pour la première exécution à 8h00
            var initialDelay = CalculateNextRunTime();
            _logger.LogInformation($"Première vérification des anniversaires dans {initialDelay.TotalHours:F1} heures");

            // Créer et démarrer le timer
            _timer = new Timer(async (state) => await CheckBirthdaysAsync(), null, initialDelay, TimeSpan.FromDays(1));

            // Maintenir le service en cours d'exécution
            while (!stoppingToken.IsCancellationRequested)
            {
                await Task.Delay(TimeSpan.FromMinutes(30), stoppingToken);
            }
        }

        private TimeSpan CalculateNextRunTime()
        {
            DateTime now = DateTime.Now;
            DateTime targetTime = DateTime.Today.AddHours(8); // 8h00 aujourd'hui

            // Si l'heure actuelle est après 8h00, planifier pour demain à 8h00
            if (now > targetTime)
            {
                targetTime = targetTime.AddDays(1);
            }

            return targetTime - now;
        }

        private async Task CheckBirthdaysAsync()
        {
            _logger.LogInformation("Vérification des anniversaires du jour");

            try
            {
                // Créer un scope pour accéder aux repositories
                using (var scope = _serviceScopeFactory.CreateScope())
                {
                    // Obtenir les repositories du scope
                    var utilisateurRepository = scope.ServiceProvider.GetRequiredService<IUtilisateurRepository>();
                    var notificationRepository = scope.ServiceProvider.GetRequiredService<INotificationRepository>();

                    // Obtenir la date du jour (jour et mois uniquement)
                    DateTime today = DateTime.Today;

                    // Récupérer tous les utilisateurs dont c'est l'anniversaire aujourd'hui
                    var utilisateursAnniversaire = await utilisateurRepository.GetUtilisateursAnniversaireAsync(
                        today.Month,today.Day);

                    _logger.LogInformation($"Trouvé {utilisateursAnniversaire.Count()} utilisateur(s) qui fêtent leur anniversaire aujourd'hui");

                    // Envoyer un email à chaque utilisateur
                    foreach (var utilisateur in utilisateursAnniversaire)
                    {
                        var age = today.Year - utilisateur.DateNaissance.Year;

                        // Construire le contenu de l'email
                        var contenu = $@"<h1>Joyeux Anniversaire {utilisateur.Prenom} !</h1>
                            <p>Toute l'équipe d'EscapeGame vous souhaite un merveilleux anniversaire pour vos {age} ans.</p>
                            <p>Profitez bien de cette journée spéciale !</p>";

                        // Envoyer l'email
                        await _emailService.SendEmailAsync(
                            utilisateur.Email,
                            "Joyeux Anniversaire !",
                            contenu,
                            true);

                        _logger.LogInformation($"Email d'anniversaire envoyé à {utilisateur.Email}");

                        // Enregistrer la notification dans la base de données
                        var nouvelleNotification = new Notification
                        {
                            DateEnvoi = DateTime.Now,
                            Contenu = contenu,
                            TypeNotif = TypeNotificationEnum.MailAnniversaire,
                            UtilisateurId = utilisateur.Id,
                            Envoye = true
                        };

                        await notificationRepository.AddAsync(nouvelleNotification);
                        _logger.LogInformation($"Notification d'anniversaire enregistrée pour l'utilisateur {utilisateur.Id}");
                    }
                }
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors de la vérification des anniversaires");
            }
        }

        public override async Task StopAsync(CancellationToken cancellationToken)
        {
            _logger.LogInformation("Arrêt du service d'envoi d'emails d'anniversaire");

            // Arrêter proprement le timer
            _timer?.Change(Timeout.Infinite, 0);
            _timer?.Dispose();

            await base.StopAsync(cancellationToken);
        }
    }
}