import {UUIDTypes} from 'uuid';

import {GameStatus} from '../enum/game-status';

export interface Game {
  uuid: UUIDTypes;
  authorLogin: string;
  subjectGameName: string;
  articleName: string;
  articleContent: string;
  status: GameStatus;
}
