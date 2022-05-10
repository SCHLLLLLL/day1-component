package com.day1.component.statemachine.impl;

import com.day1.component.statemachine.State;
import com.day1.component.statemachine.Transition;
import com.day1.component.statemachine.Visitor;

import java.util.Collection;
import java.util.List;

/**
 * 状态的实现类
 * S: 状态
 * eventTransitions: 此状态对应的流程集合
 * @author : linhanghui
 * @since : 2022/5/9 15:58
 */
public class StateImpl<S, E, C> implements State<S, E, C> {

    protected final S stateId;

    /**
     * 一个状态可以有不同的流程
     */
    private EventTransitions<S, E, C> eventTransitions = new EventTransitions();

    StateImpl(S stateId) {
        this.stateId = stateId;
    }


    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType) {
        Transition<S, E, C> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);
//        newTransition.setCondition();

        Debugger.debug("Begin to add new transition: "+ newTransition);

        eventTransitions.put(event, newTransition);
        return newTransition;
    }

    @Override
    public List<Transition<S, E, C>> getEventTransitions(E event) {
        return eventTransitions.get(event);
    }

    @Override
    public Collection<Transition<S, E, C>> getAllTransitions() {
        return eventTransitions.allTransitions();
    }

    @Override
    public String accept(Visitor visitor) {
        String entry = visitor.visitOnEntry(this);
        String exit = visitor.visitOnExit(this);
        return entry + exit;
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof State) {
            State other = (State) anObject;
            if (this.stateId.equals(other.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return stateId.toString();
    }
}
