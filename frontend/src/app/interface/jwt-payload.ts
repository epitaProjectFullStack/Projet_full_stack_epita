import {Role} from '../enum/role';

export interface JwtPayload {
  sub: string;
  role: Role;
}
