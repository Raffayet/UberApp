import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverInfoRequestComponent } from './driver-info-request.component';

describe('DriverInfoRequestComponent', () => {
  let component: DriverInfoRequestComponent;
  let fixture: ComponentFixture<DriverInfoRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverInfoRequestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverInfoRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
