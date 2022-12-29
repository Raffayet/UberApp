import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisteredDriverComponent } from './registered-driver.component';

describe('RegisteredDriverComponent', () => {
  let component: RegisteredDriverComponent;
  let fixture: ComponentFixture<RegisteredDriverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisteredDriverComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisteredDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
