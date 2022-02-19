/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museocastillosforzesco;

import Conexion.Conexion;
import Mantenimientos.Artista;
import Mantenimientos.Coleccion;
import Mantenimientos.Obra;
import Mantenimientos.imgs_Obra;
import Mantenimientos.tipo_Cultura;
import Mantenimientos.tipo_Obra;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.tk.Toolkit;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class ObraController implements Initializable {

    Timeline timeline;
    Stage stage;
    FXMLDocumentController controller;
    boolean result = false;
    Artista artista;
    String getReconocida;
    int position = 0;
    Conexion conexion;
    @FXML
    private JFXTextField txt_Nombre;
    @FXML
    private JFXComboBox<tipo_Obra> cmb_Tipo_Obra;
    @FXML
    private JFXComboBox<tipo_Cultura> cmb_Tipo_Cultura;
    @FXML
    private JFXTextArea txt_Detalle;
    @FXML
    private JFXRadioButton rb_Anonimo;
    @FXML
    private ToggleGroup Obras;
    @FXML
    private JFXRadioButton rb_Artista;
    @FXML
    private JFXComboBox<Artista> cmb_Artistas;
    @FXML
    private JFXRadioButton rb_Reconocida;
    @FXML
    private ToggleGroup Reconocida;
    @FXML
    private JFXRadioButton rb_No_Reconocida;
    @FXML
    private ImageView CodigoQR;
    @FXML
    private ImageView img_Obra;
    @FXML
    private JFXButton btn_AgregarForm;
    @FXML
    private Label lbl_Cantidad_Imagenes;
    private String Accion;
    private Obra obra;
    @FXML
    private JFXComboBox<Coleccion> cmb_Colecion;
    @FXML
    private Pane Pane_Artista;
    private ObservableList<tipo_Cultura> culturas = FXCollections.observableArrayList();
    private ObservableList<tipo_Obra> tipo_Obras = FXCollections.observableArrayList();
    private ObservableList<Coleccion> colecciones = FXCollections.observableArrayList();
    private ObservableList<Artista> artistas = FXCollections.observableArrayList();

    private ArrayList<FileInputStream> imagenes = new ArrayList<>();
    private ArrayList<File> fileImagenes = new ArrayList<>();
    private ArrayList<Image> lista = new ArrayList<>();
    ArrayList<imgs_Obra> listaImgs = new ArrayList<>();
    @FXML
    private Label txt_Limit;

    /**
     * Initializes the controller class.
     */
    public boolean getResult() {
        return result;
    }

    public void setFuncionalidad(Obra obra, String Accion, FXMLDocumentController controller, Stage stage) {
        Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.EDIT, "1.5em");;
        Icon.setFill(Color.WHITE);
        this.Accion = Accion;
        this.controller = controller;
        this.stage = stage;
        if (obra != null) {
            btn_AgregarForm.setText("ACTUALIZAR");
            btn_AgregarForm.setGraphic(Icon);
            this.obra = obra;
            txt_Nombre.setText(obra.getNombre());
            for (int i = 0; i < culturas.size(); i++) {
                if (obra.getCultura().getCodigo() == culturas.get(i).getCodigo()) {
                    cmb_Tipo_Cultura.setValue(culturas.get(i));
                }
            }
            for (int i = 0; i < tipo_Obras.size(); i++) {
                if (obra.getTipoObra().getCodigo() == tipo_Obras.get(i).getCodigo()) {
                    cmb_Tipo_Obra.setValue(tipo_Obras.get(i));
                }
            }
            for (int i = 0; i < colecciones.size(); i++) {
                if (obra.getColeccion().getCodigo() == colecciones.get(i).getCodigo()) {
                    cmb_Colecion.setValue(colecciones.get(i));
                }
            }
            txt_Detalle.setText(obra.getDetalle());
            conexion.establecerConexion();
            System.out.println(obra.getCodigo());
            listaImgs.clear();

            imgs_Obra.getImages(conexion.getConnection(), obra.getCodigo(), listaImgs);
            Artista artista = obra.getArtista(conexion.getConnection());

            conexion.cerrarConexion();
            if (artista != null) {

                Pane_Artista.setDisable(false);
                rb_Artista.setSelected(true);
                for (int i = 0; i < artistas.size(); i++) {
                    if (artista.getCodigo() == artistas.get(i).getCodigo()) {
                        cmb_Artistas.setValue(artistas.get(i));
                    }
                }

            }
            getReconocida = obra.getReconocida();
            if (getReconocida.equals("Si")) {
                rb_Reconocida.setSelected(true);
                rb_No_Reconocida.setSelected(false);
            } else {
                rb_Reconocida.setSelected(false);
                rb_No_Reconocida.setSelected(true);
            }

            for (int i = 0; i < listaImgs.size(); i++) {
                lista.add(listaImgs.get(i).getImage());
            }
            if (lista.size() != 0) {
                img_Obra.setImage(lista.get(position));
                lbl_Cantidad_Imagenes.setText((position + 1) + "/" + lista.size());
            }

            ByteArrayOutputStream out = QRCode.from(obra.getCodigoQR()).to(ImageType.PNG).withSize(200, 200).stream();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

            Image image = new Image(in);
            CodigoQR.setImage(image);

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
        conexion.establecerConexion();
        Coleccion.Colecciones(conexion.getConnection(), colecciones);
        tipo_Obra.Tipo_Obras(conexion.getConnection(), tipo_Obras);
        tipo_Cultura.Tipo_Culturas(conexion.getConnection(), culturas);
        Artista.Artistas(conexion.getConnection(), artistas);
        conexion.cerrarConexion();
        cmb_Artistas.setItems(artistas);
        cmb_Colecion.setItems(colecciones);
        cmb_Tipo_Cultura.setItems(culturas);
        cmb_Tipo_Obra.setItems(tipo_Obras);

        //Convetidor de culturas cbx
        cmb_Tipo_Cultura.setConverter(new StringConverter<tipo_Cultura>() {
            @Override
            public String toString(tipo_Cultura object) {
                if (object == null) {
                    return null;
                }
                return object.getNombre();
            }

            @Override
            public tipo_Cultura fromString(String string) throws IllegalArgumentException {
                for (tipo_Cultura nd : cmb_Tipo_Cultura.getItems()) {
                    if (nd.getNombre().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }
        });
        TextFields.bindAutoCompletion(cmb_Tipo_Cultura.getEditor(), cmb_Tipo_Cultura.getItems());
        //Convetidor de culturas obras
        cmb_Tipo_Obra.setConverter(new StringConverter<tipo_Obra>() {
            @Override
            public String toString(tipo_Obra object) {
                if (object == null) {
                    return null;
                }
                return object.getNombre();
            }

            @Override
            public tipo_Obra fromString(String string) throws IllegalArgumentException {
                for (tipo_Obra nd : cmb_Tipo_Obra.getItems()) {
                    if (nd.getNombre().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }
        });
        TextFields.bindAutoCompletion(cmb_Tipo_Obra.getEditor(), cmb_Tipo_Obra.getItems());
        //Convetidor de colecciones
        cmb_Colecion.setConverter(new StringConverter<Coleccion>() {
            @Override
            public String toString(Coleccion object) {
                if (object == null) {
                    return null;
                }
                return object.getNombre();
            }

            @Override
            public Coleccion fromString(String string) throws IllegalArgumentException {
                for (Coleccion nd : cmb_Colecion.getItems()) {
                    if (nd.getNombre().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }
        });
        //Convetidor de artistas
        TextFields.bindAutoCompletion(cmb_Colecion.getEditor(), cmb_Colecion.getItems());
        cmb_Artistas.setConverter(new StringConverter<Artista>() {
            @Override
            public String toString(Artista object) {
                if (object == null) {
                    return null;
                }
                return object.getNombre();
            }

            @Override
            public Artista fromString(String string) throws IllegalArgumentException {
                for (Artista nd : cmb_Artistas.getItems()) {
                    if (nd.getNombre().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }
        });
        TextFields.bindAutoCompletion(cmb_Artistas.getEditor(), cmb_Artistas.getItems());

        txt_Detalle.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (viejo != nuevo) {
                    txt_Limit.setStyle("-fx-text-fill: white;");
                }
                if (nuevo.length() > 200) {
                    txt_Detalle.deleteNextChar();
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
    }

    @FXML
    private void OptionAgregar(ActionEvent event) {

        if (txt_Nombre.getText().length() != 0 && cmb_Tipo_Obra.getValue() != null && cmb_Tipo_Cultura.getValue() != null && cmb_Colecion.getValue() != null && txt_Detalle.getText().length() != 0) {
            int resultado = 0;
            if (Accion.equals("Agregar")) {
                Obra obra = new Obra(0, txt_Nombre.getText(), cmb_Tipo_Obra.getSelectionModel().getSelectedItem(), cmb_Tipo_Cultura.getSelectionModel().getSelectedItem(), txt_Detalle.getText(), "", cmb_Colecion.getSelectionModel().getSelectedItem(), imagenes, fileImagenes);
                if (rb_Artista.isSelected()) {
                    if (cmb_Artistas.getValue() != null) {
                        conexion.establecerConexion();
                        resultado = obra.AgregarObra(conexion.getConnection(), true, rb_Reconocida.isSelected() ? "Si" : "No", cmb_Artistas.getSelectionModel().getSelectedItem());
                        conexion.cerrarConexion();
                    } else {
                        resultado = -1;
                        this.stage.hide();
                        controller.MostrarAviso("Agregue un artista");
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
                } else {
                    conexion.establecerConexion();
                    resultado = obra.AgregarObra(conexion.getConnection(), false, rb_Reconocida.isSelected() ? "Si" : "No", null);
                    conexion.cerrarConexion();
                }

            }
            if (Accion.equals("Editar")) {
                if (rb_Artista.isSelected()) {
                    if (cmb_Artistas.getValue() != null) {
                        Obra obra = new Obra(this.obra.getCodigo(), txt_Nombre.getText(), cmb_Tipo_Obra.getSelectionModel().getSelectedItem(), cmb_Tipo_Cultura.getSelectionModel().getSelectedItem(), txt_Detalle.getText(), "", cmb_Colecion.getSelectionModel().getSelectedItem(), imagenes, fileImagenes);
                        obra.setReconocida(rb_Reconocida.isSelected() ? "Si" : "No");
                        conexion.establecerConexion();
                        resultado = obra.actualizarObra(conexion.getConnection(), listaImgs, rb_Artista.isSelected() ? cmb_Artistas.getSelectionModel().getSelectedItem() : null);
                        conexion.cerrarConexion();

                    } else {
                        resultado = -1;
                        this.stage.hide();
                        controller.MostrarAviso("Agregue un artista");
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
                } else {

                    Obra obra = new Obra(this.obra.getCodigo(), txt_Nombre.getText(), cmb_Tipo_Obra.getSelectionModel().getSelectedItem(), cmb_Tipo_Cultura.getSelectionModel().getSelectedItem(), txt_Detalle.getText(), "", cmb_Colecion.getSelectionModel().getSelectedItem(), imagenes, fileImagenes);
                    obra.setReconocida(rb_Reconocida.isSelected() ? "Si" : "No");
                    conexion.establecerConexion();
                    resultado = obra.actualizarObra(conexion.getConnection(), listaImgs, rb_Artista.isSelected() ? cmb_Artistas.getSelectionModel().getSelectedItem() : null);
                    conexion.cerrarConexion();

                }

            }
            if (resultado == 0) {
                result = false;
                this.stage.hide();
                controller.MostrarAviso("Ocurrio un error.");
                timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
                    if (controller.cerrado() == true) {
                        controller.cerrar();
                        cerrar();
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            } else if (resultado == 1) {
                result = true;
                ((JFXButton) (btn_AgregarForm)).getScene().getWindow().hide();
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

    @FXML
    private void SelectedAnonima(ActionEvent event) {
        Pane_Artista.setDisable(true);
    }

    @FXML
    private void SelectedArtista(ActionEvent event) {
        Pane_Artista.setDisable(false);
    }

    @FXML
    private void Siguiente(MouseEvent event) {
        if (position + 1 <= lista.size() - 1) {
            position = position + 1;

            img_Obra.setImage(lista.get(position));
            lbl_Cantidad_Imagenes.setText((position + 1) + "/" + lista.size());

        }

    }

    @FXML
    private void Anterior(MouseEvent event) {
        if (position - 1 >= 0) {
            position = position - 1;

            img_Obra.setImage(lista.get(position));
            lbl_Cantidad_Imagenes.setText((position + 1) + "/" + lista.size());

        }

    }

    @FXML
    private void QuitarImagen(MouseEvent event) {

        if (lista.size() >= 1) {

            lista.remove(position);
            if (listaImgs.size() == 0) {
                imagenes.remove(position);
                fileImagenes.remove(position);
                System.out.println("borro1" + fileImagenes.size());
            } else if (position >= listaImgs.size()) {

                imagenes.remove(position - listaImgs.size());
                fileImagenes.remove(position - listaImgs.size());
                System.out.println("borro" + fileImagenes.size());
            }
            if (position < listaImgs.size() && listaImgs.size() >= 1) {
                listaImgs.remove(position);
            }

            position = 0;
            if (lista.size() > 0) {
                img_Obra.setImage(lista.get(position));
                lbl_Cantidad_Imagenes.setText((position + 1) + "/" + lista.size());
            } else {
                img_Obra.setImage(null);
                lbl_Cantidad_Imagenes.setText(0 + "/" + 0);
            }
        }

    }

    @FXML
    private void AgregarImagen(MouseEvent event) {

        try {
            String directorio;
            directorio = new File("").getAbsolutePath();
            File File = new File(directorio);
            if (!File.isDirectory()) {
                File.mkdir();
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(directorio));
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("png files(*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extensionFilter);
            File file = fileChooser.showOpenDialog(null);
            FileInputStream fs = new FileInputStream(file.getAbsoluteFile());
            if (fs != null) {
                imagenes.add(fs);
                fileImagenes.add(file);

            }

            Image Image = new Image(new FileInputStream(file.getAbsoluteFile()));
            lista.add(Image);
            img_Obra.setImage(Image);
            lbl_Cantidad_Imagenes.setText(lista.size() + "/" + lista.size());
            position = lista.size() - 1;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObraController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void DescargarQR(MouseEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(obra.getNombre());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(((JFXButton) (btn_AgregarForm)).getScene().getWindow());
        if (file != null) {
            String fileName = file.getPath();
            try {
                file = QRCode.from(obra.getCodigoQR()).to(ImageType.PNG)
                        .withSize(200, 200)
                        .file();

                Path path = Paths.get(fileName);
                if (Files.exists(path)) {
                    Files.delete(path);
                    Files.copy(file.toPath(), path);
                } else {
                    Files.copy(file.toPath(), path);
                }

            } catch (IOException ex) {
                Logger.getLogger(ObraController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void cerrar() {
        timeline.stop();
    }
}
