import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFormFieldsComponent } from './user-form-fields.component';

describe('UserFormFieldsComponent', () => {
  let component: UserFormFieldsComponent;
  let fixture: ComponentFixture<UserFormFieldsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserFormFieldsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserFormFieldsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
