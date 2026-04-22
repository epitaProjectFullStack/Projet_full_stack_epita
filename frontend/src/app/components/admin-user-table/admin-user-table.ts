import {Component, EventEmitter, Input, Output} from '@angular/core';

import {Role} from '../../enum/role';
import {AdminUser} from '../../interface/admin-user';
import {Selector} from '../selector/selector';

@Component({
  selector: 'app-admin-user-table',
  imports: [Selector],
  templateUrl: './admin-user-table.html',
  styleUrl: './admin-user-table.css',
})
export class AdminUserTable {
  @Input({required: true}) users!: AdminUser[];

  @Output() banUser = new EventEmitter<AdminUser>();
  @Output() unbanUser = new EventEmitter<AdminUser>();
  @Output()
  changeUserRole = new EventEmitter<{user: AdminUser, role: string}>();

  protected roleToString(role: Role) {
    return Role[role];
  }
}
