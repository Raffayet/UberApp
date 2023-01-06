import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RidesToDoComponent } from './rides-to-do.component';

describe('RidesToDoComponent', () => {
  let component: RidesToDoComponent;
  let fixture: ComponentFixture<RidesToDoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RidesToDoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RidesToDoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
