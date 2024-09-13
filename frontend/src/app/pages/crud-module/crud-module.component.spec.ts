import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudModuleComponent } from './crud-module.component';

describe('CrudModuleComponent', <T, U, V>() => {
  let component: CrudModuleComponent<T, U, V>;
  let fixture: ComponentFixture<CrudModuleComponent<T, U, V>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrudModuleComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CrudModuleComponent<T, U, V>);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
