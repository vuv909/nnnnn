import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JobService } from '../../../services/job/job.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { StorageService } from '../../../services/storage/storage.service';
import { JobApplicationService } from '../../../services/job-application/job-application.service';
import { FavouriteComponent } from '../../favourite/favourite.component';
import { FavouriteService } from '../../../services/favourite.service';
import { ProfileService } from '../../../services/profile/profile.service';
import { AuthService } from '../../../services/auth/auth.service';
import { SocketServiceService } from '../../../services/socket-service.service';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.scss',
})
export class DetailComponent implements OnInit {
  email: string = StorageService.getEmail();
  jobId: any;
  responsiveOptions: any[] | undefined;
  detailWork: any;
  isVisible: boolean = false;
  isSpinning: boolean = false;
  listJobCategory: any[] = [];
  coverLetter: string = '';
  userId: any;
  isFavourited: boolean = false;
  favouriteJobId: number = 0;
  validJob: boolean = true;
  role: string = '';
  hrEmail: string = '';
  //slide
  slides = [
    { img: 'http://placehold.it/350x150/000000' },
    { img: 'http://placehold.it/350x150/111111' },
    { img: 'http://placehold.it/350x150/333333' },
    { img: 'http://placehold.it/350x150/666666' },
    { img: 'http://placehold.it/350x150/000000' },
    { img: 'http://placehold.it/350x150/111111' },
    { img: 'http://placehold.it/350x150/333333' },
    { img: 'http://placehold.it/350x150/666666' },
    { img: 'http://placehold.it/350x150/000000' },
    { img: 'http://placehold.it/350x150/111111' },
    { img: 'http://placehold.it/350x150/333333' },
    { img: 'http://placehold.it/350x150/666666' },
  ];
  slideConfig = {
    slidesToShow: 4,
    slidesToScroll: 4,
    autoplay: true,
    autoplaySpeed: 2000,
    prevArrow:
      '<button class="absolute top-1/2 z-50 bg-gray-400 rounded-full p-2 text-red-500"><svg xmlns="http://www.w3.org/2000/svg" height="16" width="14" viewBox="0 0 448 512"><path d="M9.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l160 160c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L109.2 288 416 288c17.7 0 32-14.3 32-32s-14.3-32-32-32l-306.7 0L214.6 118.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-160 160z"/></svg></button>',
    nextArrow:
      '<button class="absolute top-1/2  right-0 z-50 bg-gray-400 rounded-full p-2 text-red-500"><svg xmlns="http://www.w3.org/2000/svg" height="16" width="14" viewBox="0 0 448 512"><path d="M438.6 278.6c12.5-12.5 12.5-32.8 0-45.3l-160-160c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L338.8 224 32 224c-17.7 0-32 14.3-32 32s14.3 32 32 32l306.7 0L233.4 393.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0l160-160z"/></svg></button>',
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
          infinite: true,
        },
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2,
        },
      },
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
        },
      },
    ],
  };

  constructor(
    private activatedRoute: ActivatedRoute,
    private jobService: JobService,
    private msg: NzMessageService,
    private jobApplicationService: JobApplicationService,
    private route: Router,
    private favouriteService: FavouriteService,
    private accountService: ProfileService,
    private authService: AuthService,
    private socketService: SocketServiceService
  ) {}

  ngOnInit(): void {
    if (StorageService.getEmail()) {
      this.authService.getRole().subscribe(
        (res) => {
          this.role = res.role.trim();
          console.log('====================================');
          console.log('role: ' + res.role);
          console.log('====================================');
        },
        (error) => {
          this.role = '';
        }
      );
    }

    this.accountService.findAccountByEmail({ email: this.email }).subscribe(
      (res) => {
        this.userId = res.metadata.id;
      },
      (error) => {
        console.log('====================================');
        console.log('Error occured !!!', error);
        console.log('====================================');
      }
    );

    this.activatedRoute.params.subscribe((params) => {
      this.jobId = params['jobId'];

      this.accountService.findAccountByEmail({ email: this.email }).subscribe(
        (res) => {
          this.favouriteService.viewFavouriteJob(res.metadata.id).subscribe(
            (res) => {
              res.metadata.forEach((job: any) => {
                console.log('====================================');
                console.log('res::', job);
                console.log('====================================');
                if (job.jobId === Number(this.jobId)) {
                  this.isFavourited = true;
                  this.favouriteJobId = job.id;
                }
              });
            },
            (error) => {
              console.error('Error loading favorite jobs:', error);
            }
          );
        },
        (error) => {
          console.error('Error loading account:', error);
        }
      );

      this.jobService.getJobById(this.jobId).subscribe(
        (res) => {
          console.log('detail job ::', res);
          this.hrEmail = res.hrEmail;
          this.detailWork = res;

          // Check if apply_Before date is provided
          const applyBeforeDate: string | undefined =
            this.detailWork?.apply_Before;
          if (applyBeforeDate) {
            const applyBeforeTimestamp: number = new Date(
              applyBeforeDate
            ).getTime();
            const currentDateTimestamp: number = Date.now();

            if (applyBeforeTimestamp > currentDateTimestamp) {
              this.validJob = true;
            } else {
              this.validJob = false;
            }
          } else {
            console.log('No apply_Before date specified for the job'); // Adjust this according to your application's requirements
          }

          this.jobService.getJobByCategoryId(res?.category_Id).subscribe(
            (res) => {
              this.listJobCategory = res;
              console.log('====================================');
              console.log('job::', res);
              console.log('====================================');
            },
            (err) => {
              console.log('====================================');
              console.log('err::', err);
              console.log('====================================');
            }
          );
        },
        (err) => {
          console.log('====================================');
          console.log('err::', err);
          console.log('====================================');
        }
      );
    });
  }

  favourite(data: any) {
    if (StorageService.isLoggedIn() === false) {
      this.msg.error('Please login to add favourite !!!');
    } else {
      const formData = {
        jobId: data.id,
        accountId: this.userId,
      };
      // formData.append("id",data.id);
      // formData.append("jobId",data.id);
      // formData.append("accountId",this.userId);
      // formData.append("job_Name","asdfdsf")

      this.favouriteService.addFavouriteJob(formData).subscribe(
        (res) => {
          this.msg.success('Add favourite job successfully !!!');
          this.favouriteJobId = res.id;
          this.isFavourited = true;
        },
        (error) => {
          console.log('====================================');
          console.log('err::', error);
          console.log('====================================');
        }
      );
    }
  }

  apply() {
    if (StorageService.isLoggedIn() === false) {
      this.msg.error('Please login to apply !!!');
    } else {
      console.log('====================================');
      console.log('isFavouriteJob::', this.isFavourited);
      console.log('====================================');
      this.isVisible = true;
    }
  }

  handleCancel() {
    this.isVisible = false;
  }

  handleOk() {
    this.isSpinning = true;
    if (this.coverLetter.length > 200) {
      this.isSpinning = false;
      this.msg.error('Vui lòng nhập ghi chú nhỏ hơn 200 kí tự !!!');
    } else {
      const formData = new FormData();
      formData.append('email', StorageService.getEmail());
      formData.append('job_Id', this.jobId);
      formData.append('cover_Letter', this.coverLetter.trim());
      this.jobApplicationService.applyJobApplication(formData).subscribe(
        (res) => {
          this.msg.success('Apply job successfully !!!');
          this.isSpinning = false;
          this.isVisible = false;
          const noti = {
            Email: this.detailWork.hrEmail,
            Content: `${StorageService.getEmail()} vừa ứng tuyển công việc ${this.detailWork.name} !!!`,
          };
          this.socketService.sendNoti(noti);
        },
        (error) => {
          if (error?.error?.error) {
            this.msg.error(error?.error?.error, { nzDuration: 2000 });
          } else {
            this.msg.error('Apply job failed !!!', { nzDuration: 2000 });
          }
          this.isSpinning = false;
        }
      );
    }
  }

  clickItem(data: any) {
    this.route.navigate(['/detail', data?.id]);
  }

  deleteJob(data: any) {
    this.favouriteService.deleteFavoriteJob(this.favouriteJobId).subscribe(
      (res) => {
        this.msg.success('Delete favourite job successfully !!!');
        this.isFavourited = false;
      },
      (error) => {
        console.log('====================================');
        console.log('err::', error);
        console.log('====================================');
      }
    );
  }
}
