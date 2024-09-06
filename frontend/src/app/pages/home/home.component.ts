import { Component } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { DayPart, getDayPart, getSalutation } from '../../common/day-part';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

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
  imports: [
    ButtonModule,
    ProgressSpinnerModule,
  ],
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
      error: (error) => console.log(error.message),
    })
  }

  get loading(): boolean {
    return this.status._type === 'loading';
  }

  get salutation(): string {
    if (this.status._type === 'base') {
      const salutation = getSalutation(getDayPart(new Date()));
      return `¡${salutation}, ${this.status.user.name}!`
    }
    return '';
  }

  get isDay(): boolean {
    const dayPart: DayPart = getDayPart(new Date());
    return dayPart === DayPart.DAY || dayPart === DayPart.AFTERNOON;
  }

  get formattedDate(): string {

    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

    const date: Date = new Date();

    // Extract day, month, year, hours, and minutes
    const day = date.getDate().toString().padStart(2, '0');
    const month = months[date.getMonth()];
    const year = date.getFullYear();
    let hours = date.getHours();
    const minutes = date.getMinutes().toString().padStart(2, '0');

    // Determine AM/PM and adjust hours
    const period = hours >= 12 ? 'pm' : 'am';
    hours = hours % 12 || 12; // Convert to 12-hour format

    // Format the date and time
    return `${day}/${month}/${year}. ${hours}:${minutes} ${period}`;
  }

  get userPersonalName(): string {
    if (this.status._type === 'base') {
      return this.status.user.name
    }
    return ''
  }
}