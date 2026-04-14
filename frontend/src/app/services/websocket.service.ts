import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { environment } from '../../environments/environment';

/**
 *1. User crée un jeu
  2. Backend reçoit POST
  3. Backend envoie event Kafka
  4. Kafka transmet au consumer
  5. Consumer envoie via WebSocket
  6. Front reçoit en temps réel
 */
@Injectable({ providedIn: 'root' })
export class WebSocketService {

  private client!: Client;

  connect(callback: (event: any) => void) {
    this.client = new Client({
      webSocketFactory: () => new SockJS(`${environment.apiUrl.replace('/api/', '')}/ws`),
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
