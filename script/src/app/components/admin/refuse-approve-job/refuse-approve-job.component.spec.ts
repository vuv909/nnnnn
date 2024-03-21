import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RefuseApproveJobComponent } from './refuse-approve-job.component';

describe('RefuseApproveJobComponent', () => {
  let component: RefuseApproveJobComponent;
  let fixture: ComponentFixture<RefuseApproveJobComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RefuseApproveJobComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RefuseApproveJobComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
