using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mail;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace service_notification.Services
{
    internal class SmtpEmailService : IEmailService
    {
        private readonly IConfiguration _configuration;
        private readonly ILogger<SmtpEmailService> _logger;

        public SmtpEmailService(IConfiguration configuration, ILogger<SmtpEmailService> logger)
        {
            _configuration = configuration;
            _logger = logger;
        }

        public async Task SendEmailAsync(string to, string subject, string body, bool isHtml = false)
        {
            try
            {

                var fromAddress = Environment.GetEnvironmentVariable("SMTP_FROM_ADDRESS");
                var fromName = Environment.GetEnvironmentVariable("SMTP_FROM_NAME");
                var userName = Environment.GetEnvironmentVariable("SMTP_USERNAME");
                var password = Environment.GetEnvironmentVariable("SMTP_PASSWORD");
                var host = Environment.GetEnvironmentVariable("SMTP_HOST");
                var port = int.Parse(Environment.GetEnvironmentVariable("SMTP_PORT"));
                var enableSsl = bool.Parse(Environment.GetEnvironmentVariable("SMTP_ENABLE_SSL"));

                using var message = new MailMessage
                {
                    From = new MailAddress(fromAddress, fromName),
                    Subject = subject,
                    Body = body,
                    IsBodyHtml = isHtml
                };

                message.To.Add(to);

                using var client = new SmtpClient(host, port)
                {
                    Credentials = new NetworkCredential(userName, password),
                    EnableSsl = enableSsl
                };

                await client.SendMailAsync(message);
                _logger.LogInformation($"Email envoyé à {to} avec succès");
            }
            catch (Exception ex)
            {
                _logger.LogError(ex, $"Erreur lors de l'envoi de l'email à {to}");
                throw;
            }
        }
    }
}
