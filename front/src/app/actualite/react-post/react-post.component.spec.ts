import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactPostComponent } from './react-post.component';

describe('ReactPostComponent', () => {
  let component: ReactPostComponent;
  let fixture: ComponentFixture<ReactPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactPostComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ReactPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
