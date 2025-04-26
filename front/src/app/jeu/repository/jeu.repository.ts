import {Injectable} from '@angular/core';
import {UserService} from '../../user/profil/service/user.service';
import {JeuService} from '../service/jeu.service';
import {Observable} from 'rxjs';
import {UserAnswer} from '../../models/userAnswer.modele';

@Injectable({
  providedIn: 'root'
})

export class JeuRepository {


  constructor(private jeuService: JeuService) {}


  demarrerPartie(): Observable<any> {
    return this.jeuService.demarrerPartie();
  }

  annulerPartie(idPartie: number):  Observable<any>{
    return this.jeuService.annulerPartie(idPartie);
  }

  repondreAUnEnigme(idPartie: number, idEnigme: number, reponse: UserAnswer) : Observable<any>{
    return this.jeuService.repondreAUnEnigme(idPartie, idEnigme, reponse);
  }


  consulterEnigme(idPartie: number, idEnigme: number): Observable<any> {
    return this.jeuService.consulterEnigme(idPartie, idEnigme);
  }

  consulterIndice(idPartie: number, idEnigme: number): Observable<any> {
    return this.jeuService.consulterIndice(idPartie, idEnigme);
  }

  repondreAlaQuestionMaster(idPartie: number, reponse : UserAnswer): Observable<any> {
    return this.jeuService.repondreAlaQuestionMaster(idPartie, reponse);
  }

  terminerPartie(idPartie: number): Observable<any> {
    return this.jeuService.terminerPartie(idPartie);
  }

  consulterStatsPartie(idPartie:number): Observable<any>{
    return this.jeuService.consulterStatsPartie(idPartie)
  }

}
