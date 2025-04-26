import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import {Enigme} from '../../../models/enigme.modele';
import {UserService} from '../service/user.service';
import {User} from '../../../models/user.modele';
import {HttpResponse} from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})

export class UserRepository {
  constructor(private userService: UserService) {}


  getUsers(): Observable<User[]> {
    return this.userService.getAll();
  }

  getProfil(id: string): Observable<User> {
    return this.userService.getUserProfil(id);
  }

}
