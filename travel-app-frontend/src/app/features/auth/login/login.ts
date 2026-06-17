import { Component, ChangeDetectorRef, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthResponseDto } from '../../../shared/models/app.models';

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
      localStorage.clear();
    }

    const credentials = {
      username: this.username,
      password: this.password
    };

    this.http.post<AuthResponseDto>('http://localhost:8080/api/auth/login', credentials).subscribe({
      next: (response) => {
        if (isPlatformBrowser(this.platformId) && response.token) {
          localStorage.setItem('jwt_token', response.token);

          // Decode the payload from the middle segment of the JWT token
          try {
            const payloadBase64 = response.token.split('.')[1];
            const payloadJson = atob(payloadBase64);
            const parsedToken = JSON.parse(payloadJson);

            // Match these property names to whatever claims you added in Java's JwtService!
            const userPayload = {
              id: parsedToken.userId || parsedToken.id, // Ensure your JwtService includes this claim
              username: parsedToken.sub,
              role: parsedToken.roles ? parsedToken.roles[0] : ''
            };

            localStorage.setItem('current_user', JSON.stringify(userPayload));

            if (userPayload.role === 'ROLE_ADMIN') {
              this.router.navigate(['/admin-dashboard']);
            } else {
              this.router.navigate(['/user-dashboard']);
            }
          } catch (e) {
            console.error('Failed to parse user claims from JWT token token:', e);
          }
        }
      }
    });
  }
  private getRoleFromToken(token: string): string {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.roles[0];
    } catch (e) {
      return '';
    }
  }
}
