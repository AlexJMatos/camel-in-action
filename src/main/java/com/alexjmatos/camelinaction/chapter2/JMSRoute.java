package com.alexjmatos.camelinaction.chapter2;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

//@Component
public class JMSRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("ftp://rider.com/orders?username=rider&password=secret")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("We just downloaded: " + exchange.getIn().getHeader("CamelFileName"));
                    }
                })
                .toD("jms:queue:${header.myDest}");
    }
}
