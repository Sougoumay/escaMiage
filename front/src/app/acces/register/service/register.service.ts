import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../../../models/user.modele';
import {environnementsBack} from '../../../environnementsBack';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private urlInscription = environnementsBack.apiUrlInscription;

  constructor(private http: HttpClient) { }

  register(user: User): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.post(`${this.urlInscription}`, user,{headers, observe: 'response'});
  }
}


