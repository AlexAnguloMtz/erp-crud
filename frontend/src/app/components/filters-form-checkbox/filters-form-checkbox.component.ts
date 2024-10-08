import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CheckboxModule } from 'primeng/checkbox';

@Component({
  selector: 'app-filters-form-checkbox',
  standalone: true,
  imports: [
    CheckboxModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  templateUrl: './filters-form-checkbox.component.html',
  styleUrl: './filters-form-checkbox.component.css'
})
export class FiltersFormCheckboxComponent {

  @Input() text: string;
  @Input() checked: boolean;
  @Output() onClick: EventEmitter<void> = new EventEmitter<void>();

  onCheckboxClick(): void {
    this.onClick?.emit();
  }

}
