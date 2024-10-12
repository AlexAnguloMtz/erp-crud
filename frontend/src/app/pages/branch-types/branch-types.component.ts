import { Component } from '@angular/core';
import { SortOption } from '../../common/sort-option';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { BranchTypeCommand, BranchTypesService } from '../../services/branch-types-service';
import { PaginatedRequest } from '../../common/paginated-request';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../../common/paginated-response';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';

const NAME_MAX_LENGTH: number = 60;
const DESCRIPTION_MAX_LENGTH: number = 300;

@Component({
  selector: 'app-branch-types',
  standalone: true,
  imports: [
    CrudModuleComponent,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    InputTextareaModule,
  ],
  templateUrl: './branch-types.component.html',
  styleUrl: './branch-types.component.css'
})
export class BranchTypesComponent {


  constructor(
    private branchTypesService: BranchTypesService,
  ) { }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (request: PaginatedRequest) => this.branchTypesService.getBranchTypes(request);
  }

  createForm(): (formBuilder: FormBuilder) => FormGroup {
    return function (formBuilder: FormBuilder) {
      return formBuilder.group({
        name: [
          '',
          [
            Validators.required,
            Validators.maxLength(NAME_MAX_LENGTH),
          ]
        ],
        description: [
          '',
          [
            Validators.required,
            Validators.maxLength(DESCRIPTION_MAX_LENGTH),
          ]
        ],
      });
    }
  }

  getFormErrors(): (formGroup: FormGroup) => { [key: string]: string } {
    return (formGroup: FormGroup) => {
      const errors: { [key: string]: string } = {};
      errors['name'] = this.nameError(formGroup);
      errors['description'] = this.descriptionError(formGroup);
      return errors;
    }
  }

  mapItemOperationError(): (error: Error) => DisplayableError {
    return (error: Error): DisplayableError => {
      return {
        header: 'Error',
        message: 'Ocurrió un error inesperado. Intenta de nuevo.',
      }
    }
  }

  createItem(): (dto: BranchTypeCommand) => Observable<void> {
    return (dto: BranchTypeCommand) => this.branchTypesService.createBranchType(dto);
  }

  updateItem(): (id: number, dto: BranchTypeCommand) => Observable<void> {
    return (id: number, dto: BranchTypeCommand) => this.branchTypesService.updateBranchType(id, dto);
  }

  deleteItemById(): (id: number) => Observable<void> {
    return (id: number) => this.branchTypesService.deleteBranchTypeById(id);
  }

  mapFormToDto(): (formGroup: FormGroup) => BranchTypeCommand {
    return (formGroup: FormGroup) => ({
      ...formGroup.value
    });
  }

  get tableHeaders(): Array<string> {
    return [
      'Nombre',
      'Descripción',
    ];
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Nombre (A - Z)', key: 'name-asc' },
      { name: 'Nombre (Z - A)', key: 'name-desc' },

      { name: 'Descripción (Z - A)', key: 'description-asc' },
      { name: 'Descripción (Z - A)', key: 'description-desc' },
    ];
  }

  private nameError(form: FormGroup): string {
    const control: FormControl = form.get('name') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    if (control.errors?.['maxlength']) {
      return `Máximo ${NAME_MAX_LENGTH} caracteres`;
    }

    return '';
  }


  private descriptionError(form: FormGroup): string {
    const control: FormControl = form.get('description') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    if (control.errors?.['maxlength']) {
      return `Máximo ${DESCRIPTION_MAX_LENGTH} caracteres`;
    }

    return '';
  }

}

