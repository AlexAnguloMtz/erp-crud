import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatedRequest } from '../../common/paginated-request';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../../common/paginated-response';
import { ProductCommand, ProductsService } from '../../services/products-service';
import { SortOption } from '../../common/sort-option';

const NAME_MAX_LENGTH: number = 100;

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    CrudModuleComponent,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
  ],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent {

  constructor(
    private productsService: ProductsService
  ) { }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (request: PaginatedRequest) => this.productsService.getProducts(request);
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
      return {
        header: 'Error',
        message: 'Ocurrió un error inesperado. Intenta de nuevo.',
      }
    }
  }

  createItem(): (dto: ProductCommand) => Observable<void> {
    return (dto: ProductCommand) => this.productsService.createProduct(dto);
  }

  updateItem(): (id: number, dto: ProductCommand) => Observable<void> {
    return (id: number, dto: ProductCommand) => this.productsService.updateProduct(id, dto);
  }

  deleteItemById(): (id: number) => Observable<void> {
    return (id: number) => this.productsService.deleteProductById(id);
  }

  mapFormToDto(): (formGroup: FormGroup) => ProductCommand {
    return (formGroup: FormGroup) => ({
      ...formGroup.value
    });
  }

  formatProductPrice(priceInCents: number): string {
    return `$${(priceInCents / 100).toFixed(2)}`;
  }

  get tableHeaders(): Array<string> {
    return [
      'SKU',
      'Nombre',
      'Categoría',
      'Marca',
      'Precio de venta',
      'Unidad de inventariado'
    ];
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'SKU (A - Z)', key: 'sku-asc' },
      { name: 'SKU (Z - A)', key: 'sku-desc' },
      { name: 'Nombre (A - Z)', key: 'name-asc' },
      { name: 'Nombre (Z - A)', key: 'name-desc' },
      { name: 'Categoría (A - Z)', key: 'productCategory-asc' },
      { name: 'Categoría (Z - A)', key: 'productCategory-desc' },
      { name: 'Marca (A - Z)', key: 'brand-asc' },
      { name: 'Marca (Z - A)', key: 'brand-desc' },
      { name: 'Precio de venta (0 - 9)', key: 'salePrice-asc' },
      { name: 'Precio de venta (9 - 0)', key: 'salePrice-desc' },
      { name: 'Unidad de inventariado (A - Z)', key: 'inventoryUnit-asc' },
      { name: 'Unidad de inventariado (Z - A)', key: 'inventoryUnit-desc' },
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
