/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mantenimientos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import museocastillosforzesco.ColeccionController;
import museocastillosforzesco.FXMLDocumentController;

/**
 *
 * @author Elias
 */
public class Coleccion {

    private StringProperty Nombre;
    private StringProperty Siglo;
    private StringProperty Observacion;
    private IntegerProperty Museo_Nombre;
    private IntegerProperty Codigo;
    private Museo museo;

    public Coleccion() {
    }

    public Coleccion(int Codigo, String Nombre, String Siglo, String Observacion, Museo museo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Nombre = new SimpleStringProperty(Nombre);
        this.Siglo = new SimpleStringProperty(Siglo);
        this.Observacion = new SimpleStringProperty(Observacion);
        this.museo = museo;
    }

    public Museo getMuseo() {
        return museo;
    }

    public String getMuseoNombre() {
        return museo.getNombre();
    }

    public void setMuseo(Museo museo) {
        this.museo = museo;
    }

    public int getCodigo() {
        return Codigo.get();
    }

    public void setNombre(int Codigo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
    }

    public String getNombre() {
        return Nombre.get();
    }

    public void setNombre(String Nombre) {
        this.Nombre = new SimpleStringProperty(Nombre);
    }

    public String getSiglo() {
        return Siglo.get();
    }

    public void setSiglo(String Siglo) {
        this.Siglo = new SimpleStringProperty(Siglo);
    }

    public String getObservacion() {
        return Observacion.get();
    }

    public void setObservacion(String Observacion) {
        this.Observacion = new SimpleStringProperty(Observacion);
    }

    public int getMuseo_Nombre() {
        return Museo_Nombre.get();
    }

    public void setMuseo_Nombre(int Museo_Nombre) {
        this.Museo_Nombre = new SimpleIntegerProperty(Museo_Nombre);
    }

    public static void Colecciones(Connection connection, ObservableList<Coleccion> lista) {
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "SELECT * FROM db_museo_castillo.tbl_colecciones A \n"
                    + " INNER JOIN db_museo_castillo.tbl_museos B ON(B.id_Museo =  A.id_Museo) \n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_museo C ON(B.id_Tipo =  C.id_Tipo)\n"
                    + "WHERE A.status = ? and B.status = ? "
            );
            instruccion.setString(1, "enabled");
            instruccion.setString(2, "enabled");
            ResultSet resultado = instruccion.executeQuery();
            Coleccion coleccion;
            while (resultado.next()) {
                coleccion = new Coleccion(resultado.getInt(1), resultado.getString(2), resultado.getString(3), resultado.getString(4), new Museo(resultado.getInt(5), resultado.getString(8), new tipo_Museo(resultado.getInt(11), resultado.getString(12)), resultado.getString(10)));
                lista.add(coleccion);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Comision.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void AgregarColeccion(Connection connection) {

        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "INSERT INTO tbl_colecciones (Nombre, Siglo, Observacion, id_Museo,status) VALUES (?, ?, ?,?,?)"
            );
            instruccion.setString(1, Nombre.get());
            instruccion.setString(2, Siglo.get());
            instruccion.setString(3, Observacion.get());
            instruccion.setInt(4, museo.getCodigo());
            instruccion.setString(5, "enabled");
            instruccion.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Coleccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int eliminarColeccion(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_colecciones "
                            + " SET Nombre = ?,  "
                            + "Siglo = ?,  "
                            + " Observacion= ?, "
                            + " id_Museo= ?, "
                            + " status= ? "
                            + " WHERE id_Coleccion = ?"
                    );
            instruccion.setString(1, Nombre.get());
            instruccion.setString(2, Siglo.get());
            instruccion.setString(3, Observacion.get());
            instruccion.setInt(4, museo.getCodigo());
            instruccion.setString(5, "disabled");
            instruccion.setInt(6, Codigo.get());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizarColeccion(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_colecciones "
                            + " SET Nombre = ?,  "
                            + "Siglo = ?,  "
                            + " Observacion= ?, "
                            + " id_Museo= ?, "
                            + " status= ? "
                            + " WHERE id_Coleccion = ?"
                    );
            instruccion.setString(1, Nombre.get());
            instruccion.setString(2, Siglo.get());
            instruccion.setString(3, Observacion.get());
            instruccion.setInt(4, museo.getCodigo());
            instruccion.setString(5, "enabled");
            instruccion.setInt(6, Codigo.get());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    public void AbrirFormulario(AnchorPane rootPrincipal, Window window, String Accion, Coleccion coleccion, FXMLDocumentController controller) {

        try {
            BoxBlur blur = new BoxBlur(3, 3, 3);

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/museocastillosforzesco/Coleccion.fxml"));
            Parent root = loader.load();
            ColeccionController coleccionController = (ColeccionController) loader.getController();
            coleccionController.setFuncionalidad(coleccion, Accion, controller, stage);
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
                if (coleccionController.getResult() == true) {
                    if (Accion.equals("Editar")) {
                        controller.MostrarAviso("Se actualizo exitosamente");
                    } else {
                        controller.MostrarAviso("Se agrego exitosamente");
                    }

                }
                controller.OptionColecciones();
            });
        } catch (IOException ex) {
            Logger.getLogger(Coleccion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String toString() {
        return Nombre.get();
    }
}
