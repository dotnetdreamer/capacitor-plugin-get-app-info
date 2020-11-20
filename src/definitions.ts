declare module '@capacitor/core' {
  interface PluginRegistry {
    GetAppInfo: GetAppInfoPlugin;
  }
}

export interface GetAppInfoPlugin {
  getAppIcon(options: { packageName: string }): Promise<{ value: string }>;
  getAppLabel(options: { packageName: string }): Promise<{ value: string }>;
}
