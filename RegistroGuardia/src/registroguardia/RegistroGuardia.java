/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package registroguardia;
import java.sql.*;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroGuardia {
    public static void main(String[] args) {
        
        String usuario = "root";
        String password = "123456789";
        String url ="jdbc:mysql://localhost:3308/condominio";
        
        String nombre;
        String apellido;
        String fechaNacimiento;
        String telefono;
        String tipoDocumento;
        int numeroDocumento;
        String email;
        String direccion;
        String estadoGuardia;
        
        nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Guardia");
        apellido = JOptionPane.showInputDialog("Ingrese el Apellido del Guardia");
        fechaNacimiento = JOptionPane.showInputDialog("Ingrese la Fecha de Nacimiento del Guardia");
        telefono = JOptionPane.showInputDialog("Ingrese el Numero Telefonico del Guardia");
        tipoDocumento = JOptionPane.showInputDialog("Ingrese que Tipo de documento si es CC,CE,PPT");
        numeroDocumento = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Numero de Documento del Guardia"));
        email = JOptionPane.showInputDialog("Ingrese el Email del Guardia:");
        direccion = JOptionPane.showInputDialog("Ingrese la direccion del Guardia");
        estadoGuardia = JOptionPane.showInputDialog("Ingrese el estado de guardia si se encuentra Activo, Inactivo o Suspedido");

        try {
            Connection conexion = DriverManager.getConnection(url,usuario,password);

            String sqlPersona = "INSERT INTO personas ( personas_nombre, personas_apellido, personas_fecha_nacimiento,"
                    + "         personas_telefono, personas_tipo_documento, personas_numero_documento, personas_email, personas_direccion)"
                    + "         VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statementPersona = conexion.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
            
                statementPersona.setString(1, nombre);
                statementPersona.setString(2, apellido);
                statementPersona.setString(3, fechaNacimiento);
                statementPersona.setString(4, telefono);
                statementPersona.setString(5, tipoDocumento);
                statementPersona.setInt(6, numeroDocumento);
                statementPersona.setString(7, email);
                statementPersona.setString(8, direccion);
            
            int filasInsertadasPersona = statementPersona.executeUpdate();
            int idPersona = -1;
            if (filasInsertadasPersona > 0) {
                ResultSet generatedKeys = statementPersona.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idPersona = generatedKeys.getInt(1);
                }
            }

            if (idPersona != -1) {
                String sqlGuardia = "INSERT INTO guardias (guardias_estado, personas_personas_id) VALUES (?, ?)";
                PreparedStatement statementGuardia = conexion.prepareStatement(sqlGuardia);
                statementGuardia.setString(1, estadoGuardia);
                statementGuardia.setInt(2, idPersona);

                int filasInsertadasGuardia = statementGuardia.executeUpdate();
                if (filasInsertadasGuardia > 0) {
                    JOptionPane.showMessageDialog(null, "El Guardia fue Registrado Correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al guardar datos del guardia");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar datos de la persona");
            }

            statementPersona.close();
            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
