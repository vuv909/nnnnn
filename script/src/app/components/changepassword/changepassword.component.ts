import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  NonNullableFormBuilder,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { NzFormTooltipIcon } from 'ng-zorro-antd/form';
import { PublicService } from '../../services/public/public.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { StorageService } from '../../services/storage/storage.service';
import { AuthService } from '../../services/auth/auth.service';
import { ForgotpasswordService } from '../../services/forgotpassword.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-changepassword',
  templateUrl: './changepassword.component.html',
  styleUrl: './changepassword.component.scss',
})
export class ChangepasswordComponent implements OnInit, OnDestroy {
  validateForm: FormGroup<{
    password: FormControl<string>;
    checkPassword: FormControl<string>;
  }>;
  captchaTooltipIcon: NzFormTooltipIcon = {
    type: 'info-circle',
    theme: 'twotone',
  };
  constructor(
    private fb: NonNullableFormBuilder,
    private publicService: PublicService,
    private msg: NzMessageService,
    private authService: AuthService,
    private forgotPasswordService: ForgotpasswordService,
    private router : Router,
    private forgotService: ForgotpasswordService
  ) {
    this.validateForm = this.fb.group({
      password: ['', [Validators.required]],
      checkPassword: ['', [Validators.required, this.confirmationValidator]],
    });
  }

  submitForm(): void {
    console.log('====================================');
    console.log('FormSubmit::', this.validateForm);
    console.log('====================================');

    const email = this.forgotService.getEmail();
    const password = this.validateForm.controls.password.valid;
    const checkPassword = this.validateForm.controls.checkPassword.valid;

    if (!email) {
      this.msg.error('Error occurred !!!');
    } else if (
      !password ||
      this.validateForm.controls.password.value.trim() === ''
    ) {
      this.msg.error('Vui lòng không bỏ trống mật khẩu');
    } else if (!checkPassword) {
      this.msg.error('Vui lòng nhập confirm passwod đúng mật khẩu bạn nhập');
    } else {
      const changepassword: any = {
        email,
        password: this.validateForm.controls.password.value,
      };


      this.publicService.changePassword(changepassword).subscribe(
        (res) => {
          this.msg.success('Change password successfully !!!');
          this.validateForm.reset();
          this.router.navigateByUrl('/login')
        },
        (error) => {
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Change password failed !!!', { nzDuration: 2000 });
          }
        }
      );

      // this.publicService.signup(signupData).subscribe(
      //   (res) => {
      //     this.msg.success('Đăng kí thành công', { nzDuration: 2000 });
      //     this.validateForm.reset();
      //     this.isSpinning = false;
      //   },
      //   (error) => {
      //     if (error?.error?.error) {
      //       this.msg.error(error?.error?.error, { nzDuration: 2000 });
      //     } else {
      //       this.msg.error('Đăng nhập không thành công', { nzDuration: 2000 });
      //     }
      //     this.isSpinning = false;
      //   }
      // );
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
   
  }

  ngOnDestroy(): void {
    this.forgotPasswordService.setEmail('');
  }
}
