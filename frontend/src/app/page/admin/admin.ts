import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BackendService } from '../../services/backend-service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule],
  templateUrl: './admin.html'
})
export class Admin implements OnInit {

  private backend = inject(BackendService);

  articles = this.backend.gamesList;
  users = this.backend.adminUsersList;

  ngOnInit() {
    this.backend.getAllGames();
    this.backend.getAdminUsers();
  }

  deleteArticle(id: any) {
    this.backend.deleteGame(id);
  }

  banUser(id: any) {
    this.backend.deleteUser(id);
  }
}
