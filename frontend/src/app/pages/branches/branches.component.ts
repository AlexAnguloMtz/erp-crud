import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { SortOption } from '../../common/sort-option';
import { Branch, BranchAddress, BranchesService, BranchImageAction, BranchType, BranchCommand } from '../../services/branches-service';
import { PaginatedRequest } from '../../common/paginated-request';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../../common/paginated-response';
import { BranchTypesService } from '../../services/branch-types-service';
import { loadingError, loadingOptions, options, OptionsStatus } from '../../common/options-status';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { ImageModule } from 'primeng/image';
import { DialogModule } from 'primeng/dialog';

const NAME_MAX_LENGTH: number = 60;
const PHONE_LENGTH: number = 10;
const DISTRICT_MAX_LENGTH: number = 60;
const STREET_MAX_LENGTH: number = 60;
const STREET_NUMBER_MAX_LENGTH: number = 10;

type BranchForm = {
  command: BranchCommand
  image: File | undefined
}

type BranchImageBase = {
  type: 'base'
}

type BranchImageLoading = {
  type: 'loading'
}

type BranchImageShowing = {
  type: 'ready',
  imageSrc: string
  imageFile?: File
}

type BranchImageStatus =
  | BranchImageBase
  | BranchImageLoading
  | BranchImageShowing

@Component({
  selector: 'app-branches',
  standalone: true,
  imports: [
    CrudModuleComponent,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    ProgressSpinnerModule,
    ButtonModule,
    DropdownModule,
    ImageModule,
    DialogModule,
  ],
  templateUrl: './branches.component.html',
  styleUrl: './branches.component.css'
})
export class BranchesComponent {

  branchTypesStatus: OptionsStatus<BranchType>;
  branchImageStatus: BranchImageStatus;
  branchImageAction: BranchImageAction;
  branchImageErrorDialogVisible: boolean;
  branchImageError: string;

  constructor(
    private branchesService: BranchesService,
    private branchTypesService: BranchTypesService,
  ) { }

