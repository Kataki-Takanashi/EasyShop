<p align="center">
  <img src="src/main/resources/static/images/logo.png" alt="EasyShop Logo" width="300"/>
</p>

<h1 align="center">EasyShop</h1>

<p align="center">
    <img src="https://img.shields.io/badge/YearUp%20Java%20Capstone%203-blueviolet?style=for-the-badge" alt="YearUp Java Capstone 3" />
</p>

---

### Project Overview

EasyShop is a modern e-commerce application that makes shopping easy!

Key Features:
- Browse through our **extensive product catalog** with items from electronics to fashion to Home and Kitchen
- Smart filtering by `category`, `price range`, and `color` 
- Smooth **shopping cart** experience with animated front end
- Secure user authentication and profile management
- Seamless checkout process with shipping details
- Beautiful product imagery and detailed descriptions


---

### Technology Stack

- **Frontend**: HTML5, CSS3, JavaScript
- **Backend**: Java Spring Boot
- **Database**: MySQL
- **Security**: Spring Security
- **Build Tool**: Maven

---

### One Interesting Piece of Code


```java
public class ShoppingCart
{
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();

    public Map<Integer, ShoppingCartItem> getItems()
    {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items)
    {
        this.items = items;
    }

    public boolean contains(int productId)
    {
        return items.containsKey(productId);
    }

    public void add(ShoppingCartItem item)
    {
        items.put(item.getProductId(), item);
    }

    public BigDecimal getTotal()
    {
        BigDecimal total = items.values()
                                .stream()
                                .map(i -> i.getLineTotal())
                                .reduce(BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));

        return total;
    }
}
```

This code showcases several features of the shopping cart:
1. Uses a `HashMap` for efficient item storage and retrieval
2. Implements stream operations for calculating cart totals
3. Handles decimal precision properly with `BigDecimal`
4. Provides clean and intuitive methods for cart management

---

### Future Features


1. **User Reviews and Ratings** ⭐

2. **Wishlist Functionality** 💝
   - Save your favorite items for later

3. **Enhanced Order History** 📦
   - Track all your past purchases

4. **Smart Product Search** 🔍
   - Find exactly what you're looking for
   - Search by product name or description

5. **Promotions and Discounts** 🏷️
   - Apply promotional codes at checkout

6. **Advanced Payment Integration** 💳
   - Multiple payment options
   - Secure transaction processing
   - Save payment methods

7. **Mobile-Friendly Design** 📱
   - Shop on any device
   - Responsive interface
   - Touch-optimized navigation

8. **Enhanced Product Categories** 📑
   - Better organized shopping
   - Intuitive category navigation
   - Smart product recommendations
   
9. **Checkout Process** 🛒
    - Secure and seamless checkout
    - Reset cart after checkout

See our [future features file](FUTURE.md) for more details.
