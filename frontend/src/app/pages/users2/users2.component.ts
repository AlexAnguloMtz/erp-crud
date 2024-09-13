import { Component } from '@angular/core';
import { CrudItem, CrudModuleComponent } from '../crud-module/crud-module.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PaginatedResponse } from '../../common/paginated-response';
import { UserDetails, UsersService } from '../../services/users-service';
import { PaginatedRequest } from '../../common/paginated-request';
import { Observable } from 'rxjs';
import { SortOption } from '../users/users.component';

@Component({
  selector: 'app-users2',
  standalone: true,
  imports: [CrudModuleComponent],
  templateUrl: './users2.component.html',
  styleUrl: './users2.component.css'
})
export class Users2Component {

  constructor(
    private userService: UsersService
  ) {

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

  getItems(): (token: string, request: PaginatedRequest) => Observable<PaginatedResponse<CrudItem>> {
    const func = (token: string, request: PaginatedRequest) => {
      return this.userService.getUsers(token, request);
    }

    return func;
  }

  formatUserLocation(user: UserDetails): string {
    return `${user.city}, ${user.state.id}`
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
    const func: (formBuilder: FormBuilder) => FormGroup = (formBuilder: FormBuilder) => {
      const form: FormGroup = this.createCreationForm()(formBuilder);
      form.removeControl('password');
      form.removeControl('confirmedPassword')
      return form;
    }

    return func;
  }

}
