import { Component } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { JobCategoryService } from '../../../services/job-category/job-category.service';
import { JobService } from '../../../services/job/job.service';
import { JobApplicationService } from '../../../services/job-application/job-application.service';
import { StorageService } from '../../../services/storage/storage.service';
import { ProfileService } from '../../../services/profile/profile.service';
import { ACCEPT, PENDING, REJECT } from '../../../../constant';
import { NotificationService } from '../../../services/notification.service';
import { SocketServiceService } from '../../../services/socket-service.service';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-peding-approve-job',
  templateUrl: './peding-approve-job.component.html',
  styleUrl: './peding-approve-job.component.scss',
})
export class PedingApproveJobComponent {
  isSpinning: boolean = false;
  isVisible = false;
  listJob: any = null;
  jobInfo: any;
  totalPage: number = 0;
  page: number = 0;
  time: string = '';
  address: string = '';
  dataRequest: any;

  constructor(
    private msg: NzMessageService,
    private jobCategoryService: JobCategoryService,
    private jobApply: JobApplicationService,
    private profileService: ProfileService,
    private notificationService: NotificationService,
    private socketService: SocketServiceService,
  ) {}

  showModal() {
    this.isVisible = true;
  }

  handleCancel() {
    this.isVisible = false;
  }

  handleOk() {
    // this.isVisible = false;

    if (!this.time.trim() || !this.address.trim()) {
      this.msg.error(
        'Time and/or address are empty or contain only whitespace characters.'
      );
      return;
    }

    const dateTime = new Date(this.time);

    // Check if the selected date is greater than today's date
    if (dateTime <= new Date()) {
      this.msg.error('Please select a date greater than today.');
      return;
    }

    const hours = dateTime.getHours().toString().padStart(2, '0');
    const minutes = dateTime.getMinutes().toString().padStart(2, '0');
    const time = hours + ':' + minutes;

    const day = dateTime.getDate().toString().padStart(2, '0');
    const month = (dateTime.getMonth() + 1).toString().padStart(2, '0');
    const year = dateTime.getFullYear();
    const date = day + '/' + month + '/' + year;

    const data = {
      email: this.dataRequest.emailAccount,
      jobName: this.dataRequest.jobName,
      time: time,
      date: date,
      address: this.address,
      supporter: StorageService.getEmail(),
    };
 
    this.isSpinning = true

    this.jobApply.sendEmailInterview(data).subscribe(
      (res) => {
        this.msg.success('Send mail interview successfully !!!');
        this.isVisible = false;
        this.isSpinning = false

        const formData = new FormData();
        formData.append('id', String(this.dataRequest.id));
        formData.append('status', ACCEPT);
        const noti = {
          Email: this.dataRequest.emailAccount,
          Content: `Công việc ${this.dataRequest.jobName} được duyệt chúng tôi sẽ gửi cho bạn lịch phỏng vấn qua email !!!`,
        };
        this.socketService.sendNoti(noti);
        this.jobApply.updateJobApplicationByHrEmail(formData).subscribe(
          (res) => {
            this.msg.success('Approve job successfully !!!');
            this.jobApply
              .viewJobApplicationByHrEmail(
                StorageService.getEmail(),
                'Pending',
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
            this.msg.error('Approve job failed !!!');
          }
        );

      },
      (error) => {
        this.msg.error('Send mail interview failed !!!');
        this.isVisible = false;
        this.isSpinning = false

      }
    );
  }

  ngOnInit(): void {
    this.jobApply
      .viewJobApplicationByHrEmail(StorageService.getEmail(), 'Pending', 1)
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
        'Pending',
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

  approve(data: any) {
    this.dataRequest = data;
    this.showModal();
  }

  reject(data: any) {
    const formData = new FormData();
    formData.append('id', String(data.id));
    formData.append('status', REJECT);
    const noti = {
      Email: data.emailAccount,
      Content: `Công việc ${data.jobName} đã bị từ chối vì chúng tôi thấy bạn không phù hợp với vị trí này !!!`,
    };
    this.socketService.sendNoti(noti);
    this.jobApply.updateJobApplicationByHrEmail(formData).subscribe(
      (res) => {
        this.msg.success('Reject job successfully !!!');

        this.jobApply
          .viewJobApplicationByHrEmail(
            StorageService.getEmail(),
            'Pending',
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
        this.msg.error('Reject job failed !!!');
      }
    );
  }
}
