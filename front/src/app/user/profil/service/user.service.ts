import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {map, Observable, of} from 'rxjs';
import {environnementsBack} from '../../../environnementsBack';
import {User} from '../../../models/user.modele';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private url = environnementsBack.apiUrlInscription;

  constructor(private http: HttpClient) { }


  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.url);
  }


  getUserProfil(id: string): Observable<User> {
    return this.http.get<User>(`${this.url}/${id}`);
  }

  updateUser(id: string, user: User): Observable<HttpResponse<User>> {
    return this.http.put<User>(`${this.url}/${id}`, user, { observe: 'response' });
  }

  deleteUser(id: string): Observable<HttpResponse<User>> {
    return this.http.delete<User>(`${this.url}/${id}`, { observe: 'response' });
  }

}

