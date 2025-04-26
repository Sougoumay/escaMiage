import {Component, OnInit} from '@angular/core';
import {User} from '../../models/user.modele';
import {Router} from '@angular/router';
import {UserRepository} from '../profil/repository/user.repository';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class UserComponent implements OnInit{

  users: User[] = [];

  constructor(private userRepository: UserRepository, private router: Router) {}

  ngOnInit(): void {
    this.userRepository.getUsers().subscribe({
      next: (data) => {
        this.users = data;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de users :', err);
      }
    });
  }
}
