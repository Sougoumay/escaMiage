import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyProfilComponent } from './modify-profil.component';

describe('ModifyProfilComponent', () => {
  let component: ModifyProfilComponent;
  let fixture: ComponentFixture<ModifyProfilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyProfilComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyProfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
