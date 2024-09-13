import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { MainTemplateComponent } from './pages/main-template/main-template.component';
import { HomeComponent } from './pages/home/home.component';
import { UsersComponent } from './pages/users/users.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { Users2Component } from './pages/users2/users2.component';

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
                component: UsersComponent,
            },
            {
                path: 'users-2',
                component: Users2Component,
            },
            {
                path: 'inventory',
                component: InventoryComponent
            }
        ]
    }
];