import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent, DisplayableError } from '../crud-module/crud-module.component';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { PaginatedResponse } from '../../common/paginated-response';
import { CreateUserCommand, State, UpdateUserCommand, UpdateUserResponse, UserDetails, UsersService } from '../../services/users-service';
import { PaginatedRequest } from '../../common/paginated-request';
import { Observable } from 'rxjs';
import { SortOption } from '../users/users.component';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonModule } from 'primeng/button';
import { LocationsService } from '../../services/locations-service';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService, Role } from '../../services/auth-service';
import { UserFormFieldsComponent } from './user-form-fields/user-form-fields.component';
import { OptionsStatus } from '../../common/options-status';
import { AuthenticationProofVault } from '../../services/authentication-proof-vault';
import { FiltersFormFieldComponent } from '../../components/filters-form-field/filters-form-field.component';

type FirstSurfaceState = {
  type: 'first-surface'
}

type SecondSurfaceState = {
  type: 'second-surface',
  surface: 'state' | 'role'
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
  ],
  providers: [AuthenticationProofVault],
  templateUrl: './users2.component.html',
  styleUrl: './users2.component.css'
})
export class Users2Component {

  statesOptionsStatus: OptionsStatus<State>;
  rolesOptionsStatus: OptionsStatus<Role>;
  passwordFieldProps: PasswordFieldProps;
  filtersFormState: FiltersFormState;

