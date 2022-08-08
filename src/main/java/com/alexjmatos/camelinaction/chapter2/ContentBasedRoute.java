package com.alexjmatos.camelinaction.chapter2;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ContentBasedRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:files/input?noop=true")
                .routeId("Content-Based-Route")
                .to("jms:incomingOrders");

        from("jms:incomingOrders")
                .choice()
                .when(header("CamelFileName").endsWith(".xml"))
                .to("jms:xmlOrders")
                .when(header("CamelFileName").regex("^.*(csv|csl)$"))
                .to("jms:csvOrders")
                .otherwise()
                .to("jms:badOrders")
                .end()
                .to("jms:continuedProcessing");

        from("jms:xmlOrders")
                .log("Received XML Order: ${header.CamelFileName}")
                .to("mock:xml");

        from("jms:csvOrders")
                .log("Received CSV Order: ${header.CamelFileName}")
                .to("mock:csv");

        from("jms:badOrders")
                .log("Received Bad Order: ${header.CamelFileName}")
                .to("mock:badOrder");
    }
}
