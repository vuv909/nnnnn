import { Component, OnInit } from '@angular/core';
import {
  APPROVE_JOB,
  JOB_CATEGORY,
  MANAGE_JOB,
  PENDING_JOB,
  REFUSED_JOB,
  ACCOUNT_ACTIVE,
  ACCOUNT_BLOCK,
  VIEW_JOB_ADMIN,
  EXPIRED_JOB,
  VIEW_APPLY_ADMIN,
  PASS,
  NOTPASS,
} from '../../../../constant';
import { AuthService } from '../../../services/auth/auth.service';
import { Router } from '@angular/router';
import { StorageService } from '../../../services/storage/storage.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  isCollapsed = false;
  manage_job = MANAGE_JOB;
  pending_job = PENDING_JOB;
  refuse_job = REFUSED_JOB;
  approve_job = APPROVE_JOB;
  job_category = JOB_CATEGORY;
  active_account = ACCOUNT_ACTIVE;
  block_account = ACCOUNT_BLOCK;
  view_job_admin = VIEW_JOB_ADMIN;
  view_job_apply_admin = VIEW_APPLY_ADMIN;
  expired_job = EXPIRED_JOB;
  pass = PASS;
  notpass = NOTPASS;
  defaultComponent = this.job_category;
  role: any;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (StorageService.isLoggedIn() === false) {
      this.router.navigateByUrl('/notfound');
    }
    this.authService.getRole().subscribe(
      (res) => {
        this.role = res.role.trim();
        console.log('role: ' + res.role);
        if (res.role.trim() === 'JOBSEEKER') {
          console.log('====================================');
          console.log('role: ' + res.role);
          console.log('====================================');
          this.router.navigateByUrl('/notfound');
        }
      },
      (error) => {
        console.log('====================================');
        console.log('err::', error);
        console.log('====================================');
      }
    );
  }

  toggleCollapsed(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  manageJob(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.manage_job;
  }

  jobCategory(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.job_category;
  }

  pendingJob(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.pending_job;
  }

  approveJob(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.approve_job;
  }

  refuseJob(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.refuse_job;
  }

  activeAccount(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.active_account;
  }

  blockAccount(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.block_account;
  }

  viewJob(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.view_job_admin;
  }

  expiredJob(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.expired_job;
  }

  viewJobApplyAdmin(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.view_job_apply_admin;
  }

  passJob(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.pass;
  }

  notPass(event: Event): void {
    event.stopPropagation();
    this.defaultComponent = this.notpass;
  }

}
