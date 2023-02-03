import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideTypeComponent } from './ride-type.component';

describe('RideTypeComponent', () => {
  let component: RideTypeComponent;
  let fixture: ComponentFixture<RideTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideTypeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
