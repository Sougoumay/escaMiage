import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GestionTokenService {
  private tokenKey = 'auth_token';
  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  clearToken(): void {
    localStorage.removeItem(this.tokenKey);
  }
}
