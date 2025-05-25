package com.authservice.util.constraints;

public enum CacheConstraints {

    USER_KEY("ms-user:user:%s"),
    USER_TOKEN_KEY("ms-user:all-token:%s"),
    TOKEN_KEY("ms-user:token:%s");
//    USER_ITEMS_KEY("ms-user:order-items:%s");

    private final String keyFormat;

    CacheConstraints(String keyFormat) {
        this.keyFormat = keyFormat;
    }

    public String getKey(Object... args) {
        return String.format(this.keyFormat, args);
    }
}
