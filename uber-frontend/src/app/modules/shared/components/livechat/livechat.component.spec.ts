import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LivechatComponent } from './livechat.component';

describe('LivechatComponent', () => {
  let component: LivechatComponent;
  let fixture: ComponentFixture<LivechatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LivechatComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LivechatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
