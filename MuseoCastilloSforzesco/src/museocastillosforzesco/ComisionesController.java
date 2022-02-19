/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museocastillosforzesco;

import Conexion.Conexion;
import Mantenimientos.Comision;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class ComisionesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Timeline timeline;
    Stage stage;
    FXMLDocumentController controller;
    boolean result = false;
    Conexion conexion;
    private Comision comision;
    private String Accion;

    @FXML
    private JFXButton btn_AgregarForm;

    @FXML
    private JFXTextField txt_Comision;
    @FXML
    private JFXTextField txt_Tarjeta;

    public boolean getResult() {
        return result;
    }

    public void setFuncionalidad(Comision comision, String Accion, FXMLDocumentController controller, Stage stage) {
        Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.EDIT, "1.5em");;
        Icon.setFill(Color.WHITE);
        this.controller = controller;
        this.stage = stage;
        this.comision = comision;
        this.Accion = Accion;
        if (comision != null) {
            btn_AgregarForm.setText("ACTUALIZAR");
            btn_AgregarForm.setGraphic(Icon);
            txt_Comision.setText(Double.toString(comision.getComision()));
            txt_Tarjeta.setText(comision.getTipo_Tarjeta());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        conexion = new Conexion();
        txt_Tarjeta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (nuevo.length() > 45) {
                    txt_Tarjeta.deleteNextChar();
                }
            }
        });
        txt_Comision.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                try {
                    Double.parseDouble(nuevo);
                } catch (Exception e) {
                    txt_Comision.deleteNextChar();
                }
            }
        });
    }

    @FXML
    private void OptionAgregar(ActionEvent event) {

        if (txt_Tarjeta.getText().length() != 0 && txt_Comision.getText().length() != 0) {

            try {
                if (Accion.equals("Agregar")) {
                    Comision comision = new Comision(0, txt_Tarjeta.getText(), 0, Double.parseDouble(txt_Comision.getText()));

                    conexion.establecerConexion();
                    comision.AgregarComision(conexion.getConnection());
                    conexion.cerrarConexion();
                    result = true;
                }
                if (Accion.equals("Editar")) {
                    Comision comision = new Comision(this.comision.getCodigo(), txt_Tarjeta.getText(), this.comision.getCodigo_Comision(), Double.parseDouble(txt_Comision.getText()));

                    conexion.establecerConexion();
                    comision.actualizarComision(conexion.getConnection());
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
