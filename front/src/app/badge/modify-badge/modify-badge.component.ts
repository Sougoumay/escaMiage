
import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {CommonModule} from '@angular/common';
import {ConditionType} from '../../models/condition_type.enum';
import {Badge} from '../../models/badge.modele';
import {BadgeService} from '../service/badge.service';

@Component({
  selector: 'app-modify-enigme',
  templateUrl: './modify-badge.component.html',
  styleUrls: ['./modify-badge.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})



export class ModifyBadgeComponent implements OnInit {
  updateForm: FormGroup;
  badgeId!: number;
  isLoading: boolean = true;
  errorMessage: string = '';

  currentBadge!: Badge;
  condition_types = Object.values(ConditionType);

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private badgeService: BadgeService,
    private router: Router
  ) {
    this.updateForm = this.fb.group({
      nom: ['', Validators.required],
      icone: ['', Validators.required],
      condition_type: ['', Validators.required],
      condition_value: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Récupère l'id du badge depuis l'URL
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.badgeId = +idParam;
        this.loadBadge(this.badgeId);
      } else {
        this.errorMessage = "Aucun identifiant d'énigme fourni.";
        this.isLoading = false;
      }
    });
  }

  loadBadge(id: number): void {
    this.badgeService.getById(id).subscribe({
      next: (enigme: Enigme) => {
        this.currentEnigme = enigme;
        this.updateForm.patchValue({
          question: enigme.question,
          reponse: enigme.reponse,
          difficulte: enigme.difficulte,
          indice: enigme.indice,
          theme: enigme.theme
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors du chargement de l'énigme.";
        this.isLoading = false;
      }
    });
  }


  updateEnigme(): void {
    if (this.updateForm.invalid) {
      this.errorMessage = "Veuillez corriger les erreurs dans le formulaire.";
      return;
    }

    const updatedEnigme: Enigme = {
      id: this.enigmeId,
      question: this.updateForm.get('question')?.value,
      reponse: this.updateForm.get('reponse')?.value,
      difficulte: this.updateForm.get('difficulte')?.value,
      indice: this.updateForm.get('indice')?.value,
      theme: this.updateForm.get('theme')?.value
    };

    this.enigmeService.update(this.enigmeId, updatedEnigme).subscribe({
      next: () => {
        alert("L'énigme a été mise à jour avec succès !");
        this.router.navigate(['/admin-dashboard']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors de la mise à jour de l'énigme.";
      }
    });
  }
}
