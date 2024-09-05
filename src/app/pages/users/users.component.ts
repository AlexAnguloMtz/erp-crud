import { Component } from '@angular/core';
import { UserPreview, UsersService } from '../../services/users-service';
import { PaginatedResponse } from '../../common/paginated-response';

type LoadingStatus = {
  _type: 'loading';
}

type BaseStatus = {
  _type: 'base',
  users: Array<UserPreview>;
}

type UsersStatus = LoadingStatus;

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [],
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

  }

  private handleGetUsersError(error: Error): void {

  }

  get loading(): boolean {
    return this.status._type === 'loading';
  }

}
