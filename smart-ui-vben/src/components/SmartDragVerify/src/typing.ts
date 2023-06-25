/**
 * 支持的验证类型
 */
export type VerifyType = 'SLIDER' | 'ROTATE' | 'CONCAT'

export interface MoveData {
  event: MouseEvent | TouchEvent
  moveDistance: number
  moveX: number
}

/**
 * 验证码配置信息
 */
export interface CaptchaConfig {

  key: string,
  startTime?: Date

  bgImageWidth?: number

  bgImageHeight?: number

  sliderImageWidth?: number

  sliderImageHeight?: number

  stopTime?: Date

  trackList?: CaptchaTrackData[]

  startX?: number

  startY?: number
}

export type CaptchaTrackType = 'DOWN' | 'MOVE' | 'UP'

export interface CaptchaTrackData {
  x: number
  y: number
  type: CaptchaTrackType
  t: number
}
