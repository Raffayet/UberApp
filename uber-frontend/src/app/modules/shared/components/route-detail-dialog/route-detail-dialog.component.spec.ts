import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RouteDetailDialogComponent } from './route-detail-dialog.component';

describe('RouteDetailDialogComponent', () => {
  let component: RouteDetailDialogComponent;
  let fixture: ComponentFixture<RouteDetailDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RouteDetailDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RouteDetailDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
