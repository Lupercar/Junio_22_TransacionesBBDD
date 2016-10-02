package consola;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.OracleDriver;

public class App {
	
	private static Properties prop = new Properties();

	public static void main(String[] args) throws SQLException, IOException {
		//Cargar el driver a la MVJ
        DriverManager.registerDriver(new OracleDriver());
        
        //Leemos configuraciÃ³n de un fichero de propiedades
        prop.load(App.class.getResourceAsStream("../configuracion/oracle.properties"));
        
        Savepoint savepoint1 = null; 
        Connection conexion = null;
        
        try{
        	conexion = App.getConexion();
        	conexion.setAutoCommit(false); //quitamos el auto commit de la sesión
        	
//        	Statement comando = conexion.createStatement();
//        	String sql = "INSERT INTO CLIENTE(id,nombre) VALUES (5,'Cliente5')"; 
//        	comando.executeUpdate(sql); 
        	
//        	conexion.rollback(); //deshace los cambios hasta el último commit
        	savepoint1 = conexion.setSavepoint(); 
        	
//        	comando.close();
        	
        	String sql2 = "INSERT INTO PEDIDO(id,fecha,info,idcliente) VALUES (5,?,'PedidoCliente5',5)"; 
        	PreparedStatement comando2 = conexion.prepareStatement(sql2);
        	comando2.setDate(1, new java.sql.Date(2015, 4, 30));
        	
        	comando2.execute();  
        	
        	comando2.close();
        	
        	conexion.commit(); //confirmamos
        	conexion.close();
        }catch (SQLException ex) {
        	if(savepoint1 != null)
        		conexion.rollback(savepoint1);//Deshace cambios hasta savepoint
        		conexion.commit();
		}
	}
	
	private static Connection getConexion() throws SQLException{
        return DriverManager.getConnection(
                    prop.getProperty("url"), 
                    prop.getProperty("usuario"), 
                    prop.getProperty("pass"));
    }
}//fin class consola.App
