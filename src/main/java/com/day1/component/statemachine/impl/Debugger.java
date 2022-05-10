package com.day1.component.statemachine.impl;

/**
 * @author : linhanghui
 * @since : 2022/5/9 19:09
 */
public class Debugger {

    private static boolean isDebugOn = false;

    public static void debug(String message){
        if(isDebugOn){
            System.out.println(message);
        }
    }

    public static void enableDebug(){
        isDebugOn = true;
    }
}
