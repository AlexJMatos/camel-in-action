package com.alexjmatos.camelinaction.chapter2;

import org.apache.camel.builder.RouteBuilder;

public class RecipientListRoute extends RouteBuilder {
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

        /*from("jms:xmlOrders")
                .setHeader("recipients", method(RecipientsBean.class, "recipients"))
                .recipientList("recipients");*/

        from("jms:xmlOrders")
                .bean(AnnotatedRecipientList.class);
    }
}
