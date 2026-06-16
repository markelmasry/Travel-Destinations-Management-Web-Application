import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login';
import { AdminDashboardComponent } from './features/admin/admin-dashboard/admin-dashboard';
import { UserDashboardComponent } from './features/user/user-dashboard/user-dashboard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent },
  { path: 'user-dashboard', component: UserDashboardComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
