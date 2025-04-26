import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as bootstrap from 'bootstrap';
import {JeuRepository} from '../../jeu/repository/jeu.repository';
import {UserAnswer} from '../../models/userAnswer.modele';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import { Reponse } from '../../models/reponse.modele';
import { Enigme } from '../../models/enigme.modele';
import { Router } from '@angular/router';
import { Modal } from 'bootstrap';
declare function imageMapResize(): void;

@Component({
  selector: 'app-jeu',
  imports: [FormsModule,ReactiveFormsModule,CommonModule],
  templateUrl: './jeu.component.html',
  styleUrl: './jeu.component.css'
})
export class JeuComponent implements AfterViewInit, OnInit {

  modalTitle = '';
  modalContent = '';
  userResponse: string = '';
  idEnigmeActuelle: number = 0;
  idPartie: number | null = null;
  enigmeActuelle: Reponse | undefined;
  indice: boolean = false;
  indiceMaster: boolean = false;
  texteIndice: String = "";
  texteIndiceMaster: String = "";
  chronoMinutes: number = 0;
  chronoSecondes: number = 0;
  chronoInterval: any;
  donnees: Reponse[] | undefined ;
  reponseForm!: FormGroup;
  nbTentatives: number = 0;
  reponseIncorrecte:boolean =  false;
  reponseVide: boolean = false;
  currentErrorMessage: string = "";
  phraseReussiteAfficher: string = "";
  isFullScreen: boolean = true;
  reponseIncorrecteEnigme: string = "";
  phraseAnnonceMaster:string = "";
  nbTentativesMaster:number = 0;
  reponseIncorrecteMsg:string = "";
  listeInventaire: string[] = [
    "_","_","_","_","_","_","_","_","_","_","_","_","_","_","_",
  ]

  indicesMaster: string[] = [
    "Indice 💡 : Utilise tes compétences en crypto... parce que googler “décryptage code” va pas t’emmener bien loin cette fois 😬",
    "Indice 💡 : César aurait validé ce message. Et non, pas ton pote de TD, l’autre, en toge. 🏛️✉️",
    "Indice 💡 : César n’était pas du genre excessif. 5, c’était déjà beaucoup pour lui. 🏛️📜"
  ]

  reponseIncorrecteMsgList: string[] = [
    "❌ Mauvaise réponse ! 🤯 Recalcule tes chances et réessaie !",
    "❌ Oups, ce n'est pas la bonne réponse... 🔍 Replonge dans tes calcules !", 
    "🚪 Ce code ne fonctionne pas ! 🔑 Cherche une autre combinaison !",
    "🕹️ Glitch dans la matrice ! Cette réponse ne passe pas, essaie encore...",
    "🏴‍☠️ Code incorrect ! Le coffre reste verrouillé... Essaye une autre réponses !",
    "❌ Mauvais checksum ! Vérifie ta logique et renvoie une réponse valide.", 
    "🔐 La clé ne correspond pas… Essaie un autre mot de passe !",
    "🕵️ Incorrect... Le serveur renvoie une erreur 400 ! Trouve la bonne réponse !",
    "⛔ Mauvaise entrée ! Le système refuse ta réponse, reformule ton idée !",
    "💥 Réponse incorrecte ! Le système ne valide pas ta solution, réessaie encore !",
    "🧠 Ce n’est pas ça, mais tu te rapproches ! Continue à chercher !",
    "🔒 Tu n’as pas trouvé la clé… Relance ton analyse et teste à nouveau !",
    "🛑 Réponse invalide. Tu peux encore y arriver, cherche bien !",
    "💣 Boom ! Ta réponse a échoué. Il est temps de réessayer !",
    "🔒 Accès refusé ! Ta tentative de réponse n’est pas correcte. Essaie encore !",
    "🔐 La porte reste fermée… Regarde encore les indices et retente !",
    "💡 L’éclair de génie n’a pas frappé… Cherche à nouveau la bonne réponse !",
    "🕹️ Game Over pour cette tentative… Mais ce n'est pas fini, réessaie !"
  ];

  errorMessages: string[] = [
    "Oups, il faut écrire quelque chose !",
    "Hé, tu n'oublierais pas de répondre ?",
    "Un mystère ne se résout pas tout seul... Essaie une réponse !",
    "Allez, ne laisse pas ce champ vide, tente ta chance !",
    "Même une mauvaise réponse vaut mieux que pas de réponse du tout !"
  ];

