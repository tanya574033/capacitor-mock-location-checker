import { WebPlugin } from '@capacitor/core';
export class MockLocationCheckerWeb extends WebPlugin {
    isLocationFromMockProvider() {
        throw new Error('Method not implemented.');
    }
    goToMockLocationAppDetail(options) {
        throw new Error('Method not implemented.' + options);
    }
    async checkMock(options) {
        throw new Error('Method not implemented.' + options);
    }
}
//# sourceMappingURL=web.js.map