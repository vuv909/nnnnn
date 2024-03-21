import { Component, OnInit } from '@angular/core';
import { NzMessageService } from 'ng-zorro-antd/message';
import { StorageService } from '../../services/storage/storage.service';
import { FavouriteService } from '../../services/favourite.service';
import { ProfileService } from '../../services/profile/profile.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-favourite',
  templateUrl: './favourite.component.html',
  styleUrls: ['./favourite.component.scss'],
})
export class FavouriteComponent implements OnInit {
  jobList: any[] = [];
  email = StorageService.getEmail();
  totalPage: number = 0;
  page: number = 1;

  constructor(
    private favouriteService: FavouriteService,
    private accountService: ProfileService,
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
    this.loadFavoriteJobs();
  }

  loadFavoriteJobs(): void {
    this.accountService.findAccountByEmail({ email: this.email }).subscribe(
      (res) => {
        this.favouriteService.viewFavouriteJob(res.metadata.id, 1).subscribe(
          (res) => {
            this.jobList = res.listResults;
            this.totalPage = res.totalPage * 10;
            this.page = res.page;
          },
          (error) => {
            this.jobList = [];
            this.totalPage = 0;
            this.page = 1;
          }
        );
      },
      (error) => {
        console.error('Error loading favorite jobs:', error);
      }
    );
  }

  deleteFavouriteJob(event: Event, job: any): void {
    event.stopPropagation();
    this.favouriteService.deleteFavoriteJob(job.id).subscribe(
      (res) => {
        this.msg.success('Deleted successfully !!!');
        this.loadFavoriteJobs(); // Reload the list after successful deletion
      },
      (error) => {
        this.handleError(error);
      }
    );
  }

  handleError(error: any): void {
    if (error?.error?.error) {
      this.msg.error(error.error.error, { nzDuration: 2000 });
    } else {
      this.msg.error('Error occurred !!!', { nzDuration: 2000 });
    }
  }

  pageActive(pageActive: number): void {
    this.accountService.findAccountByEmail({ email: this.email }).subscribe(
      (res) => {
        this.favouriteService
          .viewFavouriteJob(res.metadata.id, pageActive)
          .subscribe(
            (res) => {
              this.jobList = res.listResults;
              this.totalPage = res.totalPage * 10;
              this.page = res.page;
            },
            (error) => {
              this.jobList = [];
              this.totalPage = 0;
              this.page = 1;
            }
          );
      },
      (error) => {
        console.error('Error loading favorite jobs:', error);
      }
    );
  }

  changePage(page: number): void {
    this.router.navigateByUrl('/detail/' + page);
  }
}
