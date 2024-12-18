package org.yearup.data.mysql;

import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MySqlOrderDao implements OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public MySqlOrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order create(Order order) {
        String sql = "INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        // Assuming you have the necessary fields in the Order model
        jdbcTemplate.update(sql, order.getUserId(), order.getDate(), order.getAddress(), order.getCity(), order.getState(), order.getZip(), order.getShippingAmount());
        
        // You might want to retrieve the generated order ID and set it in the order object
        // order.setId(generatedId);
        
        return order; // Return the created order
    }
} 