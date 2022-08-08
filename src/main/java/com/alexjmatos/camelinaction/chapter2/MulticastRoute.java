package com.alexjmatos.camelinaction.chapter2;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class MulticastRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:files/input?noop=true")
                .routeId("Multicasting")
                .to("jms:incomingOrders");

        from("jms:incomingOrders")
                .choice()
                .when(header("CamelFileName").endsWith(".xml"))
                .to("jms:xmlOrders")
                .otherwise()
                .to("jms:badOrders")
                .end();

        from("jms:xmlOrders")
                .multicast()
                .stopOnException()
                .to("jms:accounting", "jms:production")
                        .end()
                                .to("mock:end");

        from("jms:accounting")
                .throwException(Exception.class, "I failed!")
                .log("Accounting Received Order: ${header.CamelFileName}")
                .to("mock:accounting");

        from("jms:production")
                .log("Production Received Order: ${header.CamelFileName}")
                .to("mock:production");
    }
}
