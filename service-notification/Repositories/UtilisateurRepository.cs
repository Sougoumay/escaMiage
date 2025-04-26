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
    internal class UtilisateurRepository : IUtilisateurRepository
    {
        private readonly ApplicationDBContext _context;
        private readonly ILogger<UtilisateurRepository> _logger;

        public UtilisateurRepository(ApplicationDBContext context, ILogger<UtilisateurRepository> logger)
        {
            _context = context;
            _logger = logger;
        }

        public async Task<Utilisateur> GetByIdAsync(int id)
        {
            _logger.LogInformation($"Récupération de l'utilisateur avec ID: {id}");
            return await _context.Utilisateurs
                .FirstOrDefaultAsync(u => u.Id == id);
        }

        public async Task<IEnumerable<Utilisateur>> GetAllAsync()
        {
            return await _context.Utilisateurs.ToListAsync();
        }

        public async Task AddAsync(Utilisateur utilisateur)
        {
            await _context.Utilisateurs.AddAsync(utilisateur);
            await _context.SaveChangesAsync();
        }

        public async Task UpdateAsync(Utilisateur utilisateur)
        {
            _context.Utilisateurs.Update(utilisateur);
            await _context.SaveChangesAsync();
        }

        public async Task DeleteAsync(int id)
        {
            var utilisateur = await GetByIdAsync(id);
            if (utilisateur != null)
            {
                _context.Utilisateurs.Remove(utilisateur);
                await _context.SaveChangesAsync();
            }
        }

        public async Task<IEnumerable<Utilisateur>> GetUtilisateursAnniversaireAsync(int mois, int jour)
        {
            _logger.LogInformation($"Récupération des utilisateurs ayant un anniversaire le {jour}/{mois}");

            return await _context.Utilisateurs
                .Where(u => u.DateNaissance.Month == mois && u.DateNaissance.Day == jour)
                .ToListAsync();
        }

    }
}
