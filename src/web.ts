import { WebPlugin } from '@capacitor/core';

import type { MockLocationCheckerPlugin } from './definitions';

export class MockLocationCheckerWeb extends WebPlugin implements MockLocationCheckerPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
