import { Component } from '@angular/core';
import { UserPreview, UsersService } from '../../services/users-service';
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

const RECORDS_PER_PAGE: number = 15;

type PageEvent = {
  first?: number;
  rows?: number;
  page?: number;
  pageCount?: number;
}

type SortOption = {
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
  response: PaginatedResponse<UserPreview>;
}

type UsersStatus = LoadingFirstTime | LoadingSubsequentTime | BaseStatus;

type UserFormOptions = {
  roles: Array<Role>
}

type UserFormOptionsBase = {
  _type: 'base',
}

type LoadingUserFormOptions = {
  _type: 'loading-user-form-options'
}

type UserFormOptionsReady = {
  _type: 'user-form-options-ready',
  userFormOptions: UserFormOptions
}

type UserFormOptionsStatus = UserFormOptionsBase | LoadingUserFormOptions | UserFormOptionsReady

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
  ],
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

  userFormOptionsStatus: UserFormOptionsStatus;

  userForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private usersService: UsersService,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.searchControl = new FormControl('');
    this.selectedPageNumber = 0;
    this.lastSeenTotalItems = 0;
    this.createItemVisible = false;
    this.userFormOptionsStatus = { _type: 'base' }
    this.userForm = this.createUserForm();
    this.searchUsers(this.defaultPaginatedRequest(), { _type: 'loading-first-time' });
    this.searchControl.valueChanges.subscribe(search => this.debounceSearch(search))
  }

  get loadingFirstTime(): boolean {
    return this.status._type === 'loading-first-time';
  }

  get loadingSubsequentTime(): boolean {
    return this.status._type === 'loading-subsequent-time';
  }

  get users(): Array<UserPreview> {
    if (this.status._type !== 'base') {
      return [];
    }
    return this.status.response.items;
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Nombre (Ascendente)', key: 'name-asc' },
      { name: 'Nombre (Descendente)', key: 'name-desc' },
      { name: 'Apellido (Ascendente)', key: 'last-name-asc' },
      { name: 'Apellido (Descendente)', key: 'last-name-desc' },
      { name: 'Rol (Ascendente)', key: 'role-asc' },
      { name: 'Rol (Descendente)', key: 'role-desc' },
      { name: 'Ciudad (Ascendente)', key: 'city-asc' },
      { name: 'Ciudad (Descendente)', key: 'city-desc' },
      { name: 'Estado (Ascendente)', key: 'state-asc' },
      { name: 'Estado (Descendente)', key: 'state-desc' },
    ];
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

  get createNewVisible(): boolean {
    return this.createItemVisible;
  }

  get loadingUserFormOptions(): boolean {
    return this.userFormOptionsStatus._type === 'loading-user-form-options';
  }

  get roleOptions(): Array<Role> {
    if (this.userFormOptionsStatus._type !== 'user-form-options-ready') {
      return [];
    }
    return this.userFormOptionsStatus.userFormOptions.roles;
  }

  get nameError(): string {
    const control: FormControl = this.userForm.get('name') as FormControl;

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

  get lastNameError(): string {
    const control: FormControl = this.userForm.get('lastName') as FormControl;

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

  get stateError(): string {
    const control: FormControl = this.userForm.get('state') as FormControl;

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

  onUserFormSubmit(): void {
    if (!this.userForm.valid) {
      console.log('invalid')
      this.userForm.markAllAsTouched();
      return;
    }
    // this.loginFormStatus = { _type: 'login-logging' };
    // this.loginService.logIn(this.loginForm.value).subscribe({
    //   next: (token: string) => this.handleAuthenticationToken(token),
    //   error: (error) => this.handleLoginError(error),
    // })
  }

  formatUserLocation(user: UserPreview): string {
    return `${user.city}, ${user.state.substring(0, 3)}`
  }

  searchUsers(request: PaginatedRequest, loadingStatus: LoadingFirstTime | LoadingSubsequentTime): void {
    this.status = loadingStatus;
    this.usersService.getUsers(request).subscribe({
      next: (users: PaginatedResponse<UserPreview>) => this.handleUsers(users),
      error: (error) => this.handleGetUsersError(error),
    })
  }

  onSortChanged() {
    this.searchUsers({
      ...this.defaultPaginatedRequest(),
      search: this.searchControl.value,
      sort: this.selectedSort?.key,
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
    if (this.userFormOptionsStatus._type === 'base') {
      this.userFormOptionsStatus = { _type: 'loading-user-form-options' };
      this.authService.getRoles().subscribe({
        next: (roles: Array<Role>) => this.userFormOptionsStatus = { _type: 'user-form-options-ready', userFormOptions: { roles } },
        error: (error) => console.log(error.message),
      })
    }
  }

  private debounceSearch(search: string): void {
    // Clear any existing timeout
    if (this.debounceTimeout) {
      clearTimeout(this.debounceTimeout);
    }

    // Set a new timeout
    this.debounceTimeout = setTimeout(() => {
      this.searchUsers({
        ...this.defaultPaginatedRequest(),
        search,
        sort: this.selectedSort?.key
      }, { _type: 'loading-subsequent-time' });
    }, 500); // Debounce delay
  }

  private handleUsers(response: PaginatedResponse<UserPreview>): void {
    this.status = { _type: 'base', response }
    this.lastSeenTotalItems = response.totalItems
  }

  private handleGetUsersError(error: Error): void {
    console.log(error.message)
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
          Validators.email
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
      ]
    });
  }
}