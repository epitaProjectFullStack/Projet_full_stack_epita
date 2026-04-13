import {Component} from '@angular/core';
import {RouterLink} from '@angular/router';

import {routesDict} from '../../app.routes';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  protected routes = routesDict
}
