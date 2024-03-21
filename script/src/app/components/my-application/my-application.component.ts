import { Component } from '@angular/core';
import { JobApplicationService } from '../../services/job-application/job-application.service';
import { StorageService } from '../../services/storage/storage.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { ACCEPT, PENDING, REJECT } from '../../../constant';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-my-application',
  templateUrl: './my-application.component.html',
  styleUrl: './my-application.component.scss',
})
export class MyApplicationComponent {
  accept: string = ACCEPT;
  reject: string = REJECT;
  pending: string = PENDING;
  jobList: any[] = [];
  totalPage: number = 0;
  activePage: number = 1;

  constructor(
    private jobApplication: JobApplicationService,
    private msg: NzMessageService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (StorageService.isLoggedIn() === false) {
      this.router.navigateByUrl('/notfound');
    } else {
      if (StorageService.getEmail()) {
        this.authService.getRole().subscribe(
          (res) => {
            if (res.role.trim() !== 'JOBSEEKER') {
              this.router.navigateByUrl('/notfound');
            }
          },
          (error) => {}
        );
      }
    }

    this.jobApplication
      .viewJobApplicationByEmail(StorageService.getEmail(), 1)
      .subscribe(
        (res) => {
          this.jobList = res.listResults;
          this.totalPage = res.totalPage * 10;
          this.activePage = res.page;
        },
        (error) => {
          this.jobList = [];
          this.totalPage = 0;
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Đăng nhập không thành công', { nzDuration: 2000 });
          }
        }
      );
  }

  pageActive(pageActive: number): void {
    this.jobApplication
      .viewJobApplicationByEmail(StorageService.getEmail(), pageActive)
      .subscribe(
        (res) => {
          this.jobList = res.listResults;
          this.totalPage = res.totalPage * 10;
          this.activePage = res.page;
        },
        (error) => {
          this.jobList = [];
          this.totalPage = 0;
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Đăng nhập không thành công', { nzDuration: 2000 });
          }
        }
      );
  }

  changePage(data: any): void {
    this.router.navigateByUrl('/detail/' + data.jobId);
  }
}
