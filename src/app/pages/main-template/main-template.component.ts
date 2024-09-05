import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { RouterOutlet } from '@angular/router';
import { RouterModule, Routes } from '@angular/router';

type SidebarLink = {
  href: string,
  text: string,
  icon: string,
}

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

type MainTemplateStatus = LoadingStatus | BaseStatus

const links: Array<SidebarLink> = [
  {
    href: '/home',
    text: 'Inicio',
    icon: 'home',

  },
  {
    href: '/home/users',
    text: 'Usuarios',
    icon: 'user',
  },
];

@Component({
  selector: 'app-main-template',
  standalone: true,
  imports: [
    ButtonModule,
    DividerModule,
    ProgressSpinnerModule,
    RouterOutlet,
    RouterModule,
  ],
  templateUrl: './main-template.component.html',
  styleUrl: './main-template.component.css'
})
export class MainTemplateComponent {

  sidebarOpen: boolean;
  status: MainTemplateStatus;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.sidebarOpen = false;
    this.status = { _type: 'loading' }

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

  get links(): Array<SidebarLink> {
    return links;
  }

  get userPersonalName(): string {
    if (this.status._type === 'base') {
      return this.status.user.name
    }
    return ''
  }

  isActiveLink(link: SidebarLink): boolean {
    if (link.href === 'home') {
      return true
    }
    return false
  }

  onOpenSidebarClick(): void {
    this.sidebarOpen = true;
  }

  onCloseSidebarClick(): void {
    this.sidebarOpen = false;
  }

  onSidebarOverlayClick(): void {
    this.sidebarOpen = false;
  }

  onSidebarClick(event: Event): void {
    event.stopPropagation();
  }

  onSidebarLinkClick(): void {
    this.sidebarOpen = false;
  }
}
