import {Role} from '../enum/role';

export interface User {
  login: string;
  main: string;
  role: Role;
  banned: boolean;
}
