import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/hotel_db";
        String username = "root";
        String pass = "maazkhan";

        // Create ONE Scanner
        Scanner scanner = new Scanner(System.in);

        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create DB connection
            Connection con = DriverManager.getConnection(url, username, pass);

            while (true) {
                System.out.println();
                System.out.println("Hotel Management System");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        reserveRoom(con, scanner);
                        break;

                    case 2:
                        viewReservations(con);
                        break;

                    case 3:
                        getRoomNumber(con, scanner);
                        break;

                    case 4:
                        updateReservations(con, scanner);
                        break;

                    case 5:
                        deleteReservation(con, scanner);
                        break;

                    case 0:
                        System.out.println("Exiting...");
                        scanner.close();
                        con.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateReservations(Connection con,Scanner scanner){
        System.out.print("Enter Reservation ID: ");
        int id = scanner.nextInt();

        System.out.print("Enter new guest name: ");
        String name = scanner.next();

        System.out.print("Enter new room number: ");
        int room = scanner.nextInt();

        System.out.print("Enter new contact number: ");
        String contact = scanner.next();

        String sql = "UPDATE reservations SET " +
                "guest_name = '" + name + "', " +
                "room_number = " + room + ", " +
                "contact_number = '" + contact + "' " +
                "WHERE reservation_id = " + id;

        try {
            Statement stmt = con.createStatement();
           int rows = stmt.executeUpdate(sql);

           if (rows>0){
               System.out.println("Reservation Successful");
           }else {
               System.out.println("Reservation Failed");
           }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }

    private  static void deleteReservation(Connection con, Scanner scanner) {
        System.out.print("Enter Reservation ID: ");
        int id = scanner.nextInt();

        System.out.print("Enter Guest Name: ");
        String name = scanner.next();

        String sql = "DELETE FROM reservations WHERE reservation_id = " + id;

        try {
            Statement stmt = con.createStatement();
            int rows = stmt.executeUpdate(sql);

            if (rows > 0){
                System.out.println("Reservation Deleted Successfully");
            }else {
                System.out.println("No reservation found with this id.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private static void getRoomNumber(Connection con, Scanner scanner) {
        System.out.println("Enter Reservation Id");
        int id = scanner.nextInt();
        System.out.println("Enter Guest Name");
        String name = scanner.next();

        String sql = "SELECT room_number FROM reservations " +
                "WHERE reservation_id = " + id +
                " AND guest_name = '" + name + "'";

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            if (resultSet.next()){
                System.out.println("Room Number is: "+resultSet.getInt("room_number"));
            }else {
                System.out.println("Reservation Not Found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void viewReservations(Connection con)throws SQLException {
        String sql = "SELECT reservation_id,guest_name,room_number, contact_number,reservation_date FROM reservations ;";

        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()){
                int reservationID = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNo = resultSet.getInt("room_number");
                String contactNo = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationID, guestName, roomNo, contactNo, reservationDate);

            }
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

        } catch (Exception e) {
           e.printStackTrace();
        }
    }
    // Method is OUTSIDE main (correct)
    private static void reserveRoom(Connection con, Scanner scanner) {
        try {
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();

            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();

            System.out.print("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                    "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";

            Statement stmt = con.createStatement();
            int rows = stmt.executeUpdate(sql);

            if (rows > 0) {
                System.out.println("Reservation successful!");
            } else {
                System.out.println("Reservation failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }







    }
}
