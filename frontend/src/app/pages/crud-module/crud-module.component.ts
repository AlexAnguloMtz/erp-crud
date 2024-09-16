import { Component, Input, TemplateRef } from '@angular/core';
import { PaginatedResponse } from '../../common/paginated-response';
import { TableModule } from 'primeng/table';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { PaginatedRequest } from '../../common/paginated-request';
import { InputTextModule } from 'primeng/inputtext';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DropdownModule } from 'primeng/dropdown';
import { PaginatorModule } from 'primeng/paginator';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { Router } from '@angular/router';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { AuthenticationProof, AuthenticationProofVault } from '../../services/authentication-proof-vault';

const RECORDS_PER_PAGE: number = 15;

export type CrudItem = {
  id: string
}

export type DisplayableError = {
  header: string,
  message: string,
}

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
  response: PaginatedResponse<CrudItem>;
}

type LoadItemsError = {
  _type: 'load-items-error'
}

type ItemsStatus = LoadingFirstTime | LoadingSubsequentTime | BaseStatus | LoadItemsError;

type ItemCreationBase = {
  _type: 'item-creation-base'
}

type CreatingItem = {
  _type: 'creating-item'
}

type ItemUpdateBase = {
  _type: 'item-update-base'
}

type UpdatingItem = {
  _type: 'updating-item'
}

type DeleteItemBase = {
  _type: 'delete-item-base'
}

type DeletingItem = {
  _type: 'deleting-item'
}

type CreateItemStatus = ItemCreationBase | CreatingItem

type UpdateItemStatus = ItemUpdateBase | UpdatingItem

type DeleteItemStatus = DeleteItemBase | DeletingItem

@Component({
  selector: 'app-crud-module',
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
    CommonModule
  ],
  providers: [ConfirmationService, AuthenticationProofVault],
  templateUrl: './crud-module.component.html',
  styleUrl: './crud-module.component.css'
})
export class CrudModuleComponent<CreationItemDto, UpdateItemDto, ItemUpdateResponse> {

  // Data
  @Input() sortOptions: Array<SortOption>;
  @Input() title: string;
  @Input() formTitle: string;
  @Input() tableHeaders: Array<string>
  @Input() updateItemFieldsStyle: { [key: string]: string } = {};
  @Input() searchPlaceholder: string;
  @Input() hasSearchBar: boolean = true;
  @Input() hasFilters: boolean = true;
  @Input() pluralNoun: string = '';
  @Input() choosingFilterValue: boolean = false;
  @Input() filtersSecondSurfaceTitle: string;

  // Functions
  @Input() createItemCreationForm: (formBuilder: FormBuilder) => FormGroup
  @Input() createItemUpdateForm: (formBuilder: FormBuilder) => FormGroup
  @Input() mapSaveItemError: (error: Error) => DisplayableError
  @Input() getItems: (request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>>
  @Input() createItem: (formValues: CreationItemDto) => Observable<void>
  @Input() updateItem: (id: string, formValues: UpdateItemDto) => Observable<ItemUpdateResponse>
  @Input() deleteItemById: (id: string) => Observable<void>
  @Input() loadOptionsOnRowClick: (item: CrudItem, form: FormGroup) => void;
  @Input() getCreationErrors: (form: FormGroup) => { [key: string]: string };
  @Input() getUpdateErrors: (form: FormGroup) => { [key: string]: string };
  @Input() mapFormToCreationDto: (form: FormGroup) => CreationItemDto
  @Input() mapFormToUpdateDto: (form: FormGroup) => UpdateItemDto
  @Input() handleUpdateResponse: (response: ItemUpdateResponse) => void;
  @Input() onCreateNewClick: () => void;
  @Input() onHideFiltersFormClick: () => void;
  @Input() onGoBackToChoosingFilter: () => void;

  // Templates
  @Input() filterFieldsTemplate: TemplateRef<any>;
  @Input() createItemFieldsTemplate: TemplateRef<any>;
  @Input() updateItemFieldsTemplate: TemplateRef<any>;
  @Input() rowTemplate: TemplateRef<any>;

  status: ItemsStatus;
  searchControl: FormControl;
  selectedSort: SortOption | undefined
  selectedPageNumber: number;
  lastSeenTotalItems: number;
  debounceTimeout: any;
  filtersVisible: boolean;
  createItemVisible: boolean;
  updateItemVisible: boolean;
  createItemStatus: CreateItemStatus;
  updateItemStatus: UpdateItemStatus;
  deleteItemStatus: DeleteItemStatus;

  itemCreationForm: FormGroup;

  updateItemForm: FormGroup;

  itemSavedDialogVisible: boolean;

  itemToUpdateId: string;

  itemDeletedDialogVisible: boolean;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authenticationProofVault: AuthenticationProofVault,
    private confirmationService: ConfirmationService,
  ) { }

