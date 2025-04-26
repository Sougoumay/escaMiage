import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {environnementsBack} from '../../environnementsBack';
import {Post} from '../../models/post.modele';
import {ReactionUtilisateurDTO} from '../../models/reaction-utilisateur.modele';

@Injectable({
  providedIn: 'root'
})

export class  PostService {

  private url = environnementsBack.apiUrlPost;
  constructor( private http: HttpClient) { }

  // getAllPosts(): Observable<any> {
  //   return this.http.get(`${this.url}`);
  // }


  getAllPosts(): Observable<any> {
    const token = 'eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiMSIsImV4cCI6MTc0MzYxNDg5NSwiaWF0IjoxNzQzNjExMjk1LCJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsInNjb3BlIjoiQURNSU4ifQ.pDcvhEqccb3Ru1Fif6YA9ecjabUGiDRMFUwyYudC1JEj_cnef3Ul_zqdtIxsQr18mL3zLPTWFz2AN-H4uGgV5xykenvXsgf5gf6ospEDUdhD8_e2VhNN_wJ0E4sj684-RdMwnEg2thE556ElO0dys638fL9YxDqSmkrppxkMUWgYtLKFJo7p_OsPUF1XOOHHUaKtNtgH2oDo-kaj0NzCYuDv_t1VU1csecEGIqGi5AbJ6Qgkt7jwFRa53AvCJ8raAsQ5Bn6Neol62fVn4NUP9nO3WBRTb7B3EUr9LMYiQdq2OfWcJ5j2tQjBpuLPGjC8weXkGEup6wLuOT61tREA7Q'; // Remplace par ton vrai token

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.http.get(`${this.url}`, { headers });
  }



  makePost(post: Post): Observable<any>{
    const token = 'eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiMSIsImV4cCI6MTc0MzYxNDg5NSwiaWF0IjoxNzQzNjExMjk1LCJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsInNjb3BlIjoiQURNSU4ifQ.pDcvhEqccb3Ru1Fif6YA9ecjabUGiDRMFUwyYudC1JEj_cnef3Ul_zqdtIxsQr18mL3zLPTWFz2AN-H4uGgV5xykenvXsgf5gf6ospEDUdhD8_e2VhNN_wJ0E4sj684-RdMwnEg2thE556ElO0dys638fL9YxDqSmkrppxkMUWgYtLKFJo7p_OsPUF1XOOHHUaKtNtgH2oDo-kaj0NzCYuDv_t1VU1csecEGIqGi5AbJ6Qgkt7jwFRa53AvCJ8raAsQ5Bn6Neol62fVn4NUP9nO3WBRTb7B3EUr9LMYiQdq2OfWcJ5j2tQjBpuLPGjC8weXkGEup6wLuOT61tREA7Q'; // Remplace par ton vrai token

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post(`${this.url}`, post, {headers, observe: 'response'});
  }


  deletePost(id: number) {
    return this.http.delete(`${this.url}/${id}`);
  }

  modifyPost(id:number,post: Post) {
    const token = 'eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiMSIsImV4cCI6MTc0MzYxNDg5NSwiaWF0IjoxNzQzNjExMjk1LCJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsInNjb3BlIjoiQURNSU4ifQ.pDcvhEqccb3Ru1Fif6YA9ecjabUGiDRMFUwyYudC1JEj_cnef3Ul_zqdtIxsQr18mL3zLPTWFz2AN-H4uGgV5xykenvXsgf5gf6ospEDUdhD8_e2VhNN_wJ0E4sj684-RdMwnEg2thE556ElO0dys638fL9YxDqSmkrppxkMUWgYtLKFJo7p_OsPUF1XOOHHUaKtNtgH2oDo-kaj0NzCYuDv_t1VU1csecEGIqGi5AbJ6Qgkt7jwFRa53AvCJ8raAsQ5Bn6Neol62fVn4NUP9nO3WBRTb7B3EUr9LMYiQdq2OfWcJ5j2tQjBpuLPGjC8weXkGEup6wLuOT61tREA7Q'; // Remplace par ton vrai token

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.put(`${this.url}/${id}`, post);
  }

  reactOnPost(idPost: number, reaction: ReactionUtilisateurDTO) {
    return this.http.put(`${this.url}/${idPost}/reaction`, reaction);
  }


  getById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.url}/${id}`);
  }

}
