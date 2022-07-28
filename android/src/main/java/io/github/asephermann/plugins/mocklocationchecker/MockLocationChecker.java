package io.github.asephermann.plugins.mocklocationchecker;

import android.util.Log;

public class MockLocationChecker {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
