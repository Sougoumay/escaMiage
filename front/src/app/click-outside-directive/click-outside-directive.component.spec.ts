import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClickOutsideDirectiveComponent } from './click-outside-directive.component';

describe('ClickOutsideDirectiveComponent', () => {
  let component: ClickOutsideDirectiveComponent;
  let fixture: ComponentFixture<ClickOutsideDirectiveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClickOutsideDirectiveComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClickOutsideDirectiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
