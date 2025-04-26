using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace service_notification.Modeles.dto
{
    internal class WelcomeDTO
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("nom")]
        public string Nom { get; set; }

        [JsonPropertyName("prenom")]
        public string Prenom { get; set; }

        [JsonPropertyName("email")]
        public string Email { get; set; }

        [JsonPropertyName("pseudo")]
        public string Pseudo { get; set; }


        [JsonPropertyName("dateNaissance")]
        public DateTime DateNaissance { get; set; }
    }
}
