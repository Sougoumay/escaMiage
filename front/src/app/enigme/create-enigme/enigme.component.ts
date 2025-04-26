import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Difficulte} from '../../models/difficulte.enum';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';
import {EnigmeRepository} from '../repository/enigme.repository';
import {Theme} from '../../models/theme.enum';


@Component({
  selector: 'app-enigme',
  templateUrl: './enigme.component.html',
  styleUrls: ['./enigme.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class EnigmeComponent {
  enigmeForm: FormGroup;
  difficultes = Object.values(Difficulte);
  themes = Object.values(Theme);
  errorMessage = '';

  constructor(private fb: FormBuilder, private enigmeRepo: EnigmeRepository, private router: Router) {
    this.enigmeForm = this.fb.group({
      question: ['', Validators.required],
      reponse: ['', Validators.required],
      difficulte: ['', Validators.required],
      indice: ['', Validators.required],
      theme: ['', Validators.required]
    });
  }

  creation(event: Event) {
    event.preventDefault();

    if (this.enigmeForm.invalid) {
      alert('Veuillez remplir tous les champs correctement.');
      return;
    }

    const enigme = this.enigmeForm.value;

    this.enigmeRepo.createEnigme(enigme).subscribe({
      next: () => {
        alert('Création d\'enigme réussie !');
        this.router.navigate(['/admin-dashboard']);
      },
      error: (err) => {
        console.error('Erreur lors de la création d\'enigme :', err);
        this.errorMessage = err.error && err.error.message ? 'Erreur : ' + err.error.message : 'Une erreur est survenue';
      }
    })

    console.log('Enigme créée :', this.enigmeForm.value);
    alert('Enigme créée avec succès !');


  }
}
