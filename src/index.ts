export * from './definitions';

import { registerPlugin } from '@capacitor/core';

import type { GetAppInfoPlugin } from './definitions';

const GetAppInfo = registerPlugin<GetAppInfoPlugin>('GetAppInfo', {
  web: () => import('./web').then(m => new m.GetAppInfoWeb()),
});

export * from './definitions';
export { GetAppInfo };
