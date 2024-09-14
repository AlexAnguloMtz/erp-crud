import { Component } from '@angular/core';
import { MovementService } from '../../services/movement-service';

@Component({
  selector: 'app-movements',
  standalone: true,
  imports: [],
  templateUrl: './movements.component.html',
  styleUrl: './movements.component.css'
})
export class MovementsComponent {

  constructor(
    private movementsService: MovementService,
  ) { }

  ngOnInit(): void {
    this.movementsService.getMovements(localStorage.getItem('auth-token')!, {}).subscribe({
      next: (it) => console.log(JSON.stringify(it)),
      error: (error) => console.log(error.message),
    })
  }
}