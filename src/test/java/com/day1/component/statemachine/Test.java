package com.day1.component.statemachine;

import com.day1.component.statemachine.builder.StateMachineBuilder;
import com.day1.component.statemachine.builder.StateMachineBuilderFactory;

/**
 * @author : linhanghui
 * @since : 2022/5/9 16:24
 */
public class Test {

    static String MACHINE_ID = "TestStateMachine";

    enum States {
        STATE1, STATE2, STATE3, STATE4
    }

    enum Events {
        EVENT1, EVENT2, EVENT3, EVENT4, INTERNAL_EVENT
    }

    static class Context {
        String operator = "frank";
        String entityId = "123465";
    }

    public static void main(String[] args) {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID);

        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        System.out.println(target);
    }

    private static Condition<Context> checkCondition() {
        return (ctx) -> {
            return true;
        };
    }

    private static Action<States, Events, Context> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println(ctx.operator + " is operating " + ctx.entityId + " from:" + from + " to:" + to + " on:" + event);
        };
    }
}
