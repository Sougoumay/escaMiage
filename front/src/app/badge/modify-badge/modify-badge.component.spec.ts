import { ComponentFixture, TestBed } from '@angular/core/testing';
import {ModifyBadgeComponent} from './modify-badge.component';



describe('ModifyBadgeComponent', () => {
  let component: ModifyBadgeComponent;
  let fixture: ComponentFixture<ModifyBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModifyBadgeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
