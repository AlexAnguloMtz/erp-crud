import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OptionsStatus } from '../../common/options-status';
import { State } from '../../services/users-service';
import { Role } from '../../services/auth-service';
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
  @Input() statesOptionsStatus: OptionsStatus<State>;
  @Input() errors: { [key: string]: string }
  @Input() controls: { [key: string]: FormControl }
  @Input() onRetryLoadStates: () => void
  @Input() onRetryLoadRoles: () => void

  get loadingStatesOptions(): boolean {
    return this.statesOptionsStatus._type === 'loading-options';
  }

  get loadingRolesOptions(): boolean {
    return this.rolesOptionsStatus._type === 'loading-options';
  }

  get loadingRolesError(): boolean {
    return this.rolesOptionsStatus._type === 'error';
  }

  get loadingStatesError(): boolean {
    return this.statesOptionsStatus._type === 'error';
  }

  get statesOptions(): Array<State> {
    if (this.statesOptionsStatus._type !== 'options-ready') {
      return [];
    }
    return this.statesOptionsStatus.items;
  }

  get rolesOptions(): Array<Role> {
    if (this.rolesOptionsStatus._type !== 'options-ready') {
      return [];
    }
    return this.rolesOptionsStatus.items;
  }

}