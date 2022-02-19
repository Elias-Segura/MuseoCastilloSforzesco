/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museocastillosforzesco;

import Conexion.Conexion;
import Entradas.CodigoQR;
import Entradas.Entrada;
import Entradas.Listado;
import Mantenimientos.Artista;
import Mantenimientos.Coleccion;
import Mantenimientos.Comision;
import Mantenimientos.Instalacion;
import Mantenimientos.Mantenimiento;
import Mantenimientos.Museo;
import Mantenimientos.Obra;
import Mantenimientos.Precio;
import Mantenimientos.imgs_Obra;
import Mantenimientos.tipo_Museo;
import Reportes.Generador_Reportes;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons.BOLT;
import impl.org.controlsfx.skin.AutoCompletePopup;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author Elias
 */
public class FXMLDocumentController implements Initializable {

    JFXDialog dialog;
    FXMLDocumentController controller;
    Obra obra = new Obra();
    int valoracion = 0;
    String tipo = new String();
    String tipo_Reporte = new String();
    int position = 0;
    Date date;
    Listado lista;
    private Label label;
    @FXML
    private TextField txt_Visitante;

    @FXML
    private DatePicker DatePicker_Entrada;
    @FXML
    private Button btn_Agregar;
    @FXML
    private TableView<Listado> tbl_View_Entrada;

    @FXML
    private Label SubTotal;
    @FXML
    private Label IVA;
    @FXML
    private Label TOTAL;
    private final ObservableList<Mantenimiento> Mantenimiento = FXCollections.observableArrayList();
    private final ObservableList<Comision> comisiones = FXCollections.observableArrayList();
    private final ObservableList<Museo> museos = FXCollections.observableArrayList();
    private final ObservableList<Listado> listado = FXCollections.observableArrayList();
    private final ObservableList<Artista> Artistas = FXCollections.observableArrayList();
    private final ObservableList<Coleccion> Colecciones = FXCollections.observableArrayList();
    private final ObservableList<Precio> Precios = FXCollections.observableArrayList();
    private final ObservableList<Obra> Obras = FXCollections.observableArrayList();
    private final ObservableList<Generador_Reportes> reportes = FXCollections.observableArrayList();
    private final ObservableList<Generador_Reportes> reportes_Obras = FXCollections.observableArrayList();
    ArrayList<imgs_Obra> listaImgs = new ArrayList<>();
    Conexion conexion;
    @FXML
    private TableColumn<Listado, Date> Fecha;
    @FXML
    private TableColumn<Listado, Number> _Precio;
    @FXML
    private TableColumn<Listado, String> MuseoNombre;
    @FXML
    private GridPane VentaEntrada;
    @FXML
    private GridPane ValidarEntrada;
    @FXML
    private Label Aviso_Entrada;
    @FXML
    private Label Fecha_Actual;
    @FXML
    private JFXTextArea txt_Area_Museos;
    @FXML
    private ImageView CodigoQR_Image;
    @FXML
    private JFXButton btn_Vender;
    @FXML
    private GridPane Mantenimientos;
    @FXML
    private JFXTextField txt_Filtro_Field;
    @FXML
    private JFXComboBox<Mantenimiento> cmb_Filtro2;
    @FXML
    private TableView<Mantenimiento> tbl_View_Mantenimientos;
    @FXML
    private JFXButton Filtrar;
    @FXML
    private TableColumn<Mantenimiento, String> Colum1;
    @FXML
    private TableColumn<Mantenimiento, String> Colum2;
    @FXML
    private TableColumn<Mantenimiento, String> Colum3;
    @FXML
    private TableColumn<Mantenimiento, String> Colum4;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane rootAnchor;
    @FXML
    private JFXButton btn_Eliminar;
    @FXML
    private AnchorPane rootPrincipal;
    @FXML
    private JFXButton btn_AgregarForm;
    FilteredList<Mantenimiento> filteredList = new FilteredList<>(Mantenimiento, b -> true);
    @FXML
    private GridPane Valoracion;
    @FXML
    private Pane Pane_Obra;
    @FXML
    private Label txt_Nombre_Obra;
    @FXML
    private Label txt_Nombre_Coleccion;
    @FXML
    private Label txt_Tipo_Obra;
    @FXML
    private Label txt_Cultura;
    @FXML
    private Label txt_Nombre_Artista;
    @FXML
    private JFXTextArea txt_Detalle_Obra;
    @FXML
    private ImageView img_Obra;
    @FXML
    private JFXTextArea txt_Reseña;
    @FXML
    private ImageView CodigoQRObra;
    @FXML
    private Label lbl_Reconocida;
    @FXML
    private Label lbl_Cantidad_Imagenes;
    @FXML
    private Label star_1;
    @FXML
    private Label star_2;
    @FXML
    private Label star_3;
    @FXML
    private Label star_4;
    @FXML
    private Label star_5;

    private Label lbl_prueba;
    @FXML
    private Pane Pane_Reseña_Obra;
    @FXML
    private JFXButton btn_Valorar;
    @FXML
    private JFXButton btn_Enviar_Valoracion;
    @FXML
    private JFXButton btn_Cancelar_Valoracion;
    @FXML
    private Pane Pane_Form_Valoracion;
    @FXML
    private Label Fecha_Form_Valoracion;
    @FXML
    private GridPane Reportes;
    @FXML
    private TableView<Generador_Reportes> tbl_View_Rango_Comisiones;
    @FXML
    private JFXDatePicker Rango_2;
    @FXML
    private JFXDatePicker Rango_1;
    @FXML
    private TableColumn<Generador_Reportes, String> Col_Reporte_1;
    @FXML
    private TableColumn<Generador_Reportes, Double> Col_Reporte_2;
    String Fecha_Rango = "";
    @FXML
    private GridPane Reportes_Obras;
    @FXML
    private TableView<Generador_Reportes> tbl_View_Reporte_Obras;
    @FXML
    private TableColumn<Generador_Reportes, String> Col_Nombre_Museo;
    @FXML
    private TableColumn<Generador_Reportes, String> Col_Nombre_Coleccion;
    @FXML
    private TableColumn<Generador_Reportes, String> Col_Nombre_Obra;
    @FXML
    private TableColumn<Generador_Reportes, Double> Col_Porcentaje_Obra;
    @FXML
    private Pane Pane_Validacion_Entrada;
    @FXML
    private JFXComboBox<Museo> cbx_Entrada_Museo;
    @FXML
    private JFXComboBox<Comision> cbx_Entrada_Tipo_Tarjeta;
    @FXML
    private Label lbl_Porcentaje;
    @FXML
    private Label txt_Limit;

