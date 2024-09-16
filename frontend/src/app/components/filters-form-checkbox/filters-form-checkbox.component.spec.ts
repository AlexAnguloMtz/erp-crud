import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltersFormCheckboxComponent } from './filters-form-checkbox.component';

describe('FiltersFormCheckboxComponent', () => {
  let component: FiltersFormCheckboxComponent;
  let fixture: ComponentFixture<FiltersFormCheckboxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FiltersFormCheckboxComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FiltersFormCheckboxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
