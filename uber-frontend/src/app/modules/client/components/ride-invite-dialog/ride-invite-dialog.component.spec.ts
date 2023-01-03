import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideInviteDialogComponent } from './ride-invite-dialog.component';

describe('RideInviteDialogComponent', () => {
  let component: RideInviteDialogComponent;
  let fixture: ComponentFixture<RideInviteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideInviteDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideInviteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
