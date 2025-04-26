import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinJeuComponent } from './fin-jeu.component';

describe('FinJeuComponent', () => {
  let component: FinJeuComponent;
  let fixture: ComponentFixture<FinJeuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinJeuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinJeuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
