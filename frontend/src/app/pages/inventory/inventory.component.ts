import { Component } from '@angular/core';
import { CrudModuleComponent } from '../crud-module/crud-module.component';

@Component({
  selector: 'app-inventory',
  standalone: true,
  imports: [
    CrudModuleComponent,
  ],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.css'
})
export class InventoryComponent {

}
