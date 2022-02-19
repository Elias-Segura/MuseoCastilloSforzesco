/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museocastillosforzesco;

import Conexion.Conexion;
import Mantenimientos.Instalacion;
import Mantenimientos.Museo;
import Mantenimientos.tipo_Museo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
public class MuseoController implements Initializable {

    Timeline timeline;
    Stage stage;
    FXMLDocumentController controller;
    boolean result = false;
    String Accion = "";
    int selected = 0;
    Museo museo;
    Conexion conexion;
    @FXML
    private JFXTextField txt_Nombre;
    @FXML
    private JFXListView<Label> list_View;
    ObservableList<tipo_Museo> tipo_Museos = FXCollections.observableArrayList();
    ObservableList<Instalacion> instalaciones = FXCollections.observableArrayList();
    ObservableList<Instalacion> instalacionesMuseo;
    @FXML
    private JFXButton btn_Eliminar;
    @FXML
    private JFXButton btn_AgregarForm;
    @FXML
    private JFXComboBox<tipo_Museo> cmb_Tipo_Museo;
    @FXML
    private JFXComboBox<Instalacion> cmb_Instalaciones;

    /**
     * Initializes the controller class.
     */
    public boolean getResult() {
        return result;
    }

    public void setFuncionalidad(Museo museo, String Accion, FXMLDocumentController controller, Stage stage) {
        Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.EDIT, "1.5em");;
        Icon.setFill(Color.WHITE);
        this.museo = museo;
        this.Accion = Accion;
        this.controller = controller;
        this.stage = stage;
        if (this.museo != null) {
            btn_AgregarForm.setText("ACTUALIZAR");
            btn_AgregarForm.setGraphic(Icon);
            instalacionesMuseo = FXCollections.observableArrayList(museo.getInstalaciones());
            txt_Nombre.setText(museo.getNombre());
            for (int i = 0; i < tipo_Museos.size(); i++) {
                if (museo.getTipoMuseo().getCodigo() == tipo_Museos.get(i).getCodigo()) {
                    cmb_Tipo_Museo.setValue(tipo_Museos.get(i));
                }
            }
            for (int j = 0; j < instalacionesMuseo.size(); j++) {
                Label label = instalacionesMuseo.get(j).getLabelName();
                label.setStyle("-fx-text-fill: white; ");
                list_View.getItems().add(label);

            }
            for (int i = 0; i < instalaciones.size(); i++) {
                for (int j = 0; j < instalacionesMuseo.size(); j++) {
                    if (instalaciones.get(i).getCodigo() == instalacionesMuseo.get(j).getCodigo()) {
                        System.out.println(instalaciones.get(i).getNombre());
                        instalaciones.remove(i);

                    }

                }
            }

            cmb_Instalaciones.setItems(instalaciones);

        } else {
            instalacionesMuseo = FXCollections.observableArrayList();
            cmb_Instalaciones.setItems(instalaciones);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        conexion = new Conexion();
        conexion.establecerConexion();
        tipo_Museo.Tipo_Museos(conexion.getConnection(), tipo_Museos);
        cmb_Tipo_Museo.setItems(tipo_Museos);
        Instalacion.Instalaciones(conexion.getConnection(), instalaciones);

        conexion.cerrarConexion();
        list_View.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {
                selected = list_View.getSelectionModel().getSelectedIndex();
            }
        });

        cmb_Tipo_Museo.setConverter(new StringConverter<tipo_Museo>() {
            @Override
            public String toString(tipo_Museo object) {
                if (object == null) {
                    return null;
                }
                return object.getTipo();
            }

            @Override
            public tipo_Museo fromString(String string) throws IllegalArgumentException {
                for (tipo_Museo nd : cmb_Tipo_Museo.getItems()) {
                    if (nd.getTipo().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }
        });
        cmb_Instalaciones.setConverter(new StringConverter<Instalacion>() {
            @Override
            public String toString(Instalacion object) {
                if (object == null) {
                    return null;
                }
                return object.getNombre();
            }

            @Override
            public Instalacion fromString(String string) throws IllegalArgumentException {
                for (Instalacion nd : cmb_Instalaciones.getItems()) {
                    if (nd.getNombre().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }
        });
        TextFields.bindAutoCompletion(cmb_Tipo_Museo.getEditor(), cmb_Tipo_Museo.getItems());
        TextFields.bindAutoCompletion(cmb_Instalaciones.getEditor(), cmb_Instalaciones.getItems());

        txt_Nombre.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (nuevo.length() > 45) {
                    txt_Nombre.deleteNextChar();
                }
            }
        });
    }

    @FXML
    private void Eliminar(ActionEvent event) {
        Label lbl = list_View.getItems().get(selected);
        list_View.getItems().remove(selected);
        instalaciones.clear();
        conexion.establecerConexion();

        Instalacion.Instalaciones(conexion.getConnection(), instalaciones);
        conexion.cerrarConexion();

        for (int j = 0; j < instalacionesMuseo.size(); j++) {
            if (instalacionesMuseo.get(j).getNombre().equals(lbl.getText())) {
                instalacionesMuseo.remove(j);
            }
        }

        for (int i = 0; i < instalaciones.size(); i++) {
            for (int j = 0; j < instalacionesMuseo.size(); j++) {
                if (instalaciones.get(i).getCodigo() == instalacionesMuseo.get(j).getCodigo()) {
                    instalaciones.remove(i);
                }

            }
        }

        cmb_Instalaciones.setItems(instalaciones);
        list_View.getSelectionModel().clearSelection();
    }

    @FXML
    private void Agregar(MouseEvent event) {
        if (cmb_Instalaciones.getValue() != null) {
            instalacionesMuseo.add(cmb_Instalaciones.getSelectionModel().getSelectedItem());
            Label label = instalaciones.get(cmb_Instalaciones.getSelectionModel().getSelectedIndex()).getLabelName();
            label.setStyle(" -fx-text-fill: white; ");
            list_View.getItems().add(label);
            instalaciones.remove(cmb_Instalaciones.getSelectionModel().getSelectedIndex());
            cmb_Instalaciones.setItems(instalaciones);
            list_View.getSelectionModel().clearSelection();
        } 

    }

    @FXML
    private void OptionAgregar(ActionEvent event) {
        if (txt_Nombre.getText().length() != 0 && cmb_Tipo_Museo.getValue() != null) {
            try {
                if (Accion.equals("Agregar")) {
                    Museo museo = new Museo(0, txt_Nombre.getText(), cmb_Tipo_Museo.getSelectionModel().getSelectedItem(), "enabled", new ArrayList<Instalacion>(instalacionesMuseo));
                    conexion.establecerConexion();
                    museo.AgregarMuseo(conexion.getConnection());
                    conexion.cerrarConexion();
                    result = true;
                }
                if (Accion.equals("Editar")) {
                    Museo museo = new Museo(this.museo.getCodigo(), txt_Nombre.getText(), cmb_Tipo_Museo.getSelectionModel().getSelectedItem(), "", new ArrayList<Instalacion>(instalacionesMuseo));
                    conexion.establecerConexion();
                    museo.actualizarMuseo(conexion.getConnection());
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
                        this.stage.show();
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
