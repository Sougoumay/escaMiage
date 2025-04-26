import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReglesJeuComponent } from './regles-jeu.component';

describe('ReglesJeuComponent', () => {
  let component: ReglesJeuComponent;
  let fixture: ComponentFixture<ReglesJeuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReglesJeuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReglesJeuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
