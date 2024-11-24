package repositories;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRepository {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void addOrder(int idProduct) {
        connection = DBConnection.getConnection();


        String queryProduct = "SELECT description, price FROM product WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(queryProduct);
            preparedStatement.setInt(1, idProduct);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String description = resultSet.getString("description");
                float price = resultSet.getFloat("price");


                float totalPrice = price;

                // Paso 4: Insertar el pedido en la tabla `order`
                String queryOrder = "INSERT INTO `order` (idproduct, description, totalprice) VALUES (?, ?, ?)";
                preparedStatement = connection.prepareStatement(queryOrder);
                preparedStatement.setInt(1, idProduct);
                preparedStatement.setString(2, description);
                preparedStatement.setFloat(3, totalPrice);
                preparedStatement.execute();

                System.out.println("Pedido insertado correctamente para el producto con ID: " + idProduct);
            } else {
                System.out.println("El producto con ID " + idProduct + " no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error en la query para insertar el pedido.");
            System.out.println(e.getMessage());
        } finally {
            DBConnection.closeConnection();
        }
    }

    public void showOrder() {
        System.out.println("\n Mostrando pedidos: ");
        String query = " SELECT * FROM `order`";
        connection = DBConnection.getConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idProduct = resultSet.getInt("idproduct");
                String description = resultSet.getString("description");
                float totalprice = resultSet.getFloat("totalprice");
                System.out.printf("\n ID: %s" +
                        "\n ID producto: %s" +
                        "\n Descripción: %s" +
                        "\n Precio total: %s\n" +
                        " ___________________________________________________________________________________", id, idProduct, description, totalprice);
            }
        } catch (SQLException e) {
            System.out.println("Error en la query de mostrar pedidos");
        } finally {
            DBConnection.closeConnection();
        }
    }

}
