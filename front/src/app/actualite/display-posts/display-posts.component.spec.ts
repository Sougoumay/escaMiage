import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayPostsComponent } from './display-posts.component';

describe('DisplayPostsComponent', () => {
  let component: DisplayPostsComponent;
  let fixture: ComponentFixture<DisplayPostsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisplayPostsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(DisplayPostsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
