package com.axlabs.apac;

public class SimpleContract {

    public static final int staticVar = 42;

    // Get a fixed constant value
    public static String get() {
        return "Hello world!";
    }

    // Get a fixed constant value from a static var
    public static int getStatic() {
        return staticVar;
    }

}