  ngOnInit(): void {
    if (!this.authenticationProofVault.hasValidAuthenticationProof()) {
      this.router.navigate(['/login']);
    }

    this.itemToUpdateId = '';
    this.searchControl = new FormControl('');
    this.selectedPageNumber = 0;
    this.lastSeenTotalItems = 0;
    this.createItemVisible = false;
    this.itemSavedDialogVisible = false;
    this.itemDeletedDialogVisible = false;
    this.filtersVisible = false;
    this.createItemStatus = { _type: 'item-creation-base' }
    this.updateItemStatus = { _type: 'item-update-base' }
    this.deleteItemStatus = { _type: 'delete-item-base' }
    this.itemCreationForm = this.createItemCreationForm(this.formBuilder);
    this.updateItemForm = this.createItemUpdateForm(this.formBuilder);
    this.searchItems(this.defaultPaginatedRequest(), { _type: 'loading-first-time' });
    this.searchControl.valueChanges.subscribe(search => this.debounceSearch(search))
  }

  get loadingFirstTime(): boolean {
    return this.status._type === 'loading-first-time';
  }

  get loadingSubsequentTime(): boolean {
    return this.status._type === 'loading-subsequent-time';
  }

  get items(): Array<CrudItem> {
    if (this.status._type !== 'base') {
      return [];
    }
    return this.status.response.items;
  }

  get totalRecords(): number {
    return this.lastSeenTotalItems;
  }

  get first(): number {
    return (this.selectedPageNumber) * RECORDS_PER_PAGE;
  }

  get rows(): number {
    return RECORDS_PER_PAGE;
  }

  creationErrors(): { [key: string]: string } {
    return this.getCreationErrors(this.itemCreationForm);
  }

  updateErrors(): { [key: string]: string } {
    return this.getUpdateErrors(this.updateItemForm);
  }

  get formattedResults(): string {
    let noun = this.lastSeenTotalItems === 1 ? 'resultado' : 'resultados';
    return `${this.lastSeenTotalItems} ${noun}`;
  }

  get savingItem(): boolean {
    return this.createItemStatus._type === 'creating-item';
  }

  get updatingItem(): boolean {
    return this.updateItemStatus._type === 'updating-item';
  }

  onEditClick(item: CrudItem): void {
    this.itemToUpdateId = item.id;
    this.updateItemForm.patchValue(item);
    this.updateItemVisible = true;
    this.loadOptionsOnRowClick?.(item, this.updateItemForm);
  }

  onShowFilters(): void {
    this.filtersVisible = true;
  }

  onFiltersSubmit(): void {
    this.filtersVisible = false;
    this.searchItems({
      ...this.defaultPaginatedRequest(),
      search: this.searchControl.value,
      sort: this.selectedSort?.key,
    }, { _type: 'loading-subsequent-time' });
  }

  onCreateItemSubmit(): void {
    if (!this.itemCreationForm.valid) {
      this.itemCreationForm.markAllAsTouched();
      return;
    }

    if (this.itemCreationForm.get('password')?.value !== this.itemCreationForm.get('confirmedPassword')?.value) {
      this.itemCreationForm.markAllAsTouched();
      return;
    }

    this.createItemStatus = { _type: 'creating-item' }
    this.createItem(this.mapFormToCreationDto(this.itemCreationForm)).subscribe({
      next: () => {
        this.searchItems({
          ...this.defaultPaginatedRequest(),
          search: this.searchControl.value,
          sort: this.selectedSort?.key,
        }, { _type: 'loading-subsequent-time' });
        this.createItemVisible = false;
        this.createItemStatus = { _type: 'item-creation-base' }
        this.itemSavedDialogVisible = true;
      },
      error: (error) => this.handleItemCreationError(error),
    })
  }

  onUpdateItemSubmit(): void {
    if (!this.updateItemForm.valid) {
      this.updateItemForm.markAllAsTouched();
      return;
    }

    this.updateItemStatus = { _type: 'updating-item' }
    this.updateItem(this.itemToUpdateId, this.mapFormToUpdateDto(this.updateItemForm)).subscribe({
      next: (response: ItemUpdateResponse) => {
        this.handleUpdateResponse?.(response);
        this.searchItems({
          ...this.defaultPaginatedRequest(),
          search: this.searchControl.value,
          sort: this.selectedSort?.key,
        }, { _type: 'loading-subsequent-time' });
        this.updateItemVisible = false;
        this.updateItemStatus = { _type: 'item-update-base' }
        this.itemSavedDialogVisible = true;
      },
      error: (error) => this.handlItemUpdateError(error),
    })
  }

