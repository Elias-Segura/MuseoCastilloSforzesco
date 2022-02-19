/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museocastillosforzesco;

import Conexion.Conexion;
import Mantenimientos.Museo;
import Mantenimientos.Precio;
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
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class PreciosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Timeline timeline;
    Stage stage;
    FXMLDocumentController controller;
    boolean result = false;
    Conexion conexion;
    private Precio precio;
    private String Accion;
    @FXML
    private JFXTextField txt_PSemanal;
    @FXML
    private JFXTextField txt_PDomingo;
    @FXML
    private JFXComboBox<Museo> cmb_Museo;
    private final ObservableList<Museo> museos = FXCollections.observableArrayList();
    @FXML
    private JFXButton btn_Form;
    private final ObservableList<Precio> Precios = FXCollections.observableArrayList();

    public boolean getResult() {
        return result;

    }

    public void setFuncionalidad(Precio precio, String Accion, FXMLDocumentController controller, Stage stage) {
        Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.EDIT, "1.5em");;
        Icon.setFill(Color.WHITE);
        this.precio = precio;
        this.Accion = Accion;
        this.controller = controller;
        this.stage = stage;
        conexion.establecerConexion();
        Precio.Precios(conexion.getConnection(), Precios);
        conexion.cerrarConexion();
        if (precio != null) {
            boolean found = false;
            for (int j = 0; j < museos.size(); j++) {
                if (precio.getMuseo().getCodigo() == museos.get(j).getCodigo()) {
                    cmb_Museo.getItems().add(museos.get(j));
                }
                for (int i = 0; i < Precios.size(); i++) {
                    if (Precios.get(i).getMuseo().getCodigo() == museos.get(j).getCodigo()) {
                        found = true;
                    }

                }
                if (found == false) {
                    cmb_Museo.getItems().add(museos.get(j));
                }
                found = false;
            }
            txt_PDomingo.setText(Double.toString(precio.getPrecio_Domingo()));
            txt_PSemanal.setText(Double.toString(precio.getPrecio_Semanal()));
            for (int i = 0; i < museos.size(); i++) {
                if (precio.getMuseo().getCodigo() == museos.get(i).getCodigo()) {
                    cmb_Museo.setValue(museos.get(i));

                }
            }
            btn_Form.setText("ACTUALIZAR");
            btn_Form.setGraphic(Icon);

        } else {
            boolean found = false;
            for (int j = 0; j < museos.size(); j++) {
                for (int i = 0; i < Precios.size(); i++) {
                    if (Precios.get(i).getMuseo().getCodigo() == museos.get(j).getCodigo()) {
                        found = true;
                    }
                }
                if (found == false) {
                    cmb_Museo.getItems().add(museos.get(j));
                }
                found = false;
            }
        }
        cmb_Museo.setConverter(new StringConverter<Museo>() {
            @Override
            public String toString(Museo object) {
                if (object == null) {
                    return null;
                }
                return object.getNombre();
            }

            @Override
            public Museo fromString(String string) throws IllegalArgumentException {
                for (Museo nd : cmb_Museo.getItems()) {
                    if (nd.getNombre().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }
        });
        TextFields.bindAutoCompletion(cmb_Museo.getEditor(), cmb_Museo.getItems());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Accion = new String();
        conexion = new Conexion();
        conexion.establecerConexion();
        Museo.Museos(conexion.getConnection(), museos);

        conexion.cerrarConexion();
        if (precio != null) {
            txt_PDomingo.setText(Double.toString(precio.getPrecio_Domingo()));
            txt_PSemanal.setText(Double.toString(precio.getPrecio_Semanal()));
            cmb_Museo.setValue(precio.getMuseo());
        }
        txt_PDomingo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                try {
                    Double.parseDouble(nuevo);
                } catch (Exception e) {
                    txt_PDomingo.deleteNextChar();
                }
            }
        });
        txt_PSemanal.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                try {
                    Double.parseDouble(nuevo);
                } catch (Exception e) {
                    txt_PSemanal.deleteNextChar();
                }
            }
        });
    }

    @FXML
    private void OptionAgregar(ActionEvent event) {

        if (txt_PDomingo.getText().length() != 0 && txt_PSemanal.getText().length() != 0 && cmb_Museo.getValue() != null) {
            try {
                if (Accion.equals("Agregar")) {
                    Precio precio = new Precio(0, Double.parseDouble(txt_PSemanal.getText()), Double.parseDouble(txt_PDomingo.getText()), cmb_Museo.getSelectionModel().getSelectedItem());
                    conexion.establecerConexion();
                    precio.AgregarPrecio(conexion.getConnection());
                    conexion.cerrarConexion();
                    result = true;
                }
                if (Accion.equals("Editar")) {
                    Precio precio = new Precio(this.precio.getCodigo(), Double.parseDouble(txt_PSemanal.getText()), Double.parseDouble(txt_PDomingo.getText()), cmb_Museo.getSelectionModel().getSelectedItem());
                    conexion.establecerConexion();
                    precio.actualizarPrecio(conexion.getConnection());
                    conexion.cerrarConexion();
                    result = true;
                }
                ((JFXButton) (btn_Form)).getScene().getWindow().hide();

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
            timeline = new Timeline(new KeyFrame(Duration.millis(200), r -> {
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
