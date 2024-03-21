import { Component, OnInit } from '@angular/core';
import { StorageService } from '../../../services/storage/storage.service';
import { AuthService } from '../../../services/auth/auth.service';
import { NzMessageModule, NzMessageService } from 'ng-zorro-antd/message';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-account',
  templateUrl: './verify-account.component.html',
  styleUrl: './verify-account.component.scss',
})
export class VerifyAccountComponent implements OnInit{
  otpCode: string = '';
  isSpinning: boolean = false;
  constructor(
    private authService: AuthService,
    private msg: NzMessageService,
    private router : Router
  ) {}

  ngOnInit(): void {
    if(StorageService.isLoggedIn() === false){
      this.router.navigateByUrl('/notfound');
    }
  }

  submit() {
    this.isSpinning = true;
    console.log(this.otpCode.trim());
    console.log(StorageService.getEmail());

    this.authService
      .verifyAccount({
        otpCode: this.otpCode,
        email: StorageService.getEmail(),
      })
      .subscribe(
        (res) => {
          this.msg.success('Bạn đã xác thực tài khoản thành công!!!', {
            nzDuration: 2000,
          });
          this.router.navigateByUrl('/profile')
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
