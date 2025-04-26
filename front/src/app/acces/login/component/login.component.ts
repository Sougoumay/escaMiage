import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginRepository } from '../repository/login.repository';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {GestionTokenService} from '../../../gestionToken/gestionToken.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class LoginComponent {

  loginForm: FormGroup;
  errorMessage = '';
  gestionToken = new GestionTokenService();
  submitted: boolean = false;
  error_login: boolean = false;
  error_mdp: boolean = false;
  phrasesLogin: string[] = [
    "L’algorithme ne laisse passer que ceux qui détiennent la clé... Prêt à prouver ta valeur ?",
    "Bienvenue dans le bureau... ou pas. Seuls les membres autorisés peuvent poursuivre.",
    "Authentification requise. Une seule chance ? Non... mais pas trop non plus.",
  ];
  phraselog: string = "";
  phraseIdList: string[] = [
    "🔑 Identifiant : Entre ton code d’accès, agent...",
    "🆔 Identifiant : Entre ton matricule secret...",
    "🧑‍💻 Identifiant : Saisis ton nom d’utilisateur (et pas un au hasard, Sherlock 🤨)"
  ];
  phraseId: string = "";
  phraseMdpList: string[] = [
    "🕵️‍♂️ Mot de passe : Le mot magique qui te donnera le droit d’entrer...",
    "🔐 Mot de passe : La combinaison exacte vous ouvrira la porte... 🔑",
    "🔐 Mot de passe : Le sésame qui prouve que tu n’es pas un imposteur..."
  ];
  phraseMdp: string="";


  ngOnInit(): void {
    this.phraselog =  this.phrasesLogin[Math.floor(Math.random() * this.phrasesLogin.length)];
    this.phraseId =  this.phraseIdList[Math.floor(Math.random() * this.phraseIdList.length)];
    this.phraseMdp =  this.phraseMdpList[Math.floor(Math.random() * this.phraseMdpList.length)];
  }

  constructor(private fb: FormBuilder, private loginRepo: LoginRepository, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$.!%*?&.])[A-Za-z\d@$!%*?&.]{8,}$/)
      ]]
    });
  }

  login(event: Event) {
    event.preventDefault();
    this.submitted = true;
    this.error_login = false;
    this.error_mdp = false;


    const emailControl = this.loginForm.get('email');
    if (!emailControl?.valid) {
      this.error_login = true;
      return 
    }

    const mdpControl = this.loginForm.get('password');
    if (!mdpControl?.valid) {
      this.error_mdp = true;
      return 
    }

    const user = this.loginForm.value;

    this.loginRepo.login(user.email, user.password).subscribe({
      next: (response) => {
        //alert('Connexion réussie !');
        const token = response.headers.get('Authorization');
        if (token) {
          this.gestionToken.setToken(token);
          console.log('Token récupéré et stocké :', token);

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
        console.error('Erreur de connexion :', err);
        if (err.status === 404) {
          this.errorMessage = "Identifiants incorrects. Veuillez réessayer.";
        } else {
          this.errorMessage = err.error?.message || "Une erreur est survenue lors de la connexion.";
        }
      }
    });
  }

  suppError(err: string) {
    if(err == 'login'){
      this.error_login = false;
    } else if (err == 'password'){
      this.error_mdp = false;
    } 
  }

  
}
