import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltersFormFieldComponent } from './filters-form-field.component';

describe('FiltersFormFieldComponent', () => {
  let component: FiltersFormFieldComponent;
  let fixture: ComponentFixture<FiltersFormFieldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FiltersFormFieldComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FiltersFormFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
