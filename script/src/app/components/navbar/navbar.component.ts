import {
  AfterViewChecked,
  AfterViewInit,
  Component,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { Router } from '@angular/router';
import { MegaMenuItem } from 'primeng/api';
import { StorageService } from '../../services/storage/storage.service';
import { Store } from '@ngrx/store';
import { ProfileService } from '../../services/profile/profile.service';
import { storeUser } from '../../shared/login.action';
import { getUser } from '../../shared/login.selector';
import { NzMessageModule, NzMessageService } from 'ng-zorro-antd/message';
import { GetRoleService } from '../../services/get-role.service';
import { AuthService } from '../../services/auth/auth.service';
import { NotificationService } from '../../services/notification.service';
import { Subscription } from 'rxjs';
import { SocketServiceService } from '../../services/socket-service.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  items: MegaMenuItem[] | undefined;
  count: string = '5';
  isLogin: boolean = StorageService.isLoggedIn();
  email: string = StorageService.getEmail();
  profile: any;
  user: any;
  listComment: any;
  intervalId: any;
  displaying: boolean = false;
  totalNumber: string = String(0);
  role: string = '';

  constructor(
    private router: Router,
    private profileService: ProfileService,
    private store: Store<{ user: any }>,
    private msg: NzMessageService,
    private getRoleService: GetRoleService,
    private authService: AuthService,
    private notificationService: NotificationService,
    private socketService: SocketServiceService
  ) {}

  ngOnInit() {
    if (StorageService.isLoggedIn()) {
      setTimeout(() => {
        this.socketService.joinRoom();
        this.listenerMessage();
      }, 0);
    }

    if (StorageService.getEmail()) {
      this.authService.getRole().subscribe(
        (res) => {
          this.role = res.role;
          console.log('role: ' + res.role);

          this.notificationService.viewNotification().subscribe(
            (notifications) => {
              console.log('====================================');
              console.log('notifications:', notifications);
              console.log('====================================');
              if (this.role === 'JOBSEEKER') {
                this.listComment = notifications.filter(
                  (notification: any) =>
                    notification.email === StorageService.getEmail()
                );
              } else {
                this.listComment = notifications.filter(
                  (notification: any) =>
                    notification.email === 'SUPPORT' ||
                    notification.email === StorageService.getEmail()
                );
              }
            },
            (error) => {
              console.log('====================================');
              console.log('error:', error);
              console.log('====================================');
            }
          );
        },
        (error) => {
          this.role = '';
        }
      );
    }

    this.store.select(getUser).subscribe((res) => {
      if (res) {
        this.user = res; // Update user directly from the store
        this.updateMenuItems(); // Update menu items when user data changes
      }
    });

    if (this.isLogin && this.email) {
      this.profileService.findAccountByEmail({ email: this.email }).subscribe(
        (res) => {
          this.profileService
            .viewProfileByEmail({ id: res.metadata.id })
            .subscribe(
              (res) => {
                this.store.dispatch(storeUser({ user: res.metadata }));
                this.profile = res.metadata;
                this.updateMenuItems(); // Update menu items when profile data changes
              },
              (error) => {
                this.msg.error('error occured');
              }
            );
        },
        (error) => {
          this.msg.error('error occured');
        }
      );
    }
  }

  // ngAfterViewChecked(): void {
  //   this.socketService.joinRoom();
  // }

  updateMenuItems() {
    this.items = [
      {
        label:
          this.user && this.user.avatar
            ? `<img src="data:image/png;base64,${this.user.avatar}" class="w-10 h-10 rounded-full" alt="">`
            : this.profile && this.profile.avatar
            ? `<img src="data:image/png;base64,${this.profile.avatar}" class="w-10 h-10 rounded-full" alt="">`
            : `<img src="assets/images/avatar.png" class="w-10 h-10 rounded-full" alt="">`,
        icon: '',
        items: [
          [
            {
              items: [
                {
                  label: 'Chat',
                  routerLink: '/chat',
                  visible:
                    this.role === 'ADMIN' || this.role === 'EMPLOYER'
                      ? true
                      : false,
                },
                {
                  label: 'Quản lí',
                  routerLink: '/dashboard',
                  visible:
                    this.role === 'ADMIN' || this.role === 'EMPLOYER'
                      ? true
                      : false,
                },
                {
                  label: 'Các công việc đã ứng tuyển',
                  routerLink: '/myApplication',
                  visible: this.role === 'JOBSEEKER' ? true : false,
                },
                {
                  label: 'Các công việc yêu thích',
                  routerLink: '/favourite',
                  visible: this.role === 'JOBSEEKER' ? true : false,
                },
                {
                  label: 'Hồ sơ của tôi',
                  routerLink: '/profile',
                },
                {
                  label: 'Đổi mật khẩu',
                  routerLink: '/changeforgotpassword',
                },
                {
                  label: 'Đăng xuất',
                  command: (event) => this.logout(),
                },
              ],
            },
          ],
        ],
      },
    ];
  }

  logout() {
    StorageService.signout();
  }

  about() {
    this.router.navigateByUrl('/about');
  }

  contact() {
    this.router.navigateByUrl('/contact');
  }

  toggle() {
    console.log('Toggling displaying...');
    this.displaying = !this.displaying;
    console.log('Displaying:', this.displaying);
    if (this.displaying === true) {
      console.log('Fetching notifications...');
      this.totalNumber = '';
      if(this.role === 'JOBSEEKER'){
        this.notificationService
        .updateReadNoti(
          StorageService.getEmail()
        )
        .subscribe(
          (updateRes) => {
            console.log('Update successful:', updateRes);
          },
          (updateErr) => {}
        );
      }else{
        this.notificationService
        .updateReadNoti(
          StorageService.getEmail()
        )
        .subscribe(
          (updateRes) => {
            console.log('Update successful:', updateRes);
          },
          (updateErr) => {}
        );
        this.notificationService
        .updateReadNoti(
          'SUPPORT'
        )
        .subscribe(
          (updateRes) => {
            console.log('Update successful:', updateRes);
          },
          (updateErr) => {}
        );
      }
    }
    if (this.displaying === false) {
      this.notificationService.viewNotification().subscribe(
        (notifications) => {
          console.log('====================================');
          console.log('notifications:', notifications);
          console.log('====================================');
          if (this.role === 'JOBSEEKER') {
            this.listComment = notifications.filter(
              (notification: any) =>
                notification.email === StorageService.getEmail()
            );
          } else {
            this.listComment = notifications.filter(
              (notification: any) =>
                notification.email === 'SUPPORT' ||
                notification.email === StorageService.getEmail()
            );
          }
        },
        (error) => {
          console.log('====================================');
          console.log('error:', error);
          console.log('====================================');
        }
      );
    }
  }

  countUnreadComments(): string {
    if (this.listComment && this.listComment.length > 0) {
      const unreadComments = this.listComment.filter(
        (comment: any) => !comment.read
      );
      return unreadComments.length.toString();
    } else {
      return '0';
    }
  }

  //received notification
  listenerMessage() {
    if (StorageService.getEmail()) {
      this.authService.getRole().subscribe(
        (res) => {
          this.role = res.role;
          console.log('Role:', this.role);

          this.socketService.getNotificationSubject().subscribe(
            (notiRes: any) => {
              console.log('Received notification:', notiRes);
              if (this.role === 'JOBSEEKER') {
                this.listComment = notiRes.filter(
                  (notification: any) =>
                    notification.email === StorageService.getEmail()
                );
              } else {
                this.listComment = notiRes.filter(
                  (notification: any) =>
                    notification.email === 'SUPPORT' ||
                    notification.email === StorageService.getEmail()
                );
              }
              this.totalNumber = String(this.listComment.length);
            },
            (error: any) => {
              console.error('Error in receiving notification:', error);
            }
          );
        },
        (error) => {
          console.error('Error getting user role:', error);
        }
      );
    }
  }

  ngOnDestroy(): void {
    this.socketService.disconnect();
  }
}
