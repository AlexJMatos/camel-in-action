package com.alexjmatos.camelinaction.chapter2;

import org.apache.camel.language.xpath.XPath;

public class RecipientsBean {
    public String[] recipients(@XPath("/order/@customer") String customer) {
        if (isGoldCustomer(customer)) {
            return new String[]{"jms:accounting", "jms:production"};
        } else {
            return new String[]{"jms:accounting"};
        }
    }

    private boolean isGoldCustomer(String customer) {
        return customer.equals("honda");
    }
}
