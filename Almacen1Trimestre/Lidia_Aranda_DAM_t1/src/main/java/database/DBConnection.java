package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            createConnection();
        }
        return connection;

    }

    private static void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String uri = "jdbc:mysql://127.0.0.1:3306/almacen";
            connection = DriverManager.getConnection(uri, "root", "");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar Driver");
        } catch (SQLException ex) {
            System.out.println("Error en ejecución.");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

    }

    public static void closeConnection() {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            System.out.println("Error en el cerrado de la base de datos.");
        }

    }

}

