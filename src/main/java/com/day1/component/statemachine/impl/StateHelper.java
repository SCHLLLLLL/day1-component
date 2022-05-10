package com.day1.component.statemachine.impl;

import com.day1.component.statemachine.State;

import java.util.Map;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:58
 */
public class StateHelper {

    public static <S, E, C> State<S, E, C> getState(Map<S, State<S, E, C>> stateMap, S stateId){
        State<S, E, C> state = stateMap.get(stateId);
        if (state == null) {
            state = new StateImpl<>(stateId);
            stateMap.put(stateId, state);
        }
        return state;
    }
}
