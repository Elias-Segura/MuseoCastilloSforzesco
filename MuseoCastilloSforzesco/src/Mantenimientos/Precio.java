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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import museocastillosforzesco.PreciosController;

/**
 *
 * @author Elias
 */
public class Precio {

    private IntegerProperty IDMuseo;
    private IntegerProperty Codigo;
    private DoubleProperty Precio_Semanal;
    private DoubleProperty Precio_Domingo;
    private Museo museo;

    public Precio() {
    }

    public Precio(int Codigo, Double Precio_Semanal,
            Double Precio_Domingo, Museo museo) {

        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Precio_Semanal = new SimpleDoubleProperty(Precio_Semanal);
        this.Precio_Domingo = new SimpleDoubleProperty(Precio_Domingo);
        this.museo = museo;
    }

    public Precio(Double Precio_Semanal,
            Double Precio_Domingo) {

        this.Precio_Semanal = new SimpleDoubleProperty(Precio_Semanal);
        this.Precio_Domingo = new SimpleDoubleProperty(Precio_Domingo);

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

    public void setCodigo(int Codigo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
    }

    public Double getPrecio_Semanal() {
        return Precio_Semanal.get();
    }

    public void setPrecio_Semanal(Double Precio_Semanal) {
        this.Precio_Semanal = new SimpleDoubleProperty(Precio_Semanal);
    }

    public Double getPrecio_Domingo() {
        return Precio_Domingo.get();
    }

    public void setPrecio_Domingo(Double Precio_Domingo) {
        this.Precio_Domingo = new SimpleDoubleProperty(Precio_Domingo);
    }

    public static void Precios(Connection connection, ObservableList<Precio> lista) {
        try {

            PreparedStatement instruccion = connection.prepareStatement(
                    "SELECT * FROM db_museo_castillo.tbl_precios A \n"
                    + " INNER JOIN db_museo_castillo.tbl_museos B ON(A.id_Museo =  B.id_Museo)\n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_museo C ON(B.id_Tipo =  C.id_Tipo)"
                    + "WHERE A.status = ? and B.status = ? "
            );
            instruccion.setString(1, "enabled");
            instruccion.setString(2, "enabled");
            ResultSet resultado = instruccion.executeQuery();
            Precio precio;
            while (resultado.next()) {
                precio = new Precio(resultado.getInt(1), resultado.getDouble(2), resultado.getDouble(3), new Museo(resultado.getInt(4), resultado.getString(7), new tipo_Museo(resultado.getInt(10), resultado.getString(11)), resultado.getString(8)));
                lista.add(precio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Precio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void AgregarPrecio(Connection connection) {

        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "INSERT INTO tbl_precios (Precio_Semanal, Precio_Domingo, id_Museo,status) VALUES (?, ?, ?, ? )"
            );
            instruccion.setDouble(1, Precio_Semanal.get());
            instruccion.setDouble(2, Precio_Domingo.get());
            instruccion.setInt(3, museo.getCodigo());
            instruccion.setString(4, "enabled");
            instruccion.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Precio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int eliminarPrecio(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_precios "
                            + " SET 	Precio_Semanal = ?,  "
                            + " Precio_Domingo = ?,  "
                            + " id_Museo = ?  , "
                            + " status = ? "
                            + " WHERE id_Precio = ?"
                    );
            instruccion.setDouble(1, Precio_Semanal.get());
            instruccion.setDouble(2, Precio_Domingo.get());
            instruccion.setInt(3, museo.getCodigo());
            instruccion.setString(4, "disabled");
            instruccion.setInt(5, Codigo.get());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizarPrecio(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_precios "
                            + " SET 	Precio_Semanal = ?,  "
                            + " Precio_Domingo = ?,  "
                            + " id_Museo = ?  , "
                            + " status = ? "
                            + " WHERE id_Precio = ?"
                    );
            instruccion.setDouble(1, Precio_Semanal.get());
            instruccion.setDouble(2, Precio_Domingo.get());
            instruccion.setInt(3, museo.getCodigo());
            instruccion.setString(4, "enabled");
            instruccion.setInt(5, Codigo.get());

            return instruccion.executeUpdate();

        } catch (SQLException e) {

            return 0;
        }
    }

    public void AbrirFormulario(AnchorPane rootPrincipal, Window window, String Accion, Precio precio, FXMLDocumentController controller) {

        try {

            BoxBlur blur = new BoxBlur(3, 3, 3);

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/museocastillosforzesco/Precios.fxml"));
            Parent root = loader.load();
            PreciosController preciosController = (PreciosController) loader.getController();

            preciosController.setFuncionalidad(precio, Accion, controller, stage);

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

                if (preciosController.getResult() == true) {
                    if (Accion.equals("Editar")) {
                        controller.MostrarAviso("Se actualizo exitosamente");
                    } else {
                        controller.MostrarAviso("Se agrego exitosamente");
                    }

                }
                controller.OptionPrecios();

            });
        } catch (IOException ex) {
            Logger.getLogger(Precio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String toString() {
        return getMuseoNombre();
    }

}
