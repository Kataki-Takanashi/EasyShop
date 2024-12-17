package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    private ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?"; // Note2Self: SELECT * is not maintainable
        ShoppingCart cart = new ShoppingCart();

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet row = statement.executeQuery();

            while (row.next()) {
                int productId = row.getInt("product_id");
                int quantity = row.getInt("quantity");

                // grab the product
                Product product = productDao.getById(productId);
                
                // create shopping cart item
                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(quantity);

                // add to cart
                cart.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cart;
    }

    @Override
    public ShoppingCart addItem(int userId, ShoppingCartItem item) {
        // check if item already in cart
        String checkSql = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE shopping_cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";


        try (Connection connection = getConnection()) {
            // check if exists
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, item.getProductId());
            ResultSet checkResult = checkStmt.executeQuery();

            if (checkResult.next()) { // only applies to the first item as there should only be one
                // update it
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                updateStmt.setInt(1, item.getQuantity());
                updateStmt.setInt(2, userId);
                updateStmt.setInt(3, item.getProductId());
                updateStmt.executeUpdate();
            } else {
                // insert new item
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, item.getProductId());
                insertStmt.setInt(3, item.getQuantity());
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getByUserId(userId);
    }




    @Override
    public ShoppingCart updateItem(int userId, int productId, ShoppingCartItem item) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, item.getQuantity());
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getByUserId(userId);
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
} 