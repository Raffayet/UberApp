import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderExistingRideDialogComponent } from './order-existing-ride-dialog.component';

describe('OrderExistingRideDialogComponent', () => {
  let component: OrderExistingRideDialogComponent;
  let fixture: ComponentFixture<OrderExistingRideDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderExistingRideDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderExistingRideDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
