package io.github.asephermann.plugins.mocklocationchecker

import com.getcapacitor.JSObject

@CapacitorPlugin(name = "MockLocationChecker")
class MockLocationCheckerPlugin : Plugin() {
    private val implementation = MockLocationChecker()
    @PluginMethod
    fun echo(call: PluginCall) {
        val value: String = call.getString("value")
        val ret = JSObject()
        ret.put("value", implementation.echo(value))
        call.resolve(ret)
    }
}