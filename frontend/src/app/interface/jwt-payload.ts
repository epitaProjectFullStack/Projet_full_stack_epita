import {Role} from '../enum/role';

export interface JwtPayload {
  sub: string;
  login: string;
  role: Role;
}
