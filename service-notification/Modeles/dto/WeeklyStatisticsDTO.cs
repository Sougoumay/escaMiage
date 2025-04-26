using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace service_notification.Modeles.dto
{
    internal class WeeklyStatisticsDTO
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("meilleurTemps")]
        public long MeilleurTemps { get; set; }

        [JsonPropertyName("meilleurScore")]
        public long MeilleurScore { get; set; }

        [JsonPropertyName("dateDernierePartie")]
        public DateTime DateDernierePartie { get; set; }

        [JsonPropertyName("nbPartiesJouees")]
        public int NbPartiesJouees { get; set; }

        [JsonPropertyName("scoreSemaine")]
        public int ScoreSemaine { get; set; }

       
    }
}
