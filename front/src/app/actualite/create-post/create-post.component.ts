import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {PostRepository} from '../repository/post.repository';
import {CommonModule} from '@angular/common';
import {Post} from '../../models/post.modele';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})

/** ----------------------------------------------------------------------------------------------------------- **/
/** composant non testé **/

export class CreatePostComponent {

  postForm: FormGroup;
  errorMessage: string = '';


  constructor(private fb: FormBuilder, private postRepo: PostRepository , private router: Router) {
    this.postForm = this.fb.group({
      contenu: ['', Validators.required]
    });
  }

  createPost(event: Event ): void {
    event.preventDefault();

    if (this.postForm.invalid) {
      alert('Veuillez remplir tous les champs correctement.');
      return;
    }

    const post = this.postForm.value;

    this.postRepo.createPost(post).subscribe({
      next: () => {
        alert('Création de post réussie !');
        this.router.navigate(['/admin-dashboard']);
      },
      error: (err) => {
        console.error('Erreur lors de la création de post :', err);
        this.errorMessage = err.error && err.error.message ? 'Erreur : ' + err.error.message : 'Une erreur est survenue';
      }
    })

  }


}