  searchItems(request: PaginatedRequest, loadingStatus: LoadingFirstTime | LoadingSubsequentTime): void {
    this.status = loadingStatus;
    this.getItems(request).subscribe({
      next: (response: PaginatedResponse<CrudItem>) => this.handleItems(response),
      error: (error) => this.handleGetItemsError(error),
    })
  }

  onSortChanged() {
    this.selectedPageNumber = 0;
    this.lastSeenTotalItems = 0;
    this.searchItems({
      ...this.defaultPaginatedRequest(),
      search: this.searchControl.value,
      sort: this.selectedSort?.key,
    }, { _type: 'loading-subsequent-time' });
  }

  onRetryLoadItems(): void {
    this.searchItems({
      ...this.defaultPaginatedRequest(),
      search: this.searchControl.value,
      sort: this.selectedSort?.key,
      pageNumber: this.selectedPageNumber,
    }, { _type: 'loading-subsequent-time' });
  }

  onPageChange(event: PageEvent) {
    if (event.page !== null && event.page !== undefined) {
      this.selectedPageNumber = event.page;
      this.searchItems({
        ...this.defaultPaginatedRequest(),
        search: this.searchControl.value,
        sort: this.selectedSort?.key,
        pageNumber: this.selectedPageNumber,
      }, { _type: 'loading-subsequent-time' });
    }
  }

  onCreateClick(): void {
    this.createItemVisible = true;
    this.onCreateNewClick?.();
  }

  onCancelFiltersForm(): void {
    this.filtersVisible = false;
  }

  onCancelItemForm(): void {
    this.createItemVisible = false;
  }

  onCancelUpdateItemForm(): void {
    this.updateItemVisible = false;
  }

  onHideItemForm(): void {
    this.itemCreationForm.reset();
  }

  onHideFiltersForm(): void {
    this.filtersVisible = false;
    this.onHideFiltersFormClick?.();
  }

  onCloseSavedItemDialog(): void {
    this.itemSavedDialogVisible = false;
  }

  onCloseDeletedItemDialog(): void {
    this.itemDeletedDialogVisible = false;
  }

  onDeleteItem(id: string): void {
    this.deleteItemStatus = { _type: 'deleting-item' }
    this.deleteItemById(id).subscribe({
      next: () => {
        this.deleteItemStatus = { _type: 'delete-item-base' }
        this.updateItemVisible = false;
        this.removeDeletedRow(id);
        this.itemDeletedDialogVisible = true;
      },
      error: (error) => this.handleItemDeleteError(error),
    });
  }

  private removeDeletedRow(id: string): void {
    if (this.status._type !== 'base') {
      return;
    }

    // Item was deleted, so we remove it from the user interface
    this.lastSeenTotalItems = this.lastSeenTotalItems - 1;
    this.status.response.items = this.status.response.items.filter(x => x.id !== id);
  }

  get deletingItem(): boolean {
    return this.deleteItemStatus._type === 'deleting-item';
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
      this.searchItems({
        ...this.defaultPaginatedRequest(),
        search,
        sort: this.selectedSort?.key
      }, { _type: 'loading-subsequent-time' });
    }, 500); // Debounce delay
  }

  private handleItems(response: PaginatedResponse<CrudItem>): void {
    this.status = { _type: 'base', response }
    this.lastSeenTotalItems = response.totalItems
  }

  private handleGetItemsError(error: Error): void {
    this.status = { _type: 'load-items-error' };
  }

  get loadItemsError() {
    return this.status._type === 'load-items-error';
  }

  private handleItemCreationError(error: Error): void {
    this.createItemStatus = { _type: 'item-creation-base' }
    this.handleItemOperationError(error);
  }

  private handlItemUpdateError(error: Error): void {
    this.updateItemStatus = { _type: 'item-update-base' }
    this.handleItemOperationError(error);
  }

  private handleItemDeleteError(error: Error): void {
    this.deleteItemStatus = { _type: 'delete-item-base' }
    this.handleItemOperationError(error);
  }

  private handleItemOperationError(error: Error): void {
    const err = this.mapSaveItemError(error);
    this.confirmationService.confirm({
      header: err.header,
      message: err.message,
      acceptIcon: "none",
      rejectVisible: false,
      acceptLabel: 'Cerrar',
      dismissableMask: true,
    });
  }

  private defaultPaginatedRequest(): PaginatedRequest {
    return {
      search: '',
      pageNumber: this.selectedPageNumber,
      pageSize: RECORDS_PER_PAGE,
      sort: '',
    }
  }

}
