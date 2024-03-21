import { Component } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { JobCategoryService } from '../../../services/job-category/job-category.service';
import { JobApplicationService } from '../../../services/job-application/job-application.service';
import { ProfileService } from '../../../services/profile/profile.service';
import { StorageService } from '../../../services/storage/storage.service';
import { PENDING } from '../../../../constant';

@Component({
  selector: 'app-expired-request',
  templateUrl: './expired-request.component.html',
  styleUrl: './expired-request.component.scss',
})
export class ExpiredRequestComponent {
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
    private profileService: ProfileService
  ) {}

  ngOnInit(): void {
    this.jobApply
      .viewJobApplicationByHrEmail(StorageService.getEmail(), 'EXPIRED', 1)
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
        'EXPIRED',
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
}
