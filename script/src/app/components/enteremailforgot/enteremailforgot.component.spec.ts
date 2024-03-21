import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnteremailforgotComponent } from './enteremailforgot.component';

describe('EnteremailforgotComponent', () => {
  let component: EnteremailforgotComponent;
  let fixture: ComponentFixture<EnteremailforgotComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EnteremailforgotComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EnteremailforgotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
