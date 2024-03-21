import { Component, OnInit } from '@angular/core';
import { ChatMessage } from '../types/chat.type';
import { StorageService } from './services/storage/storage.service';
import { ProfileService } from './services/profile/profile.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import e from 'express';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  messageList: any;
  userId = 1;
  roomId: any = null;
  listComment: any = null;
  commentString!: string;

  constructor(
    private profileService: ProfileService,
    private msg: NzMessageService
  ) {}

  ngOnInit(): void {
   
  }

 
}
