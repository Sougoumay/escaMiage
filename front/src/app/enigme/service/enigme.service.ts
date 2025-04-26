import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environnementsBack} from '../../environnementsBack';

import {Enigme} from '../../models/enigme.modele';


@Injectable({
  providedIn: 'root'
})
export class EnigmeService {
  private urlEnigme = environnementsBack.apiUrlEnigme;

  constructor(private http: HttpClient) {
  }

  create(enigme: Enigme): Observable<any> {
    return this.http.post(`${this.urlEnigme}`, enigme);
  }

  update(id: number, enigme: Enigme): Observable<any> {
    return this.http.put(`${this.urlEnigme}/${id}`, enigme);
  }




  delete(id: number): Observable<any> {
    return this.http.delete(`${this.urlEnigme}/${id}`);
  }

  getById(id: number): Observable<Enigme> {
    return this.http.get<Enigme>(`${this.urlEnigme}/${id}`);
  }

  getAll(): Observable<Enigme[]> {
    return this.http.get<Enigme[]>(this.urlEnigme);
  }



}
