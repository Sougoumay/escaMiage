using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace service_notification.Modeles
{
    internal class Notification
    {
        [Key]
        public int Id { get; set; }

        [Required]
        public DateTime DateEnvoi { get; set; }

        public string? Contenu { get; set; } // Peut être rempli automatiquement à partir du template

        [Required]
        public TypeNotificationEnum TypeNotif { get; set; }  // Utilisation de l'Enum

        [Required]
        public int UtilisateurId { get; set; }
        [ForeignKey("UtilisateurId")]
        public Utilisateur Utilisateur { get; set; }

        public bool Envoye { get; set; } = false;
    }
}
