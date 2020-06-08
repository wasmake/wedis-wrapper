package com.wasmake.wedis;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Andrew R.
 */
@Getter
@AllArgsConstructor
public class RedisConfig {
    private String prefix, host, password;
    private int port, timeout, maxIdle, maxTotal;
}
