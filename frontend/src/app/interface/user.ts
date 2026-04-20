import {Role} from '../enum/role';

export interface User {
  id: string;
  login: string;
  mail: string;
  role: Role;
}
