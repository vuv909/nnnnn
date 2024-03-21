import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PedingApproveJobComponent } from './peding-approve-job.component';

describe('PedingApproveJobComponent', () => {
  let component: PedingApproveJobComponent;
  let fixture: ComponentFixture<PedingApproveJobComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PedingApproveJobComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PedingApproveJobComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
