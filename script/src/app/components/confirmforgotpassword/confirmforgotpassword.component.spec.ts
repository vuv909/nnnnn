import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmforgotpasswordComponent } from './confirmforgotpassword.component';

describe('ConfirmforgotpasswordComponent', () => {
  let component: ConfirmforgotpasswordComponent;
  let fixture: ComponentFixture<ConfirmforgotpasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConfirmforgotpasswordComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConfirmforgotpasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
