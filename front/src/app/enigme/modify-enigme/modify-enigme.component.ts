
import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EnigmeService } from '../service/enigme.service';
import { Enigme } from '../../models/enigme.modele';
import { Difficulte } from '../../models/difficulte.enum';
import { Theme } from '../../models/theme.enum';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-modify-enigme',
  templateUrl: './modify-enigme.component.html',
  styleUrls: ['./modify-enigme.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})



export class ModifyEnigmeComponent implements OnInit {
  updateForm: FormGroup;
  enigmeId!: number;
  isLoading: boolean = true;
  errorMessage: string = '';

  currentEnigme!: Enigme;
  difficultes = Object.values(Difficulte);
  themes = Object.values(Theme);

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private enigmeService: EnigmeService,
    private router: Router
  ) {
    this.updateForm = this.fb.group({
      question: ['', Validators.required],
      reponse: ['', Validators.required],
      difficulte: ['', Validators.required],
      indice: ['', Validators.required],
      theme: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Récupère l'id de l'énigme depuis l'URL
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.enigmeId = +idParam;
        this.loadEnigme(this.enigmeId);
      } else {
        this.errorMessage = "Aucun identifiant d'énigme fourni.";
        this.isLoading = false;
      }
    });
  }

  loadEnigme(id: number): void {
    this.enigmeService.getById(id).subscribe({
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
