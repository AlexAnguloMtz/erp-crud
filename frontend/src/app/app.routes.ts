import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { MainTemplateComponent } from './pages/main-template/main-template.component';
import { HomeComponent } from './pages/home/home.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { Users2Component } from './pages/users2/users2.component';
import { BrandsComponent } from './pages/brands/brands.component';
import { MovementsComponent } from './pages/movements/movements.component';
import { BackupsComponent } from './pages/backups/backups.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'home',
        component: MainTemplateComponent,
        children: [
            {
                path: '',
                component: HomeComponent,
            },
            {
                path: 'users',
                component: Users2Component,
            },
            {
                path: 'inventory',
                component: InventoryComponent,
            },
            {
                path: 'brands',
                component: BrandsComponent,
            },
            {
                path: 'movements',
                component: MovementsComponent,
            },
            {
                path: 'backups',
                component: BackupsComponent,
            }
        ]
    }
];