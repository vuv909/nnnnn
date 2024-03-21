import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const BASIC_URL = ['http://localhost:8081/'];

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  constructor(private http: HttpClient) {}

  getAllPeopleHasChat() {
    return this.http.get(BASIC_URL + 'getMessages/getAllChat');
  }

  getMessageByChatId(chatId: number) {
    return this.http.get(BASIC_URL + 'getMessages/' + chatId);
  }

  createOrGetChat(email: string){
    return this.http.get(BASIC_URL + 'createOrGetChat/' + email);
  }

}
