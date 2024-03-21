import {
  AfterViewChecked,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { SocketServiceService } from '../../services/socket-service.service';
import { ChatService } from '../../services/chat.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { ChatMessage } from '../../../types/chat.type';
import { StorageService } from '../../services/storage/storage.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

interface Message {
  text: string;
  sender: 'left' | 'right';
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit, AfterViewChecked, OnDestroy {
  messages: any[] = [];
  newMessage: string = '';
  selectedUser: string | null = null;
  users: any[] = [];
  user: any;
  roomId: number = 0;
  @ViewChild('chatContainer') chatContainer!: ElementRef;
  email: string = '';
  searchName: string = '';
  searchList: any[] = [];
  role: string = '';

  constructor(
    private socketService: SocketServiceService,
    private chatService: ChatService,
    private msg: NzMessageService,
    private router: Router,
    private authService: AuthService
  ) {}

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

    this.chatService.getAllPeopleHasChat().subscribe(
      (res: any) => {
        this.users = res;
        this.searchList = res;
      },
      (error) => {
        if (error?.error?.error) {
          this.msg.error(error?.error?.error, { nzDuration: 2000 });
        } else {
          this.msg.error('Đăng nhập không thành công', { nzDuration: 2000 });
        }
      }
    );
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  handleSearch() {
    console.log('====================================');
    console.log('searchName::', this.searchName);
    console.log('====================================');

    if (this.searchName.trim() !== '') {
      this.searchList = this.users.filter((user) =>
        user.name.toLowerCase().includes(this.searchName.toLowerCase())
      );
    } else {
      this.searchList = this.users;
    }
  }

  private scrollToBottom() {
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

  sendMessage() {
    if (this.newMessage === '' || this.newMessage.trim() === '') {
      this.msg.error('Please not let empty message !!!');
    } else {
      const chatMessage = {
        message: this.newMessage,
        user: StorageService.getEmail(),
      };
      this.socketService.sendMessage(String(this.roomId), chatMessage);
      const noti = {
        Email: this.email,
        Content: `Chăm sóc khách hàng vừa nhắn tin cho bạn !!!`,
      };
      this.socketService.sendNoti(noti);
      // this.listenerMessage();
    }
  }

  handleUserSelect(user: any) {
    this.socketService.disconnect();
    this.email = user.name;
    this.selectedUser = user.id;
    this.roomId = user.id;
    this.chatService.getMessageByChatId(user.id).subscribe(
      (res: any) => {
        console.log('====================================');
        console.log('chat::', res);
        console.log('====================================');
        this.messages = res.map((item: any) => ({
          ...item,
          message_side: item.sender === user.name ? 'receiver' : 'sender',
        }));
        this.socketService.joinRoom(user.id);
        // this.listenerMessage();
      },
      (error) => {
        console.log('====================================');
        console.log('error:', error);
        console.log('====================================');
      }
    );
   
    this.listenerMessage()


  }

  listenerMessage() {
    this.socketService.getMessageSubject().subscribe((messagesRes: any) => {
      this.messages = messagesRes.map((item: any) => ({
        ...item,
        message_side: item.sender === this.email ? 'receiver' : 'sender',
      }));
      console.log('Received messages:', this.messages);
    });
  }

  ngOnDestroy(): void {
    this.socketService.disconnect();
  }
}
