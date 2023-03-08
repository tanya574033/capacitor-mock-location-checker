export interface MockLocationCheckerPlugin {
  /**
   * 
   * @param options {whiteList: Array<string>}
   */
  checkMock(options: { whiteList: Array<string> }): Promise<CheckMockResult>;

  /**
   * 
   * @param options {packageName: string}
   */
  goToMockLocationAppDetail(options: { packageName: string }): Promise<void>;
}
export interface CheckMockResult {
  isMock: boolean;
  messages?: string;
  indicated?: Array<string>;
}