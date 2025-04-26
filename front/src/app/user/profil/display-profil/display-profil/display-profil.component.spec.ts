import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayProfilComponent } from './display-profil.component';

describe('DisplayProfilComponent', () => {
  let component: DisplayProfilComponent;
  let fixture: ComponentFixture<DisplayProfilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisplayProfilComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DisplayProfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
