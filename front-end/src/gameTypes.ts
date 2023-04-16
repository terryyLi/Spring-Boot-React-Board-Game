export type GameStage = 'new' | 'init' | 'select' | 'move' | 'build' | 'endTurn'

export interface Field {
  field_id: number
  level: number
  isCapped: boolean
  hasWorker: boolean
  worker: Worker
}

export interface Worker {
  owner_id: number
  owner_name: string
}

export interface GodCard {
  card_desc: string
  ability_desc: string
  is_ability_active: boolean
}

export interface GameState {
  fields: Field[]
  validOptions: number[]
  message: string
  stage: GameStage
  godCard?: GodCard
  godCardOptions: string[]
  cardSelection: number[]
}
