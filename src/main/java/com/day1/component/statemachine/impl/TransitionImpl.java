package com.day1.component.statemachine.impl;

import com.day1.component.statemachine.Action;
import com.day1.component.statemachine.Condition;
import com.day1.component.statemachine.State;
import com.day1.component.statemachine.Transition;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:19
 */
public class TransitionImpl<S,E,C> implements Transition<S,E,C> {

    private State<S, E, C> source;

    private State<S, E, C> target;

    private E event;

    private Condition<C> condition;

    private Action<S,E,C> action;

    private TransitionType type = TransitionType.EXTERNAL;


    @Override
    public State<S, E, C> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E, C> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public State<S, E, C> getTarget() {
        return target;
    }

    @Override
    public void setTarget(State<S, E, C> state) {
        this.target = state;
    }

    @Override
    public Condition<C> getCondition() {
        return condition;
    }

    @Override
    public void setCondition(Condition<C> condition) {
        this.condition = condition;
    }

    @Override
    public Action<S, E, C> getAction() {
        return action;
    }

    @Override
    public void setAction(Action<S, E, C> action) {
        this.action = action;
    }

    @Override
    public State<S, E, C> transit(C ctx, boolean checkCondition) {
        this.verify();
        if (!checkCondition || condition == null || condition.isSatisfied(ctx)) {
            if(action != null){
                action.execute(source.getId(), target.getId(), event, ctx);
            }
            return target;
        }

        Debugger.debug("Condition is not satisfied, stay at the "+source+" state ");
        return source;
    }

    @Override
    public void verify() {
        if(type== TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() +", "+type+"]->" + target;
    }

    @Override
    public boolean equals(Object anObject){
        if(anObject instanceof Transition){
            Transition other = (Transition)anObject;
            if(this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget())){
                return true;
            }
        }
        return false;
    }
}
