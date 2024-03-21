import { Component } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { JobCategoryService } from '../../../services/job-category/job-category.service';
import { JobApplicationService } from '../../../services/job-application/job-application.service';
import { ProfileService } from '../../../services/profile/profile.service';
import { StorageService } from '../../../services/storage/storage.service';
import { NOTPASS, NOTPASSINTERVIEW } from '../../../../constant';

@Component({
  selector: 'app-notpass',
  templateUrl: './notpass.component.html',
  styleUrl: './notpass.component.scss',
})
export class NotpassComponent {
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
      .viewJobApplicationByHrEmail(
        StorageService.getEmail(),
        NOTPASSINTERVIEW,
        1
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

  pageActive(pageActive: number): void {
    this.jobApply
      .viewJobApplicationByHrEmail(
        StorageService.getEmail(),
        NOTPASSINTERVIEW,
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
