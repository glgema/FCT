package application;

import java.io.File;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class SampleController {
	
	private String database = "fctbbdd";
	
	@FXML
	private TextField tfCif;
	
	@FXML
	private TextField tfRazon;
	
	@FXML
	private TextField tfRegistro;
	
	@FXML
	private TextField tfResponsabilidad;
	
	@FXML
	private TextField tfSeguroImporte;
	
	@FXML
	private TextField tfFecha;
	
	@FXML
	private ListView<String> lvProveedores;
	
	private ArrayList<Proveedor> proveedores;
	
	@FXML
	public void subirArchivo() {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("SUBIR ARCHIVO");

        File file = fileChooser.showOpenDialog((Stage) lvProveedores.getScene().getWindow());
        
        if(file != null) 
	        System.out.println(file.getAbsolutePath());

	}
	
	@FXML
	public void cargarProveedores() {
		proveedores = new ArrayList<>();
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/"+database, "root", "9432");
								
			Statement sentencia = conexion.createStatement();
			ResultSet cursor = sentencia.executeQuery("SELECT * FROM prov_comp");
			
			while (cursor.next()) {

				Proveedor proveedor = new Proveedor(cursor.getString(1),cursor.getString(2),cursor.getInt(3), cursor.getString(4), cursor.getFloat(5),cursor.getDate(6));
			
			    proveedores.add(proveedor);
			}
			
			cursor.close();     // Cerrar ResultSet
			sentencia.close(); // Cerrar Statement
			conexion.close();  // Cerrar conexión
			
		}
		catch (ClassNotFoundException cn) {cn.printStackTrace();} 
		catch (SQLException e) {e.printStackTrace();}
		
		ObservableList<String> items =FXCollections.observableArrayList ();
		
		for(Proveedor prov : proveedores)
			items.add("CIF: " + prov.getCif() + " : " + prov.getRazon());
		
		lvProveedores.setItems(items);
		
		lvProveedores.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	        	if(lvProveedores.getSelectionModel().getSelectedIndex()>=0)
	        		mostrarInfo(proveedores.get(lvProveedores.getSelectionModel().getSelectedIndex()));
	        }
	    });
	}
	
	public void mostrarInfo(Proveedor prov) {
		tfCif.setText(prov.getCif());
		tfCif.setDisable(true);
		tfRazon.setText(prov.getRazon());
		tfRegistro.setText(String.valueOf(prov.getRegNotarial()));
		tfResponsabilidad.setText(prov.getSegResponsabilidad());
		tfSeguroImporte.setText(String.valueOf(prov.getSegImporte()));
		tfFecha.setText(prov.getFecHomologacion().toLocalDate().toString());

	}
	
	@FXML
	public void limpiar() {
		tfCif.setDisable(false);
		tfCif.setText("");
		tfRazon.setText("");
		tfRegistro.setText("");
		tfResponsabilidad.setText("");
		tfSeguroImporte.setText("");
		tfFecha.setText("");
	}
	
	@FXML
	public void eliminar() {
		
		if(lvProveedores.getSelectionModel().getSelectedIndex()>=0) {
			Proveedor prov = proveedores.get(lvProveedores.getSelectionModel().getSelectedIndex());
			
			Connection conexion = null;
			try {
				
				Class.forName("com.mysql.jdbc.Driver");
				conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/"+database, "root", "9432");
									
				PreparedStatement ps = conexion.prepareStatement("DELETE FROM prov_comp WHERE CIF_PROVEEDOR = ? ;");
		
			    ps.setString(1, prov.getCif());
			    ps.executeUpdate();
				
				conexion.close(); 
				ps.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			limpiar();
			cargarProveedores();
		}
	}
	
	@FXML
	public void modificar() {
		
		if(lvProveedores.getSelectionModel().getSelectedIndex()>=0) {
		
			String cif = tfCif.getText().toString();
			String razon = tfRazon.getText().toString();
			int registro = Integer.parseInt(tfRegistro.getText().toString());
			String seguroResponsabilidad = tfResponsabilidad.getText().toString();
			float seguroImporte = Float.parseFloat(tfSeguroImporte.getText().toString());
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		
			//convert String to LocalDate
			LocalDate localDate = LocalDate.parse(tfFecha.getText(), formatter);
			
			Date date = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	
			Proveedor prov = new Proveedor(cif,razon,registro,seguroResponsabilidad,seguroImporte,sqlDate);
			
			Connection conexion = null;
		    PreparedStatement ps;
			try {
				conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/"+database, "root", "9432");
				ps = conexion.prepareStatement("UPDATE prov_comp SET RAZ_PROVEEDOR = ?, REG_NOTARIAL = ?, SEG_RESPONSABILIDAD = ?, SEG_IMPORTE = ?, FEC_HOMOLOGACION = ? WHERE CIF_PROVEEDOR = ? ;");
				
				ps.setString(1, prov.getRazon());
				ps.setInt(2, prov.getRegNotarial());
				ps.setString(3,prov.getSegResponsabilidad());
				ps.setFloat(4,prov.getSegImporte());
				ps.setDate(5, prov.getFecHomologacion());
				ps.setString(6, prov.getCif());
				
				ps.executeUpdate();
				
				conexion.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			cargarProveedores();
		}
	}
	
	public void añadir() {
		
		String cif = tfCif.getText().toString();
		String razon = tfRazon.getText().toString();
		int registro = Integer.parseInt(tfRegistro.getText().toString());
		String seguroResponsabilidad = tfResponsabilidad.getText().toString();
		float seguroImporte = Float.parseFloat(tfSeguroImporte.getText().toString());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
	
		//convert String to LocalDate
		LocalDate localDate = LocalDate.parse(tfFecha.getText(), formatter);
		
		Date date = java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());

		Proveedor prov = new Proveedor(cif,razon,registro,seguroResponsabilidad,seguroImporte,sqlDate);
		
		Connection conexion = null;
	    PreparedStatement ps;
		try {
			conexion = DriverManager.getConnection ("jdbc:mysql://localhost:3306/"+database, "root", "9432");
			ps = conexion.prepareStatement("INSERT INTO prov_comp VALUES(?,?,?,?,?,?)");
			
			ps.setString(1, prov.getCif());
			ps.setString(2, prov.getRazon());
			ps.setInt(3, prov.getRegNotarial());
			ps.setString(4,prov.getSegResponsabilidad());
			ps.setFloat(5,prov.getSegImporte());
			ps.setDate(6, prov.getFecHomologacion());
			 
			ps.executeUpdate();
			
			conexion.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		limpiar();
		cargarProveedores();
	}
	
}