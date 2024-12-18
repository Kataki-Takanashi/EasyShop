package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal; // Helps with auth stuff
import java.util.HashMap;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping
    public Map<String, Object> getCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            ShoppingCart cart = shoppingCartDao.getByUserId(user.getId());

            // Return correct formatted JSON
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> items = new HashMap<>();

            for (Map.Entry<Integer, ShoppingCartItem> entry : cart.getItems().entrySet()) {
                ShoppingCartItem item = entry.getValue();
                Map<String, Object> itemDetails = new HashMap<>();
                itemDetails.put("product", item.getProduct());
                itemDetails.put("quantity", item.getQuantity());
                itemDetails.put("discountPercent", item.getDiscountPercent());
                itemDetails.put("lineTotal", item.getLineTotal());

                items.put(String.valueOf(entry.getKey()), itemDetails);
            }

            response.put("items", items);
            response.put("total", cart.getTotal());

            return response; // Return the formatted response
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting cart");
        }
    }

    @PostMapping("products/{productId}")
    public ShoppingCart addToCart(Principal principal, @PathVariable int productId) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            Product product = productDao.getById(productId);
            
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
            }

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(1);

            return shoppingCartDao.addItem(user.getId(), item);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding to cart");
        }
    }

    @PutMapping("products/{productId}")
    public ShoppingCart updateCartItem(Principal principal, 
                                     @PathVariable int productId, 
                                     @RequestBody ShoppingCartItem item) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            return shoppingCartDao.updateItem(user.getId(), productId, item);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating cart");
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            shoppingCartDao.clearCart(user.getId());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error clearing cart");
        }
    }
}
