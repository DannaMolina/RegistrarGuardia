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
        String url = "jdbc:mysql://localhost:3308/condominio";

        String Nombre;
        String Apellido;
        String FechaNacimiento;
        String Telefono;
        String TipoDocumento;
        int NumeroDocumento;
        String Email;
        String Direccion;
        String EstadoGuardia;

        String[] opciones = {"Registrar Guardia", "Consultar Guardia", "Actualizar Guardia", "Eliminar Guardia"};

        int opcionSeleccionada = JOptionPane.showOptionDialog(null, "Seleccione una opción:", "Menú", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        try {
            Connection conexion = DriverManager.getConnection(url, usuario, password);

            switch (opcionSeleccionada) {
                case 0:
                    // Registrar Guardia
                    JOptionPane.showMessageDialog(null, "A continuacion se le pediran los datos del guardia para poder registrarlo");

                    Nombre = JOptionPane.showInputDialog("Ingrese el Nombre");
                    Apellido = JOptionPane.showInputDialog("Ingrese el Apellido");
                    FechaNacimiento = JOptionPane.showInputDialog("Ingrese la Fecha de Nacimiento ejemplo:(2003-12-31)");
                    Telefono = JOptionPane.showInputDialog("Ingrese el Numero Telefonico");
                    TipoDocumento = JOptionPane.showInputDialog("Ingrese que Tipo de documento si es CC,CE,PPT");
                    NumeroDocumento = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el Numero de Documento"));
                    Email = JOptionPane.showInputDialog("Ingrese el Email");
                    Direccion = JOptionPane.showInputDialog("Ingrese la direccion");
                    
                    EstadoGuardia = JOptionPane.showInputDialog("Ingrese el estado de guardia si se encuentra Activo, Inactivo o Suspendido");

                    String sqlPersona = "INSERT INTO personas ( personas_nombre, personas_apellido, personas_fecha_nacimiento,"
                        + "         personas_telefono, personas_tipo_documento, personas_numero_documento, personas_email, personas_direccion)"
                        + "         VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statementPersona = conexion.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
                
                statementPersona.setString(1, Nombre);
                statementPersona.setString(2, Apellido);
                statementPersona.setString(3, FechaNacimiento);
                statementPersona.setString(4, Telefono);
                statementPersona.setString(5, TipoDocumento);
                statementPersona.setInt(6, NumeroDocumento);
                statementPersona.setString(7, Email);
                statementPersona.setString(8, Direccion);
                
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
                    statementGuardia.setString(1, EstadoGuardia);
                    statementGuardia.setInt(2, idPersona);
                    
                    int filasInsertadasGuardia = statementGuardia.executeUpdate();
                    if (filasInsertadasGuardia > 0) {
                        JOptionPane.showMessageDialog(null, "El Guardia fue Registrado Correctamente");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al Guardar los datos del guardia");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error al Guardar los datos de la persona");
                }
                    
                    break;
                case 1:
                    //Consultar Guardia
                    ConsultarGuardia(conexion);
                    break;
                    
                case 2:
                    //Actualizar Guardia
                    int idPersonaActualizar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID de la Persona a actualizar"));
                    String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre:");
                    String nuevoApellido = JOptionPane.showInputDialog("Ingrese el nuevo apellido:");
                    ActualizarGuardia(conexion, idPersonaActualizar, nuevoNombre, nuevoApellido);
                    break;
                case 3:
                    //Eliminar
                    int idPersonaEliminar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID de la Persona a eliminar"));
                    EliminarGuardia(conexion, idPersonaEliminar);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida");
                    break;
            }

            conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
     }
    

    // Método para consultar todos los guardias en la base de datos
   public static void ConsultarGuardia(Connection conexion) throws SQLException {
        String sqlConsulta = "SELECT p.personas_nombre, p.personas_apellido, p.personas_fecha_nacimiento, p.personas_telefono, p.personas_tipo_documento, p.personas_numero_documento, p.personas_email, p.personas_direccion, g.guardias_estado " +
                             "FROM personas p " +
                             "JOIN guardias g ON p.personas_id = g.personas_personas_id";
        PreparedStatement statementConsulta = conexion.prepareStatement(sqlConsulta);
        ResultSet resultSet = statementConsulta.executeQuery();
        while (resultSet.next()) {
            System.out.println("Nombre: " + resultSet.getString("personas_nombre"));
            System.out.println("Apellido: " + resultSet.getString("personas_apellido"));
            System.out.println("Fecha de Nacimiento: " + resultSet.getString("personas_fecha_nacimiento"));
            System.out.println("Teléfono: " + resultSet.getString("personas_telefono"));
            System.out.println("Tipo de Documento: " + resultSet.getString("personas_tipo_documento"));
            System.out.println("Número de Documento: " + resultSet.getInt("personas_numero_documento"));
            System.out.println("Email: " + resultSet.getString("personas_email"));
            System.out.println("Dirección: " + resultSet.getString("personas_direccion"));
            System.out.println("Estado de Guardia: " + resultSet.getString("guardias_estado"));
          
        }
   }
    // Método para actualizar
    public static void ActualizarGuardia(Connection connection, int idPersona, String Nombre, String Apellido) throws SQLException {
        String sqlUpdate = "UPDATE personas SET personas_Nombre = ?, personas_apellido = ? WHERE personas_id = ?";
        PreparedStatement statementUpdate = connection.prepareStatement(sqlUpdate);
        statementUpdate.setString(1, Nombre);
        statementUpdate.setString(2, Apellido);
        statementUpdate.setInt(3, idPersona);
        statementUpdate.executeUpdate();
    }

    // Método para eliminar
   public static void EliminarGuardia(Connection conexion, int idPersona) throws SQLException {
    try {
        // Eliminar las filas en la tabla guardia que están asociadas a la persona
        String sqlDeleteGuardia = "DELETE FROM guardias WHERE personas_personas_id = ?";
        PreparedStatement statementDeleteGuardia = conexion.prepareStatement(sqlDeleteGuardia);
        statementDeleteGuardia.setInt(1, idPersona);
        statementDeleteGuardia.executeUpdate();

        // Eliminar la persona de la tabla personas
        String sqlDeletePersona = "DELETE FROM personas WHERE personas_id = ?";
        PreparedStatement statementDeletePersona = conexion.prepareStatement(sqlDeletePersona);
        statementDeletePersona.setInt(1, idPersona);
        statementDeletePersona.executeUpdate();

        System.out.println("Persona eliminada exitosamente");
    } catch (SQLException e) {
        System.out.println("Error al intentar eliminar la persona: " + e.getMessage());
    }
}

   
    
}
