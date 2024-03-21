import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewApplyAdminComponent } from './view-apply-admin.component';

describe('ViewApplyAdminComponent', () => {
  let component: ViewApplyAdminComponent;
  let fixture: ComponentFixture<ViewApplyAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewApplyAdminComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewApplyAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
