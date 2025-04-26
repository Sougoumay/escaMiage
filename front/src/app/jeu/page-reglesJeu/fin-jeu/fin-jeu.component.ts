import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { JeuRepository } from '../../repository/jeu.repository';

@Component({
  selector: 'app-fin-jeu',
  imports: [FormsModule , ReactiveFormsModule,  CommonModule],
  templateUrl: './fin-jeu.component.html',
  styleUrl: './fin-jeu.component.css'
})
export class FinJeuComponent {
  debut: boolean = true;
  isSuite1: boolean = false;
  isSuite2: boolean = false;
  idPartie: number = 0;
  score: number = 0
  tempsRestant : string = "";
  tentativesErronees: number = 0;

  constructor(private jeuRepository: JeuRepository, private router: Router) {}

  ngOnInit() {
    // Vérification si l'ID de la partie est dans localStorage
    const storedIdPartie = localStorage.getItem("idPartie");
    if (storedIdPartie) {
      this.idPartie = parseInt(storedIdPartie, 10);
      // Si l'ID est trouvé, on peut vérifier l'état de la partie ici ou démarrer des processus en lien
      console.log(`Partie en cours avec l'ID : ${this.idPartie}`);
    } else {
      console.log("Aucune partie en cours");
    }
    
    this.getInfosPartie();
    this.resetBooleans();
  }

  getInfosPartie() {
    this.jeuRepository.consulterStatsPartie(this.idPartie).subscribe({
          next: (data) => {
            console.log(data)
            this.score = data.body.score ;
            this.tempsRestant = this.formatTime(data.body.tempsRestant);
            this.tentativesErronees = data.body.nbTentativesTotal
          },
          error: (err) => {
            console.error('Erreur lors du démarrage de la partie :', err);
          }
        });
  }

  formatTime(seconds: number): string {
    const minutes = Math.floor(seconds / 60); // Calcul des minutes
    const remainingSeconds = seconds % 60; // Calcul des secondes restantes

    // Retourne le format souhaité avec deux chiffres pour les secondes
    return `${minutes}m${remainingSeconds.toString().padStart(2, '0')}`;
}

  resetBooleans() {
    this.debut = true;
    this.isSuite1 = false;
    this.isSuite2 = false;
  }
  

  suite1(){
    this.debut = false;
    this.isSuite1 = true;
    this.isSuite2 = false;
  }

  suite2(){
    this.debut = false;
    this.isSuite1 = false;
    this.isSuite2 = true;
  }

  suite3(){
    this.changementPage()
  }

  changementPage(){
    this.quitFullScreen();
    this.router.navigate(['/user-dashboard']);
  }



  // Passer en plein écran
  quitFullScreen() {
    if (document.exitFullscreen) {
      document.exitFullscreen(); // Quitter le plein écran
    }
  }
}
