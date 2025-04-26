import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {PostRepository} from '../repository/post.repository';
import {Post} from '../../models/post.modele';

@Component({
  selector: 'app-modify-post',
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './modify-post.component.html',
  styleUrl: './modify-post.component.css'
})

/** ----------------------------------------------------------------------------------------------------------- **/
/** composant non testé **/

export class ModifyPostComponent implements OnInit {

  updateForm: FormGroup;
  postId!: number;
  isLoading: boolean = true;
  errorMessage: string = '';

  currentPost!: Post;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private postRepository: PostRepository,
    private router: Router
  ) {
    this.updateForm = this.fb.group({
      contenu: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Récupère l'id du post depuis l'URL
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.postId = 56;
        this.loadPost(this.postId);
      } else {
        this.errorMessage = "Aucun identifiant du post fourni.";
        this.isLoading = false;
      }
    });
  }

  loadPost(id: number): void {
    this.postRepository.getById(id).subscribe({
      next: (post: Post) => {
        this.currentPost = post;
        this.updateForm.patchValue({
          contenu: post.contenu
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors du chargement du post";
        this.isLoading = false;
      }
    });
  }


  updatePost(): void {
    if (this.updateForm.invalid) {
      this.errorMessage = "Veuillez corriger les erreurs dans le formulaire.";
      return;
    }

    const updatedPost: Post = {
      idPost: this.postId,
      reactionDTOList: [],
      utilisateurDTO: 0, //TODO : changer par l'utilisateur courant
      contenu: this.updateForm.get('contenu')?.value
    };

    this.postRepository.modifyPost(this.postId, updatedPost).subscribe({
      next: () => {
        alert("Le post a été mis à jour avec succès !");
        this.router.navigate(['/admin-dashboard']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors de la mise à jour du post.";
      }
    });
  }

}
