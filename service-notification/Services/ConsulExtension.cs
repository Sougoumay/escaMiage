using Consul;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;


namespace service_notification.Services
{
    public static class ConsulExtension
    {
        public static IServiceCollection AddConsul(this IServiceCollection services, IConfiguration configuration)
        {
            // Récupération des paramètres depuis la configuration ou les variables d'environnement
            string consulHost = configuration["Consul:Host"] ?? Environment.GetEnvironmentVariable("CONSUL_HOST") ?? "localhost";
            int consulPort = int.Parse(configuration["Consul:Port"] ?? Environment.GetEnvironmentVariable("CONSUL_PORT") ?? "8500");
            string serviceName = configuration["ServiceName"] ?? "service-notification";
            string serviceId = $"{serviceName}-{Guid.NewGuid()}";

            // Configuration du client Consul
            services.AddSingleton<IConsulClient>(p => new ConsulClient(config =>
            {
                config.Address = new Uri($"http://{consulHost}:{consulPort}");
            }));

            // Enregistrement du service hosted qui gère l'enregistrement et le désenregistrement
            services.AddSingleton<IHostedService, ConsulHostedService>(p => new ConsulHostedService(
                p.GetRequiredService<IConsulClient>(),
                p.GetRequiredService<ILogger<ConsulHostedService>>(),
                serviceId,
                serviceName));

            return services;
        }
    }

    public class ConsulHostedService : IHostedService
    {
        private readonly IConsulClient _consulClient;
        private readonly ILogger<ConsulHostedService> _logger;
        private readonly string _serviceId;
        private readonly string _serviceName;

        public ConsulHostedService(IConsulClient consulClient, ILogger<ConsulHostedService> logger, string serviceId, string serviceName)
        {
            _consulClient = consulClient;
            _logger = logger;
            _serviceId = serviceId;
            _serviceName = serviceName;
        }

        public async Task StartAsync(CancellationToken cancellationToken)
        {
            try
            {
                // Création de l'enregistrement dans Consul
                var registration = new AgentServiceRegistration
                {
                    ID = _serviceId,
                    Name = _serviceName,
                    // Pas de Port ou d'Address puisque c'est un worker
                    Check = new AgentServiceCheck
                    {
                        DeregisterCriticalServiceAfter = TimeSpan.FromMinutes(1),
                        TTL = TimeSpan.FromSeconds(30),
                        Status = HealthStatus.Passing
                    }
                };

                // Enregistrement du service
                await _consulClient.Agent.ServiceRegister(registration, cancellationToken);
                _logger.LogInformation($"Service {_serviceName} enregistré dans Consul avec l'ID {_serviceId}");

                // Démarrage d'une tâche en arrière-plan pour mettre à jour le TTL check
                _ = UpdateTtlHealthCheckAsync(cancellationToken);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors de l'enregistrement du service dans Consul");
            }
        }

        public async Task StopAsync(CancellationToken cancellationToken)
        {
            try
            {
                // Désenregistrement du service à l'arrêt
                await _consulClient.Agent.ServiceDeregister(_serviceId, cancellationToken);
                _logger.LogInformation($"Service {_serviceId} désenregistré de Consul");
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors du désenregistrement du service dans Consul");
            }
        }

        private async Task UpdateTtlHealthCheckAsync(CancellationToken cancellationToken)
        {
            try
            {
                while (!cancellationToken.IsCancellationRequested)
                {
                    // Mise à jour du TTL check toutes les 20 secondes
                    await _consulClient.Agent.PassTTL($"service:{_serviceId}", "Service en fonctionnement", cancellationToken);
                    await Task.Delay(TimeSpan.FromSeconds(20), cancellationToken);
                }
            }
            catch (OperationCanceledException)
            {
                // Ignore car c'est normal lors de l'arrêt
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, "Erreur lors de la mise à jour du TTL check");
            }
        }
    }
}