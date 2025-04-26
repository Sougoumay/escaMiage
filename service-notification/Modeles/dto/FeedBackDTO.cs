using System.Text.Json.Serialization;

namespace service_notification.Modeles.dto
{
    internal class FeedBackDTO
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("idUtilisateur")]
        public int IdUtilisateur { get; set; }

        [JsonPropertyName("sujet")]
        public string Sujet { get; set; }

        [JsonPropertyName("message")]
        public string Message { get; set; }
    }
}
