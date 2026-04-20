import {JwtPayload} from './jwt-payload';

export interface Tokens {
  accessToken: string;
  payload: JwtPayload;
  expAccessToken: Date;

  refreshToken: string;
}
