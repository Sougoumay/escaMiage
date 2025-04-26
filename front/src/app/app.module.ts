import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app.routes';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/component/home.component';
import { LoginComponent } from './acces/login/component/login.component';
import {AdminDashboardComponent} from './admin-dashboard/component/admin-dashboard.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RegisterComponent} from './acces/register/component/register.component';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {EnigmeComponent} from './enigme/create-enigme/enigme.component';
import {ModifyEnigmeComponent} from './enigme/modify-enigme/modify-enigme.component';
import {UserComponent} from './user/display-users/user.component';
import {CreateBadgeComponent} from './badge/create-badge/create-badge.component';
import {DisplayBadgesComponent} from './badge/display-badges/display-badges.component';
import {TokenInterceptor} from './gestionToken/token.interceptor';
import { ClickOutsideDirective } from './click-outside-directive/click-outside-directive.component';

@NgModule({
  declarations: [
    
  ],
  imports: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    AdminDashboardComponent,
    RegisterComponent,
    EnigmeComponent,
    ModifyEnigmeComponent,
    UserComponent,
    CreateBadgeComponent,
    DisplayBadgesComponent,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    ClickOutsideDirective
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useValue: TokenInterceptor, // Enregistre l'intercepteur
      multi: true // Permet d'enregistrer plusieurs intercepteurs
    }
  ]
})
export class AppModule { }
