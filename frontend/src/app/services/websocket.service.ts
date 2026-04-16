import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class WebSocketService {

  connect(callback: (event: any) => void) {
    if (typeof window === 'undefined') return; // Not in a browser environment

    const baseUrl = environment.apiUrl.replace('/api/', '');
    const wsUrl = baseUrl.replace('http://', 'ws://').replace('https://', 'wss://') + 'ws';

    const socket = new WebSocket(wsUrl);

    socket.onopen = () => {
      console.log('WebSocket connected');
    };

    socket.onmessage = (event) => {
      callback(JSON.parse(event.data));
    };

    socket.onerror = (err) => {
      console.error('WebSocket error', err);
    };

    socket.onclose = () => {
      console.log('WebSocket closed');
    };
  }
}
