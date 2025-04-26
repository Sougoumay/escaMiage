import {Component, Input, OnInit} from '@angular/core';
import {Post} from '../../models/post.modele';
import {PostRepository} from '../repository/post.repository';
import {ReactionUtilisateurDTO} from '../../models/reaction-utilisateur.modele';
import {ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';


@Component({
  selector: 'app-react-post',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './react-post.component.html',
  styleUrl: './react-post.component.css'
})

/** ----------------------------------------------------------------------------------------------------------- **/
/** composant non testé **/

export class ReactPostComponent implements OnInit{

  posts: Post[] = [];
  userId: number | null = null;

  constructor(private postRepository: PostRepository) {}


  ngOnInit() {
    this.userId = 0; // TODO: Remplacer par l'utilisateur connecté
    this.loadPosts();}

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

  react(post: Post, reactionType: string) {
      const reaction: ReactionUtilisateurDTO = {
        idUtilisateur: this.userId,
        postDTO: post,
        typeReaction: reactionType
      };

    this.postRepository.reactOnPost(post.idPost!, reaction).subscribe({
      next: () => {
        if (post.reactionDTOList) {
          post.reactionDTOList = [];
        }
        post.reactionDTOList?.push(reaction)
      },
      error: (err) => console.error("Erreur lors de la réaction:", err)
    });
  }




}
