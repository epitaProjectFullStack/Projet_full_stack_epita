import {UUIDTypes} from 'uuid';

export interface Game {
  uuid: UUIDTypes;
  authorLogin: string;
  subjectGameName: string;
  articleName: string;
  articleContent: string;
}
