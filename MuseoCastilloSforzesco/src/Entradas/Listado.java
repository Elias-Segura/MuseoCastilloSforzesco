/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entradas;

import Mantenimientos.Precio;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author Elias
 */
public class Listado {

    private IntegerProperty Codigo;
    private StringProperty Museo;
    private DoubleProperty Precio;
    private Date Fecha_Entrada;
    private DatePicker fecha;

    Precio precio;

    public Listado(int Codigo, String Museo, Double Precio,
            DatePicker Fecha, Date FechaEntrada) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Museo = new SimpleStringProperty(Museo);
        this.Precio = new SimpleDoubleProperty(Precio);
        this.fecha = Fecha;
        this.Fecha_Entrada = FechaEntrada;
    }

    public DatePicker getFechaPicker() {
        return fecha;
    }

    public Date getFecha() {
        return Fecha_Entrada;
    }

    public void setfecha(DatePicker Fecha) {
        this.fecha = fecha;
    }

    public int getCodigo() {
        return Codigo.get();
    }

    public void setCodigo(int Codigo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
    }


    public String getMuseo() {
        return Museo.get();
    }

    public void setMuseo(String Museo) {
        this.Museo = new SimpleStringProperty(Museo);
    }

    public Double getPrecio() {
        return Precio.get();
    }

    public void setPrecio(Double Precio) {
        this.Precio = new SimpleDoubleProperty(Precio);
    }


    public double GetPreciosFromBD(Connection connection) {
        try {

            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM db_museo_castillo.tbl_precios WHERE id_Museo = ? and status = ? ");
            instruccion.setInt(1, getCodigo());
            instruccion.setString(2, "enabled");
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()) {
                precio = new Precio(resultado.getDouble(2), resultado.getDouble(3));
                LocalDate fechaNacimiento = getFechaPicker().getValue();
                if (fechaNacimiento.getDayOfWeek().toString().equals("SUNDAY")) {
                    return precio.getPrecio_Domingo();

                } else {
                    return precio.getPrecio_Semanal();

                }

            } else {
                return 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return 0;
    }

    public static String generarQR(Connection connection, String Visitante, Window window) throws WriterException, IOException {
        String qrCodeText = "";
        try {

            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM db_museo_castillo.tbl_generador WHERE Generador = ? ");
            instruccion.setString(1, "ETR");
            ResultSet resultado = instruccion.executeQuery();
            if (resultado.next()) {
                qrCodeText = "ETR00000" + Integer.toString(resultado.getInt(2));

            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try {

            Statement instruccion = connection.createStatement();

            instruccion.executeUpdate("UPDATE db_museo_castillo.tbl_generador set Num = Num+1 where Generador = '" + "ETR" + "'");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(Visitante + qrCodeText);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(window);

        if (file != null) {
            String filePath = file.getPath();
            int size = 150;
            String fileType = "png";
            File qrFile = new File(filePath);
            createQRImage(qrFile, qrCodeText, size, fileType);
            return qrCodeText;
        }

        return null;

    }

    private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
            throws WriterException, IOException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        ImageIO.write(image, fileType, qrFile);
    }
}
