using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using System.Text.Json.Serialization;

namespace service_notification.Helper
{
    internal class DateArrayConverter : JsonConverter<DateTime>
    {
        public override DateTime Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
        {
            if (reader.TokenType == JsonTokenType.StartArray)
            {
                // Lire le tableau de date [année, mois, jour]
                List<int> dateComponents = new List<int>();

                reader.Read(); // Avancer au premier élément
                while (reader.TokenType != JsonTokenType.EndArray)
                {
                    dateComponents.Add(reader.GetInt32());
                    reader.Read(); // Avancer au prochain élément ou à la fin du tableau
                }

                if (dateComponents.Count >= 3)
                {
                    return new DateTime(dateComponents[0], dateComponents[1], dateComponents[2]);
                }
            }

            throw new JsonException($"Impossible de convertir la valeur en DateTime. TokenType: {reader.TokenType}");
        }

        public override void Write(Utf8JsonWriter writer, DateTime value, JsonSerializerOptions options)
        {
            writer.WriteStartArray();
            writer.WriteNumberValue(value.Year);
            writer.WriteNumberValue(value.Month);
            writer.WriteNumberValue(value.Day);
            writer.WriteEndArray();
        }
    }
}

