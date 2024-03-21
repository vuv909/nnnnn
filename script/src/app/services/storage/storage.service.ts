import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
const TOKEN_KEY = 'token';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  constructor() {}

  static getEmail(): string {
    const token = this.getToken();
    if (token) {
      const decodedToken: any = jwtDecode(token);
      return decodedToken.sub || ''; // Access 'sub' property safely
    }
    return '';
  }

  static saveToken(token: string): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.setItem(TOKEN_KEY, token);
  }

  static getToken(): string | null {
    const token = localStorage.getItem(TOKEN_KEY);
    if (!token) {
      return null;
    }
    try {
      const decodedToken: any = jwtDecode(token);
      // Check token expiration
      const now = Math.floor(Date.now() / 1000); // Get current timestamp in seconds
      if (decodedToken.exp && decodedToken.exp < now) {
        // Token is expired
        this.signout();
        return null;
      }
      return token;
    } catch (error) {
      // Token is invalid
      this.signout();
      return null;
    }
  }

  static isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  static signout(): void {
    localStorage.removeItem(TOKEN_KEY);
    // Redirect to login page
    window.location.href = '/login';
  }
}
