import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../../../../models/user.modele';
import {UserRepository} from '../../repository/user.repository';
import {CommonModule} from '@angular/common';
import {ParamService} from '../../service/param.service';

@Component({
  selector: 'app-display-profil',
  imports: [ReactiveFormsModule, CommonModule],
  standalone: true,
  templateUrl: './display-profil.component.html',
  styleUrl: './display-profil.component.css'
})
export class DisplayProfilComponent implements OnInit {

  displayForm: FormGroup;
  isLoading: boolean = true;
  errorMessage: string = '';
  userId!: string ;
  currentUser!: User;


  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private userRepository: UserRepository,
    private router: Router,
    private paramService: ParamService
  ) {
    this.displayForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: ['', Validators.required],
      dateNaissance: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUser()
  }

  loadUser(): void {
    this.paramService.id$.subscribe(value => this.userId = value);
    const id = this.userId;
    this.userRepository.getProfil(id).subscribe({
      next: (user: any) => {
        this.currentUser = user;
        console.log(user);
        this.displayForm.patchValue({
          nom: user.nom,
          prenom: user.prenom,
          email: user.email,
          dateNaissance: user.dateNaissance
        })  ;
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors du chargement des données de l'utilisateur.";
        this.isLoading = false;
      }
    });
  }

  goToModifyProfil(): void {
    this.router.navigate(['/modify-profil']);
  }

}
