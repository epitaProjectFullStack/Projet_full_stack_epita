import {CommonModule} from '@angular/common';
import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {ButtonModule} from 'primeng/button';
import {TableModule} from 'primeng/table';

import {AdminUserTable} from '../../components/admin-user-table/admin-user-table';
import {ArticleTable} from '../../components/article-table/article-table';
import {Role, stringToRole} from '../../enum/role';
import {AdminUser} from '../../interface/admin-user';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';
import {WebSocketService} from '../../services/websocket.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports:
      [CommonModule, TableModule, ButtonModule, ArticleTable, AdminUserTable],
  templateUrl: './admin.html'
})
export class Admin implements OnInit, OnDestroy {
  private backend = inject(BackendService);
  private ws = inject(WebSocketService);

  private kafkaCallback = () => {
    this.backend.getAdminGames().subscribe(result => {
      this.articles.set(result.list);
    });
  };

  articles = signal<Game[]>([]);
  users = signal<AdminUser[]>([]);

  ngOnInit() {
    this.backend.getAdminGames().subscribe(result => {
      this.articles.set(result.list);
    });

    this.backend.getAdminUsers().subscribe(result => {
      this.users.set(result.list);
    });

    this.ws.listen(this.kafkaCallback);
  }

  ngOnDestroy(): void {
    this.ws.unlisten(this.kafkaCallback);
  }

  deleteArticle(id: any) {
    this.backend.deleteGame(id).subscribe(() => {
      this.articles.update(old => old.filter(game => game.uuid !== id));
    });
  }

  banUser(user: AdminUser) {
    if (!user.banned) {
      user.banned = true;
      this.backend.modifyUser(user).subscribe(
          () => {this.users.update(users => {
            const changedUser = users.find(u => u.id === user.id);
            changedUser!!.banned = true;
            return [...users];
          })});
      ;
    }
  }

  unbanUser(user: AdminUser) {
    if (user.banned) {
      user.banned = false;
      this.backend.modifyUser(user).subscribe(
          () => {this.users.update(users => {
            const changedUser = users.find(u => u.id === user.id);
            changedUser!!.banned = false;
            return [...users];
          })});
    }
  }

  changeUserRole(user: AdminUser, newRoleString: string) {
    const newRole = stringToRole(newRoleString);
    if (newRole !== Role.GUEST && user.role !== newRole) {
      user.role = newRole;
      this.backend.modifyUser(user).subscribe(
          () => {this.users.update(users => {
            const changedUser = users.find(u => u.id === user.id);
            changedUser!!.role = newRole;
            return [...users];
          })});
    }
  }
}
