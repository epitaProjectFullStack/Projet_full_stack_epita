import {UUIDTypes} from 'uuid';

import {KafkaEventType} from '../enum/kafka-event-type';

export interface KafkaEvent {
  eventType: KafkaEventType;
  gameId: UUIDTypes;
  occurredAt: number;
}
