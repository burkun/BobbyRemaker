// game/assets/core/types/ObjectType.ts

export type ObjectType =
  | 'carrot'      // 胡萝卜，收集目标
  | 'seed'        // 种子，可捡起/放置
  | 'spring'      // 弹簧
  | 'door'        // 关卡出口
  | 'flight'      // 飞行道具
  | 'bonus'       // 奖励门
  | 'start'       // 起点
  | 'rock'        // 石头，可推动
  | 'mower';      // 割草机

export const OBJECT_TYPES: ObjectType[] = [
  'carrot', 'seed', 'spring', 'door', 'flight', 'bonus', 'start', 'rock', 'mower'
];

export const COLLECTIBLE_OBJECTS: ObjectType[] = ['carrot', 'seed', 'flight'];
