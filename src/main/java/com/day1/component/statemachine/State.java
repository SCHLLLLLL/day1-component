package com.day1.component.statemachine;

import com.day1.component.statemachine.impl.TransitionType;

import java.util.Collection;
import java.util.List;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:15
 */
public interface State<S, E, C> extends Visitable {

    /**
     * Gets the state identifier.
     *
     * @return the state identifiers
     */
    S getId();

    /**
     * Add transition to the state
     * @param event the event of the Transition
     * @param target the target of the transition
     * @param transitionType the type of transition
     * @return the transition
     */
    Transition<S,E,C> addTransition(E event, State<S, E, C> target, TransitionType transitionType);

    /**
     * Gets transition by event
     * @param event the event
     * @return
     */
    List<Transition<S,E,C>> getEventTransitions(E event);

    /**
     * Gets all transition
     * @return
     */
    Collection<Transition<S,E,C>> getAllTransitions();
}
