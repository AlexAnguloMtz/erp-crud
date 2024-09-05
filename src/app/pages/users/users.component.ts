import { Component } from '@angular/core';
import { UserPreview, UsersService } from '../../services/users-service';
import { PaginatedResponse } from '../../common/paginated-response';
import { TableModule } from 'primeng/table';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

type LoadingStatus = {
  _type: 'loading';
}

type BaseStatus = {
  _type: 'base',
  response: PaginatedResponse<UserPreview>;
}

type UsersStatus = LoadingStatus | BaseStatus;

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [TableModule, ProgressSpinnerModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent {

  status: UsersStatus;

  constructor(
    private usersService: UsersService
  ) { }

  ngOnInit(): void {
    this.status = { _type: 'loading' }
    this.usersService.getUsers().subscribe({
      next: (users: PaginatedResponse<UserPreview>) => this.handleUsers(users),
      error: (error) => this.handleGetUsersError(error),
    })
  }

  private handleUsers(response: PaginatedResponse<UserPreview>): void {
    this.status = { _type: 'base', response }
  }

  private handleGetUsersError(error: Error): void {

  }

  get loading(): boolean {
    return this.status._type === 'loading';
  }

  get users(): Array<UserPreview> {
    if (this.status._type !== 'base') {
      return [];
    }
    return this.status.response.items;
  }

  formatUserLocation(user: UserPreview): string {
    return `${user.city}, ${user.state.substring(0, 3)}`
  }

}
