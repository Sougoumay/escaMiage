import { Injectable } from '@angular/core';
import { LoginService } from '../service/login.service';
import { Observable } from 'rxjs';
import {HttpResponse} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginRepository {
  constructor(private loginService: LoginService) {}


  login(email: string, password: string): Observable<HttpResponse<any>> {
    return this.loginService.login({ email, password });
  }

}