  constructor(
    private userService: UsersService,
    private locationsService: LocationsService,
    private authenticationProofVault: AuthenticationProofVault,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.statesOptionsStatus = { _type: 'base' };
    this.rolesOptionsStatus = { _type: 'base' };
    this.filtersFormState = { type: 'first-surface' }
    this.passwordFieldProps = passwordNotVisibleProps;
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Nombre (A - Z)', key: 'name-asc' },
      { name: 'Nombre (Z - A)', key: 'name-desc' },
      { name: 'Apellido (A - Z)', key: 'lastName-asc' },
      { name: 'Apellido (Z - A)', key: 'lastName-desc' },
    ];
  }

  get tableHeaders(): Array<string> {
    return [
      'Nombre',
      'Apellido',
      'Rol',
      'Ubicación',
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
    if (this.filtersFormState.type === 'second-surface' && this.filtersFormState.surface === 'role') {
      return 'Rol';
    }

    if (this.filtersFormState.type === 'second-surface' && this.filtersFormState.surface === 'state') {
      return 'Estado de la República';
    }

    return '';
  }

  loadOptionsOnRowClick(): (item: CrudItem, formGroup: FormGroup) => void {
    return (item: CrudItem, updateItemForm: FormGroup) => {
      const user: UserDetails = (item as UserDetails);
      this.loadRolesOnRowClick(user.role.id, updateItemForm);
      this.loadStatesOnRowClick(user.state.id, updateItemForm);
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

  onRetryLoadStates(): () => void {
    return () => this.loadStates();
  }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    return (request: PaginatedRequest) => this.userService.getUsers(request);
  }

  formatUserLocation(user: UserDetails): string {
    return `${user.city}, ${user.state.id}`
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

  deleteItemById(): (id: string) => Observable<void> {
    return (id: string) => this.userService.deleteUserById(id);
  }

  onGoBackToChoosingFilter(): () => void {
    return () => {
      this.filtersFormState = { type: 'first-surface' }
    }
  }

  createCreationForm(): (formBuilder: FormBuilder) => FormGroup {
    return function (formBuilder: FormBuilder) {
      return formBuilder.group({
        name: [
          '',
          [Validators.required, Validators.maxLength(60)]
        ],
        lastName: [
          '',
          [Validators.required, Validators.maxLength(60)]
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
            Validators.maxLength(60),
          ]
        ],
        district: [
          '',
          [Validators.required, Validators.maxLength(60)]
        ],
        city: [
          '',
          [Validators.required, Validators.maxLength(60)]
        ],
        state: [
          '',
          [Validators.required, Validators.maxLength(60)]
        ],
        street: [
          '',
          [Validators.required, Validators.maxLength(60)]
        ],
        streetNumber: [
          '',
          [Validators.required, Validators.maxLength(10)]
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

  onCreateNewClick(): () => void {
    return () => {
      if (this.rolesOptionsStatus._type === 'base') {
        this.loadRoles();
      }

      if (this.statesOptionsStatus._type === 'base') {
        this.loadStates();
      }
    }
  }

  mapSaveItemError(): (error: Error) => DisplayableError {
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

  updateItem(): (id: string, dto: UpdateUserCommand) => Observable<UpdateUserResponse> {
    return (id: string, dto: UpdateUserCommand) => this.userService.updateUser(id, dto);
  }

  handleUpdateResponse(): (response: UpdateUserResponse) => void {
    return (response: UpdateUserResponse) => {
      // Set new jwt if user changed their own email, 
      // so they can keep working without interruption 
      if (response.jwt) {
        this.authenticationProofVault.setAuthenticationProof({ token: response.jwt });
      }
    }
  }

  userCreationFormControls(formGroup: FormGroup): { [key: string]: FormControl } {
    return this.getControlsAsFormControls(formGroup);
  }

  userUpdateFormControls(formGroup: FormGroup): { [key: string]: FormControl } {
    return this.getControlsAsFormControls(formGroup);
  }

  onStateFilterClick(): void {
    this.filtersFormState = { type: 'second-surface', surface: 'state' }
  }

  onRoleFilterClick(): void {
    this.filtersFormState = { type: 'second-surface', surface: 'role' }
  }

  onHideFiltersFormClick(): () => void {
    return () => {
      this.filtersFormState = { type: 'first-surface' }
    }
  }

  private loadRoles(): void {
    this.rolesOptionsStatus = { _type: 'loading-options' };
    this.authService.getRoles().subscribe({
      next: (roles: Array<Role>) => this.rolesOptionsStatus = {
        _type: 'options-ready', items: roles,
      },
      error: (_) => this.rolesOptionsStatus = { _type: 'error' },
    });
  }

  private loadStates(): void {
    this.statesOptionsStatus = { _type: 'loading-options' };
    this.locationsService.getStates().subscribe({
      next: (states: Array<State>) => {
        this.statesOptionsStatus = { _type: 'options-ready', items: states };
      },
      error: (_) => this.statesOptionsStatus = { _type: 'error' },
    })
  }

  private getControlsAsFormControls(formGroup: FormGroup): { [key: string]: FormControl } {
    const controls: { [key: string]: FormControl } = {};
    Object.entries(formGroup.controls).forEach(entry => {
      controls[entry[0]] = entry[1] as FormControl;
    })
    return controls;
  }

  private loadRolesOnRowClick(roleId: string, form: FormGroup) {
    if (this.rolesOptionsStatus._type === 'base') {
      this.loadRoles();
    }
    form.get('role')?.setValue(roleId);
  }

  private loadStatesOnRowClick(stateId: string, form: FormGroup) {
    if (this.statesOptionsStatus._type === 'base') {
      this.loadStates();
    }
    form.get('state')?.setValue(stateId);
  }

  private commonUserErrors(formGroup: FormGroup): { [key: string]: string } {
    const errors: { [key: string]: string } = {};
    errors['name'] = this.nameError(formGroup);
    errors['lastName'] = this.lastNameError(formGroup);
    errors['state'] = this.stateError(formGroup);
    errors['city'] = this.cityError(formGroup);
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
      return 'Máximo 60 caracteres';
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
      return 'Máximo 60 caracteres';
    }

    return '';
  }

  private stateError(form: FormGroup): string {
    const control: FormControl = form.get('state') as FormControl;

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

  private cityError(form: FormGroup): string {
    const control: FormControl = form.get('city') as FormControl;

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
      return 'Máximo 60 caracteres';
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
      return 'Máximo 60 caracteres';
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
      return 'Máximo 10 caracteres';
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
      return 'Máximo 60 caracteres';
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
