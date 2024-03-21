import { Injectable, OnInit } from '@angular/core';
import { ProfileService } from './profile/profile.service';
import { StorageService } from './storage/storage.service';
import { AuthService } from './auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class GetRoleService implements OnInit {
  role: any;
  authService!: AuthService;

  constructor() {}

  ngOnInit(): void {
    this.authService.getRole().subscribe(
      (res) => {
        console.log('====================================');
        console.log('res::', res);
        console.log('====================================');
        this.role = res;
      },
      (error) => {
        console.log('====================================');
        console.log('error::', error);
        console.log('====================================');
      }
    );
  }

  getRole() {
    return this.role;
  }
}
