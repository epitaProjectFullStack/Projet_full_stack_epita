import { Injectable } from '@angular/core';
import * as Stomp from '@stomp/stompjs';
import {environment} from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private stompClient: any = null;

  connect(callback: (event: any) => void) {
    console.log('🚀 connect() called');

    if (this.stompClient && this.stompClient.active) return;

    const baseUrl = environment.apiUrl.replace('/api/', '');

const wsUrl = baseUrl.replace('http', 'ws') + '/ws';

    const client = new Stomp.Client({
      brokerURL: 'ws://localhost/ws',
      reconnectDelay: 5000,
      debug: (str: string) => console.log('[STOMP]', str),
    });

    client.onConnect = () => {
      console.log('✅ STOMP CONNECTED');

      client.subscribe('/topic/games', (message: any) => {
        console.log('📩 RAW:', message.body);

        try {
          const data = JSON.parse(message.body);
          console.log('📦 PARSED:', data);
          callback(data);
        } catch (e) {
          console.error('❌ parse error', e);
        }
      });
    };

    client.onStompError = (frame: any) => {
      console.error('❌ STOMP ERROR', frame);
    };

    client.activate();
    this.stompClient = client;
  }

  disconnect() {
    this.stompClient?.deactivate();
    this.stompClient = null;
  }
}
