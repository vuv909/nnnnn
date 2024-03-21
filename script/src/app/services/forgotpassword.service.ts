import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ForgotpasswordService {
  email: string = '';

  constructor() {}

  setEmail(email: string) {
    this.email = email;
  }

  getEmail() {
    return this.email;
  }
}
