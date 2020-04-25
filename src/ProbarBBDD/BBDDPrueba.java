package probarBBDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BBDDPrueba {

	public static void main(String[] args) {
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/fctbbdd", "root", "9432");
                    
            // Preparamos la consulta
            
            Statement sentencia = conexion.createStatement();
            ResultSet cursor = sentencia.executeQuery("SELECT * FROM prov_comp");
            
            //Recorremos el resultado para visualizar cada fila
            //Se hace un bucle mientras haya registros y se van visualizando
            while (cursor.next()) {
                System.out.printf("CIF_PROVEEDOR: %s"+ "RAZ_PROVEEDOR : %s"+ "REG_NOTARIAL: %d"+ "SEG_RESPONSABILIDAD  : %s"+ "SEG_IMPORTE: %f"+ "FEC_HOMOLOGACION: %s", cursor.getString(1),cursor.getString(2),cursor.getInt(3), cursor.getString(4), cursor.getFloat(5),cursor.getDate(6));
            }
            
            cursor.close();     // Cerrar ResultSet
            sentencia.close(); // Cerrar Statement
            conexion.close();  // Cerrar conexión
        } 
        catch (ClassNotFoundException cn) {cn.printStackTrace();} 
        catch (SQLException e) {e.printStackTrace();}
    }
	
}