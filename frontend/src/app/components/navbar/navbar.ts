import {Component, inject} from '@angular/core';
import {Router, RouterLink} from '@angular/router';

import {routesDict} from '../../app.routes';
import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  protected routes = routesDict;

  public backend = inject(BackendService);
  private router = inject(Router);

  onLogout() {
    this.backend.removeTokens();
    this.router.navigate(['']);
  }
}
