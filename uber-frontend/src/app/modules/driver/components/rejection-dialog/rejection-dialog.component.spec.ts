import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectionDialogComponent } from './rejection-dialog.component';

describe('RejectionDialogComponent', () => {
  let component: RejectionDialogComponent;
  let fixture: ComponentFixture<RejectionDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectionDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RejectionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
