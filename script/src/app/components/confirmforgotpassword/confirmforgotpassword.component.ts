import { Component, OnDestroy } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { StorageService } from '../../services/storage/storage.service';
import { Router } from '@angular/router';
import { ForgotpasswordService } from '../../services/forgotpassword.service';

@Component({
  selector: 'app-confirmforgotpassword',
  templateUrl: './confirmforgotpassword.component.html',
  styleUrl: './confirmforgotpassword.component.scss'
})
export class ConfirmforgotpasswordComponent {
  otpCode: string = '';
  isSpinning: boolean = false;
  constructor(
    private authService: AuthService,
    private msg: NzMessageService,
    private router : Router,
    private forgotPasswordService : ForgotpasswordService
  ) {}
  submit() {
    this.isSpinning = true;
    console.log(this.otpCode.trim());
    console.log(StorageService.getEmail());

    this.authService
      .verifyAccount({
        otpCode: this.otpCode,
        email: this.forgotPasswordService.getEmail(),
      })
      .subscribe(
        (res) => {
          this.msg.success('Bạn đã xác thực tài khoản thành công!!!', {
            nzDuration: 2000,
          });
          this.router.navigateByUrl('/changepassword')
        },
        (error) => {
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Xác thực tài khoản không thành công ', {
              nzDuration: 2000,
            });
          }
          this.isSpinning = false;
        }
      );
  }

  
}
