import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UnauthenticatedDashboard } from './unauthenticated-dashboard.component';

describe('UnauthenticatedDashboard', () => {
  let component: UnauthenticatedDashboard;
  let fixture: ComponentFixture<UnauthenticatedDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UnauthenticatedDashboard ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnauthenticatedDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
