import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { PaginatedResponse } from '../../common/paginated-response';
import { CreateUserCommand, UpdateUserCommand, UpdateUserResponse, UserDetails, UsersService } from '../../services/users-service';
import { PaginatedRequest } from '../../common/paginated-request';
import { Observable } from 'rxjs';
import { SortOption } from '../../common/sort-option';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService, Role } from '../../services/auth-service';
import { UserFormFieldsComponent } from './user-form-fields/user-form-fields.component';
import { options, OptionsStatus } from '../../common/options-status';
import { AuthenticationHolder } from '../../services/authentication-holder';
import { FiltersFormFieldComponent } from '../../components/filters-form-field/filters-form-field.component';
import { SkeletonModule } from 'primeng/skeleton';
import { range, toggle } from '../../common/arrays';
import { CheckboxModule } from 'primeng/checkbox';
import { FiltersFormCheckboxComponent } from '../../components/filters-form-checkbox/filters-form-checkbox.component';

const NAME_MAX_LENGTH: number = 60;
const LAST_NAME_MAX_LENGTH: number = 60;
const EMAIL_MAX_LENGTH: number = 60;
const DISTRICT_MAX_LENGTH: number = 60;
const STREET_MAX_LENGTH: number = 60;
const STREET_NUMBER_MAX_LENGTH: number = 10;

type FirstSurfaceState = {
  type: 'first-surface'
}

type SecondSurfaceState = {
  type: 'second-surface',
  surface: 'role'
}

type FiltersFormState = FirstSurfaceState | SecondSurfaceState

const passwordVisibleProps: PasswordFieldProps = {
  type: 'text',
  icon: 'eye-slash',
}

const passwordNotVisibleProps: PasswordFieldProps = {
  type: 'password',
  icon: 'eye',
}

type PasswordFieldProps = {
  type: 'text' | 'password'
  icon: 'eye' | 'eye-slash'
}

@Component({
  selector: 'app-users2',
  standalone: true,
  imports: [
    CrudModuleComponent,
    ProgressSpinnerModule,
    ButtonModule,
    DropdownModule,
    InputTextModule,
    FormsModule,
    ReactiveFormsModule,
    UserFormFieldsComponent,
    FiltersFormFieldComponent,
    SkeletonModule,
    CheckboxModule,
    FiltersFormCheckboxComponent,
  ],
  providers: [AuthenticationHolder],
  templateUrl: './users2.component.html',
  styleUrl: './users2.component.css'
})
export class Users2Component {

  rolesOptionsStatus: OptionsStatus<Role>;
  passwordFieldProps: PasswordFieldProps;
  selectedRoles: Array<number>;
  filtersFormState: FiltersFormState;

  constructor(
    private userService: UsersService,
    private authenticationHolder: AuthenticationHolder,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.rolesOptionsStatus = { _type: 'base' };
    this.filtersFormState = { type: 'first-surface' }
    this.selectedRoles = [];
    this.passwordFieldProps = passwordNotVisibleProps;
  }

  get filtersSecondSurfaceClass(): string {
    if (this.filtersFormState.type === 'first-surface') {
      return '';
    }

    if (this.loadingFilters) {
      return 'loading';
    }

    return this.filtersFormState.surface;
  }

  get loadingFilters(): boolean {
    return this.rolesOptionsStatus._type === 'loading-options';
  }

