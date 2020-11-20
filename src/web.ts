import { WebPlugin } from '@capacitor/core';
import { GetAppInfoPlugin } from './definitions';

export class GetAppInfoWeb extends WebPlugin implements GetAppInfoPlugin {
  constructor() {
    super({
      name: 'GetAppInfo',
      platforms: ['web'],
    });
  }

  getAppIcon(options: { packageName: string; }): Promise<{ value: string; }> {
    throw new Error(`Method not implemented. ${options}`);
  }

  getAppLabel(options: { packageName: string; }): Promise<{ value: string; }> {
    throw new Error(`Method not implemented. ${options}`);
  }

  // async echo(options: { value: string }): Promise<{ value: string }> {
  //   console.log('ECHO', options);
  //   return options;
  // }
}

const GetAppInfo = new GetAppInfoWeb();

export { GetAppInfo };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(GetAppInfo);