  phraseReussite:string[] = [
    "🎭 Un maître du jeu comme toi ne tombe pas deux fois dans le même piège !",
    "🧩 Code déchiffré, mystère résolu ! 🏅",
    "🕵️‍♂️ Encore une enquête brillamment bouclée !",
    "💪 Déjà résolu ! Tu es une machine !",
    "🎖️ Déjà dans ton tableau de chasse !",
    "🏆 Champion(ne) ! Tu as déjà cracké cette énigme !",
    "🔍 L’affaire est classée, détective !",
    "🧩 Mystère élucidé, il n’y a plus rien à voir…",
    "🚪 Cette porte est déjà ouverte… mais qu’y a-t-il derrière la prochaine ?",
    "🎖️ Ce niveau est déjà sous ton contrôle !",
    "⚔️ Cette énigme est tombée sous tes coups de génie !"
  ];

  phraseMaster:string[] = [
    "🎉 Mission accomplie, vous avez résolu toutes les énigmes ! 🧩 Il est temps d'utiliser votre inventaire et de répondre à la question MASTER pour gagner 🏆💻", 
    "👏 Félicitations, vous avez maîtrisé toutes les énigmes ! 🔍 Utilisez maintenant votre inventaire pour trouver la clé de la question MASTER et décrocher la victoire 🚀💡",
    "🎯 Vous avez toutes les réponses, mais une seule vous sépare de la victoire : la question MASTER 🧐💻 N'oubliez pas d'utiliser votre inventaire pour vous aider à répondre ! 🔑", 
    "🚀 Vous avez piraté toutes les énigmes ! 🎮 Maintenant, utilisez votre inventaire et répondez à la question MASTER pour décrocher la gloire finale 🔓💻", 
    "💥 Vous avez décrypté chaque code, résolu chaque mystère... Mais la dernière étape ? Utilisez votre inventaire et répondez à la question MASTER pour sortir victorieux 🔑💻",
    "🔥 Enigmes résolues, maintenant place à la question MASTER ! 🧠 Utilisez votre inventaire pour trouver la solution et remporter la victoire finale 🚀💡"
  ]

  ngOnInit(): void {
    this.reponseForm = new FormGroup({
      reponse: new FormControl('', Validators.required) // ✅ Ajout du champ "reponse"
    });


    //verif si on a variable globale idPartie, 
    // si oui on fait appel au back pr verifier si finie ou pas annulée etc
    // si non en bas ->
    this.demarrerPartie();
  }

  constructor(private jeuRepository: JeuRepository, private router: Router) {}

  ngAfterViewInit(): void {
        imageMapResize();
  }

  private extractIdFromLocation(location: string): number {
    const parts = location.split('/');
    return parseInt(parts[parts.length - 1], 10); // Récupère le dernier élément
  }

  toggleFullScreen() {
    if (this.isFullScreen) {
      this.exitFullScreen();
    } else {
      this.enterFullScreen();
    }
  }

  // Passer en plein écran
  enterFullScreen() {
    const element: any = document.documentElement;

    if (element.requestFullscreen) {
      element.requestFullscreen();
    } else if (element.msRequestFullscreen) {
      element.msRequestFullscreen();
    } else if (element.mozRequestFullScreen) {
      element.mozRequestFullScreen();
    } else if (element.webkitRequestFullscreen) {
      element.webkitRequestFullscreen();
    }

    this.isFullScreen = true; // Mettre à jour l'état du plein écran
  }
  // Quitter le plein écran
  exitFullScreen() {
    if (document.exitFullscreen) {
      document.exitFullscreen(); // Quitter le plein écran
    }

    this.isFullScreen = false;
  }
  /** Démarrer une nouvelle partie **/
  demarrerPartie() {
    this.jeuRepository.demarrerPartie().subscribe({
      next: (data) => {
        this.donnees = data.body.map((q: any) => new Reponse(
          q.id, q.partieId, q.question, q.difficulte, q.indice, q.nombreTentative, q.score, q.isRepondu
        ));

        const locationHeader = data.headers.get('Location');
        
          if (locationHeader) {
            // 📌 Extraire l'ID de la partie depuis l'URL (dernière partie de l'URL)
            this.idPartie = this.extractIdFromLocation(locationHeader);
            console.log("idpartie "+ this.idPartie);
            localStorage.setItem("idPartie", this.idPartie.toString());
            this.resetChrono();
            this.startChrono();
          } else {
            console.warn("L'en-tête 'Location' est absent !");
          }
      },
      error: (err) => {
        console.error('Erreur lors du démarrage de la partie :', err);
      }
    });
  }

