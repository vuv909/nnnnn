import { Component, HostListener, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  NonNullableFormBuilder,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { NzFormTooltipIcon } from 'ng-zorro-antd/form';
import { PublicService } from '../../../services/public/public.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { error } from 'console';
import { SignUp } from '../../../types/signup.type';
import { StorageService } from '../../../services/storage/storage.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
})
export class SignupComponent implements OnInit {

  isSpinning : boolean = false
  constructor(
    private fb: NonNullableFormBuilder,
    private publicService: PublicService,
    private msg: NzMessageService,
    private router : Router
  ) {
    this.validateForm = this.fb.group({
      email: ['', [Validators.email, Validators.required]],
      password: ['', [Validators.required]],
      checkPassword: ['', [Validators.required, this.confirmationValidator]],
      nickname: ['', [Validators.required]],
      agree: [false],
    });
  }

  validateForm: FormGroup<{
    email: FormControl<string>;
    password: FormControl<string>;
    checkPassword: FormControl<string>;
    nickname: FormControl<string>;
    agree: FormControl<boolean>;
  }>;
  captchaTooltipIcon: NzFormTooltipIcon = {
    type: 'info-circle',
    theme: 'twotone',
  };

  policy(){
    this.router.navigateByUrl('/policy')
  }

  submitForm(): void {
    this.isSpinning = true
    console.log('====================================');
    console.log('FormSubmit::', this.validateForm);
    console.log('====================================');

    const username = this.validateForm.controls.nickname.valid;
    const email = this.validateForm.controls.email.valid;
    const password = this.validateForm.controls.password.valid;
    const agree = this.validateForm.controls.agree.value;
    const checkPassword = this.validateForm.controls.checkPassword.valid;

    if (!username || this.validateForm.controls.nickname.value.trim() === '') {
      this.isSpinning = false
      this.msg.error('Vui lòng không bỏ trống username');
    } else if (!email) {
      this.isSpinning = false
      this.msg.error('Vui lòng nhập đúng email');
    } else if (
      !password ||
      this.validateForm.controls.password.value.trim() === ''
    ) {
      this.isSpinning = false
      this.msg.error('Vui lòng không bỏ trống mật khẩu');
    } else if (!checkPassword) {
      this.isSpinning = false
      this.msg.error('Vui lòng nhập confirm passwod đúng mật khẩu bạn nhập');
    } else if (agree === false) {
      this.isSpinning = false
      this.msg.error('Vui lòng chấp nhận điều khoản công ty của chúng tôi');
    } else {
      const signupData: SignUp = {
        username: this.validateForm.controls.nickname.value,
        email: this.validateForm.controls.email.value,
        password: this.validateForm.controls.password.value,
      };

      this.publicService.signup(signupData).subscribe(
        (res) => {
          this.msg.success('Đăng kí thành công', { nzDuration: 2000 });
          this.validateForm.reset();
          this.isSpinning = false
        },
        (error) => {
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Đăng nhập không thành công', { nzDuration: 2000 });
          }
          this.isSpinning = false
        }
      );
    }
  }

  updateConfirmValidator(): void {
    /** wait for refresh value */
    Promise.resolve().then(() =>
      this.validateForm.controls.checkPassword.updateValueAndValidity()
    );
  }

  confirmationValidator: ValidatorFn = (
    control: AbstractControl
  ): { [s: string]: boolean } => {
    if (!control.value) {
      return { required: true };
    } else if (control.value !== this.validateForm.controls.password.value) {
      return { confirm: true, error: true };
    }
    return {};
  };

  ngOnInit(): void {
    if(StorageService.isLoggedIn() === true){
      this.router.navigateByUrl('/');
    }
  }


}
