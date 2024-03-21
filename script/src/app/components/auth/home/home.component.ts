import {
  AfterViewChecked,
  Component,
  ElementRef,
  HostListener,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Observable, from } from 'rxjs';
import { JobService } from '../../../services/job/job.service';
import { JobCategoryService } from '../../../services/job-category/job-category.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { Router } from '@angular/router';
import { SocketServiceService } from '../../../services/socket-service.service';
import { StorageService } from '../../../services/storage/storage.service';
import { ChatService } from '../../../services/chat.service';
import { Store } from '@ngrx/store';
import { getUser } from '../../../shared/login.selector';
import { AuthService } from '../../../services/auth/auth.service';

interface ChatMessage {
  message: string;
  user: string;
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent implements OnInit, AfterViewChecked, OnDestroy {
  @ViewChild('listJob') listJob: ElementRef | undefined;

  chatModal: boolean = false;
  role: string = '';

  branch: any;
  listCategory: any;
  textSearch: string = '';
  listCategoryLength: number = 0;
  limit: number = 1;
  start: number = 0;
  end: number = 1;
  displayMore: boolean = true;
  displayLess: boolean = true;
  roomId: number = 0;
  user: any;
  //chat
  messageInput: string = '';
  userId: string = '';
  messageList: any[] = [];
  @ViewChild('chatContainer') chatContainer!: ElementRef;

  constructor(
    private jobCategoryService: JobCategoryService,
    private msg: NzMessageService,
    private router: Router,
    private chatService: SocketServiceService,
    private service: ChatService,
    private authService: AuthService,
    private store: Store<{ user: any }>
  ) {}

  openModal(): void {
    this.chatModal = true;
  }

  closeModal(): void {
    this.chatModal = false;
  }

  ngOnInit() {
    if (StorageService.isLoggedIn() === true) {
      if (StorageService.getEmail()) {
        this.authService.getRole().subscribe(
          (res) => {
            this.role = res.role.trim();
            if (res.role === 'JOBSEEKER') {
              this.store.select(getUser).subscribe((res) => {
                if (res) {
                  this.user = res;
                  console.log('====================================');
                  console.log('user::', res);
                  console.log('====================================');
                }
              });

              this.service.createOrGetChat(StorageService.getEmail()).subscribe(
                (res: any) => {
                  this.roomId = res;
                  this.chatService.joinRoom(res);
                  this.listenerMessage();
                  this.service.getMessageByChatId(res).subscribe(
                    (res: any) => {
                      console.log('====================================');
                      console.log('chat::', res);
                      console.log('====================================');
                      this.messageList = res.map((item: any) => ({
                        ...item,
                        message_side:
                          item.sender === StorageService.getEmail()
                            ? 'sender'
                            : 'receiver',
                      }));
                    },
                    (error) => {
                      console.log('====================================');
                      console.log('error:', error);
                      console.log('====================================');
                    }
                  );
                },
                (error) => {}
              );
            }
          },
          (error) => {}
        );
      }
    }

    this.jobCategoryService.viewJobCategory().subscribe(
      (res) => {
        this.listCategory = res;
        this.listCategoryLength = res.length;
      },
      (err) => {
        this.msg.error('Error occurred !!!');
      }
    );
    this.jobCategoryService.getAllBranch().subscribe(
      (res) => {
        this.branch = res;
      },
      (err) => {
        this.msg.error('Error occurred !!!');
      }
    );
  }

  ngAfterViewChecked() {
    if (this.role === 'JOBSEEKER') {
      this.scrollToBottom();
    }
  }

  private scrollToBottom() {
    if (this.role === 'JOBSEEKER') {
      try {
        const chatContainerElement = this.chatContainer.nativeElement;
        if (
          typeof chatContainerElement.scrollTop === 'number' &&
          typeof chatContainerElement.scrollHeight === 'number'
        ) {
          chatContainerElement.scrollTop = chatContainerElement.scrollHeight;
        }
      } catch (err) {
        console.error('Error scrolling to bottom:', err);
      }
    }
  }

  search() {
    this.router.navigateByUrl('/search?title=' + this.textSearch.trim());
  }

  branchSearch(id: number) {
    console.log('====================================');
    console.log('id::', id);
    console.log('====================================');
    this.router.navigateByUrl('/search?branchId=' + id);
  }

  categorySearch(id: number) {
    this.router.navigateByUrl('/search?categoryId=' + id);
  }

  sendMessage() {
    if (this.messageInput === '' || this.messageInput.trim() === '') {
      this.msg.error('Please not let empty message !!!');
    } else {
      const chatMessage = {
        message: this.messageInput,
        user: StorageService.getEmail(),
      } as ChatMessage;
      this.chatService.sendMessage(String(this.roomId), chatMessage);
      const noti = {
        Email: "SUPPORT",
        Content: `${StorageService.getEmail()} vừa nhắn tin cho bạn !!!`,
      };
      this.chatService.sendNoti(noti);
    }
  }

  listenerMessage() {
    this.chatService.getMessageSubject().subscribe((messages: any) => {
      this.messageList = messages.map((item: any) => ({
        ...item,
        message_side:
          item.sender === StorageService.getEmail() ? 'sender' : 'receiver',
      }));
      // console.log('====================================');
      // console.log('messageList::', this.messageList);
      // console.log('====================================');
    });
  }

  ngOnDestroy(): void {
    this.chatService.disconnect();
  }
}
