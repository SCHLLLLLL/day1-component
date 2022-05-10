package com.day1.component.statemachine;

/**
 * S:状态
 * E:事件
 * C:Context
 * @author : linhanghui
 * @since : 2022/5/9 15:14
 */
public interface StateMachine<S, E, C> extends Visitable {

    /**
     * Send an event {@code E} to the state machine.
     *
     * @param sourceState the source state
     * @param event the event to send
     * @param ctx the user defined context
     * @return the target state
     */
    S fireEvent(S sourceState, E event, C ctx);

    /**
     * MachineId is the identifier for a State Machine
     * @return
     */
    String getMachineId();

    /**
     * Use visitor pattern to display the structure of the state machine
     */
    void showStateMachine();
}
