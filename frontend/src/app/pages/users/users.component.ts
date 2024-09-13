import { Component } from '@angular/core';
import { CreateUserCommand, State, UpdateUserCommand, UpdateUserResponse, UserDetails, UsersService } from '../../services/users-service';
import { PaginatedResponse } from '../../common/paginated-response';
import { TableModule } from 'primeng/table';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { PaginatedRequest } from '../../common/paginated-request';
import { InputTextModule } from 'primeng/inputtext';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DropdownModule } from 'primeng/dropdown';
import { PaginatorModule } from 'primeng/paginator';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { AuthService, Role } from '../../services/auth-service';
import { Router } from '@angular/router';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { LocationsService } from '../../services/locations-service';

const RECORDS_PER_PAGE: number = 10;

type DisplayableError = {
  header: string,
  message: string,
}

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

type PageEvent = {
  first?: number;
  rows?: number;
  page?: number;
  pageCount?: number;
}

export type SortOption = {
  name: string,
  key: string,
}

type LoadingFirstTime = {
  _type: 'loading-first-time';
}

type LoadingSubsequentTime = {
  _type: 'loading-subsequent-time';
}

type BaseStatus = {
  _type: 'base',
  response: PaginatedResponse<UserDetails>;
}

type LoadUsersError = {
  _type: 'load-users-error'
}

type UsersStatus = LoadingFirstTime | LoadingSubsequentTime | BaseStatus | LoadUsersError;

type RolesOptions = {
  roles: Array<Role>
}

type RoleOptionsBase = {
  _type: 'base',
}

type LoadingRoleOptions = {
  _type: 'loading-user-form-options'
}

type RoleOptionsReady = {
  _type: 'user-form-options-ready',
  userFormOptions: RolesOptions
}

type StatesOptionsBase = {
  _type: 'base',
}

type LoadingStatesOptions = {
  _type: 'loading-states-options'
}

type StatesOptionsReady = {
  _type: 'states-options-ready',
  states: Array<State>
}

type LoadRolesError = {
  _type: 'error'
}

type LoadStatesError = {
  _type: 'error'
}

type RoleOptionsStatus = RoleOptionsBase | LoadingRoleOptions | RoleOptionsReady | LoadRolesError

type StatesOptionsStatus = StatesOptionsBase | LoadingStatesOptions | StatesOptionsReady | LoadStatesError

type UserCreationBase = {
  _type: 'user-creation-base'
}

type CreatingUser = {
  _type: 'creating-user'
}

type UserUpdateBase = {
  _type: 'user-update-base'
}

type UpdatingUser = {
  _type: 'updating-user'
}

type DeleteUserBase = {
  _type: 'delete-user-base'
}

type DeletingUser = {
  _type: 'deleting-user'
}

type CreateUserStatus = UserCreationBase | CreatingUser

type UpdateUserStatus = UserUpdateBase | UpdatingUser

