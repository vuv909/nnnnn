import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageAccountBlockComponent } from './manage-account-block.component';

describe('ManageAccountBlockComponent', () => {
  let component: ManageAccountBlockComponent;
  let fixture: ComponentFixture<ManageAccountBlockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManageAccountBlockComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManageAccountBlockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
