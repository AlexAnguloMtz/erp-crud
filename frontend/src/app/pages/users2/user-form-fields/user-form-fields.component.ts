import { Component, Input } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { loadingError, loadingOptions, options, OptionsStatus } from '../../../common/options-status';
import { Role } from '../../../services/auth-service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-user-form-fields',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    ProgressSpinnerModule,
    ButtonModule,
    DropdownModule,
    InputTextModule,
  ],
  templateUrl: './user-form-fields.component.html',
  styleUrl: './user-form-fields.component.css'
})
export class UserFormFieldsComponent {

  @Input() rolesOptionsStatus: OptionsStatus<Role>;
  @Input() errors: { [key: string]: string }
  @Input() controls: { [key: string]: FormControl }
  @Input() onRetryLoadRoles: () => void

  get loadingRolesOptions(): boolean {
    return loadingOptions(this.rolesOptionsStatus);
  }

  get loadingRolesError(): boolean {
    return loadingError(this.rolesOptionsStatus);
  }

  get rolesOptions(): Array<Role> {
    return options(this.rolesOptionsStatus);
  }

}