import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import entities.Employee;

public class MainApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			Connection conn = GetConnection();

			int action = 0;
			do {
				System.out.println("оберіть операцію");
				System.out.println("0. вихід");
				System.out.println("1. показати усіx працівників");
				System.out.println("2. додати працівника");
				System.out.println("3. видалити працівника");
				Scanner in = new Scanner(System.in);
				action = in.nextInt();
				switch (action) {

				case 1:
					SelectAll(conn);
					break;

				case 2:
					Employee employee = new Employee();
					System.out.println("введіть ім'я");
					employee.setFirstname(in.next());
					System.out.println("введіть прізвище");
					employee.setLastname(in.next());
					System.out.println("введіть дату народження");
					employee.setBirthdate(in.next());
					System.out.println("введіть зарплату");
					employee.setSalary(in.next());
					System.out.println("введіть відділ");
					employee.setSection(in.next());
					InsertEmployee(conn, employee);
					break;

				case 3:
					System.out.println("введіть id");
					int id = in.nextInt();
					DeleteEmployee(conn, id);
					break;

				default:
					break;
				}
			} while (action != 0);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("проблеми" + e.getMessage());
		}
	}

	private static Connection GetConnection() throws SQLException, ClassNotFoundException {
		String hostName = "localhost";
		String dbName = "marketdb";
		String userName = "admin";
		String password = "123456";

		Class.forName("org.mariadb.jdbc.Driver");

		Connection conn = DriverManager.getConnection("jdbc:mariadb://" + hostName + ":3306/" + dbName, userName,
				password);
		return conn;
	}
	
	private static void InsertEmployee(Connection conn, Employee employee) throws SQLException {
		String query = "INSERT INTO tbl_employees(firstname, lastname, birthdate, salary, section) VALUES (?,?,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(query);
//		Date date = new Date();
		pst.setString(1, employee.getFirstname());
		pst.setString(2, employee.getLastname());
		pst.setString(3, employee.getBirthdate());
		pst.setDouble(4, Double.parseDouble(employee.getSalary()));
		pst.setString(5, employee.getSection());
		pst.executeUpdate();
	}

	private static void SelectAll(Connection conn) throws SQLException {
		PreparedStatement pst = conn.prepareStatement("SELECT id, firstname, lastname, section FROM tbl_employees");
		ResultSet rs = pst.executeQuery();

		while (rs.next()) {
			System.out.print(rs.getInt(1));
			System.out.print(": ");
			System.out.println(rs.getString(2) + ' ' + rs.getString(3) + ' ' + rs.getString(4));
		}
	}

	private static void DeleteEmployee(Connection conn, int id) throws SQLException {
		String query = "DELETE FROM tbl_employees WHERE id = ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setInt(1, id);
		pst.executeUpdate();
	}

}
