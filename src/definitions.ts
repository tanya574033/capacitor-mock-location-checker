export interface MockLocationCheckerPlugin {
  /**
   * 
   * @param options {whiteList: Array<string>}
   */
  checkMock(options: { whiteList: Array<string> }): Promise<CheckMockResult>;
}
export interface CheckMockResult {
  isMock: boolean;
  messages?: string;
  indicated?: Array<string>;
}