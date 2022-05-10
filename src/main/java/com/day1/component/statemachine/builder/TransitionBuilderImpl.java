package com.day1.component.statemachine.builder;

import com.day1.component.statemachine.Action;
import com.day1.component.statemachine.Condition;
import com.day1.component.statemachine.State;
import com.day1.component.statemachine.Transition;
import com.day1.component.statemachine.impl.TransitionType;
import com.day1.component.statemachine.impl.StateHelper;

import java.util.Map;

/**
 * from -> to -> on -> when -> perform
 * 原状态 -> 目标状态 -> 流程(事件 -> condition -> action
 * SourceState -> TargetState
 * -> SourceState.addTargetState(TargetState).add(List(Transition(sourceState, targetState, event, condition))
 * -> Transition(action)
 * @author : linhanghui
 * @since : 2022/5/9 15:55
 */
public class TransitionBuilderImpl<S, E, C> implements ExternalTransitionBuilder<S, E, C>, InternalTransitionBuilder<S, E, C>, From<S, E, C>, On<S, E, C>, To<S, E, C> {

    final Map<S, State<S, E, C>> stateMap;

    private State<S, E, C> source;

    protected State<S, E, C> target;

    private Transition<S, E, C> transition;

    final TransitionType transitionType;


    public TransitionBuilderImpl(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }

    @Override
    public From<S, E, C> from(S stateId) {
        source = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E, C> to(S stateId) {
        target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E, C> within(S stateId) {
        source = target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public On<S, E, C> on(E event) {
        transition = source.addTransition(event, target, transitionType);
        return this;
    }

    @Override
    public When<S, E, C> when(Condition<C> condition) {
        transition.setCondition(condition);
        return this;
    }

    @Override
    public void perform(Action<S, E, C> action) {
        transition.setAction(action);
    }
}
