import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotpassComponent } from './notpass.component';

describe('NotpassComponent', () => {
  let component: NotpassComponent;
  let fixture: ComponentFixture<NotpassComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotpassComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NotpassComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
