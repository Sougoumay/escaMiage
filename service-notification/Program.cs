using Microsoft.EntityFrameworkCore;
using service_notification.Data;
using service_notification.Repositories;
using service_notification.Services;
using service_notification.Workers;
using Steeltoe.Discovery.Client;
using Steeltoe.Discovery.Consul;

var builder = Host.CreateApplicationBuilder(args);
builder.Services.AddHostedService<WelcomeWorker>();
builder.Services.AddHostedService<ResetPasswordWorker>();
builder.Services.AddHostedService<BirthdayWorker>();
builder.Services.AddHostedService<FeedBackWorker>();
builder.Services.AddHostedService<WeeklyStatisticsWorker>();
builder.Services.AddSingleton<IEmailService, SmtpEmailService>();
builder.Services.AddScoped<IUtilisateurRepository, UtilisateurRepository>();
builder.Services.AddScoped<INotificationRepository, NotificationRepository>();

builder.Services.AddServiceDiscovery(o => o.UseConsul());

var server = Environment.GetEnvironmentVariable("DB_HOST") ?? "localhost";
var port = Environment.GetEnvironmentVariable("DB_PORT") ?? "3306";
var database = Environment.GetEnvironmentVariable("DB_NAME") ?? "escamiage";
var user = Environment.GetEnvironmentVariable("DB_USER") ?? "root";
var password = Environment.GetEnvironmentVariable("DB_PASSWORD") ?? "";

var connectionString = $"Server={server};Port={port};Database={database};User={user};Password={password};Charset=utf8mb4;";


builder.Services.AddDbContext<ApplicationDBContext>(options =>
{
    options.UseMySql(connectionString, new MySqlServerVersion(new Version(8, 0, 0)));
});

using (var scope = builder.Services.BuildServiceProvider().CreateScope())
{
    var dbContext = scope.ServiceProvider.GetRequiredService<ApplicationDBContext>();
    try
    {
        Console.WriteLine("Tentative de connexion à la base de données...");
        // Attendre que la base de données soit disponible
        var retryCount = 0;
        const int maxRetries = 10;

        while (retryCount < maxRetries)
        {
            try
            {
                if (dbContext.Database.CanConnect())
                {
                    Console.WriteLine("Connexion à la base de données réussie !");
                    break;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Tentative {retryCount + 1}/{maxRetries}: Échec de connexion à la BDD: {ex.Message}");
            }

            retryCount++;
            if (retryCount < maxRetries)
            {
                Console.WriteLine($"Nouvelle tentative dans 5 secondes...");
                Thread.Sleep(5000); // Attendre 5 secondes avant de réessayer
            }
        }

        if (retryCount == maxRetries)
        {
            Console.WriteLine("Impossible de se connecter à la base de données après plusieurs tentatives.");
            return;
        }

        Console.WriteLine("Application des migrations...");
        dbContext.Database.Migrate();
        Console.WriteLine("Migrations appliquées avec succès !");
    }
    catch (Exception ex)
    {
        Console.WriteLine("Erreur lors de l'application des migrations : " + ex.Message);
        Console.WriteLine(ex.StackTrace);
    }
}

var host = builder.Build();
host.Run();
