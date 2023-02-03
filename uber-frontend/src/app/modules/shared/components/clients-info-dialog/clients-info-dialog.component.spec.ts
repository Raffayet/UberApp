import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientsInfoDialogComponent } from './clients-info-dialog.component';

describe('ClientsInfoDialogComponent', () => {
  let component: ClientsInfoDialogComponent;
  let fixture: ComponentFixture<ClientsInfoDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientsInfoDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientsInfoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
