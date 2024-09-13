import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { PaginatedRequest } from '../../common/paginated-request';
import { PaginatedResponse } from '../../common/paginated-response';
import { Observable } from 'rxjs';
import { BrandCommand, ProductsService } from '../../services/products-service';
import { SortOption } from '../users/users.component';
import { InputTextModule } from 'primeng/inputtext';

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
    private productsService: ProductsService,
  ) { }

  getItems(): (token: string, request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (token: string, request: PaginatedRequest) => this.productsService.getBrands(token, request);
  }

  createForm(): (formBuilder: FormBuilder) => FormGroup {
    return function (formBuilder: FormBuilder) {
      return formBuilder.group({
        name: [
          '',
          [Validators.required, Validators.maxLength(60)]
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

  mapSaveItemError(): (error: Error) => DisplayableError {
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

  createItem(): (token: string, dto: BrandCommand) => Observable<void> {
    return (token: string, dto: BrandCommand) => this.productsService.createBrand(token, dto);
  }

  updateItem(): (token: string, id: string, dto: BrandCommand) => Observable<void> {
    return (token: string, id: string, dto: BrandCommand) => this.productsService.updateBrand(token, id, dto);
  }

  deleteItemById(): (token: string, id: string) => Observable<void> {
    return (token: string, id: string) => this.productsService.deleteBrandById(token, id);
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
      return 'Máximo 60 caracteres';
    }

    return '';
  }

}
