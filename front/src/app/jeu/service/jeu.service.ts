import {Injectable} from '@angular/core';
import {environnementsBack} from '../../environnementsBack';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../../models/user.modele';
import {UserAnswer} from '../../models/userAnswer.modele';

@Injectable({
  providedIn: 'root'
})
export class JeuService {


  private url = environnementsBack.apiUrlJeu;

  constructor(private http: HttpClient) { }

  /** 🔐 Méthode privée pour récupérer le token */
  private getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  /** 🔐 Méthode privée pour générer les headers avec le token */
  private getHeaders(): HttpHeaders {
    return new HttpHeaders().set('Authorization', `${this.getToken()}`);
  }


  demarrerPartie(): Observable<HttpResponse<any>> {
    return this.http.post(`${this.url}` , {}, {headers: this.getHeaders(), observe: 'response' });
  }


  annulerPartie(idPartie: number):  Observable<HttpResponse<any>>{
    return this.http.put(`${this.url}/${idPartie}`, {}, { headers: this.getHeaders(), observe: 'response' });
  }

  repondreAUnEnigme(idPartie: number, idEnigme: number, reponse: UserAnswer) : Observable<HttpResponse<any>>{
    return this.http.put(`${this.url}/${idPartie}/enigmes/${idEnigme}`, reponse, {headers: this.getHeaders(), observe: 'response' });
  }

  consulterEnigme(idPartie: number, idEnigme: number): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/${idPartie}/enigmes/${idEnigme}`, {headers: this.getHeaders(), observe: 'response' });
  }

  consulterIndice(idPartie: number, idEnigme: number): Observable<HttpResponse<any>> {
    return this.http.get(`${this.url}/${idPartie}/enigmes/${idEnigme}/indice`, {headers: this.getHeaders(), observe: 'response' });
  }

  repondreAlaQuestionMaster(idPartie: number, reponse : UserAnswer): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/${idPartie}/master`, reponse, {headers: this.getHeaders(), observe: 'response' });
  }

  terminerPartie(idPartie: number): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/${idPartie}/fin`, {}, {headers: this.getHeaders(), observe: 'response' });
  }

  consulterStatsPartie(idPartie: number): Observable<HttpResponse<any>> {
    return this.http.put(`${this.url}/${idPartie}/stats`, {}, {headers: this.getHeaders(), observe: 'response' })
  }

}
