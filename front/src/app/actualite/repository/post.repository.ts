import {PostService} from '../service/post.service';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {ReactionUtilisateurDTO} from '../../models/reaction-utilisateur.modele';
import {Post} from '../../models/post.modele';
import {HttpResponse} from '@angular/common/http';


@Injectable({providedIn: 'root'})
export class PostRepository {

  constructor(private postService: PostService) {
  }


  getAllPosts(): Observable<any> {
    return this.postService.getAllPosts();
  }

  createPost(post: Post): Observable<any> {
    return this.postService.makePost(post);
  }

  deletePost(id: number): Observable<any> {
    return this.postService.deletePost(id);
  }

  modifyPost(id:number,post: Post): Observable<any> {
    return this.postService.modifyPost(id,post);
  }

  getById(id: number): Observable<Post> {
    return this.postService.getById(id);
  }

  reactOnPost(idPost: number, reaction: ReactionUtilisateurDTO): Observable<any> {
    return this.postService.reactOnPost(idPost, reaction);
  }

}

