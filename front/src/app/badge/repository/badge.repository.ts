import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import {Badge} from '../../models/badge.modele';
import {BadgeService} from '../service/badge.service';


@Injectable({
  providedIn: 'root'
})

export class BadgeRepository {
  constructor(private badgeService: BadgeService) {}


  getBadges(): Observable<Badge[]> {
    return this.badgeService.getAll();
  }

  createBadge(badge: any) {
    return this.badgeService.create(badge);
  }

  deleteBadge(id: number) {
    return this.badgeService.delete(id);
  }

  getClassement(): Observable<any> {
    return this.badgeService.getClassement();
  }

  getClassementHebdo(): Observable<any> {
    return this.badgeService.getClassementHebdo();
  }
}
