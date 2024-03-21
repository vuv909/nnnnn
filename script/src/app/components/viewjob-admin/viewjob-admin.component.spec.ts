import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewjobAdminComponent } from './viewjob-admin.component';

describe('ViewjobAdminComponent', () => {
  let component: ViewjobAdminComponent;
  let fixture: ComponentFixture<ViewjobAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewjobAdminComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewjobAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
