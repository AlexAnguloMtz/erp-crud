import { Component } from '@angular/core';
import { SortOption } from '../../common/sort-option';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductCategoryCommand, ProductCategoryService } from '../../services/product-categories-service';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../../common/paginated-response';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { PaginatedRequest } from '../../common/paginated-request';
import { InputTextModule } from 'primeng/inputtext';

const NAME_MAX_LENGTH: number = 60;

@Component({
  selector: 'app-product-categories',
  standalone: true,
  imports: [
    CrudModuleComponent,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
  ],
  templateUrl: './product-categories.component.html',
  styleUrl: './product-categories.component.css'
})
export class ProductCategoriesComponent {

  constructor(
    private productCategoryService: ProductCategoryService,
  ) { }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (request: PaginatedRequest) => this.productCategoryService.getProductCategories(request);
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
      if (error.name === 'ProductCategoryExistsError') {
        return {
          header: 'Registro ya existe',
          message: 'Ya existe un registro con este nombre.',
        }
      }

      return {
        header: 'Error',
        message: 'Ocurrió un error inesperado. Intenta de nuevo.',
      }
    }
  }

  createItem(): (dto: ProductCategoryCommand) => Observable<void> {
    return (dto: ProductCategoryCommand) => this.productCategoryService.createProductCategory(dto);
  }

  updateItem(): (id: number, dto: ProductCategoryCommand) => Observable<void> {
    return (id: number, dto: ProductCategoryCommand) => this.productCategoryService.updateProductCategory(id, dto);
  }

  deleteItemById(): (id: number) => Observable<void> {
    return (id: number) => this.productCategoryService.deleteProductCategoryById(id);
  }

  mapFormToDto(): (formGroup: FormGroup) => ProductCategoryCommand {
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
