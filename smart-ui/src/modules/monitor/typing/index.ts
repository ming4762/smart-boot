/**
 * 指标数据
 */
export declare interface MeterData {
  key: string;
  name: string;
  measurements: Array<Measurement>;
  formatter?: Function;
}

export declare interface Measurement {
  statistic: string;
  value: number;
}
