import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../../../../models/user.modele';
import {UserService} from '../../service/user.service';
import {ParamService} from '../../service/param.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-modify-profil',
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './modify-profil.component.html',
  styleUrl: './modify-profil.component.css'
})
export class ModifyProfilComponent implements OnInit {
  updateForm: FormGroup;
  userId!: string;
  isLoading: boolean = true;
  errorMessage: string = '';

  currentUser!: User;


  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private userService: UserService,
    private paramService: ParamService,
    private router: Router
  ) {
    this.updateForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: ['', Validators.required],
      dateNaissance: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadUser();
  }

  loadUser(): void {
    this.paramService.id$.subscribe(value => this.userId = value);
    const id = this.userId;
    this.userService.getUserProfil(id).subscribe({
      next: (user: any) => {
        this.currentUser = user;
        this.updateForm.patchValue({
          nom: user.nom,
          prenom: user.prenom,
          email: user.email,
          dateNaissance: user.dateNaissance
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors du chargement de l'utilisateur.";
        this.isLoading = false;
      }
    });
  }


  updateUser(): void {
    if (this.updateForm.invalid) {
      this.errorMessage = "Veuillez corriger les erreurs dans le formulaire.";
      return;
    }

    const updatedUser: User  = {
      dateNaissance: this.updateForm.get('dateNaissance')?.value,
      email: this.updateForm.get('email')?.value,
      image: this.currentUser.image,
      nom: this.updateForm.get('nom')?.value,
      prenom: this.updateForm.get('prenom')?.value,
    };
    this.paramService.id$.subscribe(value => this.userId = value);
    const id = this.userId;;

    this.userService.updateUser(id, updatedUser).subscribe({
      next: () => {
        alert("L'utilisateur a été mise à jour avec succès !");
        this.router.navigate(['/admin-dashboard']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors de la mise à jour de l'utilisateur.";
      }
    });
  }

  deleteUser(): void {
    this.paramService.id$.subscribe(value => this.userId = value);
    const id = this.userId;
    this.userService.deleteUser(id).subscribe({
      next: () => {
        alert("L'utilisateur a été supprimé avec succès !");
        this.router.navigate(['/admin-dashboard']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors de la suppression de l'utilisateur.";
      }
    });
  }
}
