import {CommonModule} from '@angular/common';
import {Component, inject, OnInit, signal} from '@angular/core';
import {ButtonModule} from 'primeng/button';
import {TableModule} from 'primeng/table';

import {AdminUser} from '../../interface/admin-user';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule],
  templateUrl: './admin.html'
})
export class Admin implements OnInit {
  private backend = inject(BackendService);

  articles = signal<Game[]>([]);
  users = signal<AdminUser[]>([]);

  ngOnInit() {
    this.backend.getAdminGames().subscribe(result => {
      this.articles.set(result.list);
    });

    this.backend.getAdminUsers().subscribe(result => {
      this.users.set(result.list);
    });
  }

  deleteArticle(id: any) {
    this.backend.deleteGame(id).subscribe(() => {
      this.articles.update(old => old.filter(game => game.uuid !== id));
    });
  }

  banUser(id: any) {
    this.backend.deleteUser(id).subscribe(() => this.ngOnInit());
  }
}
