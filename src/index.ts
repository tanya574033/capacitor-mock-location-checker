import { registerPlugin } from '@capacitor/core';

import type { MockLocationCheckerPlugin } from './definitions';

const MockLocationChecker = registerPlugin<MockLocationCheckerPlugin>('MockLocationChecker', {
  web: () => import('./web').then(m => new m.MockLocationCheckerWeb()),
});

export * from './definitions';
export { MockLocationChecker };
