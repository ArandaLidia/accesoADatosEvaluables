import database.DBConnection;
import model.Employee;
import model.Order;
import repositories.EmployeeRepository;
import repositories.OrderRepository;
import repositories.ProductFavRepository;
import repositories.ProductRepository;

import java.sql.Connection;
import java.util.Scanner;

public class Entrada {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        int option;
        Connection connection = DBConnection.getConnection();
        EmployeeRepository employeeRepository = new EmployeeRepository();
        Employee employee;
        ProductRepository productRepository = new ProductRepository();
        ProductFavRepository productFavRepository = new ProductFavRepository();
        OrderRepository orderRepository = new OrderRepository();
        Order order = new Order();
        while (!exit) {
            System.out.println("Menú:" +
                    "\n 1-Agregar empleados." +
                    "\n 2-Mostrar empleados agregados." +
                    "\n 3-Agregar todos los productos." +
                    "\n 4-Mostrar todos los productos." +
                    "\n 5-Mostrar los productos con precio menor de 600€." +
                    "\n 6-Hacer 4 pedidos." +
                    "\n 7-Mostrar los pedidos." +
                    "\n 8.Agregar los productos de mas de 1000€ a Favoritos." +
                    "\n 9- Mostrar los productos favoritos." +
                    "\n 10-Salir. " +

                    "\n¿Qué opción quieres?");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    employeeRepository.addEmployee(new Employee("12345678B", "Marta", "Garrido Gutierrez", "marta@gmail.com"));
                    employeeRepository.addEmployee(new Employee("45789128N", "Juan", "Garcia Ramirez", "juan@gmail.com"));
                    employeeRepository.addEmployee(new Employee("98732123P", "Francisco", "Sánchez López", "francisco@gmail.com"));
                    employeeRepository.addEmployee(new Employee("98721321B", "Sara", "Visiedo Gómez", "Sara@gmail.com"));
                    System.out.println("_______________________________________________________________________________________________");
                    break;
                case 2:
                    employeeRepository.showEmployee();
                    System.out.println("________________________________________________________________________________________________");
                    break;
                case 3:
                    String url = "https://dummyjson.com/products";
                    productRepository.addProductJson(url);
                    break;
                case 4:
                    productRepository.showProduct();
                    System.out.println("\n");
                    break;
                case 5:
                    productRepository.showProduct600();
                    break;
                case 6:
                    orderRepository.addOrder(14);
                    orderRepository.addOrder(20);
                    orderRepository.addOrder(29);
                    orderRepository.addOrder(5);
                    break;
                case 7:
                    orderRepository.showOrder();
                    break;
                case 8:
                    productFavRepository.insterFav(1000);
                    break;
                case 9:
                    productFavRepository.showProduct();
                    break;
                case 10:
                    System.out.println("Saliendo del programa...");
                    exit = true;
                    break;
                default:

            }
        }
    }
}
