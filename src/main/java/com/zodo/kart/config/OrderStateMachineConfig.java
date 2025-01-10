package com.zodo.kart.config;

import com.zodo.kart.enums.OrderEvent;
import com.zodo.kart.enums.OrderStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachineFactory
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvent> config) throws Exception {
        config.withConfiguration()
                .listener(new StateMachineListenerAdapter<OrderStatus, OrderEvent>() {
                    @Override
                    public void stateChanged(State<OrderStatus, OrderEvent> from, State<OrderStatus, OrderEvent> to) {
                        String fromState = (from == null) ? "None" : from.getId().toString();
                        String toState = (to == null) ? "None" : to.getId().toString();
                        System.out.println("Order state changed from " + fromState + " to " + toState);
                    }

                    @Override
                    public void stateMachineError(org.springframework.statemachine.StateMachine<OrderStatus, OrderEvent> stateMachine, Exception exception) {
                        System.err.println("StateMachine encountered an error: " + exception.getMessage());
                    }
                });
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderStatus.INITIATED)
                .state(OrderStatus.PENDING)
                .state(OrderStatus.ACCEPTED)
                .state(OrderStatus.PROCESSING)
                .state(OrderStatus.SHIPPED)
                .state(OrderStatus.DELIVERED)
                .state(OrderStatus.COMPLETED)
                .state(OrderStatus.CANCELLED)
                .state(OrderStatus.RETURNED)
                .state(OrderStatus.REFUNDED)
                .state(OrderStatus.PAYMENT_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStatus.INITIATED).target(OrderStatus.PENDING).event(OrderEvent.PAYMENT_SUCCESS)
                .and()
                .withExternal().source(OrderStatus.INITIATED).target(OrderStatus.PAYMENT_ERROR).event(OrderEvent.PAYMENT_FAILURE)
                .and()
                .withExternal().source(OrderStatus.PENDING).target(OrderStatus.ACCEPTED).event(OrderEvent.ACCEPT_ORDER)
                .and()
                .withExternal().source(OrderStatus.ACCEPTED).target(OrderStatus.PROCESSING).event(OrderEvent.PROCESS_ORDER)
                .and()
                .withExternal().source(OrderStatus.PROCESSING).target(OrderStatus.SHIPPED).event(OrderEvent.SHIP_ORDER)
                .and()
                .withExternal().source(OrderStatus.SHIPPED).target(OrderStatus.DELIVERED).event(OrderEvent.DELIVER_ORDER)
                .and()
                .withExternal().source(OrderStatus.DELIVERED).target(OrderStatus.COMPLETED).event(OrderEvent.COMPLETE_ORDER)
                .and()
                .withExternal().source(OrderStatus.PENDING).target(OrderStatus.CANCELLED).event(OrderEvent.CANCEL_ORDER)
                .and()
                .withExternal().source(OrderStatus.DELIVERED).target(OrderStatus.RETURNED).event(OrderEvent.RETURN_ORDER)
                .and()
                .withExternal().source(OrderStatus.RETURNED).target(OrderStatus.REFUNDED).event(OrderEvent.REFUND_ORDER)
                .and()
                .withExternal().state(OrderStatus.CANCELLED).target(OrderStatus.REFUNDED).event(OrderEvent.REFUND_ORDER);
    }
}
