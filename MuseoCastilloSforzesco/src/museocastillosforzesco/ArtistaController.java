/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museocastillosforzesco;

import Conexion.Conexion;
import Mantenimientos.Artista;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class ArtistaController implements Initializable {

    Timeline timeline;
    Stage stage;
    FXMLDocumentController controller;
    boolean result = false;
    Conexion conexion;
    String Accion;
    Artista artista;
    @FXML
    private JFXTextField txt_Nombre;
    @FXML
    private JFXTextField txt_Pais;
    @FXML
    private JFXTextArea txt_Biografia;
    @FXML
    private JFXButton btn_AgregarForm;
    @FXML
    private Label txt_Limit;

    public boolean getResult() {
        return result;
    }

    public void setFuncionalidad(Artista artista, String Accion, FXMLDocumentController controller, Stage stage) {
        Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.EDIT, "1.5em");;
        Icon.setFill(Color.WHITE);
        this.controller = controller;
        this.stage = stage;
        this.artista = artista;
        this.Accion = Accion;
        if (artista != null) {
            txt_Nombre.setText(artista.getNombre());
            txt_Biografia.setText(artista.getBiografia());
            txt_Pais.setText(artista.getPais());
            btn_AgregarForm.setText("ACTUALIZAR");
            btn_AgregarForm.setGraphic(Icon);
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        conexion = new Conexion();

        txt_Biografia.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (viejo != nuevo) {
                    txt_Limit.setStyle("-fx-text-fill: white;");
                }
                if (nuevo.length() > 200) {
                    txt_Biografia.deleteNextChar();
                    txt_Limit.setText("200" + "/200");
                } else {
                    txt_Limit.setText(nuevo.length() + "/200");
                }

            }
        });
        txt_Nombre.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (nuevo.length() > 45) {
                    txt_Nombre.deleteNextChar();
                }
            }
        });
        txt_Pais.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (nuevo.length() > 45) {
                    txt_Pais.deleteNextChar();
                }
            }
        });

    }

    @FXML
    private void OptionAgregar(ActionEvent event) {

        if (txt_Biografia.getText().length() != 0 && txt_Nombre.getText().length() != 0 && txt_Pais.getText().length() != 0) {
            try {
                if (Accion.equals("Agregar")) {
                    Artista artista = new Artista(0, txt_Nombre.getText(), txt_Pais.getText(), txt_Biografia.getText());
                    conexion.establecerConexion();
                    artista.AgregarArtista(conexion.getConnection());
                    conexion.cerrarConexion();
                    result = true;

                }
                if (Accion.equals("Editar")) {
                    Artista artista = new Artista(this.artista.getCodigo(), txt_Nombre.getText(), txt_Pais.getText(), txt_Biografia.getText());
                    conexion.establecerConexion();
                    artista.actualizarArtista(conexion.getConnection());
                    conexion.cerrarConexion();
                    result = true;
                }
                ((JFXButton) (btn_AgregarForm)).getScene().getWindow().hide();
            } catch (Exception e) {
                result = false;
                this.stage.hide();
                controller.MostrarAviso("Ocurrio un error");
                timeline = new Timeline(new KeyFrame(Duration.millis(200), r -> {
                    if (controller.cerrado() == true) {
                        controller.cerrar();
                        cerrar();
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();

            }

        } else {
            this.stage.hide();
            controller.MostrarAviso("Complete los campos...");
            timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
                if (controller.cerrado() == true) {

                    controller.cerrar();
                    this.stage.show();
                    cerrar();

                }
            }));

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

        }
    }

    private void cerrar() {
        timeline.stop();
    }

}
