import { Injectable } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject } from 'rxjs';
import { StorageService } from './storage/storage.service';

interface ChatMessage {
  message: string;
  user: string;
}

@Injectable({
  providedIn: 'root',
})
export class SocketServiceService {
  private stompClient: any;
  private messageSubject: BehaviorSubject<ChatMessage[]> = new BehaviorSubject<
    ChatMessage[]
  >([]);
  private notifySubject: BehaviorSubject<any[]> = new BehaviorSubject<any[]>(
    []
  );
  private blockAccount: BehaviorSubject<any[]> = new BehaviorSubject<any[]>([]);

  constructor() {
    this.initConnectionSocet();
  }

  initConnectionSocet() {
    const url = 'http://localhost:8081/chat-socket';
    const socket = new SockJS(url);
    this.stompClient = Stomp.over(socket);
  }

  joinRoom(roomId?: string) {
    console.log('connected::', this.stompClient.connected);

    if (!this.stompClient.connected) {
      this.initConnectionSocet();
    }

    this.stompClient.connect({}, () => {
      console.log('Connected to WebSocket server');
      console.log('statusConnected::', this.stompClient.connected);

      if (roomId) {
        this.stompClient.subscribe(`/topic/` + roomId, (messages: any) => {
          this.messageSubject.next(JSON.parse(messages.body));
        });
      }

      this.stompClient.subscribe('/toBlockAccount', (messages: any) => {
        console.log('====================================');
        console.log('block account ::', JSON.parse(messages.body));
        console.log('====================================');
        // this.blockAccount.next(JSON.parse(messages.body));
        if(JSON.parse(messages.body).email === StorageService.getEmail()){
          StorageService.signout()
        }
      });

      // Subscribe to other topics if needed
      this.stompClient.subscribe(`/toClient`, (messages: any) => {
        this.notifySubject.next(JSON.parse(messages.body));
      });
    });
  }

  sendBlockAccount(email: string) {
    console.log('====================================');
    console.log('data:', email);
    console.log('====================================');
    this.stompClient.send(
      '/app/blockAccount',
      {},
      JSON.stringify({
        email: email,
      })
    );
  }

  sendNoti(notification: any) {
    console.log('Sending notification:', notification);
    this.stompClient.send(
      '/app/notify',
      {},
      JSON.stringify({
        email: notification.Email,
        content: notification.Content,
      })
    );
  }

  sendMessage(roomId: string, message: ChatMessage) {
    this.stompClient.send(
      `/app/chat/` + roomId,
      {},
      JSON.stringify(message) // Convert ChatMessage object to JSON string
    );
  }

  getAccountBlock() {
    return this.blockAccount.asObservable();
  }

  getNotificationSubject() {
    return this.notifySubject.asObservable();
  }

  getMessageSubject() {
    return this.messageSubject.asObservable();
  }

  disconnect() {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.disconnect(() => {
        console.log('Disconnected from Stomp server');
      });
    }
  }
}
