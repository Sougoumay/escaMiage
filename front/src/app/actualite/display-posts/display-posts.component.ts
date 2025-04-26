import {Component, OnInit} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';
import {Post} from '../../models/post.modele';
import {PostRepository} from '../repository/post.repository';

@Component({
  selector: 'app-display-posts',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './display-posts.component.html',
  styleUrl: './display-posts.component.css'
})

/** ----------------------------------------------------------------------------------------------------------- **/
/** composant non testé **/

export class DisplayPostsComponent implements OnInit {

  posts: Post[] = [];

  constructor(private postRepository: PostRepository, private router: Router) {}

  ngOnInit(): void {
    this.loadPosts();
  }

  loadPosts(): void {
    this.postRepository.getAllPosts().subscribe({
      next: (data) => {
        this.posts = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des posts:', err);
      }
    });
  }

  // Convertit l'image Base64 en URL affichable
  getImageUrl(imageBase64: any): string {
    return imageBase64 ? `data:image/png;base64,${imageBase64}` : 'assets/no-image.png';
  }

  modifierPost(id: number) {
    return this.router.navigate(['/modify-post/${id}']);
  }

  supprimerPost(id: number) {
    this.postRepository.deletePost(id).subscribe({
      next: () => {
        alert('Post supprimée !');
        this.loadPosts();
      },
      error: (err) => {
        console.error('Erreur lors de la suppression du post:', err);
      }
    });
  }

}
