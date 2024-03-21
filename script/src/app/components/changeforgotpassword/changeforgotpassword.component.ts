import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, NonNullableFormBuilder, ValidatorFn, Validators } from '@angular/forms';
import { StorageService } from '../../services/storage/storage.service';
import { PublicService } from '../../services/public/public.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { AuthService } from '../../services/auth/auth.service';
import { NzFormTooltipIcon } from 'ng-zorro-antd/form';
import { Router } from '@angular/router';

@Component({
  selector: 'app-changeforgotpassword',
  templateUrl: './changeforgotpassword.component.html',
  styleUrl: './changeforgotpassword.component.scss'
})
export class ChangeforgotpasswordComponent {
validateForm: FormGroup<{
    email: FormControl<string>;
    password: FormControl<string>;
    checkPassword: FormControl<string>;
    oldpassword: FormControl<string>;
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
    private router : Router
  ) {
    this.validateForm = this.fb.group({
      email: ['', [Validators.email, Validators.required]],
      password: ['', [Validators.required]],
      checkPassword: ['', [Validators.required, this.confirmationValidator]],
      oldpassword: ['', [Validators.required]],
    });
  }

  submitForm(): void {
    console.log('====================================');
    console.log('FormSubmit::', this.validateForm);
    console.log('====================================');

    const email = StorageService.getEmail();
    const password = this.validateForm.controls.password.valid;
    const oldpassword = this.validateForm.controls.oldpassword.value;
    const checkPassword = this.validateForm.controls.checkPassword.valid;

    if (!email) {
      this.msg.error('Error occurred !!!');
    } else if (
      !password ||
      this.validateForm.controls.password.value.trim() === ''
    ) {
      this.msg.error('Vui lòng không bỏ trống mật khẩu');
    } else if (
      !oldpassword ||
      this.validateForm.controls.oldpassword.value.trim() === ''
    ) {
      this.msg.error(
        'Vui lòng không bỏ trống mật khẩu trường nhập mật khẩu cũ'
      );
    } else if (!checkPassword) {
      this.msg.error('Vui lòng nhập confirm passwod đúng mật khẩu bạn nhập');
    } else {
      const changepassword: any = {
        oldPassword : this.validateForm.controls.oldpassword.value,
        email,
        password: this.validateForm.controls.password.value,
      };
      console.log('====================================');
      console.log(changepassword);
      console.log('====================================');

      this.publicService.changePasswordWhenLogin(changepassword).subscribe((res)=>{
        this.msg.success("Change password successfully !!!")
        this.validateForm.reset()
      },(error)=>{
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Change password failed !!!', { nzDuration: 2000 });
        }
      })


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
    if(StorageService.isLoggedIn() === false){
      this.router.navigateByUrl('/notfound');
    }
  }
}
