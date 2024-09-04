import { Component } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { DayPart, getDayPart, getSalutation } from '../../common/day-part';

type User = {
  name: string
}

type LoadingStatus = {
  _type: 'loading'
}

type BaseStatus = {
  _type: 'base',
  user: User,
}

type HomeStatus = LoadingStatus | BaseStatus

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  status: HomeStatus;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) { }

  ngOnInit() {
    this.status = { _type: 'loading' };

    const token: string | null = window.localStorage.getItem('auth-token');

    if (!token) {
      this.router.navigate(['/login']);
    }

    this.authService.getUserData(token!).subscribe({
      next: (user: User) => this.status = { _type: 'base', user },
      error: (error) => console.log(error.message)
    })
  }

  get loading(): boolean {
    return this.status._type === 'loading';
  }

  get name(): string {
    if (this.status._type === 'base') {
      return this.status.user.name;
    }
    return ''
  }

  get salutation(): string {
    if (this.status._type === 'base') {
      const salutation = getSalutation(getDayPart(new Date()));
      return `¡${salutation}, ${this.status.user.name}!`
    }
    return '';
  }

  get isDay(): boolean {
    return getDayPart(new Date()) === DayPart.DAY;
  }
}