package com.day1.component.statemachine.impl;

import com.day1.component.statemachine.State;
import com.day1.component.statemachine.StateMachine;
import com.day1.component.statemachine.Transition;
import com.day1.component.statemachine.Visitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:27
 */
public class StateMachineImpl<S, E, C> implements StateMachine<S, E, C> {

    private String machineId;

    private final Map<S, State<S, E, C>> stateMap;

    private boolean ready;

    public StateMachineImpl(Map<S, State<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    @Override
    public S fireEvent(S sourceState, E event, C ctx) {
        isReady();
        // 通过状态和事件获取实际流程
        Transition<S, E, C> transition = routeTransition(sourceState, event, ctx);

        // 此状态无流程
        if (Objects.isNull(transition)) {
            return sourceState;
        }
        // 执行流程，返回目标状态
        // TODO condition
        return transition.transit(ctx, false).getId();
//        return transition.transit(ctx, transition.getCondition().isSatisfied(ctx)).getId();
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    @Override
    public void showStateMachine() {
        // 系统观察者
        SysOutVisitor sysOutVisitor = new SysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public String accept(Visitor visitor) {
        StringBuilder sb = new StringBuilder();
        // 观察状态机信息
        sb.append(visitor.visitOnEntry(this));

        for (State state : stateMap.values()) {
            // 观察每个状态（节点）信息
            sb.append(state.accept(visitor));
        }
        sb.append(visitor.visitOnExit(this));
        return sb.toString();
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    private State getState(S currentStateId) {
        State state = StateHelper.getState(stateMap, currentStateId);
        if (state == null) {
            showStateMachine();
            throw new StateMachineException(currentStateId + " is not found, please check state machine");
        }
        return state;
    }

    /**
     * Gets transition by sourceState and event
     *
     * @param sourceStateId 状态
     * @param event         事件
     * @param ctx           上下文
     * @return 流程
     */
    private Transition<S, E, C> routeTransition(S sourceStateId, E event, C ctx) {
        // 获取状态机中的状态实体
        State sourceState = getState(sourceStateId);

        // 获取状态中的流程集合
        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        if (transitions == null || transitions.size() == 0) {
            return null;
        }

        Transition<S, E, C> transit = null;
        for (Transition<S, E, C> transition : transitions) {
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(ctx)) {
                transit = transition;
                break;
            }
        }

        return transit;
    }

    private void isReady() {
        if (!ready) {
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }
}