  get roles(): Array<Role> {
    return options(this.rolesOptionsStatus);
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Nombre (A - Z)', key: 'name-asc' },
      { name: 'Nombre (Z - A)', key: 'name-desc' },

      { name: 'Apellido (A - Z)', key: 'lastName-asc' },
      { name: 'Apellido (Z - A)', key: 'lastName-desc' },

      { name: 'Rol (A - Z)', key: 'role-asc' },
      { name: 'Rol (Z - A)', key: 'role-desc' },

      { name: 'Correo (A - Z)', key: 'email-asc' },
      { name: 'Correo (Z - A)', key: 'email-desc' },

      { name: 'Teléfono (0 - 9)', key: 'phone-asc' },
      { name: 'Teléfono (9 - 0)', key: 'phone-desc' },
    ];
  }

  get tableHeaders(): Array<string> {
    return [
      'Nombre',
      'Apellido',
      'Rol',
      'Correo',
      'Teléfono'
    ];
  }

  get passwordInputType(): string {
    return this.passwordFieldProps.type
  }

  get passwordVisibilityIcon(): string {
    return this.passwordFieldProps.icon
  }

  get choosingFilterValue(): boolean {
    return this.filtersFormState.type === 'second-surface';
  }

  get filtersSecondSurfaceTitle(): string {
    if (this.filtersFormState.type !== 'second-surface') {
      return '';
    }

    const surfaceNameMap = {
      'role': 'Rol',
    }

    return surfaceNameMap[this.filtersFormState.surface];
  }

  get selectedRolesSummary(): string {
    const defaultText: string = 'Cualquiera';

    if (this.selectedRoles.length === 0) {
      return defaultText;
    }

    if (this.rolesOptionsStatus._type !== 'options-ready') {
      return defaultText;
    }

    const rolesNames: Array<string> = [];
    for (const id of this.selectedRoles) {
      const role: Role | undefined = this.rolesOptionsStatus.items.find(x => x.id == id);
      if (role) {
        rolesNames.push(role.name);
      }
    }
    return rolesNames.sort((x, y) => x.localeCompare(y)).join(', ');
  }

  onEditRowClick(): (item: CrudItem, formGroup: FormGroup) => void {
    return (item: CrudItem, updateItemForm: FormGroup) => {
      const user: UserDetails = (item as UserDetails);
      this.loadRolesOnRowClick(user.role.id, updateItemForm);
    }
  }

  onPasswordVisibilityClick() {
    if (this.passwordFieldProps === passwordVisibleProps) {
      this.passwordFieldProps = passwordNotVisibleProps;
    } else {
      this.passwordFieldProps = passwordVisibleProps
    }
  }

  onRetryLoadRoles(): () => void {
    return () => this.loadRoles();
  }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (request: PaginatedRequest) => this.userService.getUsers(
      request, {
      roles: this.selectedRoles,
    });
  }

  getCreationErrors(): (formGroup: FormGroup) => { [key: string]: string } {
    return (formGroup: FormGroup) => {
      const errors = this.commonUserErrors(formGroup);
      errors['password'] = this.passwordError(formGroup);
      errors['confirmedPassword'] = this.confirmedPasswordError(formGroup);
      return errors;
    }
  }

  getUpdateErrors(): (formGroup: FormGroup) => { [key: string]: string } {
    return (formGroup: FormGroup) => this.commonUserErrors(formGroup);
  }

  mapFormToCreationDto(): (formGroup: FormGroup) => CreateUserCommand {
    return (formGroup: FormGroup) => ({
      ...formGroup.value,
      roleId: formGroup.value.role,
    });
  }

  mapFormToUpdateDto(): (formGroup: FormGroup) => UpdateUserCommand {
    return (formGroup: FormGroup) => ({
      ...formGroup.value,
      roleId: formGroup.value.role,
    });
  }

  deleteItemById(): (id: number) => Observable<void> {
    return (id: number) => this.userService.deleteUserById(id);
  }

  onGoBackToChoosingFilter(): () => void {
    return () => {
      this.filtersFormState = { type: 'first-surface' }
    }
  }

  onRoleCheckboxClick(id: number): void {
    this.selectedRoles = toggle(id, this.selectedRoles);
  }

  createCreationForm(): (formBuilder: FormBuilder) => FormGroup {
    return function (formBuilder: FormBuilder) {
      return formBuilder.group({
        name: [
          '',
          [Validators.required, Validators.maxLength(NAME_MAX_LENGTH)]
        ],
        lastName: [
          '',
          [Validators.required, Validators.maxLength(LAST_NAME_MAX_LENGTH)]
        ],
        phone: [
          '',
          [
            Validators.required,
            Validators.pattern(/^\d{10}$/)
          ]
        ],
        email: [
          '',
          [
            Validators.required,
            Validators.email,
            Validators.maxLength(EMAIL_MAX_LENGTH),
          ]
        ],
        district: [
          '',
          [Validators.required, Validators.maxLength(DISTRICT_MAX_LENGTH)]
        ],
        street: [
          '',
          [Validators.required, Validators.maxLength(STREET_MAX_LENGTH)]
        ],
        streetNumber: [
          '',
          [Validators.required, Validators.maxLength(STREET_NUMBER_MAX_LENGTH)]
        ],
        zipCode: [
          '',
          [
            Validators.required,
            Validators.pattern(/^\d{5}$/)
          ]
        ],
        role: [
          '',
          [
            Validators.required,
          ]
        ],
        password: [
          '',
          [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(60),
          ]
        ],
        confirmedPassword: [
          '',
          [
            Validators.required,
            Validators.minLength(8),
            Validators.maxLength(60),
          ]
        ],
      });
    }
  }

  createUpdateForm(): (formBuilder: FormBuilder) => FormGroup {
    return (formBuilder: FormBuilder) => {
      const form: FormGroup = this.createCreationForm()(formBuilder);
      form.removeControl('password');
      form.removeControl('confirmedPassword')
      return form;
    }
  }

  loadingSkeletonIndexRange(): Array<number> {
    return range(0, 20);
  }

  onCreateNewClick(): () => void {
    return () => {
      if (this.rolesOptionsStatus._type === 'base') {
        this.loadRoles();
      }
    }
  }

  mapItemOperationError(): (error: Error) => DisplayableError {
    return (error: Error): DisplayableError => {
      if (error.name === 'UserExistsError') {
        return {
          header: 'Usuario ya existe',
          message: 'El correo ya pertenece a otro usuario.',
        }
      }

      return {
        header: 'Error',
        message: 'Ocurrió un error inesperado. Intenta de nuevo.',
      }
    }
  }

  createItem(): (dto: CreateUserCommand) => Observable<void> {
    return (dto: CreateUserCommand) => this.userService.createUser(dto);
  }

  updateItem(): (id: number, dto: UpdateUserCommand) => Observable<UpdateUserResponse> {
    return (id: number, dto: UpdateUserCommand) => this.userService.updateUser(id, dto);
  }

  handleUpdateResponse(): (response: UpdateUserResponse) => void {
    return (response: UpdateUserResponse) => {
      // Set new jwt if user changed their own email, 
      // so they can keep working without interruption 
      if (response.jwt) {
        this.authenticationHolder.setAuthentication({ token: response.jwt });
      }
    }
  }

  userCreationFormControls(formGroup: FormGroup): { [key: string]: FormControl } {
    return this.getControlsAsFormControls(formGroup);
  }

  userUpdateFormControls(formGroup: FormGroup): { [key: string]: FormControl } {
    return this.getControlsAsFormControls(formGroup);
  }

  onRoleFilterClick(): void {
    this.filtersFormState = { type: 'second-surface', surface: 'role' }
    if (this.rolesOptionsStatus._type === 'base') {
      this.loadRoles();
    }
  }

  onHideFiltersFormClick(): () => void {
    return () => {
      this.filtersFormState = { type: 'first-surface' }
    }
  }

  onPatchUpdateForm(): (formGroup: FormGroup, item: CrudItem) => void {
    return (formGroup, item) => {
      const user: UserDetails = (item as UserDetails);
      formGroup.patchValue({
        ...user,
        ...user.address,
      });
    }
  }

  private loadRoles(): void {
    this.rolesOptionsStatus = { _type: 'loading-options' };
    this.authService.getRoles().subscribe({
      next: (roles: Array<Role>) => {
        this.rolesOptionsStatus = {
          _type: 'options-ready', items: roles,
        }
      },
      error: (_) => this.rolesOptionsStatus = { _type: 'error' },
    });
  }

  private getControlsAsFormControls(formGroup: FormGroup): { [key: string]: FormControl } {
    const controls: { [key: string]: FormControl } = {};
    Object.entries(formGroup.controls).forEach(entry => {
      controls[entry[0]] = entry[1] as FormControl;
    })
    return controls;
  }

  private loadRolesOnRowClick(roleId: number, form: FormGroup) {
    if (this.rolesOptionsStatus._type === 'base') {
      this.loadRoles();
    }
    form.get('role')?.setValue(roleId);
  }

  private commonUserErrors(formGroup: FormGroup): { [key: string]: string } {
    const errors: { [key: string]: string } = {};
    errors['name'] = this.nameError(formGroup);
    errors['lastName'] = this.lastNameError(formGroup);
    errors['district'] = this.districtError(formGroup);
    errors['street'] = this.streetError(formGroup);
    errors['streetNumber'] = this.streetNumberError(formGroup);
    errors['email'] = this.emailError(formGroup);
    errors['phone'] = this.phoneError(formGroup);
    errors['zipCode'] = this.zipCodeError(formGroup);
    errors['role'] = this.roleError(formGroup);
    return errors
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

  private lastNameError(form: FormGroup): string {
    const control: FormControl = form.get('lastName') as FormControl;

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
      return `Máximo ${LAST_NAME_MAX_LENGTH} caracteres`;
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

  private emailError(form: FormGroup): string {
    const control: FormControl = form.get('email') as FormControl;

    if (control.valid) {
      return '';
    }

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    if (control.errors?.['email']) {
      return 'Correo inválido';
    }

    if (control.errors?.['maxlength']) {
      return `Máximo ${EMAIL_MAX_LENGTH} caracteres`;
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
      return 'Deben ser 10 dígitos';
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

  private roleError(form: FormGroup): string {
    const control: FormControl = form.get('role') as FormControl;

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


  private passwordError(form: FormGroup): string {
    const control: FormControl = form.get('password') as FormControl;

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
      return 'Mínimo 8 caracteres';
    }

    if (control.errors?.['maxlength']) {
      return 'Máximo 60 caracteres';
    }

    return '';
  }

  private confirmedPasswordError(form: FormGroup): string {
    const control: FormControl = form.get('confirmedPassword') as FormControl;
    const passwordControl = form.get('password') as FormControl;

    if (!(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors?.['required']) {
      return 'Valor requerido';
    }

    if (control.errors?.['minlength']) {
      return 'Mínimo 8 caracteres';
    }

    if (control.errors?.['maxlength']) {
      return 'Máximo 60 caracteres';
    }

    if (control.value !== passwordControl.value) {
      return 'Las contraseñas no coinciden'
    }

    return '';
  }

}
