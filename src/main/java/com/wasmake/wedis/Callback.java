package com.wasmake.wedis;

/**
 * @author Andrew R.
 */
@FunctionalInterface
public interface Callback {
    void onMessage(String message);
}