  ngOnInit(): void {
    this.branchImageStatus = { type: 'base' }
    this.branchTypesStatus = { _type: 'base' };
    this.branchImageAction = 'none';
  }

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
        phone: [
          '',
          [
            Validators.required,
            Validators.pattern(/^\d{10}$/)
          ]
        ],
        branchType: [
          '',
          [
            Validators.required,
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
      errors['phone'] = this.phoneError(formGroup);
      errors['branchType'] = this.branchTypeError(formGroup);
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

  createItem(): (form: BranchForm) => Observable<void> {
    return (form: BranchForm) => this.branchesService.createBranch(form.command, form.image);
  }

  updateItem(): (id: number, form: BranchForm) => Observable<void> {
    return (id: number, form: BranchForm) => this.branchesService.updateBranch(id, form.command, form.image);
  }

  deleteItemById(): (id: number) => Observable<void> {
    return (id: number) => this.branchesService.deleteBranchById(id);
  }

  mapFormToCreationDto(): (formGroup: FormGroup) => BranchForm {
    return (formGroup: FormGroup) => ({
      image: this.selectedBranchImageFile(),
      command: {
        ...formGroup.value,
        branchTypeId: formGroup.get('branchType')?.value,
      },
    });
  }

  mapFormToUpdateDto(): (formGroup: FormGroup) => BranchForm {
    return (formGroup: FormGroup) => ({
      image: this.selectedBranchImageFile(),
      command: {
        ...formGroup.value,
        branchTypeId: formGroup.get('branchType')?.value,
        imageAction: this.branchImageAction,
      },
    });
  }

  formatStreetWithNumber(address: BranchAddress) {
    return `${address.street} ${address.streetNumber}`;
  }

  onPatchUpdateForm(): (formGroup: FormGroup, item: CrudItem) => void {
    return (formGroup, item) => {
      const model: Branch = (item as Branch);
      this.branchImageAction = 'none';
      formGroup.patchValue({
        ...model,
        ...model.address,
        branchType: model.branchType.id,
      });
    }
  }

  onCreateNewClick(): () => void {
    return () => {
      if (this.branchTypesStatus._type === 'base') {
        this.loadBranchTypes();
      }
    }
  }

  onRetryLoadBranchTypes(): () => void {
    return () => this.loadBranchTypes();
  }

  onEditRowClick(): (item: CrudItem, formGroup: FormGroup) => void {
    return (item: CrudItem, updateItemForm: FormGroup) => {

      const model: Branch = (item as Branch);

      this.loadBranchTypesOnRowClick(model.branchType.id, updateItemForm);

      if (model.image) {
        this.loadBranchImage(model.image);
      }
    }
  }

  loadBranchImage(image: string): void {
    this.branchImageStatus = { type: 'loading' }
    this.branchesService.getBranchImage(image).subscribe({
      next: (data: ArrayBuffer) => {
        const blob = new Blob([data]);
        const blobUrl = URL.createObjectURL(blob);
        this.branchImageStatus = { type: 'ready', imageSrc: blobUrl }
      },
      error: (err: Error) => {
        console.log(err.message);
      }
    });
  }

  loadBranchTypesOnRowClick(branchTypeId: number, form: FormGroup): void {
    if (this.branchTypesStatus._type === 'base') {
      this.loadBranchTypes();
    }
    form.get('branchType')?.setValue(branchTypeId);
  }

  onBranchImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    const validExtensions = ['image/jpeg', 'image/png'];

    if (!validExtensions.includes(file.type)) {
      this.branchImageError = 'Selecciona una imagen con extensión JPG, JPEG o PNG';
      this.branchImageErrorDialogVisible = true;
      return;
    }

    const maxMegabytes: number = 3
    const maxSizeInBytes = maxMegabytes * 1024 * 1024;
    if (file.size > maxSizeInBytes) {
      this.branchImageError = `La imagen supera el tamaño máximo de ${maxMegabytes} MB`;
      this.branchImageErrorDialogVisible = true;
      return;
    }

    const reader = new FileReader();
    reader.onload = (e) => {
      const imageUrl = e.target?.result as string;
      this.branchImageStatus = {
        type: 'ready',
        imageFile: file,
        imageSrc: imageUrl,
      }
    };

    reader.readAsDataURL(file);

    this.branchImageAction = 'edit';
  }

  onHideImageErrorDialog(): void {
    this.branchImageErrorDialogVisible = false;
    this.branchImageError = '';
  }

  onHideItemFormClick(): () => void {
    return () => {
      this.cleanBranchImageState();
      this.branchImageAction = 'none';
    }
  }

  onDeleteBranchImage(): void {
    this.cleanBranchImageState();
    this.branchImageAction = 'delete';
  }

  cleanBranchImageState(): void {
    this.branchImageStatus = { type: 'base' };
  }

  selectedBranchImageFile(): File | undefined {
    if (this.branchImageStatus.type !== 'ready') {
      return undefined;
    }
    return this.branchImageStatus.imageFile;
  }

  get loadingBranchImage(): boolean {
    return this.branchImageStatus.type === 'loading';
  }

  get selectedBranchImageSrc(): string {
    if (this.branchImageStatus.type !== 'ready') {
      return '';
    }
    return this.branchImageStatus.imageSrc;
  }

  get showingBranchImage(): boolean {
    return this.branchImageStatus.type === 'ready';
  }

  get tableHeaders(): Array<string> {
    return [
      'Nombre',
      'Tipo de sucursal',
      'Teléfono',
      'Colonia',
      'Calle y número',
      'Código postal',
    ];
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Nombre (A - Z)', key: 'name-asc' },
      { name: 'Nombre (Z - A)', key: 'name-desc' },

      { name: 'Tipo de sucursal (A - Z)', key: 'branchType-asc' },
      { name: 'Tipo de sucursal (Z - A)', key: 'branchType-desc' },

      { name: 'Teléfono (0 - 9)', key: 'phone-asc' },
      { name: 'Teléfono (9 - 0)', key: 'phone-desc' },

      { name: 'Colonia (A - Z)', key: 'district-asc' },
      { name: 'Colonia (Z - A)', key: 'district-desc' },

      { name: 'Calle (A - Z)', key: 'street-asc' },
      { name: 'Calle (Z - A)', key: 'street-desc' },

      { name: 'Código postal (0 - 9)', key: 'zipCode-asc' },
      { name: 'Código postal (9 - 0)', key: 'zipCode-desc' },
    ];
  }

  get loadingBranchTypesOptions(): boolean {
    return loadingOptions(this.branchTypesStatus);
  }

  get loadingBranchTypesError(): boolean {
    return loadingError(this.branchTypesStatus);
  }

  get branchTypesOptions(): Array<BranchType> {
    return options(this.branchTypesStatus);
  }

  private loadBranchTypes(): void {
    this.branchTypesStatus = { _type: 'loading-options' };
    this.branchTypesService.getAllBranchTypes().subscribe({
      next: (branchTypes: Array<BranchType>) => {
        this.branchTypesStatus = {
          _type: 'options-ready', items: branchTypes,
        }
      },
      error: (_) => this.branchTypesStatus = { _type: 'error' },
    });
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

  private phoneError(form: FormGroup): string {
    const control: FormControl = form.get('phone') as FormControl;

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
      return `Deben ser ${PHONE_LENGTH} dígitos`;
    }

    return '';
  }

  private branchTypeError(form: FormGroup): string {
    const control: FormControl = form.get('branchType') as FormControl;

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
