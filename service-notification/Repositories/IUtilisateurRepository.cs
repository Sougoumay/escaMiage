using service_notification.Modeles;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace service_notification.Repositories
{
    internal interface IUtilisateurRepository
    {
        Task<Utilisateur> GetByIdAsync(int id);
        Task<IEnumerable<Utilisateur>> GetAllAsync();

        Task<IEnumerable<Utilisateur>> GetUtilisateursAnniversaireAsync(int mois, int jour);

        Task AddAsync(Utilisateur utilisateur);
        Task UpdateAsync(Utilisateur utilisateur);
        Task DeleteAsync(int id);
    }
}
