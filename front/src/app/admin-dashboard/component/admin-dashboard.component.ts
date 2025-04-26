import {Component, OnInit} from '@angular/core';
import { Enigme } from '../../models/enigme.modele';
import {Router, RouterModule} from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {UserRepository} from '../../user/profil/repository/user.repository';
import {User} from '../../models/user.modele';
import {EnigmeRepository} from '../../enigme/repository/enigme.repository';
import {Badge} from '../../models/badge.modele';
import {BadgeRepository} from '../../badge/repository/badge.repository';
import {Post} from '../../models/post.modele';
import {PostRepository} from '../../actualite/repository/post.repository';
import { from, map, toArray } from 'rxjs';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css'],
  imports: [CommonModule, ReactiveFormsModule, RouterModule ]
})
export class AdminDashboardComponent implements OnInit {
  enigmes: Enigme[] = [];
  showMesEnigmes: boolean = false;
  showMesUtilisateur: boolean = false;
  users: User[] = [];
  badges:Badge[]=[];
  showMesBadges:boolean = false;
  showMesPosts:boolean = false;
  posts: Post[] = [];

  //ERREUR BADGE
  error_nomBadge: boolean | undefined;
  error_conditionTypeBadge: boolean | undefined;
  error_conditionValueBadge: boolean | undefined;
  error_comparison_signBadge: boolean | undefined;
  error_numeric_valueBadge: boolean | undefined;

  constructor(
    private userRepository: UserRepository,
    private enigmeRepository: EnigmeRepository,
    private badgeRepository: BadgeRepository,
    private postRepository: PostRepository,
    private router: Router,
    private fb: FormBuilder
  ) {
    //BADGE
    this.badgeForm = this.fb.group({
      nom: ['', Validators.required],
      icone: ['', Validators.required],
      condition_type: ['', Validators.required],
      condition_value: ['', Validators.required],
      comparison_sign: [''],
      numeric_value: ['']
    });
  }

  ngOnInit(): void {
    // ENIGME
    this.showMesEnigmes = true;
    this.loadEnigmes();
    // BADGE
    this.badgeForm.get('condition_type')?.valueChanges.subscribe(value => {
      if (value !== 'THEME') {
        this.badgeForm.get('comparison_sign')?.setValidators([Validators.required]);
        this.badgeForm.get('numeric_value')?.setValidators([Validators.required]);
      } else {
        this.badgeForm.get('comparison_sign')?.clearValidators();
        this.badgeForm.get('numeric_value')?.clearValidators();
      }
      this.badgeForm.get('comparison_sign')?.updateValueAndValidity();
      this.badgeForm.get('numeric_value')?.updateValueAndValidity();
    });
    this.createdBadge = new Badge();
    const modalElement = document.getElementById('createBadgeModal');

    if (modalElement) {
      modalElement.addEventListener('shown.bs.modal', () => {
        this.badgeForm.reset(); // ✅ Réinitialise le formulaire à chaque ouverture
      });
    }
  }

  //------------------------------------------------------------------------------------------------------------------------
  // Enigme

  loadEnigmes(): void {
    this.enigmeRepository.getAllEnigme().subscribe({
      next: (data) => {
        this.enigmes = data;
        console.log(this.enigmes);
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des énigmes :', err);
      }
    });
  }

  createEnigme(): void {
    this.router.navigate(['/create-enigme']);
  }

  modifierEnigme(enigme: Enigme): void {
    this.router.navigate(['modify-enigme/', enigme.id]);
  }

  supprimerEnigme(id: number): void {
    this.enigmeRepository.deleteEnigme(id).subscribe({
      next: () => {
        alert('Enigme supprimée !');
        this.loadEnigmes();
      },
      error: (err) => {
        console.error('Erreur lors de la suppression de l\'enigme :', err);
      }
    })
  }


  onMesEnigmesClicked(): void {
    this.showMesEnigmes = true;
    this.showMesUtilisateur = false;
    this.showMesBadges = false;
    this.loadEnigmes();
  }


  //------------------------------------------------------------------------------------------------------------------------
  // User

  loadUsers(): void {
    this.userRepository.getUsers().subscribe({
      next: (data) => {
        this.users = data;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de users :', err);
      }
    });
  }

  onMesUtilisateursClicked(): void {
    this.showMesEnigmes = false;
    this.showMesUtilisateur = true;
    this.showMesBadges = false;
    this.showMesPosts = false;
    this.loadUsers();
  }

  //------------------------------------------------------------------------------------------------------------------------
  // Badge

  badgeForm: FormGroup;
  createdBadge: Badge | null = null;
  errorMessage: string = '';
  typeBadgeOptions: string[] = ['THEME', 'TEMPS_MOYEN', 'MEILLEUR_TEMPS', 'PIRE_TEMPS', 'TENTATIVES']; // Options disponibles
  themesOptions: string[] = ['CRYPTO', 'WEB_SERVICE', 'TEST'];
  comparisonSigns: string[] = ['<', '>', '<=', '>=', '=']; // Liste des signes de comparaison
  submitted: boolean = false;

