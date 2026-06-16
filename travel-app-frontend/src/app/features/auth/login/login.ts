import { Component, ChangeDetectorRef, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthResponse, UserResponse, Role } from '../../../shared/models/app.models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  login() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('current_user');
    }

    const credentials = { username: this.username, password: this.password };

    this.http.post<AuthResponse>('http://localhost:8080/api/auth/login', credentials).subscribe({
      next: (response) => {
        if (isPlatformBrowser(this.platformId)) {

          if (response.token) {
            localStorage.setItem('jwt_token', response.token);
          }

          let resolvedRole: Role | undefined;
          let userPayload: UserResponse | null = null;

          if ('user' in response && response['user']) {
            const nestedUser = response['user'] as UserResponse;
            resolvedRole = nestedUser.role;
            userPayload = nestedUser;
          }
          else if ('role' in response) {
            const flatResponse = response as any;
            resolvedRole = flatResponse.role;
            userPayload = {
              id: flatResponse.id,
              username: flatResponse.username,
              role: flatResponse.role
            };
          }

          if (userPayload && resolvedRole) {
            localStorage.setItem('current_user', JSON.stringify(userPayload));
            this.cdr.detectChanges();

            if (resolvedRole === 'ROLE_ADMIN') {
              this.router.navigate(['/admin-dashboard']);
            } else {
              this.router.navigate(['/user-dashboard']);
            }
          } else {
            console.error('Authentication response structural mismatch: Could not resolve user roles.');
            this.errorMessage = 'An internal application layout mismatch occurred.';
            this.cdr.detectChanges();
          }
        }
      },
      error: (err) => {
        console.error('Authentication request rejected:', err);
        this.errorMessage = 'Invalid credentials. Please try again.';
        this.cdr.detectChanges();
      }
    });
  }
}
