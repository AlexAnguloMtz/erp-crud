import { Component } from '@angular/core';
import { Movement, MovementService } from '../../services/movement-service';
import { CrudModuleComponent } from '../crud-module/crud-module.component';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../../common/paginated-response';
import { PaginatedRequest } from '../../common/paginated-request';
import { capitalize } from '../../common/strings';
import { SortOption } from '../../common/sort-option';

@Component({
  selector: 'app-movements',
  standalone: true,
  imports: [
    CrudModuleComponent,
  ],
  templateUrl: './movements.component.html',
  styleUrl: './movements.component.css'
})
export class MovementsComponent {

  constructor(
    private movementsService: MovementService,
  ) { }

  createItemCreationForm(): (formBuilder: FormBuilder) => FormGroup {
    return (formBuilder: FormBuilder) => {
      return formBuilder.group({

      });
    }
  }

  createItemUpdateForm(): (formBuilder: FormBuilder) => FormGroup {
    return (formBuilder: FormBuilder) => {
      return formBuilder.group({

      });
    }
  }

  getItems(): (request: PaginatedRequest) => Observable<PaginatedResponse<Movement>> {
    return (request: PaginatedRequest) => this.movementsService.getMovements(request, {});
  }

  getCreationErrors(): (formGroup: FormGroup) => { [key: string]: string } {
    return (formGroup: FormGroup) => {
      const errors: { [key: string]: string } = {};
      return errors;
    }
  }

  getUpdateErrors(): (formGroup: FormGroup) => { [key: string]: string } {
    return (formGroup: FormGroup) => {
      const errors: { [key: string]: string } = {};
      return errors;
    }
  }

  formatTime(date: Date): string {
    const formatter = new Intl.DateTimeFormat('es-ES', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    });

    const formattedDateParts = formatter.formatToParts(date);

    const day = formattedDateParts.find(part => part.type === 'day')?.value;
    const month = formattedDateParts.find(part => part.type === 'month')?.value;
    const year = formattedDateParts.find(part => part.type === 'year')?.value;
    const hour = formattedDateParts.find(part => part.type === 'hour')?.value;
    const minute = formattedDateParts.find(part => part.type === 'minute')?.value;

    const formattedMonth = capitalize(month!);

    return `${day}/${formattedMonth}/${year}, ${hour}:${minute}`;
  }

  formatResponsibleName(movement: Movement): string {
    return `${movement.responsible.name} ${movement.responsible.lastName}`;
  }

  get tableHeaders(): Array<string> {
    return [
      'Fecha/Hora',
      'Responsable',
      'Tipo movimiento',
      'Observaciones',
    ];
  }

  get sortOptions(): Array<SortOption> {
    return [
      { name: 'Más recientes primero', key: 'timestamp-desc' },
      { name: 'Más antiguos primero', key: 'timestamp-asc' },
    ];
  }
}