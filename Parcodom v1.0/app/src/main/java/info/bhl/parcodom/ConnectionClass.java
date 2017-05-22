package info.bhl.parcodom;


import java.sql.*;
/*
public class ConnectionClass { //database connection class creation.
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver"); //database driver import

        }
        catch (java.lang.ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }



        try {

            Connection db = DriverManager.getConnection("jdbc:postgresql://horton.elephantsql.com:5432/mucgvykr","mucgvykr" ,"aPYb8Z6_Xvb3kOKw3jSaSgj8XxA9Xbjv" ); //connection to the database, including the database url,database user name and pasword.
            Statement st = db.createStatement();
            ResultSet rs = st.executeQuery("SELECT version, latitude, longitude FROM parkingmeters LIMIT 1"); //query for database.
            while (rs.next()) {
                System.out.print("Column 1 returned ");
                System.out.println(rs.getString(2));
                System.out.print("Column 2 returned ");
                System.out.println(rs.getString(3));

            }
            rs.close();
            st.close();
        }
        catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
*/