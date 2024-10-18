import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatedRequest } from '../../common/paginated-request';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../../common/paginated-response';
import { InventoryUnit, Product, ProductCategory, ProductCommand, ProductImageAction, ProductsService } from '../../services/products-service';
import { SortOption } from '../../common/sort-option';
import { Brand, BrandsService } from '../../services/brands-service';
import { loadingError, loadingOptions, options, OptionsStatus } from '../../common/options-status';
import { ImageModule } from 'primeng/image';
import { DialogModule } from 'primeng/dialog';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonModule } from 'primeng/button';
import { ProductCategoryService } from '../../services/product-categories-service';
import { InventoryUnitService } from '../../services/inventory-unit-service';
import { DropdownModule } from 'primeng/dropdown';

const NAME_MAX_LENGTH: number = 60;
const SKU_LENGTH: number = 8;

type ProductForm = {
  command: ProductCommand
  image: File | undefined
}

type ProductImageBase = {
  type: 'base'
}

type ProductImageLoading = {
  type: 'loading'
}

type ProductImageShowing = {
  type: 'ready',
  imageSrc: string
  imageFile?: File
}

type ProductImageStatus =
  | ProductImageBase
  | ProductImageLoading
  | ProductImageShowing

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [
    CrudModuleComponent,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    ProgressSpinnerModule,
    ImageModule,
    DialogModule,
    ButtonModule,
    DropdownModule,
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent {

  // Product form options
  brandsStatus: OptionsStatus<Brand>;
  inventoryUnitsStatus: OptionsStatus<InventoryUnit>;
  productCategoriesStatus: OptionsStatus<ProductCategory>;

  // Product image state
  productImageStatus: ProductImageStatus;
  productImageAction: ProductImageAction;
  productImageErrorDialogVisible: boolean;
  productImageError: string;

  constructor(
    private productsService: ProductsService,
    private brandsService: BrandsService,
    private productCategoriesService: ProductCategoryService,
    private inventoryUnitsService: InventoryUnitService,
  ) { }

  ngOnInit(): void {
    this.productImageAction = 'none';
    this.productImageStatus = { type: 'base' }
    this.brandsStatus = { _type: 'base' };
    this.productCategoriesStatus = { _type: 'base' }
    this.inventoryUnitsStatus = { _type: 'base' }
  }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (request: PaginatedRequest) => this.productsService.getProducts(request);
  }

  createForm(): (formBuilder: FormBuilder) => FormGroup {
    return function (formBuilder: FormBuilder) {
      const form: FormGroup = formBuilder.group({
        name: [
          '',
          [
            Validators.required,
            Validators.maxLength(NAME_MAX_LENGTH),
          ]
        ],
        sku: [
          '',
          [
            Validators.required,
            Validators.minLength(SKU_LENGTH),
            Validators.maxLength(SKU_LENGTH),
          ]
        ],
        brand: [
          '',
          [
            Validators.required,
          ]
        ],
        productCategory: [
          '',
          [
            Validators.required,
          ]
        ],
        inventoryUnit: [
          '',
          [
            Validators.required,
          ]
        ],
        salePrice: [
          '',
          [
            Validators.required,
          ]
        ]
      });

      form.get('sku')?.valueChanges.subscribe(value => {
        form.get('sku')?.setValue(value.toUpperCase(), { emitEvent: false });
      });

      return form;
    }
  }

  getFormErrors(): (formGroup: FormGroup) => { [key: string]: string } {
    return (formGroup: FormGroup) => {
      const errors: { [key: string]: string } = {};
      errors['name'] = this.nameError(formGroup);
      errors['sku'] = this.skuError(formGroup);
      errors['brand'] = this.brandError(formGroup);
      errors['productCategory'] = this.productCategoryError(formGroup);
      errors['inventoryUnit'] = this.inventoryUnitError(formGroup);
      errors["salePrice"] = this.salePriceError(formGroup);
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

  createItem(): (form: ProductForm) => Observable<void> {
    return (form: ProductForm) => this.productsService.createProduct(form.command, form.image);
  }

  updateItem(): (id: number, form: ProductForm) => Observable<void> {
    return (id: number, form: ProductForm) => this.productsService.updateProduct(id, form.command, form.image);
  }

  deleteItemById(): (id: number) => Observable<void> {
    return (id: number) => this.productsService.deleteProductById(id);
  }

  formatProductPrice(priceInCents: number): string {
    return `$${(priceInCents / 100).toFixed(2)}`;
  }

  onHideImageErrorDialog(): void {
    this.productImageErrorDialogVisible = false;
    this.productImageError = '';
  }

  cleanProductImageState(): void {
    this.productImageStatus = { type: 'base' };
  }

  onProductImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    const validExtensions = ['image/jpeg', 'image/png'];

    if (!validExtensions.includes(file.type)) {
      this.productImageError = 'Selecciona una imagen con extensión JPG, JPEG o PNG';
      this.productImageErrorDialogVisible = true;
      return;
    }

    const maxMegabytes: number = 3
    const maxSizeInBytes = maxMegabytes * 1024 * 1024;
    if (file.size > maxSizeInBytes) {
      this.productImageError = `La imagen supera el tamaño máximo de ${maxMegabytes} MB`;
      this.productImageErrorDialogVisible = true;
      return;
    }

    const reader = new FileReader();
    reader.onload = (e) => {
      const imageUrl = e.target?.result as string;
      this.productImageStatus = {
        type: 'ready',
        imageFile: file,
        imageSrc: imageUrl,
      }
    };

    reader.readAsDataURL(file);

    this.productImageAction = 'edit';
  }

  onDeleteProductImage(): void {
    this.cleanProductImageState();
    this.productImageAction = 'delete';
  }

  onHideItemFormClick(): () => void {
    return () => {
      this.cleanProductImageState();
      this.productImageAction = 'none';
    }
  }

  onEditRowClick(): (item: CrudItem, formGroup: FormGroup) => void {
    return (item: CrudItem, updateItemForm: FormGroup) => {
      const model: Product = (item as Product);

      this.loadBrandsOnRowClick(model.brand.id, updateItemForm);
      this.loadProductCategoriesOnRowClick(model.productCategory.id, updateItemForm);
      this.loadInventoryUnitsOnRowClick(model.inventoryUnit.id, updateItemForm);

      if (model.image) {
        this.loadProductImage(model.image);
      }
    }
  }

  loadBrandsOnRowClick(brandId: number, form: FormGroup): void {
    if (this.brandsStatus._type === 'base') {
      this.loadBrands();
    }
    form.get('brand')?.setValue(brandId);
  }

  loadProductCategoriesOnRowClick(productCategoryId: number, form: FormGroup): void {
    if (this.productCategoriesStatus._type === 'base') {
      this.loadProductCategories();
    }
    form.get('productCategory')?.setValue(productCategoryId);
  }

  loadInventoryUnitsOnRowClick(inventoryUnitId: number, form: FormGroup): void {
    if (this.inventoryUnitsStatus._type === 'base') {
      this.loadInventoryUnits();
    }
    form.get('inventoryUnit')?.setValue(inventoryUnitId);
  }

  mapFormToCreationDto(): (formGroup: FormGroup) => ProductForm {
    return (formGroup: FormGroup) => ({
      image: this.selectedProductImageFile(),
      command: {
        ...formGroup.value,
        brandId: formGroup.get('brand')?.value,
        productCategoryId: formGroup.get('productCategory')?.value,
        inventoryUnitId: formGroup.get('inventoryUnit')?.value,
      },
    });
  }

  mapFormToUpdateDto(): (formGroup: FormGroup) => ProductForm {
    return (formGroup: FormGroup) => ({
      image: this.selectedProductImageFile(),
      command: {
        ...formGroup.value,
        brandId: formGroup.get('brand')?.value,
        productCategoryId: formGroup.get('productCategory')?.value,
        inventoryUnitId: formGroup.get('inventoryUnit')?.value,
        imageAction: this.productImageAction,
      },
    });
  }

  selectedProductImageFile(): File | undefined {
    if (this.productImageStatus.type !== 'ready') {
      return undefined;
    }
    return this.productImageStatus.imageFile;
  }

  onRetryLoadBrands(): () => void {
    return () => this.loadBrands();
  }

  onRetryLoadProductCategories(): () => void {
    return () => this.loadProductCategories();
  }

  onRetryLoadInventoryUnits(): () => void {
    return () => this.loadInventoryUnits();
  }

  onCreateNewClick(): () => void {
    return () => {
      if (this.brandsStatus._type === 'base') {
        this.loadBrands();
      }

      if (this.productCategoriesStatus._type === 'base') {
        this.loadProductCategories();
      }

      if (this.inventoryUnitsStatus._type === 'base') {
        this.loadInventoryUnits();
      }
    }
  }

  private loadBrands(): void {
    this.brandsStatus = { _type: 'loading-options' };
    this.brandsService.getAllBrands().subscribe({
      next: (brands: Array<Brand>) => {
        const sorted = brands.sort((a, b) => a.name.localeCompare(b.name));

        this.brandsStatus = {
          _type: 'options-ready',
          items: sorted,
        };
      },
      error: (_) => this.brandsStatus = { _type: 'error' },
    });
  }

  private loadProductCategories(): void {
    this.productCategoriesStatus = { _type: 'loading-options' };
    this.productCategoriesService.getAllProductCategories().subscribe({
      next: (productCategories: Array<ProductCategory>) => {
        const sorted = productCategories.sort((x, y) => x.name.localeCompare(y.name));

        this.productCategoriesStatus = {
          _type: 'options-ready',
          items: sorted,
        };
      },
      error: (_) => this.productCategoriesStatus = { _type: 'error' },
    });
  }

  private loadInventoryUnits(): void {
    this.inventoryUnitsStatus = { _type: 'loading-options' };
    this.inventoryUnitsService.getAllInventoryUnits().subscribe({
      next: (inventoryUnits: Array<InventoryUnit>) => {
        const sorted = inventoryUnits.sort((x, y) => x.name.localeCompare(y.name));

        this.inventoryUnitsStatus = {
          _type: 'options-ready',
          items: sorted,
        };
      },
      error: (_) => this.inventoryUnitsStatus = { _type: 'error' },
    });
  }

  private loadProductImage(image: string): void {
    this.productImageStatus = { type: 'loading' }
    this.productsService.getProductImage(image).subscribe({
      next: (data: ArrayBuffer) => {
        const blob = new Blob([data]);
        const blobUrl = URL.createObjectURL(blob);
        this.productImageStatus = { type: 'ready', imageSrc: blobUrl }
      },
      error: (err: Error) => {
        console.log(err.message);
      }
    });
  }

  get loadingProductImage(): boolean {
    return this.productImageStatus.type === 'loading';
  }

  get showingProductImage(): boolean {
    return this.productImageStatus.type === 'ready';
  }

  get selectedProductImageSrc(): string {
    if (this.productImageStatus.type !== 'ready') {
      return '';
    }
    return this.productImageStatus.imageSrc;
  }

  get loadingBrandsOptions(): boolean {
    return loadingOptions(this.brandsStatus);
  }

  get loadingBrandsError(): boolean {
    return loadingError(this.brandsStatus);
  }

  get brandsOptions(): Array<Brand> {
    return options(this.brandsStatus);
  }

  get loadingProductCategories(): boolean {
    return loadingOptions(this.productCategoriesStatus);
  }

  get loadingProductCategoriesError(): boolean {
    return loadingError(this.productCategoriesStatus);
  }

  get productCategoriesOptions(): Array<ProductCategory> {
    return options(this.productCategoriesStatus);
  }

  get loadingInventoryUnits(): boolean {
    return loadingOptions(this.inventoryUnitsStatus);
  }

  get loadingInventoryUnitsError(): boolean {
    return loadingError(this.inventoryUnitsStatus);
  }

  get inventoryUnitsOptions(): Array<InventoryUnit> {
    return options(this.inventoryUnitsStatus);
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

  private skuError(form: FormGroup): string {
    const control: FormControl = form.get('sku') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    if (control.errors?.['minlength']) {
      return `Deben ser ${SKU_LENGTH} caracteres`;
    }

    if (control.errors?.['maxlength']) {
      return `Deben ser ${SKU_LENGTH} caracteres`;
    }

    return '';
  }

  private brandError(form: FormGroup): string {
    const control: FormControl = form.get('brand') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    return '';
  }

  private productCategoryError(form: FormGroup): string {
    const control: FormControl = form.get('productCategory') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    return '';
  }

  private inventoryUnitError(form: FormGroup): string {
    const control: FormControl = form.get('inventoryUnit') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    return '';
  }

  private salePriceError(form: FormGroup): string {
    const control: FormControl = form.get('salePrice') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    return '';
  }
}
