import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyEnigmeComponent } from './modify-enigme.component';

describe('ModifyEnigmeComponent', () => {
  let component: ModifyEnigmeComponent;
  let fixture: ComponentFixture<ModifyEnigmeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyEnigmeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyEnigmeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
