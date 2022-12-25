import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideInvitesPageComponent } from './ride-invites-page.component';

describe('RideInvitesPageComponent', () => {
  let component: RideInvitesPageComponent;
  let fixture: ComponentFixture<RideInvitesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideInvitesPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideInvitesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
