import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchTypesComponent } from './branch-types.component';

describe('BranchTypesComponent', () => {
  let component: BranchTypesComponent;
  let fixture: ComponentFixture<BranchTypesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchTypesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BranchTypesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
