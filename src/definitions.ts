export interface MockLocationCheckerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
