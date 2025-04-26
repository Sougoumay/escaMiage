import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ParamService} from '../profil/service/param.service';
import {ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {UserService} from '../profil/service/user.service';
import {BadgeService} from '../../badge/service/badge.service';
import {PostService} from '../../actualite/service/post.service';
import {Post} from '../../models/post.modele';
import {User} from '../../models/user.modele';
import {Badge} from '../../models/badge.modele';
import {UserClassement} from '../../models/userClassement.modele';



@Component({
  selector: 'app-user-dashboard',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './user-dashboard.component.html',
  styleUrl: './user-dashboard.component.css'
})

/** ----------------------------------------------------------------------------------------------------------- **/
/** composant non testé **/


export class UserDashboardComponent  implements OnInit{

  userId!: string ;
  user: User = {dateNaissance: '', email: '', nom: '', prenom: ''};
  classement: UserClassement[] = [];
  classementHebdo : UserClassement[] = [];
  badge: Badge = {condition_type: "", condition_value: "", icone: "", nom: ""};
  actualites: Post[] = [];
  top3JoueursGlobal: UserClassement[] = [];
  top3JoueursHebdo: UserClassement[] = [];
  classementUser: UserClassement[] = [];
  monClassement= {position: 0, score: 0};
  latestPosts: Post[] = [];

  constructor(
    private router: Router,
    private userService: UserService,
    private badgeService: BadgeService,
    private actualiteService: PostService,
    private paramService: ParamService
  ) {}

  ngOnInit() {
    this.getClassementGlobal();
    //this.getClassementHebdo();
    //this.getClassementUser()
    //this.getBadge();
    this.getActualite();
  }


 /** on recup le classement global puis on trie la liste et on extrait le top 3 des meilleurs joueurs **/

  getClassementGlobal() {
    this.badgeService.getClassement().subscribe({
      next: (data) => {
        this.classement = data;

        this.classement.sort((a, b) => b.meilleurScore - a.meilleurScore);

        // Extraire le Top 3 des meilleurs joueurs
        this.top3JoueursGlobal = this.classement.slice(0, 3);
      },
      error: (err) => {
        console.error('Erreur lors de la récupération du classement global:', err);
      }
    });
  }

  /** on recup le classement hebdo puis on trie la liste et on extrait le top 3 des meilleurs joueurs **/

  getClassementHebdo() {
    this.badgeService.getClassementHebdo().subscribe({
      next: (data) => {
        this.classementHebdo = data;
        this.classementHebdo.sort((a, b) => b.meilleurScore - a.meilleurScore);

        // Extraire le Top 3 des meilleurs joueurs
        this.top3JoueursHebdo = this.classementHebdo.slice(0, 3);
      },
      error: (err) => {
        console.error("Erreur lors de la récupération du classement Hebdo :", err);
      }
    });
  }

  /** on recup le classement global puis on trie la liste et on cherche l'indice du user courant et on affiche son classement **/

  getClassementUser() {
    this.badgeService.getClassement().subscribe({
      next: (data) => {
        this.classementUser = data; // Stocker la liste complète

        // 🔥 Trier la liste par score (du plus haut au plus bas)
        this.classementUser.sort((a, b) => b.meilleurScore - a.meilleurScore);

        // 🏆 Trouver l'index du user courant
        this.paramService.id$.subscribe(value => this.userId = value);
        const userIndex = this.classementUser.findIndex(user => user.id?.toString() === this.userId);

        if (userIndex !== -1) {
          this.monClassement = {
            position: userIndex + 1, // 🔢 Position réelle (1er, 2e, etc.)
            score: this.classementUser[userIndex].meilleurScore
          };
        } else {
          this.monClassement = { position: 0, score: 0 };
          console.warn("⚠️ Utilisateur non trouvé dans le classement !");
        }
      },
      error: (err) => {
        console.error("Erreur lors de la récupération du classement global:", err);
      }
    });
  }

  goToClassement(): void {
    this.getClassementGlobal();
  }

  /** on recup le badge de user courant  **/

  getBadge() {
    this.paramService.id$.subscribe(value => this.userId = value);
    const id = this.userId;
    this.badgeService.getById(id).subscribe({
      next: (data) => {
        this.badge = data;
      },
      error: (err) => {
        console.error("Erreur lors de la récupération du badge :", err);
      }
    });
  }

  /** on recup la liste des actualites  **/
  getActualite() {
    this.actualiteService.getAllPosts().subscribe({
      next: (data) => {
        this.actualites = data;
        this.latestPosts = this.actualites.slice(0, 3);
      },
      error: (err) => {
        console.error("Erreur lors de la récupération des actualités :", err);
      }
    });
  }



  /** Liens vers les autres pages  **/

  goToFeedbackSuggestions(): void {
    this.router.navigate(['/suggestions-feedback']);
  }

  goToJeu():  void {
    this.router.navigate(['/regles-jeu']);
  }


  //
  // openAllPostsModal() {
  //   document.getElementById('allPostsModal')!.style.display = 'block';
  // }
  //
  // closeAllPostsModal() {
  //   document.getElementById('allPostsModal')!.style.display = 'none';
  // }
  //
  // openTop3Modal() {
  //   document.getElementById('top3Modal')!.style.display = 'block';
  // }
  //
  // closeTop3Modal() {
  //   document.getElementById('top3Modal')!.style.display = 'none';
  // }

  logout(){
    //this.authService.logout();
  }



}







