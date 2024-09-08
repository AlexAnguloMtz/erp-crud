import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { UsersService } from '../../services/users-service';
import { MenuItem } from 'primeng/api';
import { MenuModule } from 'primeng/menu';
import { ToastModule } from 'primeng/toast';

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

type ErrorStatus = {
  _type: 'error',
}

type MainTemplateStatus = LoadingStatus | BaseStatus | ErrorStatus

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
    MenuModule,
    ToastModule,
  ],
  templateUrl: './main-template.component.html',
  styleUrl: './main-template.component.css'
})
export class MainTemplateComponent {

  items: MenuItem[] | undefined;
  sidebarOpen: boolean;
  status: MainTemplateStatus;

  constructor(
    private usersService: UsersService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.items = [
      {
        label: 'Opciones',
        items: [
          {
            label: 'Cerrar sesión',
            icon: 'pi pi-sign-out',
            command: () => this.onLogoutClick(),
          }
        ]
      }
    ];

    this.sidebarOpen = false;

    const token: string | null = window.localStorage.getItem('auth-token');

    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    this.getMe(token);
  }

  get loading(): boolean {
    return this.status._type === 'loading';
  }

  get loadError(): boolean {
    return this.status._type === 'error';
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
    return this.router.isActive(link.href, { paths: 'exact', matrixParams: 'ignored', queryParams: 'ignored', fragment: 'ignored' });
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

  onRetryGetMe(): void {
    this.getMe(localStorage.getItem('auth-token')!);
  }

  onLogoutClick(): void {
    localStorage.removeItem('auth-token');
    this.router.navigate(['/login']);
    console.log('should be logged out')
  }

  private handleGetMeError(error: any): void {
    this.status = { _type: 'error' }
  }

  private getMe(token: string): void {
    this.status = { _type: 'loading' }
    this.usersService.getMe(token).subscribe({
      next: (user: User) => this.status = { _type: 'base', user },
      error: (error) => this.handleGetMeError(error),
    })
  }
}