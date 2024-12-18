package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.UserDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.OrderLineItemDao;
import org.yearup.data.ProfileDao;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.models.Profile;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.OrderLineItem;
import org.yearup.models.OrderResponse;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    @Qualifier("mySqlUserDao")
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderResponse> createOrder(Principal principal) {
        User user = userDao.getByUserName(principal.getName());
        
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        int currentUserId = user.getId();
        ShoppingCart cart = shoppingCartDao.getByUserId(currentUserId);
        
        Profile profile = profileDao.getByUserId(currentUserId);

        Order order = new Order();
        order.setUserId(currentUserId);
        order.setDate(Timestamp.valueOf(LocalDateTime.now()));
        
        if (profile != null) {
            order.setFirstName(profile.getFirstName());
            order.setLastName(profile.getLastName());
            order.setEmail(profile.getEmail());
            order.setPhoneNumber(profile.getPhoneNumber());
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile not found");
        }

        BigDecimal shippingAmount = calculateShippingAmount(cart);
        order.setShippingAmount(shippingAmount);

        orderDao.create(order);

        List<OrderLineItem> lineItems = new ArrayList<>();
        for (ShoppingCartItem item : cart.getItems().values()) {
            OrderLineItem orderLineItem = new OrderLineItem(order.getId(), item.getProductId(), item.getQuantity(), item.getLineTotal(), BigDecimal.ZERO);
            orderLineItemDao.create(orderLineItem);
            lineItems.add(orderLineItem);
        }

        shoppingCartDao.clearCart(currentUserId); // Clearing cart after order is made.

        OrderResponse orderResponse = new OrderResponse(order, lineItems);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
    // Just sets the shipping cost to 5.99, in the future this could be calculated on the fly based of distance idk
    private BigDecimal calculateShippingAmount(ShoppingCart cart) {
        return BigDecimal.valueOf(5.99);
    }
} 