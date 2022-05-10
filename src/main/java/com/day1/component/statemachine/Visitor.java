package com.day1.component.statemachine;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:14
 */
public interface Visitor {

    char LF = '\n';

    /**
     * 状态机信息
     * @param visitable the element to be visited.
     * @return 状态机信息
     */
    String visitOnEntry(StateMachine<?, ?, ?> visitable);

    /**
     * 状态机退出信息
     * @param visitable the element to be visited.
     * @return 状态机退出信息
     */
    String visitOnExit(StateMachine<?, ?, ?> visitable);

    /**
     * 状态（节点）信息，状态包含（原状态，目标状态，流程
     * @param visitable the element to be visited.
     * @return 状态信息，状态包含（原状态，目标状态，流程
     */
    String visitOnEntry(State<?, ?, ?> visitable);

    /**
     * 状态（节点）退出信息
     * @param visitable the element to be visited.
     * @return
     */
    String visitOnExit(State<?, ?, ?> visitable);
}
