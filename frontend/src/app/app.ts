import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

import {ErrorPopup} from './components/error-popup/error-popup';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ErrorPopup],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App {
}
