package repositories;

import database.DBConnection;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRepository {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private Connection connection;

    public boolean isEmployeeExists(String id) {
        connection = DBConnection.getConnection();
        String query = "SELECT COUNT(*) FROM employee WHERE id = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Fallo en la sentencia SQL" + e.getMessage());
        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("Error en el cerrado de la consulta.");
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println("Error en el cerrado de la querye.");
                }
            }
            DBConnection.closeConnection();
            connection = null;
        }
        return false;
    }

    public void addEmployee(Employee employee) {
        if (!isEmployeeExists(employee.getId())) {
            String query = "INSERT INTO employee (id, name, surname, email) VALUES (?,?,?,?)";
            connection = DBConnection.getConnection();
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, employee.getId());
                preparedStatement.setString(2, employee.getName());
                preparedStatement.setString(3, employee.getSurname());
                preparedStatement.setString(4, employee.getEmail());
                preparedStatement.execute();
                System.out.println("Empleado insertado: " + employee.getName());

            } catch (SQLException e) {
                System.out.println("Error en la query para insertar empleado.");
                System.out.println(e.getMessage());
                e.printStackTrace();
            } finally {
                DBConnection.closeConnection();
                connection = null;
            }
        } else {
            System.out.println("El empleado con ID " + employee.getId() + " ya existe.");
        }
    }

    public void deleteEmployee(String id) {
        String query = "DELETE FROM employee WHERE id = ?";
        connection = DBConnection.getConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Error en la query para eliminar empleado.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
            connection = null;
        }
    }

    public void showEmployee() {
        System.out.println("\n Mostrando todos los empleados: ");
        String query = "SELECT * FROM employee";

        connection = DBConnection.getConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery(query);
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String email = resultSet.getString("email");
                System.out.printf("\nID: %s" +
                        "\nNombre: %s" +
                        "\nApellido: %s" +
                        "\nEmail: %s\n", id, name, surname, email);
            }
        } catch (SQLException e) {
            System.out.println("Error en la query para mostrar empleados.");
        }
    }
}
