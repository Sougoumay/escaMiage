import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-regles-jeu',
  imports: [FormsModule , ReactiveFormsModule, CommonModule],
  templateUrl: './regles-jeu.component.html',
  styleUrl: './regles-jeu.component.css'
})
export class ReglesJeuComponent {
  debut: boolean = true;
  isSuite1: boolean = false;
  isSuite2: boolean = false;
  isSuite3: boolean = false;
  isSuite4: boolean = false;
  isSuite5: boolean = false;

  constructor(private router: Router) {}

  ngOnInit() {
    this.resetBooleans();
  }

  resetBooleans() {
    this.debut = true;
    this.isSuite1 = false;
    this.isSuite2 = false;
    this.isSuite3 = false;
    this.isSuite4 = false;
    this.isSuite5 = false;
  }

  
  

  suite1(){
    this.debut = false;
    this.isSuite1 = true;
    this.isSuite2 = false;
    this.isSuite3 = false;
    this.isSuite4 = false;
    this.isSuite5 = false;
  }

  suite2(){
    this.debut = false;
    this.isSuite1 = false;
    this.isSuite2 = true;
    this.isSuite3 = false;
    this.isSuite4 = false;
    this.isSuite5 = false;
  }

  suite3(){
    this.debut = false;
    this.isSuite1 = false;
    this.isSuite2 = false;
    this.isSuite3 = true;
    this.isSuite4 = false;
    this.isSuite5 = false;
  }

  suite4(){
    this.debut = false;
    this.isSuite1 = false;
    this.isSuite2 = false;
    this.isSuite3 = false;
    this.isSuite4 = true;
    this.isSuite5 = false;
  }

  suite5(){
    this.debut = false;
    this.isSuite1 = false;
    this.isSuite2 = false;
    this.isSuite3 = false;
    this.isSuite4 = false;
    this.isSuite5 = true;
  }

  changementPage(){
    this.enterFullScreen();
    this.router.navigate(['/jeu']);
    
  }

  // Passer en plein écran
  enterFullScreen() {
    const element: any = document.documentElement;

    if (element.requestFullscreen) {
      element.requestFullscreen();
    } else if (element.msRequestFullscreen) {
      element.msRequestFullscreen();
    } else if (element.mozRequestFullScreen) {
      element.mozRequestFullScreen();
    } else if (element.webkitRequestFullscreen) {
      element.webkitRequestFullscreen();
    }
  }


}
