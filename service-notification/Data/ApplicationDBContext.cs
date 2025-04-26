using Microsoft.EntityFrameworkCore;
using service_notification.Modeles;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace service_notification.Data
{
    internal class ApplicationDBContext : DbContext
    {
        public ApplicationDBContext(DbContextOptions dbContextOptions) : base(dbContextOptions)
        {
        }

        public DbSet<Utilisateur> Utilisateurs { get; set; }
        public DbSet<Notification> Notifications { get; set; }
        public DbSet<Template> Templates { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Notification>()
                .Property(n => n.TypeNotif)
                .HasConversion<string>();  // Stocker l'Enum en INT

            modelBuilder.Entity<Template>()
                .Property(t => t.TypeNotif)
                .HasConversion<string>();  // Stocker l'Enum en INT
        }
    }


}
