import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrudModuleComponent } from './crud-module.component';

describe('CrudModuleComponent', <T, U>() => {
  let component: CrudModuleComponent<T, U>;
  let fixture: ComponentFixture<CrudModuleComponent<T, U>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrudModuleComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CrudModuleComponent<T, U>);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
