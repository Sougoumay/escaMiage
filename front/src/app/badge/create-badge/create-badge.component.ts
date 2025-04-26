import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {CommonModule} from '@angular/common';

import {Router} from '@angular/router'; // Assurez-vous que le chemin est correct
import { Badge } from '../../models/badge.modele';
import { BadgeRepository } from '../repository/badge.repository';

declare var bootstrap: any;


@Component({
  selector: 'app-create-badge',
  templateUrl: './create-badge.component.html',
  styleUrls: ['./create-badge.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class CreateBadgeComponent
{
  badgeForm: FormGroup;
  createdBadge: Badge | null = null;
  errorMessage: string = '';
  typeBadgeOptions: string[] = ['THEME', 'TEMPS_MOYEN', 'MEILLEUR_TEMPS', 'PIRE_TEMPS', 'TENTATIVES']; // Options disponibles
  themesOptions: string[] = ['CRYPTO', 'WEB_SERVICE', 'TEST'];
  comparisonSigns: string[] = ['<', '>', '<=', '>=', '=']; // Liste des signes de comparaison
  submitted: boolean = false; 

   @ViewChild('myModal', { static: false }) modalElement!: ElementRef;
   private modalInstance: any;

   ngAfterViewInit() {
     this.modalInstance = new bootstrap.Modal(this.modalElement.nativeElement);
   }
 
   openModal() {
     this.modalInstance.show();
   }
 
   closeModal() {
     this.modalInstance.hide();
   }

  ngOnInit(): void {
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
  }

  constructor(private fb: FormBuilder, private badgeRepo: BadgeRepository , private router: Router) {
    this.badgeForm = this.fb.group({
      nom: ['', Validators.required],
      icone: ['', Validators.required],
      condition_type: ['', Validators.required],
      condition_value: ['', Validators.required],
      comparison_sign: [''],
      numeric_value: ['']
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

    if (this.badgeForm.invalid) {
      return; // Si le formulaire est invalide, ne rien faire
    }

    const valeurs = this.badgeForm.value;
    let b: Badge;

    if (valeurs.condition_type == "THEME") {
      b = new Badge(
        valeurs.nom,
        valeurs.icone,
        valeurs.condition_type,
        valeurs.condition_value
      );
    } else {
      b = new Badge(
        valeurs.nom,
        valeurs.icone,
        valeurs.condition_type,
        "" + valeurs.comparison_sign + valeurs.numeric_value
      );
    }
    
    console.log("b = " + b);
    console.log(b);

    this.badgeRepo.createBadge(b).subscribe({
      next: () => {
        alert('Création de badge réussie !');
        this.router.navigate(['/badges']);
      },
      error: (err) => {
        console.error('Erreur lors de la création de badge :', err);
        this.errorMessage = err.error && err.error.message ? 'Erreur : ' + err.error.message : 'Une erreur est survenue';
      }
    })

    console.log('badge créée :', this.badgeForm.value);

  }
}