    public boolean cerrado() {
        return dialog.isDisabled();
    }

    public void cerrar() {
        dialog.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Pane_Validacion_Entrada.setVisible(false);

        controller = this;
        Pane_Form_Valoracion.setVisible(false);
        Pane_Reseña_Obra.setVisible(false);
        date = new Date(Calendar.getInstance().getTimeInMillis());
        LocalDate localDate = date.toLocalDate();
        Fecha_Actual.setText(localDate.getDayOfMonth() + " de " + localDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " del " + localDate.getYear());
        Fecha_Form_Valoracion.setText(localDate.getDayOfMonth() + " de " + localDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) + " del " + localDate.getYear());

        Rango_1.valueProperty().addListener((observable, viejo, nuevo) -> {
            Rango_2.setValue(Rango_1.getValue());
            if (nuevo.isBefore(localDate)) {
                Rango_2.setValue(localDate);
            }
        });

        conexion = new Conexion();
        conexion.establecerConexion();

        Comision.Comisiones(conexion.getConnection(), comisiones);
        Museo.Museos(conexion.getConnection(), museos);
        cbx_Entrada_Tipo_Tarjeta.setItems(comisiones);
        cbx_Entrada_Museo.setItems(museos);
        conexion.cerrarConexion();

        TextFields.bindAutoCompletion(cbx_Entrada_Museo.getEditor(), cbx_Entrada_Museo.getItems());
        TextFields.bindAutoCompletion(cbx_Entrada_Tipo_Tarjeta.getEditor(), cbx_Entrada_Tipo_Tarjeta.getItems());
        cbx_Entrada_Museo.setConverter(new StringConverter<Museo>() {

            @Override
            public String toString(Museo object) {
                return object == null ? "" : object.getNombre();
            }

            @Override
            public Museo fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                for (Museo nd : cbx_Entrada_Museo.getItems()) {
                    if (nd.getNombre().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }

        });
        cbx_Entrada_Tipo_Tarjeta.setConverter(new StringConverter<Comision>() {

            @Override
            public String toString(Comision object) {
                return object == null ? "" : object.getTipo_Tarjeta();
            }

            @Override
            public Comision fromString(String string) {
                if (string == null || string.isEmpty()) {
                    return null;
                }
                for (Comision nd : cbx_Entrada_Tipo_Tarjeta.getItems()) {
                    if (nd.getTipo_Tarjeta().equalsIgnoreCase(string)) {
                        return nd;
                    }
                }
                return null;
            }
        });
        txt_Reseña.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue< ? extends String> observable, String viejo, String nuevo) {
                if (viejo != nuevo) {
                    txt_Limit.setStyle("-fx-text-fill: white;");
                }
                if (nuevo.length() > 200) {
                    txt_Reseña.deleteNextChar();
                    txt_Limit.setText("200" + "/200");
                } else {
                    txt_Limit.setText(nuevo.length() + "/200");
                }

            }
        });
    }

    public void MostrarAviso(String aviso) {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        JFXButton button = new JFXButton();
        button.setText("OK.");

        button.setStyle("-fx-background-color:#3b6978;-fx-text-fill : white ;-fx-border-color:#3b6978;-fx-border-width: 0 3 3 0;");
        dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialog.setDisable(false);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            dialog.close();
        });
        dialogLayout.setHeading(new Label(aviso));
        dialogLayout.setActions(button);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            rootAnchor.setEffect(null);
            dialog.setDisable(true);

        });
        rootAnchor.setEffect(blur);

    }

    @FXML
    private void Agregar(ActionEvent event) throws WriterException, IOException {
        if (txt_Visitante.getText().length() != 0
                && cbx_Entrada_Museo.getValue() != null
                && cbx_Entrada_Tipo_Tarjeta.getValue() != null && DatePicker_Entrada.getValue() != null) {
            cbx_Entrada_Museo.setEditable(true);
            conexion.establecerConexion();
            lista = new Listado(cbx_Entrada_Museo.getSelectionModel().getSelectedItem().getCodigo(), cbx_Entrada_Museo.getSelectionModel().getSelectedItem().getNombre(), 0.0, DatePicker_Entrada, Date.valueOf(DatePicker_Entrada.getValue()));
            double precio = lista.GetPreciosFromBD(conexion.getConnection());
            conexion.cerrarConexion();
            if (precio != 0) {
                lista.setPrecio(precio);
                boolean found = false;
                for (int i = 0; i < listado.size(); i++) {
                    if (lista.getCodigo() == listado.get(i).getCodigo() && lista.getFecha().equals(listado.get(i).getFecha())) {
                        found = true;
                    }
                }
                if (found == false) {
                    listado.add(lista);
                    ActualizarEstadoVenta();
                } else {
                    MostrarAviso("No puede agregar el mismo museo con la misma fecha mas de una vez");
                }

            } else {
                MostrarAviso("El museo no tiene un precio, agregue uno por favor...");
            }

        } else {
            MostrarAviso("Complete los campos...");
        }
    }

    private void ActualizarEstadoVenta() {

        tbl_View_Entrada.setItems(listado);

        MuseoNombre.setCellValueFactory(new PropertyValueFactory<Listado, String>("Museo"));
        _Precio.setCellValueFactory(new PropertyValueFactory<Listado, Number>("Precio"));
        Fecha.setCellValueFactory(new PropertyValueFactory<Listado, Date>("Fecha"));
        double subtotal = 0;
        double iva = 0;
        double total = 0;
        for (int i = 0; i < listado.size(); i++) {
            subtotal += listado.get(i).getPrecio();
            SubTotal.setText(Double.toString(subtotal));
            iva = subtotal * cbx_Entrada_Tipo_Tarjeta.getSelectionModel().getSelectedItem().getComision();
            IVA.setText(Double.toString(iva));
            total = subtotal + iva;
            TOTAL.setText(Double.toString(total));

        }
    }

    @FXML
    private void Vender(ActionEvent event) {
        int resultado = 0;
        if (listado.size() != 0) {
            conexion.establecerConexion();
            String CodigoQR = "";
            try {
                CodigoQR = Listado.generarQR(conexion.getConnection(), txt_Visitante.getText(), ((JFXButton) (btn_Vender)).getScene().getWindow());
                if (CodigoQR != null) {
                    Entrada entrada = new Entrada(CodigoQR, Double.parseDouble(IVA.getText()), txt_Visitante.getText(), cbx_Entrada_Museo.getSelectionModel().getSelectedItem(), cbx_Entrada_Tipo_Tarjeta.getSelectionModel().getSelectedItem());
                    resultado = entrada.VenderEntrada(conexion.getConnection(), listado, date, Double.parseDouble(TOTAL.getText()));
                    conexion.cerrarConexion();

                    if (resultado == 1) {
                        MostrarAviso("Entrada vendida con éxito");
                        listado.clear();
                        ActualizarEstadoVenta();
                        txt_Visitante.setText("");
                        cbx_Entrada_Tipo_Tarjeta.setValue(null);
                        cbx_Entrada_Museo.setValue(null);
                        DatePicker_Entrada.setValue(null);
                        SubTotal.setText("0");
                        TOTAL.setText("0");
                        IVA.setText("0");
                    } else {
                        MostrarAviso("Ocurrió un error al intentar vender la entrada");
                    }
                } else {
                    MostrarAviso("Seleccione una ubicacion para guardar el codigo QR.");
                }
            } catch (WriterException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            MostrarAviso("Debe agregar entradas para vender");
        }

    }

    @FXML
    private void ValidarQR(MouseEvent event) throws IOException {
        CodigoQR codigoQR = new CodigoQR();
        String QR = "";
        txt_Area_Museos.setText("");
        try {
            conexion.establecerConexion();
            QR = codigoQR.LeerQR(CodigoQR_Image);
            if (QR != null) {

                Entrada.ValidarEntrada(conexion.getConnection(), QR, date, txt_Area_Museos);
                conexion.cerrarConexion();
                if (txt_Area_Museos.getText().length() == 0) {
                    MostrarAviso("La entrada no es valida");
                } else {
                    Pane_Validacion_Entrada.setVisible(true);
                }
            } else {
                MostrarAviso("Ocurrio un error al leer el codigo");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void OptionVenderEntrada(ActionEvent event) {
        VentaEntrada.setVisible(true);
        ValidarEntrada.setVisible(false);
        Mantenimientos.setVisible(false);
        Valoracion.setVisible(false);
        Pane_Form_Valoracion.setVisible(false);
        Pane_Reseña_Obra.setVisible(false);
        Reportes.setVisible(false);
        Reportes_Obras.setVisible(false);

    }

    @FXML
    private void OptionValidarEntrada(ActionEvent event) {
        Pane_Validacion_Entrada.setVisible(false);
        VentaEntrada.setVisible(false);
        ValidarEntrada.setVisible(true);
        Mantenimientos.setVisible(false);
        Valoracion.setVisible(false);
        Pane_Form_Valoracion.setVisible(false);
        Pane_Reseña_Obra.setVisible(false);
        Reportes.setVisible(false);
        Reportes_Obras.setVisible(false);
    }

    @FXML
    private void OptionMantenimientos(MouseEvent event) {

        try {
            Valoracion.setVisible(false);
            VentaEntrada.setVisible(false);
            ValidarEntrada.setVisible(false);
            Mantenimientos.setVisible(true);
            tbl_View_Mantenimientos.getItems().clear();
            Pane_Form_Valoracion.setVisible(false);
            Pane_Reseña_Obra.setVisible(false);
            cmb_Filtro2.getItems().clear();
            cmb_Filtro2.setPromptText("");
            txt_Filtro_Field.setText("");
            Reportes.setVisible(false);
            Reportes_Obras.setVisible(false);
        } catch (UnsupportedOperationException e) {

        }

    }

    @FXML
    private void OptionValoracion(MouseEvent event) {
        VentaEntrada.setVisible(false);
        ValidarEntrada.setVisible(false);
        Mantenimientos.setVisible(false);
        Valoracion.setVisible(true);
        Pane_Reseña_Obra.setVisible(false);
        Reportes.setVisible(false);
        Reportes_Obras.setVisible(false);
        btn_Valorar.setVisible(true);
    }

    @FXML
    private void OptionReporteComisiones(ActionEvent event) {
        VentaEntrada.setVisible(false);
        ValidarEntrada.setVisible(false);
        Mantenimientos.setVisible(false);
        Valoracion.setVisible(false);
        Pane_Form_Valoracion.setVisible(false);
        Pane_Reseña_Obra.setVisible(false);
        Reportes_Obras.setVisible(false);
        Reportes.setVisible(true);
        tbl_View_Rango_Comisiones.getItems().clear();
    }

    @FXML
    private void OptionReporteValoraciones(ActionEvent event) {
        VentaEntrada.setVisible(false);
        ValidarEntrada.setVisible(false);
        Mantenimientos.setVisible(false);
        Valoracion.setVisible(false);
        Pane_Form_Valoracion.setVisible(false);
        Pane_Reseña_Obra.setVisible(false);
        Reportes.setVisible(false);
        Reportes_Obras.setVisible(true);
        tbl_View_Reporte_Obras.getItems().clear();
    }

    @FXML
    private void OptionColecciones(ActionEvent event) {
        tipo = "Coleccion";
        OptionColecciones();

    }

    public void OptionColecciones() {
        try {
            conexion.establecerConexion();
            Colecciones.clear();
            Coleccion.Colecciones(conexion.getConnection(), Colecciones);
            NewFiltro();
            Mantenimiento.clear();
            for (int i = 0; i < Colecciones.size(); i++) {
                Mantenimiento.add(new Mantenimiento(Colecciones.get(i).getCodigo(), Colecciones.get(i).getNombre(), Colecciones.get(i).getSiglo(), Colecciones.get(i).getObservacion(), Colecciones.get(i).getMuseoNombre()));
            }
            cmb_Filtro2.setItems(Mantenimiento);
            tbl_View_Mantenimientos.setItems(Mantenimiento);

            Colum1.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna1"));
            Colum1.setText("Nombre");
            Colum2.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna2"));
            Colum2.setText("Siglo");
            Colum3.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna3"));
            Colum3.setText("Observacion");
            Colum4.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna4"));
            Colum4.setText("Museo");
            conexion.cerrarConexion();
        } catch (Exception e) {
            MostrarAviso("Ocurrio un error");
        }
    }

    @FXML
    private void OptionArtistas(ActionEvent event) {
        tipo = "Artista";
        OptionArtistas();
    }

    public void OptionArtistas() {
        try {
            conexion.establecerConexion();
            Artistas.clear();
            Artista.Artistas(conexion.getConnection(), Artistas);
            NewFiltro();

            Mantenimiento.clear();
            for (int i = 0; i < Artistas.size(); i++) {
                Mantenimiento.add(new Mantenimiento(Artistas.get(i).getCodigo(), Artistas.get(i).getNombre(), Artistas.get(i).getPais(), Artistas.get(i).getBiografia(), ""));
            }
            cmb_Filtro2.setItems(Mantenimiento);
            TextFields.bindAutoCompletion(cmb_Filtro2.getEditor(), cmb_Filtro2.getItems());
            tbl_View_Mantenimientos.setItems(Mantenimiento);

            Colum1.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna1"));
            Colum1.setText("Nombre");
            Colum2.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna2"));
            Colum2.setText("Pais");
            Colum3.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna3"));
            Colum3.setText("Biografia");
            Colum4.setText("");

            conexion.cerrarConexion();
        } catch (Exception e) {
            MostrarAviso("Ocurrio un error");
        }

    }

    @FXML
    private void OptionComisiones(ActionEvent event) {
        tipo = "Comision";
        OptionComisiones();
    }

    public void OptionComisiones() {
        try {
            conexion.establecerConexion();
            comisiones.clear();
            Comision.Comisiones(conexion.getConnection(), comisiones);
            NewFiltro();
            Mantenimiento.clear();
            for (int i = 0; i < comisiones.size(); i++) {
                Mantenimiento.add(new Mantenimiento(comisiones.get(i).getCodigo(), comisiones.get(i).getTipo_Tarjeta(), Double.toString(comisiones.get(i).getComision()), "", ""));
            }
            cmb_Filtro2.setItems(Mantenimiento);
            tbl_View_Mantenimientos.setItems(Mantenimiento);
            Colum1.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna1"));
            Colum1.setText("Tipo Tarjeta");
            Colum2.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna2"));
            Colum2.setText("Comision");
            Colum3.setText("");
            Colum4.setText("");
            conexion.cerrarConexion();
        } catch (Exception e) {
            MostrarAviso("Ocurrio un error");
        }

    }

    @FXML
    private void OptionMuseos(ActionEvent event) {
        tipo = "Museo";
        OptionMuseos();

    }

    public void OptionMuseos() {
        try {
            conexion.establecerConexion();
            museos.clear();
            Museo.Museos(conexion.getConnection(), museos);
            NewFiltro();

            Mantenimiento.clear();
            for (int i = 0; i < museos.size(); i++) {
                Mantenimiento.add(new Mantenimiento(museos.get(i).getCodigo(), museos.get(i).getNombre(), museos.get(i).getTipo(), "", ""));
            }
            cmb_Filtro2.setItems(Mantenimiento);

            tbl_View_Mantenimientos.setItems(Mantenimiento);

            Colum1.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna1"));
            Colum1.setText("Nombre");
            Colum2.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna2"));
            Colum2.setText("Tipo");

            Colum3.setText("");
            Colum4.setText("");
            conexion.cerrarConexion();
        } catch (Exception e) {
            MostrarAviso("Ocurrio un error");
        }

    }

    @FXML
    private void OptionObras(ActionEvent event) {
        tipo = "Obra";
        OptionObras();
    }

    public void OptionObras() {
        try {
            conexion.establecerConexion();
            Obras.clear();
            Obra.Obras(conexion.getConnection(), Obras);

            NewFiltro();
            Mantenimiento.clear();
            for (int i = 0; i < Obras.size(); i++) {
                Mantenimiento.add(new Mantenimiento(Obras.get(i).getCodigo(), Obras.get(i).getNombre(), Obras.get(i).getCultura().getNombre(), Obras.get(i).getColeccion().getNombre(), Obras.get(i).getColeccion().getMuseoNombre()));
            }
            cmb_Filtro2.setItems(Mantenimiento);

            tbl_View_Mantenimientos.setItems(Mantenimiento);
            Colum1.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna1"));
            Colum1.setText("Nombre");
            Colum2.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna2"));
            Colum2.setText("Cultura");
            Colum3.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna3"));
            Colum3.setText("Coleccion");
            Colum4.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna4"));
            Colum4.setText("Museo");
            conexion.cerrarConexion();
        } catch (Exception e) {
            MostrarAviso("Ocurrio un error");
        }

    }

    @FXML
    private void OptionPrecios(ActionEvent event) {
        tipo = "Precio";
        OptionPrecios();
    }

    public void OptionPrecios() {

        try {
            conexion.establecerConexion();
            Precios.clear();
            Precio.Precios(conexion.getConnection(), Precios);
            NewFiltro();
            Mantenimiento.clear();
            for (int i = 0; i < Precios.size(); i++) {
                Mantenimiento.add(new Mantenimiento(Precios.get(i).getCodigo(), Precios.get(i).getMuseoNombre(), Double.toString(Precios.get(i).getPrecio_Semanal()), Double.toString(Precios.get(i).getPrecio_Domingo()), ""));
            }
            cmb_Filtro2.setItems(Mantenimiento);
            tbl_View_Mantenimientos.setItems(Mantenimiento);

            Colum1.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna1"));
            Colum1.setText("Museo");
            Colum2.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna2"));
            Colum2.setText("Precio Semanal");
            Colum3.setCellValueFactory(new PropertyValueFactory<Mantenimiento, String>("Columna3"));
            Colum3.setText("Precio Domingo");
            Colum4.setText("");
            conexion.cerrarConexion();
        } catch (Exception e) {
            MostrarAviso("Ocurrio un error");
        }

    }

    private void EliminarRegistro() {
        try {
            int resultado = 0;
            conexion.establecerConexion();
            if (tbl_View_Mantenimientos.getSelectionModel().getSelectedItem() == null || tbl_View_Mantenimientos.getSelectionModel().getSelectedIndex() == -1) {
                MostrarAviso("Para eliminar debe seleccionar algun registro.");
            } else {

                int selected = tbl_View_Mantenimientos.getSelectionModel().getSelectedIndex();
                if (tipo.equals("Obra")) {
                    for (int i = 0; i < Obras.size(); i++) {
                        if (Obras.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    resultado = Obras.get(selected).eliminarObra(conexion.getConnection());

                } else if (tipo.equals("Museo")) {
                    for (int i = 0; i < museos.size(); i++) {
                        if (museos.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    resultado = museos.get(selected).eliminarMuseo(conexion.getConnection());
                } else if (tipo.equals("Coleccion")) {
                    for (int i = 0; i < Colecciones.size(); i++) {
                        if (Colecciones.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    resultado = Colecciones.get(selected).eliminarColeccion(conexion.getConnection());
                } else if (tipo.equals("Precio")) {
                    for (int i = 0; i < Precios.size(); i++) {
                        if (Precios.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    resultado = Precios.get(selected).eliminarPrecio(conexion.getConnection());
                } else if (tipo.equals("Comision")) {
                    for (int i = 0; i < comisiones.size(); i++) {
                        if (comisiones.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    resultado = comisiones.get(selected).eliminarComision(conexion.getConnection());
                } else if (tipo.equals("Artista")) {
                    for (int i = 0; i < Artistas.size(); i++) {
                        if (Artistas.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    resultado = Artistas.get(selected).eliminarArtista(conexion.getConnection());
                }
                if (resultado == 1) {
                    Mantenimiento.remove(selected);
                    txt_Filtro_Field.setText("");
                    cmb_Filtro2.setValue(null);
                }
                conexion.cerrarConexion();
            }
        } catch (Exception e) {
            MostrarAviso("Ocurrio un error");
        }

    }

    @FXML
    private void Eliminar(ActionEvent event) {

        BoxBlur blur = new BoxBlur(3, 3, 3);
        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        JFXButton aceptar = new JFXButton();
        aceptar.setText("SI.");
        JFXButton cancelar = new JFXButton();
        cancelar.setText("CANCELAR");
        aceptar.setStyle("-fx-background-color:#3b6978;-fx-text-fill : white ;-fx-border-color:#3b6978;-fx-border-width: 0 3 3 0;");
        cancelar.setStyle("-fx-background-color:#da7272;-fx-text-fill : white ;-fx-border-color:#da7272;-fx-border-width: 0 3 3 0;");

        dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialog.setDisable(false);
        cancelar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            dialog.close();
        });
        aceptar.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {

            EliminarRegistro();
            dialog.close();
        });
        dialogLayout.setHeading(new Label("Esta seguro que desea eliminar este registro ?"));
        dialogLayout.setActions(cancelar, aceptar);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1) -> {
            rootAnchor.setEffect(null);
            dialog.setDisable(true);

        });
        rootAnchor.setEffect(blur);

    }

    @FXML
    private void OptionAgregar(ActionEvent event) {
        try {
            if (tipo.equals("Obra")) {
                Obra obra = new Obra();
                obra.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Agregar", null, controller);
            } else if (tipo.equals("Museo")) {
                Museo museo = new Museo();
                museo.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Agregar", null, controller);

            } else if (tipo.equals("Precio")) {
                Precio precio = new Precio();
                precio.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Agregar", null, controller);
            } else if (tipo.equals("Comision")) {
                Comision comision = new Comision();
                comision.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Agregar", null, controller);

            } else if (tipo.equals("Artista")) {
                Artista artista = new Artista();
                artista.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Agregar", null, controller);
            } else if (tipo.equals("Coleccion")) {
                Coleccion coleccion = new Coleccion();
                coleccion.AbrirFormulario(rootPrincipal, ((btn_AgregarForm)).getScene().getWindow(), "Agregar", null, controller);

            }
        } catch (Exception e) {
            MostrarAviso("Ocurrio un error");
        }

    }

    @FXML
    private void OptionEditar(ActionEvent event) {
        try {
            int selected = tbl_View_Mantenimientos.getSelectionModel().getSelectedIndex();
            if (tbl_View_Mantenimientos.getSelectionModel().getSelectedItem() != null) {
                if (tipo.equals("Obra")) {
                    for (int i = 0; i < Obras.size(); i++) {
                        if (Obras.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    Obra obra = new Obra();
                    obra.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Editar", Obras.get(selected), controller);
                } else if (tipo.equals("Museo")) {
                    for (int i = 0; i < museos.size(); i++) {
                        if (museos.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    Museo museo = new Museo();
                    museo.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Editar", museos.get(selected), controller);
                } else if (tipo.equals("Coleccion")) {
                    for (int i = 0; i < Colecciones.size(); i++) {
                        if (Colecciones.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    Coleccion coleccion = new Coleccion();
                    coleccion.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Editar", Colecciones.get(selected), controller);
                } else if (tipo.equals("Precio")) {
                    for (int i = 0; i < Precios.size(); i++) {
                        if (Precios.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    Precio precio = new Precio();
                    precio.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_Eliminar)).getScene().getWindow(), "Editar", Precios.get(selected), controller);
                } else if (tipo.equals("Comision")) {
                    for (int i = 0; i < comisiones.size(); i++) {
                        if (comisiones.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    Comision comision = new Comision();
                    comision.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Editar", comisiones.get(selected), controller);
                } else if (tipo.equals("Artista")) {
                    for (int i = 0; i < Artistas.size(); i++) {
                        if (Artistas.get(i).getCodigo() == tbl_View_Mantenimientos.getSelectionModel().getSelectedItem().getCodigo()) {
                            selected = i;
                        }
                    }
                    Artista artista = new Artista();
                    artista.AbrirFormulario(rootPrincipal, ((JFXButton) (btn_AgregarForm)).getScene().getWindow(), "Editar", Artistas.get(selected), controller);

                }

            } else {
                MostrarAviso("Seleccione una opcion");
            }

        } catch (Exception e) {

            MostrarAviso("Ocurrio un error");
        }

    }

    public void NewFiltro() {

        txt_Filtro_Field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                tbl_View_Mantenimientos.setItems(Mantenimiento);
            }
            filteredList.setPredicate(Mantenimiento -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (Mantenimiento.getColumna1().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        //        
        cmb_Filtro2.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                tbl_View_Mantenimientos.setItems(Mantenimiento);

            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    filteredList.setPredicate(Mantenimiento -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (Mantenimiento.getColumna1().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true; // Filter matches first name.
                        } else {
                            return false; // Does not match.
                        }
                    });
                }
            });

        });
        cmb_Filtro2.setItems(filteredList);
    }

    @FXML
    private void OptionFiltrar(ActionEvent event) {

        if (txt_Filtro_Field.getText().length() == 0 && cmb_Filtro2.getValue() == null) {
            MostrarAviso("Debe llenar alguna opcion de filtro para poder mostrar informacion");
        } else {
            SortedList<Mantenimiento> sortedData = new SortedList<>(filteredList);
            sortedData.comparatorProperty().bind(tbl_View_Mantenimientos.comparatorProperty());

            tbl_View_Mantenimientos.setItems(sortedData);
        }
    }

    @FXML
    private void Siguiente(MouseEvent event) {

        if (position + 1 <= listaImgs.size() - 1) {
            position = position + 1;

            img_Obra.setImage(listaImgs.get(position).getImage());
            lbl_Cantidad_Imagenes.setText((position + 1) + "/" + listaImgs.size());

        }
    }

    @FXML
    private void Anterior(MouseEvent event) {
        if (position - 1 >= 0) {
            position = position - 1;

            img_Obra.setImage(listaImgs.get(position).getImage());
            lbl_Cantidad_Imagenes.setText((position + 1) + "/" + listaImgs.size());

        }
    }

    @FXML
    private void ValidarQRObra(MouseEvent event) throws IOException {
        CodigoQR codigoQR = new CodigoQR();
        String QR = "";
        Obra obra = new Obra();
        img_Obra.setImage(null);
        lbl_Cantidad_Imagenes.setText("0/0");
        txt_Reseña.setText("");
        try {
            QR = codigoQR.LeerQR(CodigoQRObra);
            if (QR == null) {
                MostrarAviso("Ocurrió un error al intentar leer la imagen...");
            } else {
                conexion.establecerConexion();
                obra.getObraByQR(conexion.getConnection(), QR);;
                conexion.cerrarConexion();
                this.obra = obra.getObra();
                if (this.obra != null) {
                    Pane_Form_Valoracion.setVisible(true);
                    conexion.establecerConexion();
                    imgs_Obra.getImages(conexion.getConnection(), this.obra.getCodigo(), listaImgs);
                    Artista artista = this.obra.getArtista(conexion.getConnection());
                    Double porcentaje = this.obra.getCatalogo(conexion.getConnection());
                    conexion.cerrarConexion();
                    BigDecimal bigDecimal = new BigDecimal(porcentaje * 20).setScale(2, RoundingMode.UP);
                    lbl_Porcentaje.setText(bigDecimal.toString() + "%");
                    txt_Nombre_Obra.setText(this.obra.getNombre());
                    txt_Nombre_Coleccion.setText(this.obra.getColeccion().getNombre());
                    txt_Tipo_Obra.setText(this.obra.getTipoObra().getNombre());
                    txt_Cultura.setText(this.obra.getCultura().getNombre());
                    if (artista != null) {
                        txt_Nombre_Artista.setText(artista.getNombre());
                    } else {
                        txt_Nombre_Artista.setText("Anonima");
                    }
                    if (this.obra.getReconocida().equals("Si")) {
                        lbl_Reconocida.setText("Obra reconocida");
                    } else {
                        lbl_Reconocida.setText("Obra no reconocida");
                    }
                    txt_Detalle_Obra.setText(this.obra.getDetalle());

                    if (listaImgs.size() != 0) {
                        img_Obra.setImage(listaImgs.get(position).getImage());
                        lbl_Cantidad_Imagenes.setText((position + 1) + "/" + listaImgs.size());
                    }
                    Double p = 5.0;
                    Boolean[] visitados = new Boolean[]{false, false, false, false, false};
                    Label[] stars = new Label[]{star_1, star_2, star_3, star_4, star_5};
                    Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR_ALT, "2em");;

                    for (int i = 4; i >= 0; i--) {
                        if ((p - 0.5) < porcentaje && visitados[i] == false) {
                            visitados[i] = true;
                            Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR, "2em");
                            Icon.setFill(Color.YELLOW);
                            stars[i].setGraphic(Icon);

                        }

                        if ((p - 1) <= porcentaje && visitados[i] == false && porcentaje != 1) {
                            visitados[i] = true;
                            Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR_HALF_ALT, "2em");
                            Icon.setFill(Color.YELLOW);
                            stars[i].setGraphic(Icon);

                        }
                        if ((p - 1) >= porcentaje && visitados[i] == false) {
                            visitados[i] = true;

                            Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR_ALT, "2em");
                            Icon.setFill(Color.YELLOW);
                            stars[i].setGraphic(Icon);

                        }
                        p = p - 1;

                    }

                } else {
                    Pane_Form_Valoracion.setVisible(false);
                    MostrarAviso("El codigoQR no pertene a ninguna obra existente...");
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            MostrarAviso("Ocurrio un error");
        } catch (NotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            MostrarAviso("Ocurrio un error");
        }

    }

    @FXML
    private void ValorarObra(ActionEvent event) {
        btn_Valorar.setVisible(false);
        Pane_Reseña_Obra.setVisible(true);

        try {
            BoxBlur blur = new BoxBlur(3, 3, 3);

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/museocastillosforzesco/Valoracion.fxml"));
            Parent root = loader.load();
            ValoracionController valoracionController = (ValoracionController) loader.getController();
            stage.getIcons().add(new javafx.scene.image.Image("/Imagenes/icon.png"));
            stage.setTitle("Museo Castillo Sforzesco");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new javafx.scene.image.Image("/Imagenes/icon.png"));
            stage.setTitle("Museo Castillo Sforzesco");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((JFXButton) (btn_Valorar)).getScene().getWindow());
            stage.setResizable(false);
            stage.show();
            rootPrincipal.setEffect(blur);
            stage.setOnHidden((WindowEvent event1) -> {
                rootPrincipal.setEffect(null);
                valoracion = valoracionController.getValoracion();
            });
        } catch (IOException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
            MostrarAviso("Ocurrio un error");
        }
    }

    @FXML
    private void Cancelar_Obra(ActionEvent event) {
        btn_Valorar.setVisible(true);
        Pane_Reseña_Obra.setVisible(false);
        txt_Reseña.setText("");
    }

    @FXML
    private void Enviar_Valoracion(ActionEvent event) {
        int result = 0;
        if (this.obra != null) {
            conexion.establecerConexion();
            result = this.obra.AgregarValoracion(conexion.getConnection(), valoracion, txt_Reseña.getText());
            conexion.cerrarConexion();
            Pane_Form_Valoracion.setVisible(false);
            Pane_Reseña_Obra.setVisible(false);
            btn_Valorar.setVisible(true);
            txt_Reseña.setText("");
            if (result == 0) {
                MostrarAviso("Ocurrio un error");
            } else {
                MostrarAviso("Se agrego exitosamente");
            }
        }
    }

    @FXML
    private void FiltroInput(ActionEvent event) {
        cmb_Filtro2.setValue(null);
    }

    @FXML
    private void FiltroCombo(ActionEvent event) {
        txt_Filtro_Field.setText("");
    }

    @FXML
    private void GenerarInformacionComision(ActionEvent event) {

        if (Rango_1.getValue() != null && Rango_2.getValue() != null) {

            if (Rango_1.getValue().isBefore(Rango_2.getValue()) || Rango_1.getValue().isEqual(Rango_2.getValue())) {

                reportes.clear();
                conexion.establecerConexion();
                Generador_Reportes.ReporteComision(conexion.getConnection(), reportes, Rango_2.getValue().toString(), Rango_1.getValue().toString());
                conexion.cerrarConexion();
                tbl_View_Rango_Comisiones.setItems(reportes);
                Col_Reporte_1.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, String>("NombreTarjeta"));
                Col_Reporte_1.setText("Tarjeta");
                Col_Reporte_2.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, Double>("Comision"));
                Col_Reporte_2.setText("Comision");
                Fecha_Rango = Rango_1.getValue().toString() + " al " + Rango_2.getValue().toString();
                if (reportes.size() == 0) {
                    MostrarAviso("No se encontro información en el rango de fechas seleccionado.");
                }
            } else {
                MostrarAviso("El rango de las fechas no es correcto... \n" + Rango_1.getValue().toString() + " y " + Rango_2.getValue().toString());
                tbl_View_Rango_Comisiones.getItems().clear();
            }
        } else {
            MostrarAviso("Debe seleccionar un rango de fechas");
        }

    }

    @FXML
    private void GenerarPDFComision(ActionEvent event) {
        if (reportes.size() > 0) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("ReporteComisiones");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(((JFXButton) (btn_AgregarForm)).getScene().getWindow());

            if (file != null) {
                try {

                    Document document = new Document();

                    PdfWriter.getInstance(document, new FileOutputStream(file));

                    document.open();

                    Chunk titulo = new Chunk("Museo Castillo SForzesco", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.NORMAL, BaseColor.BLACK));
                    document.add(titulo);
                    document.add(Chunk.NEWLINE);
                    Image image = Image.getInstance("museo.png");

                    Paragraph txtParagraph = new Paragraph();

                    txtParagraph.add("Reporte de comisiones                                Generado el " + Fecha_Actual.getText() + "               ");
                    txtParagraph.add(new Chunk(image, 0, 0, true));
                    document.add(txtParagraph);

                    txtParagraph = new Paragraph("_____________________________________________________________________________ \n");
                    document.add(txtParagraph);
                    txtParagraph = new Paragraph("Rango de fecha: " + Fecha_Rango);
                    document.add(txtParagraph);
                    txtParagraph = new Paragraph("\n");
                    document.add(txtParagraph);

                    PdfPTable table = new PdfPTable(2);
                    PdfPCell c1 = new PdfPCell(new Phrase("Tarjeta"));
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase("Comisión"));
                    table.addCell(c1);
                    table.setHeaderRows(1);
                    for (int i = 0; i < reportes.size(); i++) {
                        table.addCell(reportes.get(i).getNombreTarjeta());
                        c1 = new PdfPCell(new Phrase(Double.toString(reportes.get(i).getComision()) + "$"));
                        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table.addCell(c1);
                    }
                    document.add(table);
                    document.close();
                } catch (Exception e) {
                    MostrarAviso("Ocurrio un error al generar el PDF.");
                }
            } else {
                MostrarAviso("Debe seleccionar una ruta correcta y agregar un nombre al archivo");
            }
        } else {
            MostrarAviso("No se ha generado información...");
        }
    }

    @FXML
    private void GenerarPDFObras(ActionEvent event) {
        if (reportes_Obras.size() > 0) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("ReporteObras");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(((JFXButton) (btn_AgregarForm)).getScene().getWindow());

            if (file != null) {
                try {

                    Document document = new Document();

                    PdfWriter.getInstance(document, new FileOutputStream(file));

                    document.open();

                    Chunk titulo = new Chunk("Museo Castillo SForzesco", FontFactory.getFont(FontFactory.TIMES_ROMAN, 20, Font.NORMAL, BaseColor.BLACK));
                    document.add(titulo);
                    document.add(Chunk.NEWLINE);
                    Image image = Image.getInstance("museo.png");

                    Paragraph txtParagraph = new Paragraph();

                    txtParagraph.add("Reporte de Obras                                Generado el " + Fecha_Actual.getText() + "               ");
                    txtParagraph.add(new Chunk(image, 0, 0, true));
                    document.add(txtParagraph);

                    txtParagraph = new Paragraph("_____________________________________________________________________________ \n");
                    document.add(txtParagraph);
                    txtParagraph = new Paragraph(tipo_Reporte);
                    document.add(txtParagraph);
                    txtParagraph = new Paragraph("\n");
                    document.add(txtParagraph);

                    PdfPTable table = new PdfPTable(5);
                    PdfPCell c1 = new PdfPCell(new Phrase("Museo"));
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase("Colección"));
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase("Obra"));
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase("Puntaje"));
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase("Porcentaje"));
                    table.addCell(c1);
                    table.setHeaderRows(1);
                    
                    for (int i = 0; i < reportes_Obras.size(); i++) {
                        table.addCell(reportes_Obras.get(i).getMuseo());
                        table.addCell(reportes_Obras.get(i).getColeccion());
                        table.addCell(reportes_Obras.get(i).getObra());
                        table.addCell(Integer.toString(reportes_Obras.get(i).getPuntaje()));
                        table.addCell(new BigDecimal(reportes_Obras.get(i).getPorcentaje()).setScale(3, RoundingMode.UP).toString() + "%");
                    }
                    document.add(table);
                    document.close();
                } catch (Exception e) {
                    MostrarAviso("Ocurrio un error al generar el PDF.");
                }
            } else {
                MostrarAviso("Debe seleccionar una ruta correcta y agregar un nombre al archivo");
            }
        } else {
            MostrarAviso("No se ha generado informacion...");
        }
    }

    @FXML
    private void OptionMejorValoradas(ActionEvent event) {
        tbl_View_Reporte_Obras.getItems().clear();
        museos.clear();
        conexion.establecerConexion();
        Museo.Museos(conexion.getConnection(), museos);
        conexion.cerrarConexion();
        tipo_Reporte = "Mejores obras valoradas por museo";
        if (museos.size() > 0) {
            reportes_Obras.clear();
            conexion.establecerConexion();
            Generador_Reportes.Reporte_Obras(conexion.getConnection(), reportes_Obras, museos, "DESC");
            conexion.cerrarConexion();
            if (reportes_Obras.size() > 0) {
                tbl_View_Reporte_Obras.setItems(reportes_Obras);
                Col_Nombre_Museo.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, String>("Museo"));

                Col_Nombre_Coleccion.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, String>("Coleccion"));
                Col_Nombre_Obra.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, String>("Obra"));
                Col_Porcentaje_Obra.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, Double>("Porcentaje"));
            } else {
                MostrarAviso("No hay valoraciones registradas");
            }
        } else {
            MostrarAviso("No hay museos");
        }
    }

    @FXML
    private void OptionPeorValoradas(ActionEvent event) {
        tbl_View_Reporte_Obras.getItems().clear();
        museos.clear();
        conexion.establecerConexion();
        Museo.Museos(conexion.getConnection(), museos);
        conexion.cerrarConexion();
        tipo_Reporte = "Peores obras valoradas por museo";
        if (museos.size() > 0) {
            reportes_Obras.clear();
            conexion.establecerConexion();
            Generador_Reportes.Reporte_Obras(conexion.getConnection(), reportes_Obras, museos, "ASC");
            conexion.cerrarConexion();
            if (reportes_Obras.size() > 0) {
                tbl_View_Reporte_Obras.setItems(reportes_Obras);
                Col_Nombre_Museo.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, String>("Museo"));

                Col_Nombre_Coleccion.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, String>("Coleccion"));
                Col_Nombre_Obra.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, String>("Obra"));
                Col_Porcentaje_Obra.setCellValueFactory(new PropertyValueFactory<Generador_Reportes, Double>("Porcentaje"));
            } else {
                MostrarAviso("No hay valoraciones registradas");
            }
        } else {
            MostrarAviso("No hay museos");
        }
    }

}
