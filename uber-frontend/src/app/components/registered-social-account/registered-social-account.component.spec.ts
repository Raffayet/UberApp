import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisteredSocialAccountComponent } from './registered-social-account.component';

describe('RegisteredSocialAccountComponent', () => {
  let component: RegisteredSocialAccountComponent;
  let fixture: ComponentFixture<RegisteredSocialAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisteredSocialAccountComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisteredSocialAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