  /** Annuler la partie en cours **/
  annulerPartie() {
    const idPartieString = localStorage.getItem("idPartie");
    if (idPartieString !== null) {
      this.idPartie = parseInt(idPartieString, 10);
    }else {
      alert('Aucune partie en cours.');
      return;
    }

    this.jeuRepository.annulerPartie(this.idPartie).subscribe({
      next: (data) => {
        if(data.status == 200){
          this.closeModal('annulerPartieModale');
          localStorage.setItem("idPartie", "");
        const modalElement = document.getElementById('annulationReussie');
      
        if (modalElement) { // Vérifie si l'élément existe
          const modal = new bootstrap.Modal(modalElement);
          modal.show();

          this.idPartie = null;
          localStorage.removeItem('idPartie');
          this.stopChrono();
      
          // Fermer la modale après 5 secondes
          setTimeout(() => {
            modal.hide();
            this.router.navigate(['/user-dashboard']);
          }, 8000);
          
        }

          
        } else {
          console.log("la partie n'a pas pu être annulée");
        }
        
      },
      error: (err) => {
        console.error('Erreur lors de l\'annulation de la partie :', err);
      }
    });
  }
  /** Terminer la partie en cours **/
  terminerPartie() {
    if (this.idPartie === null) {
      console.log('Aucune partie en cours.');
      return;
    }

    this.jeuRepository.terminerPartie(this.idPartie).subscribe({
      next: () => {
        localStorage.setItem("idPartie", "");
        this.idPartie = null;
        alert('Félicitations ! Vous avez terminé la partie.');
        this.stopChrono();
      },
      error: (err) => {
        console.error('Erreur lors de la fin de la partie :', err);
      }
    });
  }

  getRandomPhrase() {
    const randomIndex = Math.floor(Math.random() * this.phraseReussite.length);
    this.phraseReussiteAfficher =  this.phraseReussite[randomIndex];
  }

  /** Ouvrir une énigme **/
  openModal(enigme: string, idEnigme: number | null) {
    this.resetForm();
    this.getRandomPhrase();
    console.log("enigme : " + enigme + " id: " + idEnigme);
  
    if (this.idPartie === null) {
      alert('Veuillez démarrer une partie avant de jouer.');
      return;
    }
  
    if (idEnigme == null) {
      console.error('Erreur lors du chargement de l\'énigme');
      this.modalContent = 'Impossible de charger l\'énigme.';
      return;
    } else {
      this.idEnigmeActuelle = idEnigme;
    }
  
    this.jeuRepository.consulterEnigme(this.idPartie, this.idEnigmeActuelle).subscribe({
      next: (data) => {
        this.enigmeActuelle = {
          id: data.body.id,
          question: data.body.question,
          difficulte: data.body.difficulte,
          indice: data.body.indice,
          nombreTentative: data.body.nombreTentative,
          partieId: data.body.partieId,
          score: data.body.score,
          isRepondu: data.body.repondu
        };
        this.nbTentatives = data.body.nombreTentative;
        this.texteIndice = "Indice 💡: " + this.enigmeActuelle.indice;
        this.modalContent = this.enigmeActuelle.question;
  
        if (this.enigmeActuelle.indice != "") {
          this.indice = true;
        } else {
          this.indice = false;
        }

        
  
        // ✅ Déplacer la gestion des modales ici, une fois les données chargées
        if (data.body.repondu) {
          this.showModal('enigmeRepondue');
        } else {
          this.showModal('enigmeModal');
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement de l\'énigme :', err);
        this.modalContent = 'Impossible de charger l\'énigme.';
      }
    });
  
    this.modalTitle = enigme;
  }
  
