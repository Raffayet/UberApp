import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideToTakeDialogComponent } from './ride-to-take-dialog.component';

describe('RideToTakeDialogComponent', () => {
  let component: RideToTakeDialogComponent;
  let fixture: ComponentFixture<RideToTakeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideToTakeDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideToTakeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
