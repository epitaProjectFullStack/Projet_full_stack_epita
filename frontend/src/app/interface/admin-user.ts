import {Role} from '../enum/role';
import { UUIDTypes } from 'uuid';

export interface AdminUser {
  id: UUIDTypes;
  login: String;
  password: String;
  mail: String;
  role: Role;
  token: String;
  banned: boolean;
}
