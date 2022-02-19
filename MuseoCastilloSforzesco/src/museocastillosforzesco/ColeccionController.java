/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museocastillosforzesco;

import Conexion.Conexion;
import Mantenimientos.Coleccion;
import Mantenimientos.Museo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class ColeccionController implements Initializable {

    Timeline timeline;
    Stage stage;
    FXMLDocumentController controller;
    boolean result = false;
    Conexion conexion;
    Coleccion coleccion;
    String Accion;
    @FXML
    private JFXComboBox<Museo> cmb_Museo;
    @FXML
    private JFXTextField txt_Nombre;
    @FXML
    private JFXTextField txt_Siglo;
    @FXML
    private JFXTextArea txt_Observacion;
    @FXML
    private JFXButton btn_AgregarForm;
    private final ObservableList<Museo> museos = FXCollections.observableArrayList();
    @FXML
    private Label txt_Limit;

    public boolean getResult() {
        return result;
    }

    public void setFuncionalidad(Coleccion coleccion, String Accion, FXMLDocumentController controller, Stage stage) {
        Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.EDIT, "1.5em");;
        Icon.setFill(Color.WHITE);
        this.controller = controller;
        this.stage = stage;
        this.coleccion = coleccion;
        this.Accion = Accion;
        if (coleccion != null) {
            btn_AgregarForm.setText("ACTUALIZAR");
            btn_AgregarForm.setGraphic(Icon);
            txt_Nombre.setText(coleccion.getNombre());
            txt_Siglo.setText(coleccion.getSiglo());
            txt_Observacion.setText(coleccion.getObservacion());
            for (int i = 0; i < museos.size(); i++) {
                if (coleccion.getMuseo().getCodigo() == museos.get(i).getCodigo()) {
                    cmb_Museo.setValue(museos.get(i));

                }
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        conexion = new Conexion();
        coleccion = new Coleccion();
        conexion.establecerConexion();
        Museo.Museos(conexion.getConnection(), museos);
        conexion.cerrarConexion();
        cmb_Museo.setItems(museos);

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

        txt_Observacion.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (viejo != nuevo) {
                    txt_Limit.setStyle("-fx-text-fill: white;");
                }
                if (nuevo.length() > 100) {
                    txt_Observacion.deleteNextChar();
                    txt_Limit.setText("100" + "/100");
                } else {
                    txt_Limit.setText(nuevo.length() + "/100");
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
        txt_Siglo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (nuevo.length() > 6) {
                    txt_Siglo.deleteNextChar();
                }
            }
        });
    }

    @FXML
    private void OptionAgregar(ActionEvent event) {
        if (cmb_Museo.getValue() != null && txt_Nombre.getText().length() != 0 && txt_Siglo.getText().length() != 0 && txt_Observacion.getText().length() != 0) {
            try {
                if (Accion.equals("Agregar")) {
                    Coleccion coleccion = new Coleccion(0, txt_Nombre.getText(), txt_Siglo.getText(), txt_Observacion.getText(), cmb_Museo.getSelectionModel().getSelectedItem());
                    conexion.establecerConexion();
                    coleccion.AgregarColeccion(conexion.getConnection());
                    conexion.cerrarConexion();
                    result = true;
                }
                if (Accion.equals("Editar")) {
                    Coleccion coleccion = new Coleccion(this.coleccion.getCodigo(), txt_Nombre.getText(), txt_Siglo.getText(), txt_Observacion.getText(), cmb_Museo.getSelectionModel().getSelectedItem());
                    conexion.establecerConexion();
                    coleccion.actualizarColeccion(conexion.getConnection());
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
