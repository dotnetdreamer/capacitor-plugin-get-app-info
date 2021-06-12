import { WebPlugin } from '@capacitor/core';

import { GetAppInfoPlugin } from './definitions';

export class GetAppInfoWeb extends WebPlugin implements GetAppInfoPlugin {
  constructor() {
    super({
      name: 'GetAppInfo',
      platforms: ['web'],
    });
  }

  getAvailableApps(): Promise<{ applications: string; }> {
    throw new Error('Method not implemented.');
  }

  getAppIcon(options: { packageName: string; }): Promise<{ value: string; }> {
    throw new Error(`Method not implemented. ${options}`);
  }

  getAppLabel(options: { packageName: string; }): Promise<{ value: string; }> {
    throw new Error(`Method not implemented. ${options}`);
  }

  launchApp(options: { packageName: string; }): Promise<void> {
    throw new Error(`Method not implemented. ${options}`);
  }

  canLaunchApp(options: { packageName: string; }): Promise<void> {
    throw new Error(`Method not implemented. ${options}`);
  }
}