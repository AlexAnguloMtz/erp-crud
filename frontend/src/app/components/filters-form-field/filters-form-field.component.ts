import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-filters-form-field',
  standalone: true,
  imports: [],
  templateUrl: './filters-form-field.component.html',
  styleUrl: './filters-form-field.component.css'
})
export class FiltersFormFieldComponent {

  @Input() labelText: string;
  @Input() valueText: string;
  @Output() onClick: EventEmitter<void> = new EventEmitter();

  onClicked(): void {
    this.onClick?.emit();
  }

}
