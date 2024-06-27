'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const MockLocationChecker = core.registerPlugin('MockLocationChecker', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.MockLocationCheckerWeb()),
});

class MockLocationCheckerWeb extends core.WebPlugin {
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

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    MockLocationCheckerWeb: MockLocationCheckerWeb
});

exports.MockLocationChecker = MockLocationChecker;
//# sourceMappingURL=plugin.cjs.js.map
