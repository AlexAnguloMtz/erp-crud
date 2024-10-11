import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SortOption } from '../../common/sort-option';
import { Branch, BranchAddress, BranchCommand, BranchesService } from '../../services/branches-service';
import { PaginatedRequest } from '../../common/paginated-request';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../../common/paginated-response';

const NAME_MAX_LENGTH: number = 60;
const DISTRICT_MAX_LENGTH: number = 60;
const STREET_MAX_LENGTH: number = 60;
const STREET_NUMBER_MAX_LENGTH: number = 10;

@Component({
  selector: 'app-branches',
  standalone: true,
  imports: [
    CrudModuleComponent,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
  ],
  templateUrl: './branches.component.html',
  styleUrl: './branches.component.css'
})
export class BranchesComponent {

  constructor(
    private branchesService: BranchesService,
  ) { }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (request: PaginatedRequest) => this.branchesService.getBranches(request);
  }

  createForm(): (formBuilder: FormBuilder) => FormGroup {
    return (formBuilder: FormBuilder) => {
      return formBuilder.group({
        name: [
          '',
          [
            Validators.required,
            Validators.maxLength(NAME_MAX_LENGTH),
          ]
        ],
        district: [
          '',
          [
            Validators.required,
            Validators.maxLength(DISTRICT_MAX_LENGTH),
          ]
        ],
        street: [
          '',
          [
            Validators.required,
            Validators.maxLength(STREET_MAX_LENGTH),
          ]
        ],
        streetNumber: [
          '',
          [
            Validators.required,
            Validators.maxLength(STREET_NUMBER_MAX_LENGTH),
          ]
        ],
        zipCode: [
          '',
          [
            Validators.required,
            Validators.pattern(/^\d{5}$/)
          ]
        ],
      });
    }
  }

  getFormErrors(): (formGroup: FormGroup) => { [key: string]: string } {
    return (formGroup: FormGroup) => {
      const errors: { [key: string]: string } = {};
      errors['name'] = this.nameError(formGroup);
      errors['district'] = this.districtError(formGroup);
      errors['street'] = this.streetError(formGroup);
      errors['streetNumber'] = this.streetNumberError(formGroup);
      errors['zipCode'] = this.zipCodeError(formGroup);
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

  createItem(): (dto: BranchCommand) => Observable<void> {
    return (dto: BranchCommand) => this.branchesService.createBranch(dto);
  }

  updateItem(): (id: number, dto: BranchCommand) => Observable<void> {
    return (id: number, dto: BranchCommand) => this.branchesService.updateBranch(id, dto);
  }

  deleteItemById(): (id: number) => Observable<void> {
    return (id: number) => this.branchesService.deleteBranchById(id);
  }

  mapFormToDto(): (formGroup: FormGroup) => BranchCommand {
    return (formGroup: FormGroup) => ({
      ...formGroup.value
    });
  }

  formatStreetWithNumber(address: BranchAddress) {
    return `${address.street} ${address.streetNumber}`;
  }

  onPatchUpdateForm(): (formGroup: FormGroup, item: CrudItem) => void {
    return (formGroup, item) => {
      const model: Branch = (item as Branch);
      formGroup.patchValue({
        ...model,
        ...model.address,
      });
    }
  }

  get tableHeaders(): Array<string> {
    return [
      'Nombre',
      'Colonia',
      'Calle y número',
      'Código postal',
    ];
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Nombre (A - Z)', key: 'name-asc' },
      { name: 'Nombre (Z - A)', key: 'name-desc' },

      { name: 'Colonia (A - Z)', key: 'district-asc' },
      { name: 'Colonia (Z - A)', key: 'district-desc' },

      { name: 'Calle (A - Z)', key: 'street-asc' },
      { name: 'Calle (Z - A)', key: 'street-desc' },

      { name: 'Código postal (0 - 9)', key: 'zipCode-asc' },
      { name: 'Código postal (9 - 0)', key: 'zipCode-desc' },
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

  private districtError(form: FormGroup): string {
    const control: FormControl = form.get('district') as FormControl;

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
      return `Máximo ${DISTRICT_MAX_LENGTH} caracteres`;
    }

    return '';
  }

  private streetError(form: FormGroup): string {
    const control: FormControl = form.get('street') as FormControl;

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
      return `Máximo ${STREET_MAX_LENGTH} caracteres`;
    }

    return '';
  }

  private streetNumberError(form: FormGroup): string {
    const control: FormControl = form.get('streetNumber') as FormControl;

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
      return `Máximo ${STREET_NUMBER_MAX_LENGTH} caracteres`;
    }

    return '';
  }

  private zipCodeError(form: FormGroup): string {
    const control: FormControl = form.get('zipCode') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    if (control.errors?.['pattern']) {
      return 'Deben ser 5 dígitos';
    }

    return '';
  }
}
