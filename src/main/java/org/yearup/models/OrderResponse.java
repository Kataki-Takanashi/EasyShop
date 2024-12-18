package org.yearup.models;

import java.util.List;

public class OrderResponse {
    private Order order;
    private List<OrderLineItem> lineItems;

    public OrderResponse(Order order, List<OrderLineItem> lineItems) {
        this.order = order;
        this.lineItems = lineItems;
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }
} 