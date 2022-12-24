import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdditionalLoginInfoComponent } from './additional-login-info.component';

describe('AdditionalLoginInfoComponent', () => {
  let component: AdditionalLoginInfoComponent;
  let fixture: ComponentFixture<AdditionalLoginInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdditionalLoginInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdditionalLoginInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
