using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace service_notification.Modeles
{
    internal enum TypeNotificationEnum
    {
        MailBienvenue,
        MailReinitMDP,
        MailStatsSemaine,
        MailMiseAJourClassement,
        MailAnniversaire,
        MailFeedback,
    }
}
