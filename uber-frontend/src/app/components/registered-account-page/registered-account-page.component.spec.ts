import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisteredAccountPageComponent } from './registered-account-page.component';

describe('RegisteredAccountPageComponent', () => {
  let component: RegisteredAccountPageComponent;
  let fixture: ComponentFixture<RegisteredAccountPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisteredAccountPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisteredAccountPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
