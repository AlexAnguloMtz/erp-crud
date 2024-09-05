import { Component } from '@angular/core';
import { UserPreview, UsersService } from '../../services/users-service';
import { PaginatedResponse } from '../../common/paginated-response';
import { TableModule } from 'primeng/table';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { PaginatedRequest } from '../../common/paginated-request';
import { InputTextModule } from 'primeng/inputtext';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DropdownModule } from 'primeng/dropdown';
import { PaginatorModule } from 'primeng/paginator';

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
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {

  status: UsersStatus;
  searchControl: FormControl;
  selectedSort: SortOption | undefined
  debounceTimeout: any;

  constructor(
    private usersService: UsersService
  ) { }

  ngOnInit(): void {
    this.searchControl = new FormControl('');
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
    if (this.status._type !== 'base') {
      return 0;
    }
    return this.status.response.totalItems;
  }

  get first(): number {
    if (this.status._type !== 'base') {
      return 0;
    }
    // TODO
    // Debug for zero and one
    return (this.status.response.pageNumber - 1) * RECORDS_PER_PAGE + 1;
  }

  get rows(): number {
    return RECORDS_PER_PAGE
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
    console.log(JSON.stringify(event))
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
  }

  private handleGetUsersError(error: Error): void {
    console.log(error.message)
  }

  private defaultPaginatedRequest(): PaginatedRequest {
    return {
      search: '',
      pageNumber: 0,
      pageSize: RECORDS_PER_PAGE,
      sort: '',
    }
  }
}