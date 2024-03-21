import { Component } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NzMessageService } from 'ng-zorro-antd/message';
import { JobCategoryService } from '../../services/job-category/job-category.service';
import { JobService } from '../../services/job/job.service';
import { formatDate } from '@angular/common';
import { StorageService } from '../../services/storage/storage.service';

@Component({
  selector: 'app-viewjob-admin',
  templateUrl: './viewjob-admin.component.html',
  styleUrl: './viewjob-admin.component.scss',
})
export class ViewjobAdminComponent {
  isVisibleAdd = false;
  isVisibleUpdate = false;
  listJob: any = null;
  idUpdate!: number;
  isAddSpinning: boolean = false;
  isUpdateSpinning: boolean = false;
  jobInfo: any;
  updateForm!: FormGroup;
  listJobCategory: any = null;
  branchList: any = null;
  totalPages: number = 0;
  page: number = 0;

  constructor(
    private msg: NzMessageService,
    private jobCategoryService: JobCategoryService,
    private jobService: JobService
  ) {}

  ngOnInit(): void {
    this.jobService.getAllJob(1).subscribe(
      (res) => {
        this.listJob = res.listResults;
        this.totalPages = res.totalPage;
        this.page = res.page;
      },
      (error) => {
        this.listJob = [];
        this.totalPages = 0;
        this.page = 0;
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Error occured', { nzDuration: 2000 });
        }
      }
    );
    this.jobCategoryService.viewJobCategory().subscribe(
      (res) => {
        this.listJobCategory = res;
      },
      (error) => {
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Error occured', { nzDuration: 2000 });
        }
      }
    );
    this.jobCategoryService.getAllBranch().subscribe(
      (res) => {
        this.branchList = res;
      },
      (error) => {
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Error occured', { nzDuration: 2000 });
        }
      }
    );
  }

  pageActive(pageActive: number): void {
    this.jobService.getAllJob(pageActive).subscribe(
      (res) => {
        this.listJob = res.listResults;
        this.totalPages = res.totalPage;
        this.page = res.page;
      },
      (error) => {
        this.listJob = [];
        this.totalPages = 0;
        this.page = 0;
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Error occured', { nzDuration: 2000 });
        }
      }
    );
  }
}