  // Fonction utilitaire pour afficher une modale Bootstrap
  showModal(modalId: string) {
    const modalElement = document.getElementById(modalId);
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  closeModal(modalId: string) {
    const modalElement = document.getElementById(modalId); 
    if (modalElement) {
      let modal = Modal.getInstance(modalElement); 
      if (!modal) {
        modal = new Modal(modalElement); 
      }
      modal.hide();
    }
  }

  resetError(){
    this.reponseVide = false;
    this.reponseIncorrecte = false;
  }

  resetForm() {
    this.reponseForm.reset(); // Réinitialise complètement le formulaire
    this.reponseVide = false; // Cache les erreurs de champ vide
    this.reponseIncorrecte = false; // Cache l'erreur de mauvaise réponse
    this.indice = false;
  }

  /** Soumettre une réponse **/
  repondreAEnigme() {

    //gestion erreurs aléatoires
    var randomIndex = Math.floor(Math.random() * this.errorMessages.length);
    this.currentErrorMessage = this.errorMessages[randomIndex];


    if (!this.reponseForm.valid) {
      this.reponseVide = true;
      console.log('Formulaire invalide');
      return;
    }

    this.reponseVide = false;
    this.reponseIncorrecte = false;
  
    const reponseUser = this.reponseForm.get('reponse')?.value;
    const reponse: UserAnswer = { answer: reponseUser };

    this.jeuRepository.repondreAUnEnigme(this.idPartie!, this.idEnigmeActuelle, reponse).subscribe({
      next: (result) => {
        console.log(result.body.indice)
        if(result.body.repondu){
          this.closeModal("enigmeModal");
          var indexEnigme = this.donnees?.findIndex(reponse => reponse.id === this.idEnigmeActuelle);
          if (indexEnigme !== undefined && indexEnigme !== -1) {
            //lettre pour question master
            var lettreMajuscule = reponse.answer.charAt(0).toUpperCase();
            let numIndex = indexEnigme;
            this.listeInventaire[numIndex] = lettreMajuscule; 
          }
          if (!this.listeInventaire.some(element => element.includes("_"))){
            this.afficherQuestionMaster();
            return
          }
          this.afficherModaleEnigmeReussi();
          
        }  else {
          var randomIndex = Math.floor(Math.random() * this.reponseIncorrecteMsgList.length);
          this.reponseIncorrecteEnigme = this.reponseIncorrecteMsgList[randomIndex];
          this.reponseIncorrecte = true;
          this.nbTentatives = result.body.nombreTentative
        }
        if(result.body.indice != null){
          this.texteIndice = "Indice 💡: " + result.body.indice
          this.indice = true;
        }
      },
      error: (err) => {
        console.error('Erreur lors de la soumission de la réponse :', err);
      }
    });
  }

  repondreMaster(){
    this.reponseVide = false;
    this.reponseIncorrecte = false;

    const reponseUser = this.reponseForm.get('reponse')?.value;
    if (reponseUser == ""){
      var randomIndexVide = Math.floor(Math.random() * this.errorMessages.length);
      this.currentErrorMessage = this.errorMessages[randomIndexVide];
      this.reponseVide = true;
      return
    }
    const reponse: UserAnswer = { answer: reponseUser };

    this.jeuRepository.repondreAlaQuestionMaster(this.idPartie!, reponse).subscribe({
      next: (result) => {
        if(result.body.etat != "ENCOURS"){
          this.closeModal('enigmeMaster');
          this.afficherReussiteMaster();

        }else{
          
          this.nbTentativesMaster = result.body.nbreTentativeCode;
          if(this.nbTentativesMaster < 2){
            this.indiceMaster = false;
          }
          if(this.nbTentativesMaster == 2){
            this.indiceMaster = true;
            this.texteIndiceMaster = this.indicesMaster[0]
          }if (this.nbTentativesMaster == 3){
            this.indiceMaster = true;
            this.texteIndiceMaster = this.indicesMaster[1]
          }if (this.nbTentativesMaster > 3){
            this.indiceMaster = true;
            this.texteIndiceMaster = this.indicesMaster[2]
          }
          var randomIndex = Math.floor(Math.random() * this.reponseIncorrecteMsgList.length);
          this.reponseIncorrecteMsg = this.reponseIncorrecteMsgList[randomIndex];
          this.reponseIncorrecte = true;
        }
      },
      error: (err) => {
        console.error('Erreur lors de la soumission de la réponse :', err);
      }
    });
  }
  
  async afficherReussiteMaster() {
    const modalElement = document.getElementById('enigmeMasterReussie');
  
  if (modalElement) { // Vérifie si l'élément existe
    const modal = new bootstrap.Modal(modalElement);
    modal.show();

    // Fermer la modale après 5 secondes
    setTimeout(() => {
      modal.hide();
      this.router.navigate(['/fin-jeu']);
    }, 5000);
    
  }}
  

  afficherQuestionMaster() {
    const randomIndex = Math.floor(Math.random() * this.phraseMaster.length);
    this.phraseAnnonceMaster = this.phraseMaster[randomIndex];
    this.showModal('enigmeMaster');
  }

  afficherModaleEnigmeReussi(){
    const modalElement = document.getElementById('enigmeResolueModal');
  
  if (modalElement) { // Vérifie si l'élément existe
    const modal = new bootstrap.Modal(modalElement);
    modal.show();

    // Fermer la modale après 5 secondes
    setTimeout(() => {
      modal.hide();
    }, 3000);
  } else {
    console.error('L\'élément modal n\'a pas été trouvé.');
  }
  }

  private resetChrono() {
    this.stopChrono();
    this.chronoMinutes = 0;
    this.chronoSecondes = 0;
  }

  /** Démarrer le chrono  **/
  private startChrono() {
    this.stopChrono(); // S'assurer qu'aucun chrono précédent ne tourne
    this.chronoInterval = setInterval(() => {
      this.chronoSecondes++;
      if (this.chronoSecondes === 60) {
        this.chronoSecondes = 0;
        this.chronoMinutes++;
      }

      // Vérifier si le chronomètre atteint 25 minutes
      if (this.chronoMinutes === 25 && this.chronoSecondes === 0) {
        this.startFlashing(10000);
      }

      // Vérifier si le chronomètre atteint 25 minutes
      if (this.chronoMinutes === 29 && this.chronoSecondes === 0) {
        this.startFlashing(60000);
      }

      // Vérifier si le chronomètre atteint 30 minutes
      if (this.chronoMinutes === 30 && this.chronoSecondes === 0) {
        this.stopChrono(); // Arrêter le chronomètre
        this.navigateToNextPage(); // Changer de page
      }

    }, 1000);
  }

  // Méthode pour démarrer le clignotement du chronomètre à 25 minutes
  startFlashing(secondes: number): void {
    const chronoElement = document.getElementById('chronoTimer');
    if (chronoElement) {
      console.log("Clignotement du chronomètre activé");
      chronoElement.classList.remove('clignotage');
      chronoElement.classList.add('flash-animation'); // Ajouter l'animation de clignotement
      setTimeout(() => {
        chronoElement.classList.remove('flash-animation'); // Retirer l'animation après 5 secondes
        chronoElement.classList.add('clignotage');
      }, secondes);
    }else {
      console.error("Élément du chrono non trouvé");
    }
  }

  // Méthode pour naviguer vers la page suivante après 30 minutes
  navigateToNextPage(): void {
    this.router.navigate(['/nextPage']); // Remplace '/nextPage' par la route de la page suivante
  }

  /** Arrêter le chrono **/
  private stopChrono() {
    if (this.chronoInterval) {
      clearInterval(this.chronoInterval);
    }
  }

  /** Retourne le chrono sous format mm:ss **/
  getChronoString(): string {
    const min = this.chronoMinutes < 10 ? `0${this.chronoMinutes}` : this.chronoMinutes;
    const sec = this.chronoSecondes < 10 ? `0${this.chronoSecondes}` : this.chronoSecondes;
    return `${min}:${sec}`;
  }

  // Optionnel: pour afficher le chronomètre au format MM:SS
  get formattedTime(): string {
    return this.formatTime();
  }

  formatTime(): string {
    const minutes = this.chronoMinutes < 10 ? '0' + this.chronoMinutes : this.chronoMinutes;
    const seconds = this.chronoSecondes < 10 ? '0' + this.chronoSecondes : this.chronoSecondes;
    return `${minutes}:${seconds}`;
  }


}
