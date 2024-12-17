package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCart addItem(int userId, ShoppingCartItem item);
    ShoppingCart updateItem(int userId, int productId, ShoppingCartItem item);
    void clearCart(int userId);
}
