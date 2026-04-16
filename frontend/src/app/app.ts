import {Component, inject} from '@angular/core';
import {RouterOutlet} from '@angular/router';

import {ErrorPopup} from './components/error-popup/error-popup';
import {Navbar} from './components/navbar/navbar';
import { BackendService } from './services/backend-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ErrorPopup, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App {
  private backend = inject(BackendService);

  ngOnInit() {
    this.backend.checkAdmin();
  }
}
