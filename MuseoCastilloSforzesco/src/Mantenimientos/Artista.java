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
import museocastillosforzesco.ArtistaController;
import museocastillosforzesco.FXMLDocumentController;

/**
 *
 * @author Elias
 */
public class Artista {

    private StringProperty Nombre;
    private StringProperty Pais;
    private StringProperty Biografia;
    private IntegerProperty Codigo;

    public Artista() {
    }

    public Artista(int Codigo, String Nombre, String Pais, String Biografia) {
        this.Nombre = new SimpleStringProperty(Nombre);
        this.Pais = new SimpleStringProperty(Pais);
        this.Biografia = new SimpleStringProperty(Biografia);
        this.Codigo = new SimpleIntegerProperty(Codigo);
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

    public String getPais() {
        return Pais.get();
    }

    public void setPais(String Pais) {
        this.Pais = new SimpleStringProperty(Pais);
    }

    public String getBiografia() {
        return Biografia.get();
    }

    public void setBiografia(String Biografia) {
        this.Biografia = new SimpleStringProperty(Biografia);
    }

    public static void Artistas(Connection connection, ObservableList<Artista> lista) {
        try {

            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM db_museo_castillo.tbl_artistas WHERE status = ?");
            instruccion.setString(1, "enabled");
            ResultSet resultado = instruccion.executeQuery();
            Artista artista;
            while (resultado.next()) {
                artista = new Artista(resultado.getInt(1), resultado.getString(2), resultado.getString(3), resultado.getString(4));
                lista.add(artista);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Comision.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void AgregarArtista(Connection connection) {

        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "INSERT INTO tbl_artistas (Nombre, Pais, Biografia, status) VALUES (?, ?, ?,?)"
            );
            instruccion.setString(1, Nombre.get());
            instruccion.setString(2, Pais.get());
            instruccion.setString(3, Biografia.get());
            instruccion.setString(4, "enabled");
            instruccion.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Precio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int eliminarArtista(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_artistas "
                            + " SET Nombre = ?,  "
                            + " Pais = ?,  "
                            + " Biografia = ?, "
                            + " status = ? "
                            + " WHERE id_Artista = ?"
                    );
            instruccion.setString(1, Nombre.get());
            instruccion.setString(2, Pais.get());
            instruccion.setString(3, Biografia.get());
            instruccion.setString(4, "disabled");
            instruccion.setInt(5, Codigo.get());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int actualizarArtista(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_artistas "
                            + " SET Nombre = ?,  "
                            + " Pais = ?,  "
                            + " Biografia = ?, "
                            + " status = ? "
                            + " WHERE id_Artista = ?"
                    );
            instruccion.setString(1, Nombre.get());
            instruccion.setString(2, Pais.get());
            instruccion.setString(3, Biografia.get());
            instruccion.setString(4, "enabled");
            instruccion.setInt(5, Codigo.get());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void AbrirFormulario(AnchorPane rootPrincipal, Window window, String Accion, Artista artista, FXMLDocumentController controller) {

        try {
            BoxBlur blur = new BoxBlur(3, 3, 3);

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/museocastillosforzesco/Artista.fxml"));
            Parent root = loader.load();
            ArtistaController artistaController = (ArtistaController) loader.getController();
            artistaController.setFuncionalidad(artista, Accion, controller, stage);
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
                if (artistaController.getResult() == true) {
                    if (Accion.equals("Editar")) {
                        controller.MostrarAviso("Se actualizo exitosamente");
                    } else {
                        controller.MostrarAviso("Se agrego exitosamente");
                    }

                }
                controller.OptionArtistas();
            });
        } catch (IOException ex) {
            Logger.getLogger(Artista.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String toString() {
        return Nombre.get();
    }

}
