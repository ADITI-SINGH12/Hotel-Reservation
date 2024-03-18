import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/ accountant";
        String username = "root";
        String password = "Aditi@12345";
        Class.forName("com.mysql.jdbc.Driver");
        try(Connection conn = DriverManager.getConnection(url,username,password)){
            while(true){
                System.out.println();
                System.out.println( "HOTEL MANAGEMENT SYSTEM");
                System.out.println("1. New Reservation");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("6. Exits");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter your choice");
                int choice = scanner.nextInt();
                switch(choice){
                    case 1:
                        reservation(conn,scanner);
                        break;
                    case 2:
                        viewReservation(conn);
                        break;
                    case 3:
                        getRoomNumber(conn,scanner);
                        break;
                    case 4:
                        updateReservation(conn,scanner);
                        break;
                    case 5:
                        deleteReservation(conn,scanner);
                        break;
                    case 6:
                        exits();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }catch(Exception e){
            e.getMessage();
        }
    }

    private static void exits() throws InterruptedException {
        int i=5;
        System.out.print("Exiting");
        while(i-->0){
            System.out.print(".");
            Thread.sleep(450);
        }
        System.out.println();
        System.out.println("Thank you for using Hotel Reservation System");
    }

    private static void deleteReservation(Connection conn, Scanner scanner) {
        System.out.println("Enter your reservation id to delete :");
        int id = scanner.nextInt();
        if(resrvationExist(conn,id)){
            String sql = "DELETE FROM reservation WHERE resrvation_id = "+id;
            try(Statement stmt = conn.createStatement()){
                int rowsAffexted = stmt.executeUpdate(sql);
                if(rowsAffexted > 0){
                    System.out.println("Reservation is deleted successfully");
                }else{
                    System.out.println("Not deleted");
                }
            }catch(SQLException e){
                e.getMessage();
            }
        }else{
            System.out.println("You have typed wrong reservation id.");
        }

    }

    private static boolean resrvationExist(Connection conn,int id) {
        int ids=0;
        String sql = "SELECT  resrvation_id from reservation WHERE  resrvation_id = "+id;
        try(Statement stmt = conn.createStatement()){
            ResultSet rst = stmt.executeQuery(sql);
            if(rst.next()){
               ids = rst.getInt("resrvation_id");
            }else{
                return false;
            }
        }catch(SQLException e){
            e.getMessage();
        }
        return id==ids;
    }

    private static void updateReservation(Connection conn, Scanner scanner) {
        System.out.print("Enter reservation id: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        if(!resrvationExist(conn,id)){
            System.out.println("Reservation ID doesn't exist");
            return;
        }
        System.out.print("Enter new guest name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new room number: ");
        int number = scanner.nextInt();
        System.out.println("Enter new contact number: ");
        String mobile = scanner.next();

        String sql = "UPDATE reservation SET guest_name = '" + name + "', " +
                "roomNumber = " + number + ", " +
                "contactNumber = '" + mobile + "' " +
                "WHERE resrvation_id  = " + id;
        try(Statement stmt = conn.createStatement()){
            int rowsAffected = stmt.executeUpdate(sql);
            if(rowsAffected > 0){
                System.out.println("Updated Succesfully!!");
            }else{
                System.out.println("Not Updated Properly!!");
            }
        }catch(SQLException e){
            e.getMessage();
        }
    }

    private static void getRoomNumber(Connection conn, Scanner scanner) {
        System.out.println("Enter your reservation id");
        int id = scanner.nextInt();
        System.out.println("Enter your guest name: ");
        String name = scanner.next();

        String sql = "SELECT roomNumber FROM reservation WHERE resrvation_id = "+id;
        try(Statement stmt = conn.createStatement()){
            ResultSet rst = stmt.executeQuery(sql);
            if(rst.next()){
                int roomnumber = rst.getInt("roomNumber");
                System.out.println("The room number of "+name+"is: "+roomnumber);
            }else{
                System.out.println("The given input is invalid!!");
            }
        }catch (SQLException e){
            e.getMessage();
        }
    }

    private static void viewReservation(Connection conn) throws SQLException {
        String sql = "SELECT resrvation_id, guest_name, roomNumber, contactNumber, reservation_date FROM reservation";
        try(Statement stmt = conn.createStatement()) {
            ResultSet rst = stmt.executeQuery(sql);
            System.out.println("Current Reservations:");
            System.out.println("=======================================");
            System.out.println("---------------+--------------+------------+------------------+------------------+");
            System.out.println("reservation_id | guest_name   | roomNumber | contactNumber    | reservation_date |");
            System.out.println("---------------+---------------+------------+-----------------+------------------+");
            while (rst.next()) {
                int id = rst.getInt("resrvation_id");
                String name = rst.getString("guest_name");
                int number = rst.getInt("roomNumber");
                String mobile = rst.getString("contactNumber");
                String t = rst.getTimestamp("reservation_date").toString();
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        id, name, number, mobile, t);
            }
        }
            System.out.println("---------------+------------+------------+---------------+------------------+");

    }

    private static void reservation(Connection conn, Scanner scanner) {
        try {
            System.out.print("Enter guest name: ");
            String name = scanner.next();
            scanner.nextLine();
            System.out.println("Enter Room Number: ");
            int roomNumb = scanner.nextInt();
            System.out.println("Enter contact number");
            String number = scanner.next();

            String sql = "INSERT INTO reservation (guest_name,roomNumber,contactNumber) "+
                    "values('"+name+"', "+roomNumb+", '"+ number+"')";
            try(Statement stmt = conn.createStatement()){
               int rowsAffeted =  stmt.executeUpdate(sql);
                if(rowsAffeted > 0){
                    System.out.println("Reservation Sucessfull !!");
                }else{
                    System.out.println("Reservation failed !!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}