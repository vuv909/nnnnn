import { Component } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { AdminService } from '../../../services/admin/admin.service';

@Component({
  selector: 'app-manage-account-block',
  templateUrl: './manage-account-block.component.html',
  styleUrl: './manage-account-block.component.scss',
})
export class ManageAccountBlockComponent {
  listAccounts: any;
  isVisibleUpdate: boolean = false;
  isAddHrSpinning: boolean = true;
  totalPage: number = 0;
  page: number = 0;

  constructor(
    private adminService: AdminService,
    private msg: NzMessageService
  ) {}

  ngOnInit(): void {
    this.adminService
      .getAccountByEnabledStatus({ enabled: false }, 1)
      .subscribe(
        (res) => {
          this.listAccounts = res.listResults;
          this.totalPage = res.totalPage;
          this.page = res.page;
        },
        (err) => {
          this.listAccounts = [];
          this.totalPage = 0;
          this.page = 0;
        }
      );
  }

  pageActive(pageActive: number) {
    this.adminService
      .getAccountByEnabledStatus({ enabled: false }, pageActive)
      .subscribe(
        (res) => {
          this.listAccounts = res.listResults;
          this.totalPage = res.totalPage;
          this.page = res.page;
        },
        (err) => {
          this.listAccounts = [];
          this.totalPage = 0;
          this.page = 0;
        }
      );
  }

  unBlockAccount(data: any) {
    console.log('====================================');
    console.log('data::', data);
    console.log('====================================');
    this.adminService
      .blockAndActiveAccount({
        email: data?.email,
        enabled: true,
      })
      .subscribe(
        (res) => {
          this.msg.success('Unblock Account Successfully !!!');
          this.adminService
            .getAccountByEnabledStatus({ enabled: false }, this.page)
            .subscribe(
              (res) => {
                this.listAccounts = res.listResults;
                this.totalPage = res.totalPage;
                this.page = res.page;
              },
              (err) => {
                this.listAccounts = [];
                this.totalPage = 0;
                this.page = 0;
              }
            );
        },
        (err) => {
          this.listAccounts = [];
          this.msg.error('Unblock Account Error !!!');
        }
      );
  }
}
