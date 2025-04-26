 import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import {environnementsBack} from '../../../environnementsBack';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private urlConnexion = environnementsBack.apiUrlConnexion;

  constructor(private http: HttpClient) { }

  login(infoUser: { email: string, password: string }): Observable<HttpResponse<any>> {
    return this.http.post(`${this.urlConnexion}`, infoUser, {observe: 'response'});
  }

}
