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
            System.out.println("Error en SQL: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        return false;
    }


    public void insterFav(float price) {
        String querySelect = "SELECT id FROM product WHERE price > ?";
        String queryInsert = "INSERT INTO productfav (idProduct) VALUES (?)";

        Connection connection = DBConnection.getConnection();
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;
        ResultSet resultSet = null;

        try {

            selectStatement = connection.prepareStatement(querySelect);
            selectStatement.setFloat(1, price);
            resultSet = selectStatement.executeQuery();
            boolean found = false;


            while (resultSet.next()) {
                found = true;
                int idProduct = resultSet.getInt("id");
                System.out.println("Producto encontrado con id: " + idProduct);

                if (!isProductExists(idProduct)) {
                    insertStatement = connection.prepareStatement(queryInsert);
                    insertStatement.setInt(1, idProduct);
                    insertStatement.execute();
                    System.out.println("Producto agregado a favoritos con id: " + idProduct);
                } else {
                    System.out.println("El producto con id: " + idProduct + " ya está en favoritos.");
                }
            }

            if (!found) {
                System.out.println("No hay productos con precio mayor de " + price + "€.");
            }
        } catch (SQLException e) {
            System.out.println("Error en SQL insertar Favoritos: " + e.getMessage());
        } finally {
            // Cerrar recursos
            try {
                if (resultSet != null) resultSet.close();
                if (selectStatement != null) selectStatement.close();
                if (insertStatement != null) insertStatement.close();
                DBConnection.closeConnection();
            } catch (SQLException e) {
                System.out.println("Error al cerrar recursos: " + e.getMessage());
            }
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
                int favId = result.getInt("idFav");
                int productId = result.getInt("productId");
                String title = result.getString("title");
                String description = result.getString("description");
                int stock = result.getInt("stock");
                float price = result.getFloat("price");


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
