import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ParamService {
  private idSubject = new BehaviorSubject<string>('');

  id$ = this.idSubject.asObservable();

  setParams( id: string) {
    this.idSubject.next(id);
  }
}
