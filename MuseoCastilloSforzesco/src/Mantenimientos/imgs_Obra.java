/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mantenimientos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Elias
 */
public class imgs_Obra {

    private IntegerProperty Codigo;
    private Image image;
    private IntegerProperty id_Obra;

    public imgs_Obra(int Codigo, Image image, int id_Obra) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.image = image;
        this.id_Obra = new SimpleIntegerProperty(id_Obra);
    }

    public int getCodigo() {
        return Codigo.get();
    }

    public void setCodigo(int Codigo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
    }

    public Image getImage() {
        return image;
    }

    public int getIDObra() {
        return id_Obra.get();
    }

    public static void getImages(Connection connection, int codigo, ArrayList<imgs_Obra> listaImgs) {
        listaImgs.clear();
        try {
            byte b[];

            PreparedStatement instruccion = connection.prepareStatement("select * from tbl_imagenes_obra where idObra= ?");
            instruccion.setInt(1, codigo);
            ResultSet imagenes = instruccion.executeQuery();
            while (imagenes.next()) {

                b = imagenes.getBytes(2);
                ByteArrayInputStream bis = new ByteArrayInputStream(b);
                BufferedImage read = ImageIO.read(bis);
                if (read != null) {
                    Image image = SwingFXUtils.toFXImage(read, null);
                    imgs_Obra img = new imgs_Obra(imagenes.getInt(1), image, imagenes.getInt(3));
                    listaImgs.add(img);
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(imgs_Obra.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
