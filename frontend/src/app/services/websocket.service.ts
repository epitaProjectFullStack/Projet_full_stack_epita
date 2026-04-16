import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class WebSocketService {

  connect(callback: (event: any) => void) {
    const socket = new WebSocket(`${environment.apiUrl.replace('/api/', '')}ws`);

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
