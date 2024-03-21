import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { FileUpload, FileUploadEvent, UploadEvent } from 'primeng/fileupload';
import { Store } from '@ngrx/store';
import { getUser } from '../../shared/login.selector';
import { ProfileService } from '../../services/profile/profile.service';
import { StorageService } from '../../services/storage/storage.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { storeUser } from '../../shared/login.action';
import { NzModalService } from 'ng-zorro-antd/modal';
import { NzUploadFile } from 'ng-zorro-antd/upload';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  isSpinningUpdateInfo: boolean = false;
  formProfile!: FormGroup;
  user: any;
  isVisible = false;
  isSpinning: boolean = false;
  isSpinningCvAndAvatar: boolean = false;
  checkUserVerify: any;
  email: string = StorageService.getEmail();
  gender!: boolean;
  username!: string;
  changeUsername!: string;
  isUpdateUserName: boolean = false;
  isSpinningUpdateAvatar: boolean = false;
  isVisibleInUpdateAvatar : boolean = false;
  constructor(
    private modalService: NzModalService,
    private fb: FormBuilder,
    private messageService: MessageService,
    private store: Store<{ user: any }>,
    private profileService: ProfileService,
    private router: Router,
    private authService: AuthService,
    private msg: NzMessageService
  ) {}

  ngOnInit(): void {

    this.store.select(getUser).subscribe((res) => {
      if(StorageService.isLoggedIn() === false){
        this.router.navigateByUrl('/notfound');
      }
      if (res) {
        this.profileService
          .findAccountByEmail({ email: StorageService.getEmail() })
          .subscribe((res) => {
            console.log('====================================');
            console.log('Profile::', res);
            console.log('====================================');
            this.checkUserVerify = res.metadata;
            this.username = res.metadata.username;
            this.changeUsername = res.metadata.username;
          });
      }
      this.user = res;
      this.gender = res?.gender;
      this.initializeForm();
    });
  }

  initializeForm() {
    this.formProfile = this.fb.group({
      firstName: [this.user?.firstName || '', Validators.required],
      lastName: [this.user?.lastName || '', Validators.required],
      phoneNumber: [this.user?.phoneNumber || '', Validators.required],
      gender: [
        this.user?.gender !== undefined ? this.user?.gender : null,
        Validators.required,
      ],
      address: [this.user?.address || '', Validators.required],
    });
  }

  onUpload(event: FileUploadEvent) {
    console.log('event:', event.files[0]);
    this.isSpinningCvAndAvatar = true;
    const data = new FormData();

    data.append('email', StorageService.getEmail());
    data.append('CV', event.files[0]);

    this.profileService.updateCvByEmail(data).subscribe(
      (res) => {
        this.msg.success('Cập nhập CV thành công!!!');
        this.isSpinningCvAndAvatar = false;
        this.profileService.findAccountByEmail({ email: this.email }).subscribe(
          (res) => {
            this.profileService
              .viewProfileByEmail({ id: res.metadata.id })
              .subscribe(
                (res) => {
                  this.store.dispatch(storeUser({ user: res.metadata }));
                  this.isSpinningCvAndAvatar = false;
                  this.handleCancel();
                },
                (error) => {
                  this.isSpinningCvAndAvatar = false;

                  this.msg.error('Not found profile', {
                    nzDuration: 2000,
                  });
                }
              );
          },
          (error) => {
            this.isSpinningCvAndAvatar = false;

            this.msg.error('Not found account', {
              nzDuration: 2000,
            });
          }
        );
      },
      (error) => {
        this.isSpinningCvAndAvatar = false;
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Cập nhập CV không thành công', {
            nzDuration: 2000,
          });
        }
      }
    );
  }

  verify() {
    this.isSpinning = true;
    this.authService
      .sendOtpCode({ email: StorageService.getEmail() })
      .subscribe(
        (res) => {
          this.isSpinning = false;
          this.msg.success('Gửi mã xác thực thành công !!!');
          this.router.navigateByUrl('/verifyAccount');
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

  showModal(): void {
    this.isVisible = true;
  }

  handleOk(): void {
    this.isSpinningUpdateInfo = true;
    console.log('formProfile::', this.formProfile.value);
    const formData = new FormData();
    formData.append('email', StorageService.getEmail());
    formData.append('firstName', this.formProfile.value['firstName']);
    formData.append('lastName', this.formProfile.value['lastName']);
    formData.append('gender', this.formProfile.value['gender']);
    formData.append('phoneNumber', `${this.formProfile.value['phoneNumber']}`);
    formData.append('address', this.formProfile.value['address']);

    this.profileService.updateProfileByEmail(formData).subscribe(
      (res) => {
        this.msg.success('Cập nhập profile thành công');
        this.profileService.findAccountByEmail({ email: this.email }).subscribe(
          (res) => {
            this.profileService
              .viewProfileByEmail({ id: res.metadata.id })
              .subscribe(
                (res) => {
                  this.store.dispatch(storeUser({ user: res.metadata }));
                  this.isSpinningUpdateInfo = false;
                  this.handleCancel();
                },
                (error) => {
                  this.isSpinningUpdateInfo = false;

                  this.msg.error('Cập nhập profile không thành công', {
                    nzDuration: 2000,
                  });
                }
              );
          },
          (error) => {
            this.isSpinningUpdateInfo = false;

            this.msg.error('Cập nhập profile không thành công', {
              nzDuration: 2000,
            });
          }
        );
      },
      (error) => {
        this.isSpinningUpdateInfo = false;
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Cập nhập profile không thành công', {
            nzDuration: 2000,
          });
        }
      }
    );
  }

  downloadCV() {
    this.isSpinningUpdateInfo = true;
    this.profileService.downloadCvByInProfile(StorageService.getEmail()).subscribe(
      (res) => {
        this.isSpinningUpdateInfo = false;
        // this.msg.success("Tải cv về máy thành công")
      },
      (error) => {
        this.isSpinningUpdateInfo = false;
        // this.msg.error("Tải cv về máy không thành công")
      }
    );
  }

  handleCancel(): void {
    this.isVisible = false;
  }

  handleOkUpdateAvatar(){
    this.isVisibleInUpdateAvatar = false
  }

  handleCancelUpdateAvatar(){
    this.isVisibleInUpdateAvatar = false
  }

  showModalUpdateAvatar(): void {
    this.isVisibleInUpdateAvatar = true;
  }

  saveUsername(): void {
    this.isSpinningUpdateInfo = true;
    console.log('====================================');
    console.log(this.changeUsername);
    console.log('====================================');
    if (this.changeUsername.trim() === '') {
      this.msg.error('Vui lòng không để trống !!!');
    } else {
      const data = {
        email: StorageService.getEmail(),
        username: this.changeUsername.trim(),
      };
      this.profileService.updateUsernameByEmail(data).subscribe(
        (res) => {
          this.msg.success('Update username successfully!!!');
          this.profileService
            .findAccountByEmail({ email: StorageService.getEmail() })
            .subscribe((res) => {
              console.log('====================================');
              console.log('Profile::', res);
              console.log('====================================');
              this.checkUserVerify = res.metadata;
              this.username = res.metadata.username;
              this.changeUsername = res.metadata.username;
            });
        },
        (error) => {
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Error when updating username!!!', {
              nzDuration: 2000,
            });
          }
          this.isSpinning = false;
        }
      );
    }
    this.isUpdateUserName=false
  }

  cancelSaveUsername(): void {
    this.isUpdateUserName = false;
  }

  clickChangeUserName(): void {
    this.isUpdateUserName = true;
  }

  onUploadAvatar(event : FileUploadEvent){
    console.log('event:', event.files[0]);
    this.isSpinningUpdateAvatar = true;
    const data = new FormData();

    data.append('email', StorageService.getEmail());
    data.append('avatar', event.files[0]);

    this.profileService.updateAvatar(data).subscribe(
      (res) => {
        this.msg.success('Cập nhập avatar thành công!!!');
        this.isSpinningUpdateAvatar = false;
        this.handleCancelUpdateAvatar()
        this.profileService.findAccountByEmail({ email: this.email }).subscribe(
          (res) => {
            this.profileService
              .viewProfileByEmail({ id: res.metadata.id })
              .subscribe(
                (res) => {
                  console.log('====================================');
                  console.log("storeUser",res.metadata);
                  console.log('====================================');
                  this.store.dispatch(storeUser({ user: res.metadata }));
                  this.isSpinningUpdateAvatar = false;
                  this.handleCancel();
                },
                (error) => {
                  this.isSpinningUpdateAvatar = false;

                  this.msg.error('Not found profile', {
                    nzDuration: 2000,
                  });
                }
              );
          },
          (error) => {
            this.isSpinningUpdateAvatar = false;

            this.msg.error('Not found account', {
              nzDuration: 2000,
            });
          }
        );
      },
      (error) => {
        this.isSpinningUpdateAvatar = false;
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Cập nhập avatar không thành công', {
            nzDuration: 2000,
          });
        }
      }
    );
  }
}
