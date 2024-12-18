package org.yearup.data;

import org.yearup.models.Order;

public interface OrderDao {
    Order create(Order order); // Method to create a new order
    // You can add more methods as needed, like retrieving orders, etc.
} 