type DeleteUserStatus = DeleteUserBase | DeletingUser

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    TableModule,
    ProgressSpinnerModule,
    InputTextModule,
    FormsModule,
    InputGroupModule,
    InputGroupAddonModule,
    ReactiveFormsModule,
    DropdownModule,
    PaginatorModule,
    ButtonModule,
    DialogModule,
    ConfirmDialogModule,
  ],
  providers: [ConfirmationService],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {

  status: UsersStatus;
  searchControl: FormControl;
  selectedSort: SortOption | undefined
  selectedPageNumber: number;
  lastSeenTotalItems: number;
  debounceTimeout: any;
  createItemVisible: boolean;
  updateItemVisible: boolean;

  roleOptionsStatus: RoleOptionsStatus;

  stateOptionsStatus: StatesOptionsStatus;

  createUserStatus: CreateUserStatus;
  updateUserStatus: UpdateUserStatus;
  deleteUserStatus: DeleteUserStatus;

  userForm: FormGroup;

  updateUserForm: FormGroup;

  userSavedDialogVisible: boolean;

  passwordFieldProps: PasswordFieldProps;

  userToUpdateId: string;

  userDeletedDialogVisible: boolean;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private confirmationService: ConfirmationService,
    private usersService: UsersService,
    private authService: AuthService,
    private locationsService: LocationsService,
  ) { }

  ngOnInit(): void {
    const token: string | null = window.localStorage.getItem('auth-token');

    if (!token) {
      this.router.navigate(['/login']);
    }

    this.userToUpdateId = '';
    this.searchControl = new FormControl('');
    this.selectedPageNumber = 0;
    this.lastSeenTotalItems = 0;
    this.createItemVisible = false;
    this.userSavedDialogVisible = false;
    this.userDeletedDialogVisible = false;
    this.passwordFieldProps = passwordNotVisibleProps;
    this.roleOptionsStatus = { _type: 'base' }
    this.stateOptionsStatus = { _type: 'base' }
    this.createUserStatus = { _type: 'user-creation-base' }
    this.updateUserStatus = { _type: 'user-update-base' }
    this.deleteUserStatus = { _type: 'delete-user-base' }
    this.userForm = this.createUserForm();
    this.updateUserForm = this.createUpdateUserForm();
    this.searchUsers(this.defaultPaginatedRequest(), { _type: 'loading-first-time' });
    this.searchControl.valueChanges.subscribe(search => this.debounceSearch(search))
  }

  get loadingFirstTime(): boolean {
    return this.status._type === 'loading-first-time';
  }

  get loadingSubsequentTime(): boolean {
    return this.status._type === 'loading-subsequent-time';
  }

  get users(): Array<UserDetails> {
    if (this.status._type !== 'base') {
      return [];
    }
    return this.status.response.items;
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Nombre (A - Z)', key: 'name-asc' },
      { name: 'Nombre (Z - A)', key: 'name-desc' },
      { name: 'Apellido (A - Z)', key: 'lastName-asc' },
      { name: 'Apellido (Z - A)', key: 'lastName-desc' },
    ];
  }

  get passwordInputType(): string {
    return this.passwordFieldProps.type
  }

  get passwordVisibilityIcon(): string {
    return this.passwordFieldProps.icon
  }


  get totalRecords(): number {
    return this.lastSeenTotalItems;
  }

  get first(): number {
    return (this.selectedPageNumber) * RECORDS_PER_PAGE;
  }

  get rows(): number {
    return RECORDS_PER_PAGE
  }

  get loadingUserFormOptions(): boolean {
    return this.roleOptionsStatus._type === 'loading-user-form-options';
  }

  get loadingStatesOptions(): boolean {
    return this.stateOptionsStatus._type === 'loading-states-options';
  }

  get roleOptions(): Array<Role> {
    if (this.roleOptionsStatus._type !== 'user-form-options-ready') {
      return [];
    }
    return this.roleOptionsStatus.userFormOptions.roles;
  }

  get loadingStatesError(): boolean {
    return this.stateOptionsStatus._type === 'error';
  }

  get loadingRolesError(): boolean {
    return this.roleOptionsStatus._type === 'error';
  }

  get stateOptions(): Array<State> {
    if (this.stateOptionsStatus._type !== 'states-options-ready') {
      return [];
    }
    return this.stateOptionsStatus.states;
  }

  get creationErrors(): { [key: string]: string } {
    const errors: { [key: string]: string } = this.commonUserFormErrors(this.userForm);
    errors['password'] = this.passwordError();
    errors['confirmedPassword'] = this.confirmedPasswordError();
    return errors
  }

  get updateErrors(): { [key: string]: string } {
    return this.commonUserFormErrors(this.updateUserForm);
  }

  get formattedResults(): string {
    let noun = this.lastSeenTotalItems === 1 ? 'resultado' : 'resultados';
    return `${this.lastSeenTotalItems} ${noun}`;
  }

  private commonUserFormErrors(form: FormGroup): { [key: string]: string } {
    const errors: { [key: string]: string } = {};
    errors['name'] = this.nameError(form);
    errors['lastName'] = this.lastNameError(form);
    errors['state'] = this.stateError(form);
    errors['city'] = this.cityError(form);
    errors['district'] = this.districtError(form);
    errors['street'] = this.streetError(form);
    errors['streetNumber'] = this.streetNumberError(form);
    errors['email'] = this.emailError(form);
    errors['phone'] = this.phoneError(form);
    errors['zipCode'] = this.zipCodeError(form);
    errors['role'] = this.roleError(form);
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


  private passwordError(): string {
    const control: FormControl = this.userForm.get('password') as FormControl;

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

  private confirmedPasswordError(): string {
    const control: FormControl = this.userForm.get('confirmedPassword') as FormControl;
    const passwordControl = this.userForm.get('password') as FormControl;

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

  get savingUser(): boolean {
    return this.createUserStatus._type === 'creating-user';
  }

  get updatingUser(): boolean {
    return this.updateUserStatus._type === 'updating-user';
  }

  onPasswordVisibilityClick() {
    if (this.passwordFieldProps === passwordVisibleProps) {
      this.passwordFieldProps = passwordNotVisibleProps;
    } else {
      this.passwordFieldProps = passwordVisibleProps
    }
  }

  onEditClick(user: UserDetails): void {
    this.userToUpdateId = user.id;
    this.updateUserForm.patchValue(user);
    this.updateItemVisible = true;
    this.loadRolesOnRowClick(user.role.id, this.updateUserForm);
    this.loadStatesOnRowClick(user.state.id, this.updateUserForm);
  }

  private loadRolesOnRowClick(roleId: string, form: FormGroup) {
    if (this.roleOptionsStatus._type === 'base') {
      this.roleOptionsStatus = { _type: 'loading-user-form-options' };
      this.authService.getRoles(window.localStorage.getItem('auth-token')!).subscribe({
        next: (roles: Array<Role>) => {
          this.roleOptionsStatus = { _type: 'user-form-options-ready', userFormOptions: { roles } };
        },
        error: (_) => this.roleOptionsStatus = { _type: 'error' },
      })
    }

    form.get('role')?.setValue(roleId);
  }

  private loadStatesOnRowClick(stateId: string, form: FormGroup) {
    if (this.stateOptionsStatus._type === 'base') {
      this.stateOptionsStatus = { _type: 'loading-states-options' };
      this.locationsService.getStates(window.localStorage.getItem('auth-token')!).subscribe({
        next: (states: Array<State>) => {
          this.stateOptionsStatus = { _type: 'states-options-ready', states };
        },
        error: (_) => this.stateOptionsStatus = { _type: 'error' },
      })
    }

    form.get('state')?.setValue(stateId);
  }

  onCreateUserSubmit(): void {
    if (!this.userForm.valid) {
      this.userForm.markAllAsTouched();
      return;
    }

    if (this.userForm.get('password')?.value !== this.userForm.get('confirmedPassword')?.value) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.createUserStatus = { _type: 'creating-user' }
    this.usersService.createUser(localStorage.getItem('auth-token')!, this.userCreationCommand()).subscribe({
      next: () => {
        this.searchUsers({
          ...this.defaultPaginatedRequest(),
          search: this.searchControl.value,
          sort: this.selectedSort?.key,
        }, { _type: 'loading-subsequent-time' });
        this.createItemVisible = false;
        this.createUserStatus = { _type: 'user-creation-base' }
        this.userSavedDialogVisible = true;
      },
      error: (error) => this.handleUserCreationError(error),
    })
  }

  onUpdateUserSubmit(): void {
    if (!this.updateUserForm.valid) {
      this.updateUserForm.markAllAsTouched();
      return;
    }

    this.updateUserStatus = { _type: 'updating-user' }
    this.usersService.updateUser(localStorage.getItem('auth-token')!, this.userToUpdateId, this.userUpdateCommand()).subscribe({
      next: (response: UpdateUserResponse) => {

        // Set new jwt if user changed their own email, 
        // so they can keep working without interruption 
        if (response.jwt) {
          localStorage.setItem('auth-token', response.jwt);
        }

        this.updateUserRow(response.user);
        this.updateItemVisible = false;
        this.updateUserStatus = { _type: 'user-update-base' }
        this.userSavedDialogVisible = true;
      },
      error: (error) => this.handleUserUpdateError(error),
    })
  }

  private updateUserRow(user: UserDetails): void {
    if (this.status._type !== 'base') {
      return;
    }

    this.status.response.items = this.status.response.items.map(x => {
      if (x.id == user.id) {
        return user;
      }
      return x;
    })
  }

  formatUserLocation(user: UserDetails): string {
    return `${user.city}, ${user.state.id}`
  }

  searchUsers(request: PaginatedRequest, loadingStatus: LoadingFirstTime | LoadingSubsequentTime): void {
    this.status = loadingStatus;
    this.usersService.getUsers(localStorage.getItem('auth-token')!, request).subscribe({
      next: (users: PaginatedResponse<UserDetails>) => this.handleUsers(users),
      error: (error) => this.handleGetUsersError(error),
    })
  }

  onSortChanged() {
    this.selectedPageNumber = 0;
    this.lastSeenTotalItems = 0;
    this.searchUsers({
      ...this.defaultPaginatedRequest(),
      search: this.searchControl.value,
      sort: this.selectedSort?.key,
    }, { _type: 'loading-subsequent-time' });
  }

  onRetryLoadUsers(): void {
    this.searchUsers({
      ...this.defaultPaginatedRequest(),
      search: this.searchControl.value,
      sort: this.selectedSort?.key,
      pageNumber: this.selectedPageNumber,
    }, { _type: 'loading-subsequent-time' });
  }

  onPageChange(event: PageEvent) {
    if (event.page !== null && event.page !== undefined) {
      this.selectedPageNumber = event.page;
      this.searchUsers({
        ...this.defaultPaginatedRequest(),
        search: this.searchControl.value,
        sort: this.selectedSort?.key,
        pageNumber: this.selectedPageNumber,
      }, { _type: 'loading-subsequent-time' });
    }
  }

  onCreateClick(): void {
    this.createItemVisible = true;
    if (this.roleOptionsStatus._type === 'base') {
      this.roleOptionsStatus = { _type: 'loading-user-form-options' };
      this.authService.getRoles(window.localStorage.getItem('auth-token')!).subscribe({
        next: (roles: Array<Role>) => {
          this.roleOptionsStatus = { _type: 'user-form-options-ready', userFormOptions: { roles } }
        },
        error: (_) => this.roleOptionsStatus = { _type: 'error' },
      })
    }

    if (this.stateOptionsStatus._type === 'base') {
      this.stateOptionsStatus = { _type: 'loading-states-options' };
      this.locationsService.getStates(window.localStorage.getItem('auth-token')!).subscribe({
        next: (states: Array<State>) => this.stateOptionsStatus = { _type: 'states-options-ready', states },
        error: (_) => this.stateOptionsStatus = { _type: 'error' },
      })
    }
  }

  onRetryLoadRoles(): void {
    this.roleOptionsStatus = { _type: 'loading-user-form-options' }
    this.authService.getRoles(window.localStorage.getItem('auth-token')!).subscribe({
      next: (roles: Array<Role>) => this.roleOptionsStatus = { _type: 'user-form-options-ready', userFormOptions: { roles } },
      error: (_) => this.roleOptionsStatus = { _type: 'error' },
    });
  }

  onRetryLoadStates(): void {
    this.stateOptionsStatus = { _type: 'loading-states-options' }
    this.locationsService.getStates(window.localStorage.getItem('auth-token')!).subscribe({
      next: (states: Array<State>) => this.stateOptionsStatus = { _type: 'states-options-ready', states },
      error: (_) => this.stateOptionsStatus = { _type: 'error' },
    })
  }

  onCancelUserForm(): void {
    this.createItemVisible = false;
  }

  onCancelUpdateUserForm(): void {
    this.updateItemVisible = false;
  }

  onHideUserForm(): void {
    this.userForm.reset();
  }

  onCloseSavedUserDialog(): void {
    this.userSavedDialogVisible = false;
  }

  onCloseDeletedUserDialog(): void {
    this.userDeletedDialogVisible = false;
  }

  onDeleteUser(id: string): void {
    this.deleteUserStatus = { _type: 'deleting-user' }
    this.usersService.deleteUserById(localStorage.getItem('auth-token')!, id).subscribe({
      next: () => {
        this.deleteUserStatus = { _type: 'delete-user-base' }
        this.updateItemVisible = false;
        this.removeDeletedRow(id);
        this.userDeletedDialogVisible = true;
      },
      error: (error) => this.handleUserDeleteError(error),
    });
  }

  private removeDeletedRow(id: string): void {
    if (this.status._type !== 'base') {
      return;
    }

    this.status.response.items = this.status.response.items.filter(x => x.id !== id);
  }

  get deletingUser(): boolean {
    return this.deleteUserStatus._type === 'deleting-user';
  }

  private userCreationCommand(): CreateUserCommand {
    return {
      ...this.userForm.value,
      roleId: this.userForm.value.role,
    }
  }

  private userUpdateCommand(): UpdateUserCommand {
    return {
      ...this.updateUserForm.value,
      roleId: this.updateUserForm.value.role,
    }
  }

  private debounceSearch(search: string): void {
    // Clear any existing timeout
    if (this.debounceTimeout) {
      clearTimeout(this.debounceTimeout);
    }

    // Set a new timeout
    this.debounceTimeout = setTimeout(() => {
      this.selectedPageNumber = 0;
      this.lastSeenTotalItems = 0;
      this.searchUsers({
        ...this.defaultPaginatedRequest(),
        search,
        sort: this.selectedSort?.key
      }, { _type: 'loading-subsequent-time' });
    }, 500); // Debounce delay
  }

  private handleUsers(response: PaginatedResponse<UserDetails>): void {
    this.status = { _type: 'base', response }
    this.lastSeenTotalItems = response.totalItems
  }

  private handleGetUsersError(error: Error): void {
    this.status = { _type: 'load-users-error' };
  }

  get loadUsersError() {
    return this.status._type === 'load-users-error';
  }

  private handleUserCreationError(error: Error): void {
    this.createUserStatus = { _type: 'user-creation-base' }
    this.handleUserError(error);
  }

  private handleUserUpdateError(error: Error): void {
    this.updateUserStatus = { _type: 'user-update-base' }
    this.handleUserError(error);
  }

  private handleUserDeleteError(error: Error): void {
    this.deleteUserStatus = { _type: 'delete-user-base' }
    this.handleUserError(error);
  }

  private handleUserError(error: Error): void {
    const err = this.mapSaveUserError(error);
    this.confirmationService.confirm({
      header: err.header,
      message: err.message,
      acceptIcon: "none",
      rejectVisible: false,
      acceptLabel: 'Cerrar',
      dismissableMask: true,
    });
  }

  private mapSaveUserError(error: Error): DisplayableError {
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

  private defaultPaginatedRequest(): PaginatedRequest {
    return {
      search: '',
      pageNumber: this.selectedPageNumber,
      pageSize: RECORDS_PER_PAGE,
      sort: '',
    }
  }

  private createUserForm(): FormGroup {
    return this.formBuilder.group({
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

  private createUpdateUserForm(): FormGroup {
    const userForm: FormGroup = this.createUserForm();
    userForm.removeControl('password');
    userForm.removeControl('confirmedPassword')
    return userForm;
  }
}