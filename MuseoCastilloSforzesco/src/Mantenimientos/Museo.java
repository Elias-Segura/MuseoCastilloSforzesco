/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mantenimientos;

import Conexion.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import museocastillosforzesco.FXMLDocumentController;
import museocastillosforzesco.MuseoController;

/**
 *
 * @author Elias
 */
public class Museo {

    private IntegerProperty Codigo;
    private StringProperty Nombre;
    private StringProperty Status;
    private Instalacion instalacion;
    private tipo_Museo tipoMuseo;

    private ObservableList<Instalacion> instalaciones;

    public Museo() {
    }

    public Museo(int Codigo, String Nombre, tipo_Museo tipoMuseo, String status) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Nombre = new SimpleStringProperty(Nombre);
        this.tipoMuseo = tipoMuseo;
        this.Status = new SimpleStringProperty(status);
    }

    public Museo(int Codigo, String Nombre, tipo_Museo tipoMuseo, String status, ArrayList<Instalacion> lista) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Nombre = new SimpleStringProperty(Nombre);
        this.tipoMuseo = tipoMuseo;
        this.Status = new SimpleStringProperty(status);
        this.instalaciones = FXCollections.observableArrayList(lista);

    }

    public ObservableList getInstalaciones() {
        Conexion conexion = new Conexion();
        conexion.establecerConexion();
        ArrayList<Instalacion> listaInstalaciones = new ArrayList<>();
        Instalacion.getInstalaciones(conexion.getConnection(), listaInstalaciones, Codigo.get(), "enabled");
        conexion.cerrarConexion();
        instalaciones.setAll(listaInstalaciones);
        return instalaciones;
    }

    public int getCodigo() {
        return Codigo.get();
    }

    public void setCodigo(int Codigo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
    }

    public String getNombre() {
        return Nombre.get();
    }

    public void setNombre(String Nombre) {
        this.Nombre = new SimpleStringProperty(Nombre);
    }

    public String getTipo() {
        return tipoMuseo.getTipo();
    }

    public tipo_Museo getTipoMuseo() {
        return tipoMuseo;
    }

    public static void Museos(Connection connection, ObservableList<Museo> lista) {
        ArrayList<Instalacion> listaInstalaciones = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultado = statement.executeQuery(
                    "SELECT * FROM db_museo_castillo.tbl_museos \n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_museo ON(tbl_museos.id_Tipo =  tbl_tipo_museo.id_Tipo)"
            );
            Museo museo;
            while (resultado.next()) {
                Instalacion.getInstalaciones(connection, listaInstalaciones, resultado.getInt(1), "enabled");
                if (resultado.getString(4).equals("enabled")) {
                    museo = new Museo(resultado.getInt(1), resultado.getString(2), new tipo_Museo(resultado.getInt(5), resultado.getString(6)), resultado.getString(4), listaInstalaciones);
                    lista.add(museo);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(Museo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void actualizarMuseo(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "UPDATE tbl_museos "
                    + " SET Nombre = ?,  "
                    + " id_Tipo = ?,  "
                    + " status = ? "
                    + " WHERE id_Museo = ?"
            );
            instruccion.setString(1, Nombre.get());
            instruccion.setInt(2, tipoMuseo.getCodigo());
            instruccion.setString(3, "enabled");
            instruccion.setInt(4, Codigo.get());

            instruccion.executeUpdate();
            actualizarInstalaciones(connection);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void AgregarMuseo(Connection connection) {
        try {

            Statement stm = connection.createStatement();
            PreparedStatement instruccion = connection.prepareStatement(
                    " START TRANSACTION "
            );
            instruccion.executeUpdate();
            instruccion = connection.prepareStatement(
                    "INSERT INTO tbl_museos (Nombre, id_Tipo, status) VALUES (?,?,?)"
            );
            instruccion.setString(1, Nombre.get());
            instruccion.setInt(2, tipoMuseo.getCodigo());
            instruccion.setString(3, "enabled");
            instruccion.executeUpdate();

            ResultSet rs = stm.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                for (int i = 0; i < instalaciones.size(); i++) {
                    instruccion = connection.prepareStatement(
                            "INSERT INTO tbl_instalaciones_museos (id_Instalacion, id_Museo,status) VALUES (?,?,?);"
                    );
                    instruccion.setInt(1, instalaciones.get(i).getCodigo());
                    instruccion.setInt(2, rs.getInt(1));
                    instruccion.setString(3, "enabled");
                    instruccion.executeUpdate();
                    instruccion = connection.prepareStatement(
                            "COMMIT"
                    );
                    instruccion.executeUpdate();
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Museo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void actualizarInstalaciones(Connection connection) {
        boolean encontrado = false;
        ArrayList<Instalacion> listaInstalaciones = new ArrayList<>();
        Instalacion.getInstalaciones(connection, listaInstalaciones, Codigo.get(), "enabled");
        for (int i = 0; i < listaInstalaciones.size(); i++) {
            for (int j = 0; j < instalaciones.size(); j++) {
                if (listaInstalaciones.get(i).getCodigo() == instalaciones.get(j).getCodigo()) {
                    encontrado = true;
                    instalaciones.remove(j);
                }

            }
            if (encontrado == false) {
                try {
                    PreparedStatement instruccion = connection.prepareStatement(
                            "UPDATE tbl_instalaciones_museos "
                            + " SET id_Instalacion = ?,  "
                            + " id_Museo = ?,  "
                            + " status = ? "
                            + " WHERE id_Instalacion_Museo = ?"
                    );
                    instruccion.setInt(1, listaInstalaciones.get(i).getCodigo());
                    instruccion.setInt(2, Codigo.get());
                    instruccion.setString(3, "disabled");
                    instruccion.setInt(4, listaInstalaciones.get(i).getIDInstalacionMuseo());
                    instruccion.executeUpdate();

                } catch (SQLException e) {
                    System.out.println(e);
                    e.printStackTrace();

                }
            }
            encontrado = false;

        }
        listaInstalaciones.clear();
        Instalacion.getInstalaciones(connection, listaInstalaciones, Codigo.get(), "disabled");
        for (int i = 0; i < listaInstalaciones.size(); i++) {
            for (int j = 0; j < instalaciones.size(); j++) {
                if (listaInstalaciones.get(i).getCodigo() == instalaciones.get(j).getCodigo()) {
                    try {
                        PreparedStatement instruccion = connection.prepareStatement(
                                "UPDATE tbl_instalaciones_museos "
                                + " SET id_Instalacion = ?,  "
                                + " id_Museo = ?,  "
                                + " status = ? "
                                + " WHERE id_Instalacion_Museo = ?"
                        );
                        instruccion.setInt(1, listaInstalaciones.get(i).getCodigo());
                        instruccion.setInt(2, Codigo.get());
                        instruccion.setString(3, "enabled");
                        instruccion.setInt(4, listaInstalaciones.get(i).getIDInstalacionMuseo());
                        instruccion.executeUpdate();
                        instalaciones.remove(j);
                    } catch (SQLException e) {
                        e.printStackTrace();

                    }
                }

            }
        }
        for (int j = 0; j < instalaciones.size(); j++) {
            try {
                PreparedStatement instruccion = connection.prepareStatement(
                        "INSERT INTO tbl_instalaciones_museos (id_Instalacion,id_Museo, status) VALUES (?, ?, ?)"
                );
                instruccion.setInt(1, instalaciones.get(j).getCodigo());
                instruccion.setInt(2, Codigo.get());
                instruccion.setString(3, "enabled");

                instruccion.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Museo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public int eliminarMuseo(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "UPDATE tbl_museos "
                    + " SET Nombre = ?,  "
                    + " id_Tipo = ?,  "
                    + " status = ? "
                    + " WHERE id_Museo = ?"
            );
            instruccion.setString(1, Nombre.get());
            instruccion.setInt(2, tipoMuseo.getCodigo());
            instruccion.setString(3, "disabled");
            instruccion.setInt(4, Codigo.get());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    public void AbrirFormulario(AnchorPane rootPrincipal, Window window, String Accion, Museo museo, FXMLDocumentController controller) {

        try {
            BoxBlur blur = new BoxBlur(3, 3, 3);

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/museocastillosforzesco/Museo.fxml"));
            Parent root = loader.load();
            MuseoController museoController = (MuseoController) loader.getController();
            museoController.setFuncionalidad(museo, Accion, controller, stage);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/Imagenes/icon.png"));
            stage.setTitle("Museo Castillo Sforzesco");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    window);
            stage.setResizable(false);
            stage.show();
            rootPrincipal.setEffect(blur);

            stage.setOnHidden((WindowEvent event1) -> {
                rootPrincipal.setEffect(null);
                if (museoController.getResult() == true) {
                    if (Accion.equals("Editar")) {
                        controller.MostrarAviso("Se actualizo exitosamente");
                    } else {
                        controller.MostrarAviso("Se agrego exitosamente");
                    }
                }
                controller.OptionMuseos();
            });
        } catch (IOException ex) {
            Logger.getLogger(Precio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String toString() {
        return Nombre.get();
    }
}
