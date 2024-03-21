import { Component } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { JobCategoryService } from '../../../services/job-category/job-category.service';
import { JobApplicationService } from '../../../services/job-application/job-application.service';
import { ProfileService } from '../../../services/profile/profile.service';
import { StorageService } from '../../../services/storage/storage.service';
import { ACCEPT, NOTPASSINTERVIEW, PASSINTERVIEW } from '../../../../constant';
import { SocketServiceService } from '../../../services/socket-service.service';

@Component({
  selector: 'app-approve-job',
  templateUrl: './approve-job.component.html',
  styleUrl: './approve-job.component.scss',
})
export class ApproveJobComponent {
  isVisibleAdd = false;
  isVisibleUpdate = false;
  listJob: any = null;
  idUpdate!: number;
  isAddSpinning: boolean = false;
  isUpdateSpinning: boolean = false;
  jobInfo: any;
  totalPage: number = 0;
  page: number = 0;

  constructor(
    private msg: NzMessageService,
    private jobCategoryService: JobCategoryService,
    private jobApply: JobApplicationService,
    private profileService: ProfileService,
    private socketService: SocketServiceService
  ) {}

  ngOnInit(): void {
    this.jobApply
      .viewJobApplicationByHrEmail(StorageService.getEmail(), ACCEPT, 1)
      .subscribe(
        (res) => {
          this.listJob = res.listResults;
          this.totalPage = res.totalPage;
          this.page = res.page;
        },
        (error) => {
          this.listJob = [];
          this.totalPage = 0;
          this.page = 0;
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Error occured', { nzDuration: 2000 });
          }
        }
      );
  }

  pageActive(pageActive: number): void {
    this.jobApply
      .viewJobApplicationByHrEmail(
        StorageService.getEmail(),
        ACCEPT,
        pageActive
      )
      .subscribe(
        (res) => {
          this.listJob = res.listResults;
          this.totalPage = res.totalPage;
          this.page = res.page;
        },
        (error) => {
          this.listJob = [];
          this.totalPage = 0;
          this.page = 0;
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Error occured', { nzDuration: 2000 });
          }
        }
      );
  }

  dowloadCV(id: string) {
    this.profileService.downloadCvById(id).subscribe(
      (res) => {
        this.msg.success('Download CV successfully !!!');
      },
      (error) => {}
    );
  }

  pass(data: any) {
    console.log('data::', data);

    const formData = new FormData();
    formData.append('id', String(data.id));
    formData.append('status', PASSINTERVIEW);
    const noti = {
      Email: data.emailAccount,
      Content: `Chúc mừng bạn đã pass phỏng vẫn công việc ${data.jobName} !!!`,
    };
    this.socketService.sendNoti(noti);
    this.jobApply.updateJobApplicationByHrEmail(formData).subscribe(
      (res) => {
        this.msg.success('Update successfully !!!');

        this.jobApply
          .viewJobApplicationByHrEmail(
            StorageService.getEmail(),
            ACCEPT,
            this.page
          )
          .subscribe(
            (res) => {
              this.listJob = res.listResults;
              this.totalPage = res.totalPage;
              this.page = res.page;
            },
            (error) => {
              this.listJob = [];
              this.totalPage = 0;
              this.page = 0;
              if (error?.error?.error) {
                this.msg.error(error?.error?.error, { nzDuration: 2000 });
              } else {
                this.msg.error('Error occured', { nzDuration: 2000 });
              }
            }
          );
      },
      (error) => {
        this.msg.error('Update failed !!!');
      }
    );
  }

  notpass(data: any) {
    console.log('data::', data);

    const formData = new FormData();
    formData.append('id', String(data.id));
    formData.append('status', NOTPASSINTERVIEW);
    const noti = {
      Email: data.emailAccount,
      Content: `Bạn đã không pass phỏng vấn công việc ${data.jobName} !!!`,
    };
    this.socketService.sendNoti(noti);
    this.jobApply.updateJobApplicationByHrEmail(formData).subscribe(
      (res) => {
        this.msg.success('Update successfully !!!');

        this.jobApply
          .viewJobApplicationByHrEmail(
            StorageService.getEmail(),
            ACCEPT,
            this.page
          )
          .subscribe(
            (res) => {
              this.listJob = res.listResults;
              this.totalPage = res.totalPage;
              this.page = res.page;
            },
            (error) => {
              this.listJob = [];
              this.totalPage = 0;
              this.page = 0;
              if (error?.error?.error) {
                this.msg.error(error?.error?.error, { nzDuration: 2000 });
              } else {
                this.msg.error('Error occured', { nzDuration: 2000 });
              }
            }
          );
      },
      (error) => {
        this.msg.error('Update failed !!!');
      }
    );
  }
}
