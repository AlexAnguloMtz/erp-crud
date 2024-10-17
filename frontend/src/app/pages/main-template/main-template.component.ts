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
import { AuthenticationHolder } from '../../services/authentication-holder';

type SidebarLink = {
  id: string
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
    id: 'home',
    href: '/home',
    text: 'Inicio',
    icon: 'home',

  },
  {
    id: 'users',
    href: '/home/users',
    text: 'Usuarios',
    icon: 'user',
  },
  {
    id: 'products',
    href: '/home/products',
    text: 'Productos',
    icon: 'products',
  },
  //{
  //  id: 'movements',
  //  href: '/home/movements',
  //  text: 'Movimientos',
  //  icon: 'truck',
  //},
  {
    id: 'branches',
    href: '/home/branches',
    text: 'Sucursales',
    icon: 'map-marker',
  },
  {
    id: 'branch-types',
    href: '/home/branch-types',
    text: 'Tipos de sucursal',
    icon: 'home',
  },
  {
    id: 'product-categories',
    href: '/home/product-categories',
    text: 'Categorías de productos',
    icon: 'bookmark',
  },
  {
    id: 'brands',
    href: '/home/brands',
    text: 'Marcas',
    icon: 'tags',
  },
  {
    id: 'backups',
    href: '/home/backups',
    text: 'Respaldos',
    icon: 'download',
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
    private authenticationHolder: AuthenticationHolder,
    private usersService: UsersService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.items = this.createNavMenuItems();

    this.sidebarOpen = false;

    if (!this.authenticationHolder.hasValidAuthentication()) {
      this.router.navigate(['/login']);
      return;
    }

    this.getMe();
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
    this.getMe();
  }

  onLogoutClick(): void {
    this.authenticationHolder.removeAuthentication();
    this.router.navigate(['/login']);
  }

  private createNavMenuItems(): Array<MenuItem> {
    return [
      {
        label: 'Opciones',
        items: [
          {
            id: 'logout',
            label: 'Cerrar sesión',
            icon: 'pi pi-sign-out',
            command: () => this.onLogoutClick(),
          }
        ]
      }
    ];
  }

  private getMe(): void {
    this.status = { _type: 'loading' }
    this.usersService.getMe().subscribe({
      next: (user: User) => this.status = { _type: 'base', user },
      error: (error) => this.handleGetMeError(error),
    })
  }

  private handleGetMeError(error: any): void {
    if (error.name === 'ForbiddenError') {
      this.authenticationHolder.removeAuthentication();
      this.router.navigate(['/login']);
      return;
    }
    this.status = { _type: 'error' }
  }

}