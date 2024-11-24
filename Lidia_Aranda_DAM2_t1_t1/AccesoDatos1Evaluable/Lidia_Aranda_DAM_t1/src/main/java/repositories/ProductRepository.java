package repositories;

import database.DBConnection;
import model.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRepository {

    BufferedReader bufferedReader = null;
    Product newProduct;
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public void addProductJson(String url) {

        try {
            URL url1 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String read = bufferedReader.readLine();
            JSONObject aswer = new JSONObject(read);
            JSONArray products = aswer.getJSONArray("products");

            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                int id = product.getInt("id");
                String title = product.getString("title");
                String description = product.getString("description");
                int stock = product.getInt("stock");
                float price = product.getFloat("price");
                newProduct = new Product(id, title, description, stock, price);
                createProduct(id, title, description, stock, price);
                System.out.println("Productos insertados correctamente.");
            }

        } catch (MalformedURLException e) {
            System.out.println("Error en URL" + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de conexión" + e.getMessage());
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el buffer: " + e.getMessage());
            }
        }


    }

    public void createProduct(int id, String title, String description, int stock, float price) {
        String query = "INSERT INTO product (id, title, description, stock, price) VALUES (?, ?, ?, ?, ?)";
        connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, stock);
            preparedStatement.setFloat(5, price);
            preparedStatement.executeUpdate();

            System.out.println("Producto guardado: " + title);
        } catch (SQLException e) {
            System.out.println("Error al guardar producto: " + e.getMessage());
            {
            }
        }
    }

    public boolean isProductExist(int id) {
        String query = "SELECT COUNT(*) FROM product WHERE id= ?";
        connection = DBConnection.getConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error en la query de id duplicado " + e.getMessage());
        }
        return false;
    }

    public void showProduct() {
        String query = "SELECT * FROM product";
        System.out.println("\nMostrando todos los productos: ");
        connection = DBConnection.getConnection();

        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                int stock = resultSet.getInt("stock");
                float price = resultSet.getFloat("price");

                System.out.printf(" ID: %s" +
                        "\n Nombre: %s" +
                        "\n Descripción: %s" +
                        "\n Stock: %s" +
                        "\n Precio: %s", id, title, description, stock, price);
                System.out.println("\n__________________________________________________________________________");
            }
        } catch (SQLException e) {
            System.out.println("Error en la query " + e.getMessage());
        } finally {
            DBConnection.closeConnection();
        }

    }

    public void showProduct600() {
        String query = " SELECT id, title, description, stock, price FROM product WHERE price < 600";
        connection = DBConnection.getConnection();

        try {

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getNString("title");
                String description = resultSet.getString("description");
                int stock = resultSet.getInt("stock");
                float price = resultSet.getFloat("price");
                System.out.printf("ID: %s" +
                        "\n Nombre: %s" +
                        "\n Descripción: %s" +
                        "\n Cantidad: %s" +
                        "\n Precio: %s\n", id, name, description, stock, price);
                System.out.println("_______________________________________________________________________________");
            }
        } catch (SQLException e) {
            System.out.println("Error en la query devolver productos menor de 600€" + e.getMessage());
        }

    }
}

