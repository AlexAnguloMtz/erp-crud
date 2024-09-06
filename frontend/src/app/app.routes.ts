import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { MainTemplateComponent } from './pages/main-template/main-template.component';
import { HomeComponent } from './pages/home/home.component';
import { UsersComponent } from './pages/users/users.component';

export const routes: Routes = [
    {
        path: '',
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
            }
        ]
    }
];
