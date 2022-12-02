import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideRequestPageComponent } from './ride-request-page.component';

describe('RideRequestPageComponent', () => {
  let component: RideRequestPageComponent;
  let fixture: ComponentFixture<RideRequestPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideRequestPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideRequestPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
