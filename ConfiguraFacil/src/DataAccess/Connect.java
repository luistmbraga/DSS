package DataAccess;

import java.sql.*;

/**
 * 
 * @author Grupo 10
 */
public class Connect {
    
    /**
     * Estabelece a ligação com a base de dados.
     * @return 
     */
 public static Connection connect() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String connectionUrl = "jdbc:mysql://localhost/configurafacil?serverTimezone=GMT&verifyServerCertificate=false&useSSL=true";
        Connection cn = DriverManager.getConnection(connectionUrl,"ola","123456");
        return cn;
    } 
    catch (ClassNotFoundException | SQLException e) {e.printStackTrace(); }
    return null;
 }

     /**
     * Fecha a ligação de uma determinda connection.
     *@param connection
     */
 @SuppressWarnings("empty-statement")
 public static void close(Connection connection) {
        try {
            connection.close();
        } 
        catch (SQLException e) {;}
    }
    
}
