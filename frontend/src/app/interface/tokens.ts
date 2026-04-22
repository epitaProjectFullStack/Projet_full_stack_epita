import {JwtPayload} from './jwt-payload';

export interface Tokens {
  accessToken: string;
  payload: JwtPayload;
  expAccessToken: number;

  refreshToken: string;
}
