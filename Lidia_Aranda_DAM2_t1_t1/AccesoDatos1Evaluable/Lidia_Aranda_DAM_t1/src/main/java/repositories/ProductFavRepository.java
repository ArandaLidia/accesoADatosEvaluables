package repositories;

import database.DBConnection;

import java.sql.*;

public class ProductFavRepository {

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public boolean isProductExists(int id) {
        String query = "SELECT COUNT(*) FROM productfav WHERE idProduct = ?";


        Connection connection = DBConnection.getConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error en SQL" + e.getMessage());
        }
        return false;
    }

    public void insterFav(float price) {
        String query = "SELECT id FROM product WHERE price > ?";

        Connection connection = DBConnection.getConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFloat(1, price);
            resultSet = preparedStatement.executeQuery();
            boolean found = false;

            while (resultSet.next()) {
                found = true;
                int idProduct = resultSet.getInt("id");
                System.out.println("Producto encontrado con id: " + idProduct);
                if (!isProductExists(idProduct)) {
                    String queryInsert = "INSERT INTO productfav (idProduct) VALUES (?)";
                    preparedStatement = connection.prepareStatement(queryInsert);
                    preparedStatement.setInt(1, idProduct);
                    preparedStatement.execute();
                    System.out.println("Producto encontrado con id: " + idProduct);
                } else {
                    System.out.println("El producto con id: " + idProduct + " ya está en favoritos.");
                }
            }
            if (!found) {
                System.out.println("No hay productos con precio mayor de 1000€. ");
            }
        } catch (SQLException e) {
            System.out.println("Error en SQL insertar Favoritos" + e.getMessage());
        } finally {
            DBConnection.closeConnection();
        }
    }

    public void showProduct() {

        String query = "SELECT productfav.id AS idFav, product.id AS productId, product.title, product.description, product.stock, product.price " +
                "FROM productfav INNER JOIN product ON productfav.idProduct = product.id";

        System.out.println("\nMostrando todos los productos favoritos: ");

        Connection connection = DBConnection.getConnection();

        try {

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                int favId = result.getInt("idFav"); // id de favoritos
                int productId = result.getInt("productId"); // id del producto
                String title = result.getString("title"); // título del producto
                String description = result.getString("description"); // descripción del producto
                int stock = result.getInt("stock"); // cantidad en stock
                float price = result.getFloat("price"); // precio del producto


                System.out.printf("\n\nID Favorito: %d" +
                                "\nID Producto: %d" +
                                "\nNombre: %s" +
                                "\nDescripción: %s" +
                                "\nStock: %d" +
                                "\nPrecio: %.2f" +
                                "\n",
                        favId, productId, title, description, stock, price);
            }
            System.out.println("\n___________________________________________________________");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }
}
