import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

@Injectable({ providedIn: 'root' })
export class WebSocketService {

  private client!: Client;

  connect(callback: (event: any) => void) {
    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: str => console.log(str),
      reconnectDelay: 5000,
    });

    this.client.onConnect = () => {
      this.client.subscribe('/topic/games', message => {
        callback(JSON.parse(message.body));
      });
    };

    this.client.activate();
  }
}
