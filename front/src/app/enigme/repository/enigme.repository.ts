import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {Enigme} from '../../models/enigme.modele';
import {EnigmeService} from '../service/enigme.service';



@Injectable({
  providedIn: 'root'
})

export class EnigmeRepository {
  constructor(private enigmeService: EnigmeService) {}


  createEnigme(enigme: Enigme): Observable<any> {
    return this.enigmeService.create(enigme);
  }

  getAllEnigme(): Observable<Enigme[]> {
    return this.enigmeService.getAll();
  }

  getEnigmeById(id: number): Observable<Enigme> {
    return this.enigmeService.getById(id);}

  deleteEnigme(id: number): Observable<any> {
    return this.enigmeService.delete(id);
  }

}
