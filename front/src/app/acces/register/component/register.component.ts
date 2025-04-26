import { Component } from '@angular/core';
import {RegisterRepository} from '../repository/register.repository';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {GestionTokenService} from '../../../gestionToken/gestionToken.service';
import {ParamService} from '../../../user/profil/service/param.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage = '';
  gestionToken = new GestionTokenService();
  errNom: boolean = false;
  errPrenom: boolean = false;
  errEmail: boolean = false;
  errDate: boolean = false;
  errMdp: boolean = false;
  errConfirmMpd: boolean = false;
  errNeMatchePas: boolean = false;

  ngOnInit(): void {
  }


  constructor(private fb: FormBuilder, private registerRepo: RegisterRepository, private router: Router, private paramService: ParamService) {
    this.registerForm = this.fb.group({
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]],
      dateNaissance: ['', Validators.required],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=]).*$/)
      ]],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator }) ;
  }

  passwordMatchValidator(form: FormGroup) {
    return form.get('password')?.value === form.get('confirmPassword')?.value
      ? null : { mismatch: true };
  }

  // à voir comment intégrer cette fonction quand on focus sur les input
  makeErrFalse(err: String){
    switch(err){
      case "nom": this.errNom = false; break;
      case "prenom": this.errPrenom = false; break;
      case "date": this.errDate = false; break;
      case "email": this.errEmail = false; break;
      case "mdp": this.errMdp = false; break;
      case "conf": this.errConfirmMpd = false; break;
    }
  }


  register(event: Event) {
    event.preventDefault();

    this.errNom= false;
    this.errPrenom = false;
    this.errEmail = false;
    this.errDate = false;
    this.errMdp = false;

    console.log(73);

    const controls = this.registerForm.controls;
    if (controls['nom'].invalid) {
      this.errNom = true;
      return;
    }
    if (controls['prenom'].invalid) {
      this.errPrenom = true;
      return;
    }
    if (controls['email'].invalid) {
      this.errEmail = true;
      return;
    }
    if (controls['dateNaissance'].invalid) {
      this.errDate = true;
      return;
    }
    if (controls['password'].invalid) {
      this.errMdp = true;
      return;
    }
    if (controls['confirmPassword'].invalid) {
      this.errConfirmMpd = true;
      return;
    }

    const user = { ...this.registerForm.getRawValue() };  // Récupère tous les champs du formulaire
    delete user.confirmPassword;  // Supprime le champ confirmPassword

    console.log(user); 

    //const user = this.registerForm.value;
    


    this.registerRepo.register(user).subscribe({
          next: (response) => {
            const token = response.headers.get('Authorization');
            console.log(113);
            console.log(response);
          if (token) {
            this.gestionToken.setToken(token);
            console.log('Token récupéré et stocké :', token);
            console.log(117);
            const tokenParts = token.split('.');
            const decodedPayload = JSON.parse(atob(tokenParts[1]));
            console.log(decodedPayload);
            const userRole = decodedPayload.scope; // Assurez-vous que le rôle est dans le token
            console.log(userRole);
            // Redirection basée sur le rôle
            if (userRole === 'ADMIN') {
              this.router.navigate(['/admin-dashboard']);
            } else if (userRole === 'JOUEUR') {
              this.router.navigate(['/user-dashboard']);
            }

        } else {
          console.warn('Token non présent dans l’en-tête de la réponse');
        }
          },
      error: (err) => {
        console.error('Erreur lors de l\'inscription :', err);
        this.errorMessage = err.error && err.error.message ? 'Erreur : ' + err.error.message : 'Une erreur est survenue';
      }
        });

  }

}
