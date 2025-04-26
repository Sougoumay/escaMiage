import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {FeedbackSuggestionRepository} from './repository/feedback-suggestion.repository';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-feedback-suggestions',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './feedback-suggestions.component.html',
  styleUrl: './feedback-suggestions.component.css'
})

/** ----------------------------------------------------------------------------------------------------------- **/
/** composant non testé **/


export class FeedbackSuggestionsComponent {

  suggFeedForm: FormGroup;
  errorMessage='';



  constructor( private fb: FormBuilder, private suggestionFeedBackRepo : FeedbackSuggestionRepository, private router: Router) {
    this.suggFeedForm = this.fb.group({
      besoin: ['', Validators.required]
    })
  }

  creation(event : Event){
    event.preventDefault()

    if (this.suggFeedForm.invalid) {
      alert('Veuillez remplir le formulaire correctement.');
      return;
    }

    const feedback = this.suggFeedForm.value;
    this.suggestionFeedBackRepo.createSuggestion(feedback).subscribe({
      next: () => {
        alert('Création de feedback réussie !');
        this.router.navigate(['/user-dashboard']);
      },
      error: (err) => {
        console.error('Erreur lors de la création feedback :', err);
        this.errorMessage = err.error && err.error.message ? 'Erreur : ' + err.error.message : 'Une erreur est survenue';
      }
    })

  }

}
