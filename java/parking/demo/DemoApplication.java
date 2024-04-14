/*
package parking.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
 */
package parking.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(DataSource dataSource) {
		return args -> {
			System.out.println("Database connection status: " + testDatabaseConnection(dataSource));
			// Add your application logic here using the dataSource
			// For example, you can use Spring Data JPA repositories to interact with the database
		};
	}

	private boolean testDatabaseConnection(DataSource dataSource) {
		try (Connection connection = dataSource.getConnection()) {
			DatabaseMetaData metaData = connection.getMetaData();

			System.out.println("Connected to database:");
			System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
			System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());
			System.out.println("Driver Name: " + metaData.getDriverName());
			System.out.println("Driver Version: " + metaData.getDriverVersion());

			return connection.isValid(5); // Check if the connection is valid within 5 seconds
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error testing database connection: " + e.getMessage());
			return false;
		}
	}

//	private boolean testDatabaseConnection(DataSource dataSource) {
//		try (Connection connection = dataSource.getConnection()) {
//			return connection.isValid(5); // Check if the connection is valid within 5 seconds
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.err.println("Error testing database connection: " + e.getMessage());
//			return false;
//		}
//	}
}
//package parking.demo;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//@SpringBootApplication
//@ComponentScan(basePackages = "parking.demo")
//@EntityScan(basePackages = "parking.demo")
//public class DemoApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);
//	}
//
//	@Bean
//	public CommandLineRunner commandLineRunner(DataSource dataSource, UserRepository userRepository) {
//		return args -> {
//			System.out.println("Database connection status: " + testDatabaseConnection(dataSource));
//
//			// Add your application logic here using the userRepository
//			// For example, you can save and retrieve users from the database
//			saveAndRetrieveUser(userRepository);
//		};
//	}
//
//	private boolean testDatabaseConnection(DataSource dataSource) {
//		try (Connection connection = dataSource.getConnection()) {
//			return connection.isValid(5); // Check if the connection is valid within 5 seconds
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.err.println("Error testing database connection: " + e.getMessage());
//			return false;
//		}
//	}
//
//	private void saveAndRetrieveUser(UserRepository userRepository) {
//		// Save a user
//		User user = new User("john.doe@example.com", "password123");
//		userRepository.save(user);
//
//		// Retrieve all users
//		List<User> users = userRepository.findAll();
//		System.out.println("Users in the database: " + users);
//	}
//}
//
//@Entity
//class User {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private String email;
//	private String password;
//
//	public User() {
//		// Default constructor required by JPA
//	}
//
//	public User(String email, String password) {
//		this.email = email;
//		this.password = password;
//	}
//
//	// Getters and setters
//}
//
//interface UserRepository extends JpaRepository<User, Long> {
//	// Custom query methods can be added here if needed
//}
//
