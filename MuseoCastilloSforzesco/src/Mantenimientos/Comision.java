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
import museocastillosforzesco.ComisionesController;
import museocastillosforzesco.FXMLDocumentController;

/**
 *
 * @author Elias
 */
public class Comision {

    private StringProperty Tipo_Tarjeta;
    private DoubleProperty Comision;
    private IntegerProperty codigo;
    private IntegerProperty codigo_Comision;

    public Comision() {
    }

    public Comision(int codigo, String Tipo_Tarjeta, int codigo_Comision, Double Comision) {
        this.Tipo_Tarjeta = new SimpleStringProperty(Tipo_Tarjeta);
        this.Comision = new SimpleDoubleProperty(Comision);
        this.codigo = new SimpleIntegerProperty(codigo);
        this.codigo_Comision = new SimpleIntegerProperty(codigo_Comision);
    }

    public int getCodigo() {
        return codigo.get();
    }

    public int getCodigo_Comision() {
        return codigo_Comision.get();
    }

    public String getTipo_Tarjeta() {
        return Tipo_Tarjeta.get();
    }

    public void setTipo_Tarjeta(String Tipo_Tarjeta) {
        this.Tipo_Tarjeta = new SimpleStringProperty(Tipo_Tarjeta);
    }

    public Double getComision() {
        return Comision.get();
    }

    public void setComision(Double Comision) {
        this.Comision = new SimpleDoubleProperty(Comision);
    }

    public static void Comisiones(Connection connection, ObservableList<Comision> lista) {

        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "SELECT * FROM db_museo_castillo.tbl_tarjetas \n"
                    + "INNER JOIN db_museo_castillo.tbl_comisiones ON(tbl_tarjetas.id_Tarjeta =  tbl_comisiones.id_Tarjeta) \n "
                    + "WHERE status = ?"
            );
            instruccion.setString(1, "enabled");
            ResultSet resultado = instruccion.executeQuery();
            Comision comision;
            while (resultado.next()) {
                comision = new Comision(resultado.getInt(1), resultado.getString(2), resultado.getInt(4), resultado.getDouble(5));
                lista.add(comision);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Comision.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void AgregarComision(Connection connection) {

        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "START TRANSACTION ");
            instruccion.executeUpdate();
            instruccion = connection.prepareStatement(" INSERT INTO tbl_tarjetas (Tipo_Tarjeta,status) VALUES (?,?)");
            instruccion.setString(1, Tipo_Tarjeta.get());
            instruccion.setString(2, "enabled");
            instruccion.executeUpdate();

            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                instruccion = connection.prepareStatement("INSERT INTO tbl_comisiones (Comision, id_Tarjeta) VALUES(?,?)");
                instruccion.setDouble(1, Comision.get());
                instruccion.setInt(2, rs.getInt(1));
                instruccion.executeUpdate();

                instruccion = connection.prepareStatement("COMMIT");
                instruccion.executeUpdate();

            }

        } catch (SQLException ex) {
            Logger.getLogger(Comision.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print(ex);
        }
    }

    public int eliminarComision(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_tarjetas "
                            + " SET Tipo_Tarjeta= ?,  "
                            + " status = ?"
                            + " WHERE id_Tarjeta = ?"
                    );
            instruccion.setString(1, Tipo_Tarjeta.get());
            instruccion.setString(2, "disabled");
            instruccion.setInt(3, codigo.get());
            return instruccion.executeUpdate();

        } catch (SQLException e) {

            return 0;
        }

    }

    public void actualizarComision(Connection connection) {
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "START TRANSACTION ");
            instruccion.executeUpdate();
            instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_comisiones "
                            + " SET Comision = ?,  "
                            + " id_Tarjeta = ?"
                            + " WHERE id_Comision = ?"
                    );
            instruccion.setDouble(1, Comision.get());
            instruccion.setDouble(2, codigo.get());
            instruccion.setInt(3, codigo_Comision.get());
            instruccion.executeUpdate();
            instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_tarjetas "
                            + " SET Tipo_Tarjeta= ?,  "
                            + " status = ?"
                            + " WHERE id_Tarjeta = ?"
                    );
            instruccion.setString(1, Tipo_Tarjeta.get());
            instruccion.setString(2, "enabled");
            instruccion.setInt(3, codigo.get());
            instruccion.executeUpdate();
            instruccion = connection.prepareStatement(
                    "COMMIT"
            );
            instruccion.executeUpdate();
            instruccion.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void AbrirFormulario(AnchorPane rootPrincipal, Window window, String Accion, Comision comision, FXMLDocumentController controller) {

        try {
            BoxBlur blur = new BoxBlur(3, 3, 3);

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/museocastillosforzesco/Comision.fxml"));
            Parent root = loader.load();
            ComisionesController comisionesController = (ComisionesController) loader.getController();
            comisionesController.setFuncionalidad(comision, Accion, controller, stage);
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
                if (comisionesController.getResult() == true) {
                    if (Accion.equals("Editar")) {
                        controller.MostrarAviso("Se actualizo exitosamente");
                    } else {
                        controller.MostrarAviso("Se agrego exitosamente");
                    }

                }
                controller.OptionComisiones();
            });

        } catch (IOException ex) {
            Logger.getLogger(Precio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return Tipo_Tarjeta.get();
    }
}
