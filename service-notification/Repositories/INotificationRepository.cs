using service_notification.Modeles;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace service_notification.Repositories
{
    internal interface INotificationRepository
    {
        Task AddAsync(Notification notification);
    }
}
