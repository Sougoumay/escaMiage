using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace service_notification.Modeles
{
    internal class Template
    {
        [Key]
        public int Id { get; set; }

        [Required]
        public string Nom { get; set; }  // Ex: "Template de Bienvenue"

        [Required]
        public string Contenu { get; set; }  // Contenu du mail

        [Required]
        public TypeNotificationEnum TypeNotif { get; set; }  // Enum pour lier au type de notification
    }
}
