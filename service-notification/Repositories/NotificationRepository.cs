using Microsoft.EntityFrameworkCore;
using service_notification.Data;
using service_notification.Modeles;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace service_notification.Repositories
{
    internal class NotificationRepository : INotificationRepository
    {
        private readonly ApplicationDBContext _context;

        public NotificationRepository(ApplicationDBContext context)
        {
            _context = context;
        }

        public async Task AddAsync(Notification notification)
        {
            await _context.Notifications.AddAsync(notification);
            await _context.SaveChangesAsync();
        }
    }
}
