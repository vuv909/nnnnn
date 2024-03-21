import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin/admin.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SocketServiceService } from '../../../services/socket-service.service';

@Component({
  selector: 'app-manage-account',
  templateUrl: './manage-account.component.html',
  styleUrl: './manage-account.component.scss',
})
export class ManageAccountComponent implements OnInit {
  listAccounts: any;
  isVisibleUpdate: boolean = false;
  isAddHrSpinning: boolean = true;
  updateForm!: FormGroup;
  totalPage: number = 0;
  page: number = 0;

  constructor(
    private adminService: AdminService,
    private msg: NzMessageService,
    private socketService: SocketServiceService
  ) {}

  ngOnInit(): void {
    this.adminService.getAccountByEnabledStatus({ enabled: true }, 1).subscribe(
      (res) => {
        console.log('====================================');
        console.log('res::', res);
        console.log('====================================');
        this.listAccounts = res.listResults;
        this.totalPage = res.totalPage;
        this.page = res.page;
      },
      (err) => {
        this.listAccounts = [];
        this.totalPage = 0;
        this.page = 0;
        console.log('====================================');
        console.log('err');
        console.log('====================================');
      }
    );
    this.updateForm = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(100),
      ]),
      email: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(100),
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(100),
      ]),
    });
  }

  pageActive(pageActive: number) {
    this.adminService
      .getAccountByEnabledStatus({ enabled: true }, pageActive)
      .subscribe(
        (res) => {
          console.log('====================================');
          console.log('res::', res);
          console.log('====================================');
          this.listAccounts = res.listResults;
          this.totalPage = res.totalPage;
          this.page = res.page;
        },
        (err) => {
          this.listAccounts = [];
          this.totalPage = 0;
          this.page = 0;
          console.log('====================================');
          console.log('err');
          console.log('====================================');
        }
      );
  }

  showModal() {
    this.isVisibleUpdate = true;
  }

  handleCancelUpdate() {
    this.isVisibleUpdate = false;
    this.updateForm.reset();
  }

  handleOkUpdate() {
    this.isAddHrSpinning = true;
    if (this.updateForm.get('username')!.invalid) {
      this.msg.error(
        'username must be not null and less than 100 characters !!!'
      );
    } else if (this.updateForm.get('email')!.invalid) {
      this.msg.error('email must be not null and less than 100 characters !!!');
    } else if (this.updateForm.get('password')!.invalid) {
      this.msg.error(
        'password must be not null and less than 100 characters !!!'
      );
    } else {
      this.adminService
        .createAccountHR({
          username: this.updateForm.get('username')?.value,
          email: this.updateForm.get('email')?.value,
          password: this.updateForm.get('password')?.value,
        })
        .subscribe(
          (res) => {
            this.msg.success('Create account HR successfully !!!');
            this.adminService
              .getAccountByEnabledStatus({ enabled: true }, this.page)
              .subscribe(
                (res) => {
                  console.log('====================================');
                  console.log('res::', res);
                  console.log('====================================');
                  this.listAccounts = res.listResults;
                  this.totalPage = res.totalPage;
                  this.page = res.page;
                  this.isAddHrSpinning = false;
                },
                (err) => {
                  this.isAddHrSpinning = false;
                  this.listAccounts = [];
                  this.totalPage = 0;
                  this.page = 0;
                  console.log('====================================');
                  console.log('err');
                  console.log('====================================');
                }
              );
            this.isVisibleUpdate = false;
            this.updateForm.reset();
          },
          (error) => {
            this.isAddHrSpinning = false;
            if (error?.error?.error) {
              this.msg.error(error?.error?.error, { nzDuration: 2000 });
            } else {
              this.msg.error('Đăng nhập không thành công', {
                nzDuration: 2000,
              });
            }
          }
        );
    }
  }

  blockAccount(data: any) {
    console.log('====================================');
    console.log('data::', data);
    console.log('====================================');
    this.adminService
      .blockAndActiveAccount({
        email: data?.email,
        enabled: false,
      })
      .subscribe(
        (res) => {
          this.msg.success('Block Account Successfully !!!');
          this.socketService.sendBlockAccount(data?.email)
          this.adminService
            .getAccountByEnabledStatus({ enabled: true }, this.page)
            .subscribe(
              (res) => {
                console.log('====================================');
                console.log('res::', res);
                console.log('====================================');
                this.listAccounts = res.listResults;
                this.totalPage = res.totalPage;
                this.page = res.page;
              },
              (err) => {
                this.listAccounts = [];
                this.totalPage = 0;
                this.page = 0;
                console.log('====================================');
                console.log('err');
                console.log('====================================');
              }
            );
        },
        (err) => {
          this.msg.error('Block Account Error !!!');
        }
      );
  }
}
