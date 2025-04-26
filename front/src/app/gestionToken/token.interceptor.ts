import {inject} from '@angular/core';
import {
  HttpRequest,
  HttpEvent,
  HttpHeaders, HttpHandlerFn
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {GestionTokenService} from './gestionToken.service';

export function TokenInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
  const auth = inject(GestionTokenService);
  const token = auth.getToken()

  if (!req.url.includes('/connexion')) {
    if (!token) {
      return next(req)
    }

    const headers = new HttpHeaders({
      Authorization: token
    })

    const newReq = req.clone({
      headers
    })

    return next(newReq)
  }
  return next(req);
}
