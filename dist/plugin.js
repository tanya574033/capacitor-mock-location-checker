var capacitorMockLocationChecker = (function (exports, core) {
    'use strict';

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

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
