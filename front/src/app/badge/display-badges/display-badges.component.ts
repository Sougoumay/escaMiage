import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {Badge} from '../../models/badge.modele';
import {Router} from '@angular/router';
import {BadgeRepository} from '../repository/badge.repository';

@Component({
  selector: 'app-display-badges',
  templateUrl: './display-badges.component.html',
  styleUrl: './display-badges.component.css',
  imports: [CommonModule, ReactiveFormsModule]
})
export class DisplayBadgesComponent implements OnInit{

  badges: Badge[] = [];

  constructor(private badgeRepository: BadgeRepository, private router: Router) {}

  ngOnInit(): void {
    this.badgeRepository.getBadges().subscribe({
      next: (data) => {
        this.badges = data;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de badges :', err);
      }
    });
  }

}



