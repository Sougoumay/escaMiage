import { Injectable } from '@angular/core';
import { RegisterService } from '../service/register.service';
import { Observable } from 'rxjs';
import {User} from '../../../models/user.modele';
import {HttpResponse} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegisterRepository {
  constructor(private registerService: RegisterService) {}


  register(user: User): Observable<HttpResponse<any>> {
    return this.registerService.register(user);
  }

}
