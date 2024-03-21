import { Component, HostListener, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  NonNullableFormBuilder,
  Validators,
} from '@angular/forms';
import { PublicService } from '../../../services/public/public.service';
import { hostname } from 'os';
import { Login } from '../../../types/login.type';
import { NzMessageService } from 'ng-zorro-antd/message';
import { error } from 'console';
import { StorageService } from '../../../services/storage/storage.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  isSpinning: boolean = false;

  validateForm: FormGroup<{
    email: FormControl<string>;
    password: FormControl<string>;
    remember: FormControl<boolean>;
  }> = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
    remember: [true],
  });

  constructor(
    private fb: NonNullableFormBuilder,
    private msg: NzMessageService,
    private router: Router,
    private publicService: PublicService
  ) {}

  ngOnInit(): void {
    if(StorageService.isLoggedIn()===true){
      this.router.navigateByUrl("/")
    }
  }

  submitForm(): void {
    this.isSpinning = true;

    console.log('====================================');
    console.log('login::', this.validateForm);
    console.log('====================================');

    if (this.validateForm.valid === false) {
      this.isSpinning = false;
      this.msg.error('Vui lòng nhập đúng format của email và mật khẩu.');
    } else if (this.validateForm.valid === true) {
      console.log('====================================');
      console.log(this.validateForm.value);
      console.log('====================================');

      const email = this.validateForm.value.email;
      const password = this.validateForm.value.password;
      const rememberMe = this.validateForm.value.remember;

      if (email && password && rememberMe) {
        const loginData: Login = {
          email: email,
          password: password,
          remember: rememberMe,
        };

        this.publicService.login(loginData).subscribe(
          (res) => {
            console.log('====================================');
            console.log('res::', res);
            console.log('====================================');
            const token = res?.jwt;
            StorageService.saveToken(token);
            this.msg.success('Đăng nhập thành công', { nzDuration: 2000 });
            this.router.navigateByUrl('/');
            console.log('====================================');
            console.log('Email::', StorageService.getEmail());
            console.log('====================================');
            this.isSpinning = false;
          },
          (error) => {
            if (error?.error?.error) {
              this.msg.error(error?.error?.error, { nzDuration: 2000 });
            } else {
              this.msg.error('Đăng nhập không thành công', {
                nzDuration: 2000,
              });
            }
            this.isSpinning = false;
          }
        );
      } else {
        this.isSpinning = false;
        this.msg.error('Email and password are required.');
      }
    }
  }

  changePage() {
    this.router.navigateByUrl('/forgotemail');
  }
}
