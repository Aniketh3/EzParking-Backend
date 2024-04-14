package demo;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/login1", new LoginHandler1());
        server.createContext("/api/login2", new LoginHandler2());
        server.createContext("/api/signup", new SignupHandler());
        server.createContext("/api/bookings1", new BookingsHandler1());
        server.createContext("/api/bookings/all1", new AllBookedSlotsHandler1());
        server.createContext("/api/bookings/remove1", new RemoveBookedSlotsHandler1());
        server.createContext("/api/bookings2", new BookingsHandler2());
        server.createContext("/api/bookings/all2", new AllBookedSlotsHandler2());
        server.createContext("/api/bookings/remove2", new RemoveBookedSlotsHandler2());
        server.createContext("/api/bookings3", new BookingsHandler3());
        server.createContext("/api/bookings/all3", new AllBookedSlotsHandler3());
        server.createContext("/api/bookings/remove3", new RemoveBookedSlotsHandler3());
        server.createContext("/api/bookings4", new BookingsHandler4());
        server.createContext("/api/bookings/all4", new AllBookedSlotsHandler4());
        server.createContext("/api/bookings/remove4", new RemoveBookedSlotsHandler4());
        server.createContext("/api/bookings5", new BookingsHandler5());
        server.createContext("/api/bookings/all5", new AllBookedSlotsHandler5());
        server.createContext("/api/bookings/remove5", new RemoveBookedSlotsHandler5());
        server.createContext("/api/bookings6", new BookingsHandler6());
        server.createContext("/api/bookings/all6", new AllBookedSlotsHandler6());
        server.createContext("/api/bookings/remove6", new RemoveBookedSlotsHandler6());
        server.createContext("/api/bookings7", new BookingsHandler7());
        server.createContext("/api/bookings/all7", new AllBookedSlotsHandler7());
        server.createContext("/api/bookings/remove7", new RemoveBookedSlotsHandler7());
        server.createContext("/api/bookings8", new BookingsHandler8());
        server.createContext("/api/bookings/all8", new AllBookedSlotsHandler8());
        server.createContext("/api/bookings/remove8", new RemoveBookedSlotsHandler8());
        server.createContext("/api/bookings9", new BookingsHandler9());
        server.createContext("/api/bookings/all9", new AllBookedSlotsHandler9());
        server.createContext("/api/bookings/remove9", new RemoveBookedSlotsHandler9());
        server.createContext("/api/bookings10", new BookingsHandler10());
        server.createContext("/api/bookings/all10", new AllBookedSlotsHandler10());
        server.createContext("/api/bookings/remove10", new RemoveBookedSlotsHandler10());
        server.createContext("/api/bookings11", new BookingsHandler11());
        server.createContext("/api/bookings/all11", new AllBookedSlotsHandler11());
        server.createContext("/api/bookings/remove11", new RemoveBookedSlotsHandler11());
        server.createContext("/api/bookings/remove12", new RemoveBookedSlotsHandler12());
        server.createContext("/api/user/vehicle/history", new UserVehicleHistoryHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on port 8080");
    }

    static class AllBookedSlotsHandler1 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots1();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all1" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots1() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings1";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler1 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot1(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings1" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean removeBookedSlot1(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings1 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler1 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData1(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            }
            else
            {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings1" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData1(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings1 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings1 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }

    }
    static class AllBookedSlotsHandler2 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots2();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all2" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots2() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings2";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    }
                    catch (JSONException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler2 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot2(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings2" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot2(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings2 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler2 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData2(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings2" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData2(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings2 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings2 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }
    static class AllBookedSlotsHandler3 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots3();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all3" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots3() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings3";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler3 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot3(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings3" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot3(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings3 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler3 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData3(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings3" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData3(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings3 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings3 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }

    static class AllBookedSlotsHandler4 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);



            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots4();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all4" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots4() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings4";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }
    static class RemoveBookedSlotsHandler4 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot4(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings4" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot4(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings4 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler4 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData4(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings4" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData4(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings4 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings4 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }
    static class AllBookedSlotsHandler5 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots5();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all5" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots5() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings5";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler5 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot5(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings5" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot5(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings5 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler5 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData5(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings5" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData5(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings5 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings5 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }
    static class AllBookedSlotsHandler6 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots6();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all6" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots6() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings6";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler6 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot6(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings6" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot6(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings6 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }
    static class BookingsHandler6 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData6(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings6" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData6(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings6 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings6 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }
    static class AllBookedSlotsHandler7 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots7();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all7" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots7() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings7";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler7 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot7(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings7" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot7(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings7 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler7 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData7(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings7" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData7(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings7 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings7 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }


    static class AllBookedSlotsHandler8 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);



            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots8();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all7" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots8() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings8";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler8 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot8(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings8" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot8(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings8 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler8 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData8(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings8" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData8(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings8 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings8 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }

    static class AllBookedSlotsHandler9 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);



            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots9();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all7" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots9() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings9";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler9 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot9(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings9" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot9(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings9 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler9 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData9(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings9" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData9(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings9 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings9 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }


    static class AllBookedSlotsHandler10 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);


            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots10();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all10" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots10() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings10";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler10 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot10(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings10" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot10(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings10 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class BookingsHandler10 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData10(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings10" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData10(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings10 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings10 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }



    }

    static class AllBookedSlotsHandler11 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);


            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    JSONArray bookedSlots = getAllBookedSlots11();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";

                    sendResponse(exchange, 200, bookedSlots.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch booked slots" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings/all11" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static JSONArray getAllBookedSlots11() {
            JSONArray bookedSlotsArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM bookings11";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject bookedSlot = new JSONObject();
                            bookedSlot.put("id", resultSet.getString("id"));
                            bookedSlot.put("vehicle", resultSet.getString("vehicle"));
                            bookedSlot.put("email", resultSet.getString("email"));
                            bookedSlotsArray.put(bookedSlot);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching booked slots: " + e.getMessage());
            }

            return bookedSlotsArray;
        }
    }

    static class RemoveBookedSlotsHandler11 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot11(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings11" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot11(String email) {
            try (Connection connection = Database.getConnection()) {
                String query = "DELETE FROM bookings11 WHERE email = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    int rowsAffected = preparedStatement.executeUpdate();
                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    static class RemoveBookedSlotsHandler12 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");


                    boolean success = removeBookedSlot12(email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings12" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
        private static boolean removeBookedSlot12(String email) {
            try (Connection connection = Database.getConnection()) {

                String[] tables = {"bookings1", "bookings2", "bookings3", "bookings4", "bookings5",
                        "bookings6", "bookings7", "bookings8", "bookings9", "bookings10", "bookings11"};

                for (String table : tables) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE " + table)) {

                        preparedStatement.executeUpdate();
                    }
                }
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }


    }


    static class BookingsHandler11 implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();

                try {
                    JSONObject json = new JSONObject(requestBody);
                    String vehicle = json.getString("vehicle");
                    String email = json.getString("email");
                    String selectedSpot = json.getString("selectedSpot");


                    boolean success = storeBookingData11(selectedSpot,vehicle, email);

                    if (success) {

                        JSONObject responseJson = new JSONObject();
                        String jsonResponse = "{\"message\": \"" + "Booking Successful" + "\"}";
                        sendResponse(exchange, 200, jsonResponse);
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Failed to store booking data" + "\"}";
                        sendResponse(exchange, 500, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/bookings11" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private static boolean storeBookingData11(String id, String vehicle, String email) {
            try (Connection connection = Database.getConnection()) {

                String checkBookingQuery = "SELECT COUNT(*) FROM bookings11 WHERE email = ?";
                try (PreparedStatement preparedStatementCheckBooking = connection.prepareStatement(checkBookingQuery)) {
                    preparedStatementCheckBooking.setString(1, email);
                    ResultSet resultSet = preparedStatementCheckBooking.executeQuery();
                    resultSet.next();
                    int existingBookings = resultSet.getInt(1);

                    if (existingBookings > 0) {

                        return false;
                    }
                }


                String bookingsQuery = "INSERT INTO bookings11 (id, vehicle, email) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatementBookings = connection.prepareStatement(bookingsQuery)) {
                    preparedStatementBookings.setString(1, id);
                    preparedStatementBookings.setString(2, vehicle);
                    preparedStatementBookings.setString(3, email);
                    int affectedRowsBookings = preparedStatementBookings.executeUpdate();


                    String userVehiclesQuery = "INSERT INTO uservehicles (id, vehicle, email, booking_date, booking_time) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatementUserVehicles = connection.prepareStatement(userVehiclesQuery)) {
                        preparedStatementUserVehicles.setString(1, id);
                        preparedStatementUserVehicles.setString(2, vehicle);
                        preparedStatementUserVehicles.setString(3, email);
                        preparedStatementUserVehicles.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        preparedStatementUserVehicles.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.now()));

                        int affectedRowsUserVehicles = preparedStatementUserVehicles.executeUpdate();

                        return affectedRowsBookings > 0 && affectedRowsUserVehicles > 0;
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error registering user: " + e.getMessage());
            }


            return false;
        }
    }

    public static class UserVehicleHistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("GET".equals(exchange.getRequestMethod())) {
                try {

                    String email = getEmailFromQueryParams(exchange.getRequestURI().getQuery());
                    if (email != null) {

                        JSONArray userVehicleHistory = getUserVehicleHistory(email);
                        if (userVehicleHistory.length() > 0) {
                            sendResponse(exchange, 200, userVehicleHistory.toString());
                        } else {
                            String jsonResponse = "{\"message\": \"" + "User has no vehicle history" + "\"}";
                            sendResponse(exchange, 404, jsonResponse);
                        }
                    } else {
                        String jsonResponse = "{\"message\": \"" + "Email parameter is missing" + "\"}";
                        sendResponse(exchange, 400, jsonResponse);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    String jsonResponse = "{\"message\": \"" + "Failed to fetch user vehicle history" + "\"}";
                    sendResponse(exchange, 500, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/user/vehicle/history" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }

        private String getEmailFromQueryParams(String queryParams) {
            if (queryParams != null && !queryParams.isEmpty()) {
                String[] params = queryParams.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("email")) {
                        return keyValue[1];
                    }
                }
            }
            return null;
        }

        private JSONArray getUserVehicleHistory(String email) {
            JSONArray userVehicleHistoryArray = new JSONArray();

            try (Connection connection = Database.getConnection()) {
                String query = "SELECT * FROM uservehicles WHERE email=? ORDER BY booking_date DESC, booking_time DESC";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, email);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            JSONObject userVehicle = new JSONObject();
                            userVehicle.put("id", resultSet.getString("id"));
                            userVehicle.put("vehicle", resultSet.getString("vehicle"));
                            userVehicle.put("email", resultSet.getString("email"));
                            userVehicle.put("date", resultSet.getString("booking_date"));
                            userVehicle.put("time", resultSet.getString("booking_time"));
                            userVehicleHistoryArray.put(userVehicle);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error fetching user vehicle history: " + e.getMessage());
            }

            return userVehicleHistoryArray;
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            handleCORS(t);

            if ("POST".equals(t.getRequestMethod())) {

                String requestBody = new Scanner(t.getRequestBody()).useDelimiter("\\A").next();


                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");
                    String pass = json.getString("password");


                    boolean isValid = validateLogin(email, pass);


                    String response = isValid ? "Authenticated" : "Not Authenticated";
                    String jsonResponse = "{\"message\": \"" + response + "\"}";

                    sendResponse(t, 200, jsonResponse);
                } catch (Exception e) {

                    e.printStackTrace();

                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(t, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/login" + "\"}";
                sendResponse(t, 500, jsonResponse);
            }
        }
    }
    private static boolean validateLogin(String email, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/parking_db", "root", "root123")) {
            String query = "SELECT * FROM USERS WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean result = resultSet.next();
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error executing the login query: " + e.getMessage());
        }


        return false;
    }
    static class LoginHandler1 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            handleCORS(t);

            if ("POST".equals(t.getRequestMethod())) {

                String requestBody = new Scanner(t.getRequestBody()).useDelimiter("\\A").next();


                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");
                    String pass = json.getString("password");


                    boolean isValid = validateLogin1(email, pass);


                    String response = isValid ? "Authenticated" : "Not Authenticated";
                    String jsonResponse = "{\"message\": \"" + response + "\"}";

                    sendResponse(t, 200, jsonResponse);
                } catch (Exception e) {

                    e.printStackTrace();

                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(t, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/login1" + "\"}";
                sendResponse(t, 500, jsonResponse);
            }
        }
    }
    private static boolean validateLogin1(String email, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/parking_db", "root", "root123")) {
            String query = "SELECT * FROM ADMINLOGIN WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean result = resultSet.next();
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error executing the login query: " + e.getMessage());
        }


        return false;
    }

    static class LoginHandler2 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            handleCORS(t);

            if ("POST".equals(t.getRequestMethod())) {

                String requestBody = new Scanner(t.getRequestBody()).useDelimiter("\\A").next();


                try {
                    JSONObject json = new JSONObject(requestBody);
                    String email = json.getString("email");
                    String pass = json.getString("password");


                    boolean isValid = validateLogin2(email, pass);


                    String response = isValid ? "Authenticated" : "Not Authenticated";
                    String jsonResponse = "{\"message\": \"" + response + "\"}";

                    sendResponse(t, 200, jsonResponse);
                } catch (Exception e) {

                    e.printStackTrace();

                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(t, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/login2" + "\"}";
                sendResponse(t, 500, jsonResponse);
            }
        }
    }
    private static boolean validateLogin2(String email, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/parking_db", "root", "root123")) {
            String query = "SELECT * FROM history_users WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean result = resultSet.next();
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error executing the login query: " + e.getMessage());
        }


        return false;
    }

    static class SignupHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handleCORS(exchange);

            if ("POST".equals(exchange.getRequestMethod())) {

                String requestBody = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();


                try {
                    JSONObject json = new JSONObject(requestBody);
                    String name = json.getString("name");
                    String email = json.getString("email");
                    String password = json.getString("password");
                    boolean userExists = userExists(email);

                    if (userExists) {
                        String jsonResponse = "{\"message\": \"" + "UserExists" + "\"}";
                        sendResponse(exchange, 400, jsonResponse);
                    } else {
                        boolean success = registerUser(name, email, password);
                        if (success) {
                            String jsonResponse = "{\"message\": \"" + "Signup successful" + "\"}";
                            sendResponse(exchange, 200, jsonResponse);
                        } else {
                            String jsonResponse = "{\"message\": \"" + "Signup failed" + "\"}";
                            sendResponse(exchange, 500, jsonResponse);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    String jsonResponse = "{\"message\": \"" + "Invalid JSON format" + "\"}";
                    sendResponse(exchange, 400, jsonResponse);
                }
            } else {
                String jsonResponse = "{\"message\": \"" + "Unsupported HTTP method for /api/signup" + "\"}";
                sendResponse(exchange, 400, jsonResponse);
            }
        }
    }
    private static boolean userExists(String email) {
        try (Connection connection = Database.getConnection()) {
            String query = "SELECT * FROM USERS WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking if user exists: " + e.getMessage());
        }

        return false;
    }
    private static boolean registerUser(String name, String email, String password) {
        try (Connection connection = Database.getConnection()) {
            String query = "INSERT INTO USERS (name, email, password) VALUES (?, ?, ?)";
            String historyQuery = "INSERT INTO history_users (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 PreparedStatement historyStatement = connection.prepareStatement(historyQuery)) {

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                int affectedRows = preparedStatement.executeUpdate();


                historyStatement.setString(1, name);
                historyStatement.setString(2, email);
                historyStatement.setString(3, password);
                historyStatement.executeUpdate();

                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error registering user: " + e.getMessage());
        }
        return false;
    }
    private static void sendResponse(HttpExchange exchange, int code,  String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    private static void handleCORS(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }
    }
    static class Database {
        private static final String JDBC_URL = "jdbc:mysql://localhost:3306/parking_db";
        private static final String JDBC_USER = "root";
        private static final String JDBC_PASSWORD = "root123";

        static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        }
    }
}