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

  @Input() value: string;
  @Input() text: string;
  @Input() model: Array<string>;
  @Output() onClick: EventEmitter<void> = new EventEmitter<void>();

  onCheckboxClick(): void {
    this.onClick?.emit();
  }

}
