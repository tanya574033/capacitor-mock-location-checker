import { WebPlugin } from '@capacitor/core';
import type { MockLocationCheckerPlugin, CheckMockResult } from './definitions';
export declare class MockLocationCheckerWeb extends WebPlugin implements MockLocationCheckerPlugin {
    isLocationFromMockProvider(): Promise<Boolean>;
    goToMockLocationAppDetail(options: {
        packageName: string;
    }): Promise<void>;
    checkMock(options: {
        whiteList: Array<string>;
    }): Promise<CheckMockResult>;
}
