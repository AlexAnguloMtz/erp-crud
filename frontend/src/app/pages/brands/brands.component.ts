import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { PaginatedRequest } from '../../common/paginated-request';
import { PaginatedResponse } from '../../common/paginated-response';
import { Observable } from 'rxjs';
import { BrandCommand, BrandsService } from '../../services/brands-service';
import { InputTextModule } from 'primeng/inputtext';
import { SortOption } from '../../common/sort-option';

const NAME_MAX_LENGTH: number = 60;

@Component({
  selector: 'app-brands',
  standalone: true,
  imports: [
    CrudModuleComponent,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
  ],
  templateUrl: './brands.component.html',
  styleUrl: './brands.component.css'
})
export class BrandsComponent {

  constructor(
    private brandsService: BrandsService,
  ) { }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (request: PaginatedRequest) => this.brandsService.getBrands(request);
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
      });
    }
  }

  getFormErrors(): (formGroup: FormGroup) => { [key: string]: string } {
    return (formGroup: FormGroup) => {
      const errors: { [key: string]: string } = {};
      errors['name'] = this.nameError(formGroup);
      return errors;
    }
  }

  mapItemOperationError(): (error: Error) => DisplayableError {
    return (error: Error): DisplayableError => {
      if (error.name === 'BrandExistsError') {
        return {
          header: 'Marca ya existe',
          message: 'Ya existe una marca con este nombre.',
        }
      }

      return {
        header: 'Error',
        message: 'Ocurrió un error inesperado. Intenta de nuevo.',
      }
    }
  }

  createItem(): (dto: BrandCommand) => Observable<void> {
    return (dto: BrandCommand) => this.brandsService.createBrand(dto);
  }

  updateItem(): (id: number, dto: BrandCommand) => Observable<void> {
    return (id: number, dto: BrandCommand) => this.brandsService.updateBrand(id, dto);
  }

  deleteItemById(): (id: number) => Observable<void> {
    return (id: number) => this.brandsService.deleteBrandById(id);
  }

  mapFormToDto(): (formGroup: FormGroup) => BrandCommand {
    return (formGroup: FormGroup) => ({
      ...formGroup.value
    });
  }

  get tableHeaders(): Array<string> {
    return [
      'Nombre',
    ];
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Nombre (A - Z)', key: 'name-asc' },
      { name: 'Nombre (Z - A)', key: 'name-desc' },
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

}
