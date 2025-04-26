import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './acces/login/component/login.component';
import { RegisterComponent } from './acces/register/component/register.component';
import {HomeComponent} from './home/component/home.component';
import {AdminDashboardComponent} from './admin-dashboard/component/admin-dashboard.component';
import {EnigmeComponent} from './enigme/create-enigme/enigme.component';
import {ModifyEnigmeComponent} from './enigme/modify-enigme/modify-enigme.component';
import {UserComponent} from './user/display-users/user.component';
import {DisplayBadgesComponent} from './badge/display-badges/display-badges.component';
import {CreateBadgeComponent} from './badge/create-badge/create-badge.component';
import {DisplayProfilComponent} from './user/profil/display-profil/display-profil/display-profil.component';
import {UserDashboardComponent} from './user/user-dashboard/user-dashboard.component';
import {FeedbackSuggestionsComponent} from './user/feedback-suggestions/feedback-suggestions.component';
import {CreatePostComponent} from './actualite/create-post/create-post.component';
import {JeuComponent} from './jeu/jeu/jeu.component';
import {ReglesJeuComponent} from './jeu/page-reglesJeu/regles-jeu/regles-jeu.component';
import {ModifyPostComponent} from './actualite/modify-post/modify-post.component';
import {DisplayPostsComponent} from './actualite/display-posts/display-posts.component';
import {ModifyProfilComponent} from './user/profil/modify-profil/modify-profil/modify-profil.component';
import {ReactPostComponent} from './actualite/react-post/react-post.component';
import { FinJeuComponent } from './jeu/page-reglesJeu/fin-jeu/fin-jeu.component';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {path: 'admin-dashboard', component: AdminDashboardComponent},
  {path: 'create-enigme', component: EnigmeComponent},
  {path: 'modify-enigme/:id', component: ModifyEnigmeComponent},
  {path: 'users', component: UserComponent},
  {path: 'create-badge', component: CreateBadgeComponent},
  {path: 'badges', component: DisplayBadgesComponent},
  {path: 'profile', component: DisplayProfilComponent},
  {path: 'modify-profil', component: ModifyProfilComponent}, //TODO
  {path: 'user-dashboard', component: UserDashboardComponent},
  {path: 'suggestions-feedback', component: FeedbackSuggestionsComponent},
  {path: 'create-post', component: CreatePostComponent},
  {path: 'modify-post/:id', component: ModifyPostComponent},
  {path: 'posts', component: DisplayPostsComponent},
  {path: 'regles-jeu', component: ReglesJeuComponent},
  {path: 'jeu', component: JeuComponent},
  {path: 'react-post', component: ReactPostComponent},
  {path: 'fin-jeu', component: FinJeuComponent}
];
///:id
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
