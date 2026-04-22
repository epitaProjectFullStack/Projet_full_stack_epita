import {Injectable} from '@angular/core';
import * as Stomp from '@stomp/stompjs';

import {environment} from '../../environments/environment';
import {KafkaEvent} from '../interface/kafka-event';

@Injectable({providedIn: 'root'})
export class WebSocketService {
  private baseUrl = environment.apiUrl.replace('/api/', '');
  private wsUrl = this.baseUrl.replace('http', 'ws') + '/ws';
  private stompClient!: any;

  private listeners: ((event: KafkaEvent) => void)[] = [];

  constructor() {
    this.stompClient = new Stomp.Client({
      brokerURL: this.wsUrl,
      reconnectDelay: 5000,
      debug: (str: string) => console.log('[STOMP]', str),
    });

    this.stompClient.onConnect = () => {
      console.log('STOMP CONNECTED');
      this.stompClient.subscribe('/topic/games', (message: any) => {
        console.log('RAW:', message.body);

        try {
          const data: KafkaEvent = JSON.parse(message.body);
          console.log('PARSED:', data);

          this.listeners.forEach(l => l(data));
        } catch (e) {
          console.error('parse error', e);
        }
      });
    };

    this.stompClient.onStompError = (frame: any) => {
      console.error('STOMP ERROR', frame);
    };

    this.stompClient.activate();
  }

  listen(callback: (event: KafkaEvent) => void) {
    this.listeners.push(callback);
  }

  unlisten(callback: (event: KafkaEvent) => void) {
    this.listeners.filter(l => l !== callback);
  }
}
