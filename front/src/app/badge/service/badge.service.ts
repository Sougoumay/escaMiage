import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Badge} from '../../models/badge.modele';
import {environnementsBack} from '../../environnementsBack';

@Injectable({
  providedIn: 'root'
})
export class BadgeService {
  private url = environnementsBack.apiUrlBadge;

  constructor(private http: HttpClient) { }


  getAll(): Observable<Badge[]> {
    return this.http.get<Badge[]>(this.url);
  }

  create(badge: Badge): Observable<any> {
    return this.http.post(this.url, badge );
  }

  getById(id: string): Observable<Badge> {
    return this.http.get<Badge>(this.url + '/' + id);}

  delete(id: number): Observable<any> {
    return this.http.delete(this.url + '/' + id);
  }

  getClassement(): Observable<any> {
    return this.http.get(this.url + '/classement');
  }

  getClassementHebdo(): Observable<any> {
    return this.http.get(this.url + '/classement-hebdo');
  }
}

