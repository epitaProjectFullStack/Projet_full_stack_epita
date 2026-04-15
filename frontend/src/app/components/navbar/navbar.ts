import {Component, inject} from '@angular/core';
import {RouterLink} from '@angular/router';

import {routesDict} from '../../app.routes';
import { BackendService } from '../../services/backend-service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  protected routes = routesDict

  private backend = inject(BackendService);

  ngOnInit() {
    this.backend.getAdminUsers();
  }
}