  loadBadges(): void {
    this.badgeRepository.getBadges().subscribe({
      next: (data) => {
        this.badges = data;
      },
      error: (err) => {
        console.error('Erreur lors de la sélection des badges :', err);
      }
    });
  }

  isThemeSelected(): boolean {
    return this.badgeForm.get('condition_type')?.value === 'THEME';
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];

    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        try {
          const base64String = (reader.result as string).split(',')[1]; // Supprime le préfixe "data:image/png;base64,"
          this.badgeForm.patchValue({ icone: base64String });
          this.badgeForm.get('icone')?.updateValueAndValidity();
        } catch (error) {
          console.error('Erreur lors de la conversion de l’image en Base64', error);
          alert('Impossible de charger l’image.');
        }
      };
      reader.onerror = () => {
        alert("Erreur lors de la lecture du fichier.");
      };
      reader.readAsDataURL(file);
    }
  }

  createBadge(event: Event ): void {
    event.preventDefault();
    this.submitted = true;

    this.error_nomBadge = false;
    this.error_conditionTypeBadge = false;
    this.error_conditionValueBadge = false;
    this.error_comparison_signBadge = false;
    this.error_numeric_valueBadge = false;

    //APRES ICONE A REVOIR
    //if (!valeurs.icone || valeurs.icone.trim() === "") {
    //  this.nomBadge = true;
    //  return;
    //}

    const valeurs = this.badgeForm.value;
    let b: Badge;
    //ERREUR NOM
    if (!valeurs.nom || valeurs.nom.trim() === "") {
      this.error_nomBadge = true;
      return;
    } else {
      this.error_nomBadge = false;
    }
    // ERREUR CONDITION TYPE
    if (!valeurs.condition_type || valeurs.condition_type.trim() === "") {
      this.error_conditionTypeBadge = true;
      return;
    } else {
      this.error_conditionTypeBadge = false;
    }

    if (valeurs.condition_type == "THEME") {
      // ERREUR CONDITION VALUE
      if (!valeurs.condition_value || valeurs.condition_value.trim() === "") {
        this.error_conditionValueBadge = true;
        return;
      } else {
        this.error_conditionValueBadge = false;
      }

      b = new Badge(
        valeurs.nom,
        valeurs.icone,
        valeurs.condition_type,
        valeurs.condition_value
      );
    } else {
      //CONDITION comparaison sign
      if(!valeurs.comparison_sign || valeurs.comparison_sign.trim() === ""){
        this.error_comparison_signBadge = true;
        return;
      } else {
        this.error_comparison_signBadge = false;
      }

      //CONDITION numeric value
      if(valeurs.numeric_value == null){
        this.error_numeric_valueBadge = true;
        return;
      } else {
        this.error_numeric_valueBadge = false;
      }

      b = new Badge(
        valeurs.nom,
        valeurs.icone,
        valeurs.condition_type,
        "" + valeurs.comparison_sign + valeurs.numeric_value
      );
    }

    this.badgeRepository.createBadge(b).subscribe({
      next: () => {
        //alert('Création de badge réussie !');
        const closeButton = document.getElementById('closeModalButton');
        if (closeButton) {
          closeButton.click();
        }
        this.loadBadges();
      },
      error: (err) => {
        console.error('Erreur lors de la création de badge :', err);
        this.errorMessage = err.error && err.error.message ? 'Erreur : ' + err.error.message : 'Une erreur est survenue';
      }
    })

    console.log('badge créée :', this.badgeForm.value);

  }


  modifierBadge(badge: Badge): void {
    this.router.navigate(['modify-badge/', badge.id]);
  }

  supprimerBadge(id: number): void {
    if (!id) {
      console.error('ID invalide');
      return;
    }

    console.log('ID du badge à supprimer :', id);

    this.badgeRepository.deleteBadge(id).subscribe({
      next: () => {
        alert('Badge supprimé !');
        this.loadBadges(); // Recharger les badges après suppression
      },
      error: (err) => {
        console.error('Erreur lors de la suppression du badge :', err);
      }
    });
  }


  onMesBadgesClicked(): void {
    this.showMesEnigmes = false;
    this.showMesUtilisateur = false;
    this.showMesBadges = true;
    this.showMesPosts = false;
    this.loadBadges();
  }

  //------------------------------------------------------------------------------------------------------------------------
  // Post


  loadPosts(): void {
    this.postRepository.getAllPosts().subscribe({
      next: (data) => {
        this.posts = data;
      },
      error: (err) => {
        console.error('Erreur lors de la sélection des posts :', err);
      }
    });
  }

  createPost(): void {
    this.router.navigate(['/create-post']);
  }

  modifierPost(post: Post): void {
    this.router.navigate(['modify-post/${post.idPost}']);
  }

  supprimerPost(id: number): void {
    this.postRepository.deletePost(id).subscribe({
      next: () => {
        alert('Post supprimée !');
        this.loadPosts() ;
      },
      error: (err) => {
        console.error('Erreur lors de la suppression du post :', err);
      }
    })
  }
  onMesPostClicked(): void {
      this.showMesEnigmes = false;
      this.showMesUtilisateur = false;
      this.showMesBadges = false;
      this.showMesPosts = true;
      this.loadPosts();
    }



}
