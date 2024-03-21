import { Component } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { Router } from '@angular/router';
import { StorageService } from '../../services/storage/storage.service';
import { ForgotpasswordService } from '../../services/forgotpassword.service';

@Component({
  selector: 'app-enteremailforgot',
  templateUrl: './enteremailforgot.component.html',
  styleUrl: './enteremailforgot.component.scss',
})
export class EnteremailforgotComponent {
  email: string = '';
  isSpinning: boolean = false;
  constructor(
    private authService: AuthService,
    private msg: NzMessageService,
    private router: Router,
    private forgotService: ForgotpasswordService
  ) {}

  verify() {
    this.isSpinning = true;

    if (!this.validateEmailFormat(this.email)) {
      this.msg.error(
        'Email không hợp lệ. Vui lòng nhập đúng định dạng email.',
        {
          nzDuration: 2000,
        }
      );
      this.isSpinning = false;
      return;
    }

    this.authService.sendOtpCode({ email: this.email }).subscribe(
      (res) => {
        this.isSpinning = false;
        this.forgotService.setEmail(this.email)
        this.msg.success('Gửi mã xác thực thành công !!!');
        this.router.navigateByUrl('/verifyforgotpassword');
      },
      (error) => {
        this.isSpinning = false;
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Gửi mã xác thực không thành công', {
            nzDuration: 2000,
          });
        }
      }
    );
  }

  // Email format validation function
  validateEmailFormat(email: string): boolean {
    // Regular expression for email format validation
    const emailRegex: RegExp =
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  }
}
