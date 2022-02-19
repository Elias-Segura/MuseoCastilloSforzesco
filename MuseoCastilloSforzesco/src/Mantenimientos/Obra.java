/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mantenimientos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
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
import javax.imageio.ImageIO;
import museocastillosforzesco.FXMLDocumentController;
import museocastillosforzesco.ObraController;

/**
 *
 * @author Elias
 */
public class Obra {

    private IntegerProperty Codigo;
    private StringProperty Nombre;
    private StringProperty Detalle;
    private StringProperty Reconocida;
    private StringProperty CodigoQR;
    private Coleccion coleccion;
    private tipo_Cultura tipo_Cultura;
    private tipo_Obra tipo_Obra;
    private ArrayList<FileInputStream> imagenes;
    private ArrayList<File> fileImagenes;
    private ArrayList<imgs_Obra> listaImgs = new ArrayList<>();
    private Obra obra;

    public Obra() {
    }

    public Obra(int Codigo, String Nombre, tipo_Obra tipoObra, tipo_Cultura tipoCultura, String Detalle, String CodigoQR, Coleccion coleccion, String Reconocida) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Nombre = new SimpleStringProperty(Nombre);
        this.Detalle = new SimpleStringProperty(Detalle);
        this.CodigoQR = new SimpleStringProperty(CodigoQR);
        this.coleccion = coleccion;
        this.tipo_Cultura = tipoCultura;
        this.tipo_Obra = tipoObra;
        this.Reconocida = new SimpleStringProperty(Reconocida);
    }

    public Obra(int Codigo, String Nombre, tipo_Obra tipoObra, tipo_Cultura tipoCultura, String Detalle, String CodigoQR, Coleccion coleccion, ArrayList<FileInputStream> imagenes, ArrayList<File> fileImagenes) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Nombre = new SimpleStringProperty(Nombre);
        this.Detalle = new SimpleStringProperty(Detalle);
        this.CodigoQR = new SimpleStringProperty(CodigoQR);
        this.coleccion = coleccion;
        this.tipo_Cultura = tipoCultura;
        this.tipo_Obra = tipoObra;
        this.imagenes = imagenes;
        this.fileImagenes = fileImagenes;

    }

    public Obra getObra() {
        return obra;
    }

    public Coleccion getColeccion() {
        return coleccion;
    }

    public tipo_Cultura getCultura() {
        return tipo_Cultura;
    }

    public tipo_Obra getTipoObra() {
        return tipo_Obra;
    }

    public void setColeccion(Coleccion coleccion) {
        this.coleccion = coleccion;
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

    public String getDetalle() {
        return Detalle.get();
    }

    public void setDetalle(String Detalle) {
        this.Detalle = new SimpleStringProperty(Detalle);
    }

    public String getCodigoQR() {
        return CodigoQR.get();
    }

    public void setCodigoQR(String CodigoQR) {
        this.CodigoQR = new SimpleStringProperty(CodigoQR);
    }

    public Double getCatalogo(Connection connection) {
        int total = 0;
        int sumatoria = 0;
        double porcentaje = 0;

        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "SELECT * FROM db_museo_castillo.tbl_catalogo_obras \n"
                    + "WHERE id_Obra=?");
            instruccion.setInt(1, Codigo.get());
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()) {
                total = total + 1;
                sumatoria = sumatoria + resultado.getInt(3);

            }
            if (total != 0) {
                porcentaje = (double) sumatoria / (double) total;

            }
            return porcentaje;

        } catch (SQLException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);

        }
        return 0.0;
    }

    public static void Obras(Connection connection, ObservableList<Obra> lista) {
        try {

            PreparedStatement instruccion = connection.prepareStatement(
                    "SELECT * FROM db_museo_castillo.tbl_obras A \n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_obras B ON(A.id_Tipo =  B.id_Tipo) \n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_culturas C ON(A.id_Cultura =  C.id_Cultura) \n"
                    + "INNER JOIN db_museo_castillo.tbl_colecciones D ON(A.idColeccion =  D.id_Coleccion) \n"
                    + "INNER JOIN db_museo_castillo.tbl_museos E ON(D.id_Museo =  E.id_Museo) \n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_museo F ON(E.id_Tipo =  F.id_Tipo)"
                    + "WHERE A.status = ? and D.status = ? and E.status= ? "
            );
            instruccion.setString(1, "enabled");
            instruccion.setString(2, "enabled");
            instruccion.setString(3, "enabled");
            ResultSet resultado = instruccion.executeQuery();

            Obra obra;

            while (resultado.next()) {
                obra = new Obra(resultado.getInt(1), resultado.getString(2),
                        new tipo_Obra(resultado.getInt(10), resultado.getString(11)),
                        new tipo_Cultura(resultado.getInt(12), resultado.getString(13)),
                        resultado.getString(5), resultado.getString(6),
                        new Coleccion(resultado.getInt(14), resultado.getString(15), resultado.getString(16), resultado.getString(17),
                                new Museo(resultado.getInt(20), resultado.getString(21),
                                        new tipo_Museo(resultado.getInt(24), resultado.getString(25)), resultado.getString(23))), resultado.getString(8));
                lista.add(obra);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getObraByQR(Connection connection, String CodigoQR) {
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "SELECT * FROM db_museo_castillo.tbl_obras A \n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_obras B ON(A.id_Tipo =  B.id_Tipo) \n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_culturas C ON(A.id_Cultura =  C.id_Cultura) \n"
                    + "INNER JOIN db_museo_castillo.tbl_colecciones D ON(A.idColeccion =  D.id_Coleccion) \n"
                    + "INNER JOIN db_museo_castillo.tbl_museos E ON(D.id_Museo =  E.id_Museo) \n"
                    + "INNER JOIN db_museo_castillo.tbl_tipo_museo F ON(E.id_Tipo =  F.id_Tipo)"
                    + "WHERE A.CodigoQR = ? and A.status = ?"
            );
            instruccion.setString(1, CodigoQR);
            instruccion.setString(2, "enabled");
            ResultSet resultado = instruccion.executeQuery();
            boolean found = false;
            while (resultado.next()) {
                found = true;
                obra = new Obra(resultado.getInt(1), resultado.getString(2),
                        new tipo_Obra(resultado.getInt(10), resultado.getString(11)),
                        new tipo_Cultura(resultado.getInt(12), resultado.getString(13)),
                        resultado.getString(5), resultado.getString(6),
                        new Coleccion(resultado.getInt(14), resultado.getString(15), resultado.getString(16), resultado.getString(17),
                                new Museo(resultado.getInt(20), resultado.getString(21),
                                        new tipo_Museo(resultado.getInt(24), resultado.getString(25)), resultado.getString(23))), resultado.getString(8));
            }
            if (found == false) {

                this.obra = null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getReconocida() {
        return Reconocida.get();
    }

    public void setReconocida(String reconocida) {
        this.Reconocida = new SimpleStringProperty(reconocida);
    }

    public Artista getArtista(Connection connection) {

        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "SELECT * FROM db_museo_castillo.tbl_obras_artista A \n"
                    + "INNER JOIN db_museo_castillo.tbl_artistas B ON(A.idArtista = B.id_Artista) \n"
                    + "WHERE A.idObra= ? ");
            instruccion.setInt(1, Codigo.get());
            ResultSet resultado = instruccion.executeQuery();

            Artista artista;
            while (resultado.next()) {
                artista = new Artista(resultado.getInt(4), resultado.getString(5), resultado.getString(6), resultado.getString(7));
                return artista;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }

    public int getLastID(Connection connection) {
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public int AgregarObra(Connection connection, boolean artista, String reconocida, Artista artist) {

        try {

            PreparedStatement instruccion = connection.prepareStatement(
                    "START TRANSACTION ");
            instruccion.executeUpdate();
            instruccion = connection.prepareStatement(
                    "INSERT INTO tbl_obras (Nombre, id_Tipo, id_Cultura, Detalle, CodigoQR, idColeccion, Reconocida,status) VALUES (?,?,?,?,?,?,?,?)"
            );
            instruccion.setString(1, Nombre.get());
            instruccion.setInt(2, tipo_Obra.getCodigo());
            instruccion.setInt(3, tipo_Cultura.getCodigo());
            instruccion.setString(4, Detalle.get());
            instruccion.setString(5, Nombre.get() + "000");
            instruccion.setInt(6, coleccion.getCodigo());
            instruccion.setString(7, reconocida);
            instruccion.setString(8, "enabled");
            instruccion.executeUpdate();

            int ID = getLastID(connection);
            if (ID != -1) {

                if (artista == true) {
                    instruccion = connection.prepareStatement(
                            "INSERT INTO tbl_obras_artista (idArtista, idObra) VALUES (?,?);"
                    );
                    instruccion.setInt(1, artist.getCodigo());
                    instruccion.setInt(2, ID);
                    instruccion.executeUpdate();
                }
                for (int i = 0; i < imagenes.size(); i++) {

                    instruccion = connection.prepareStatement("INSERT INTO tbl_imagenes_obra (Imagen, idObra) VALUES(?,?)");
                    instruccion.setBinaryStream(1, imagenes.get(i), (int) fileImagenes.get(i).length());
                    instruccion.setInt(2, ID);
                    instruccion.executeUpdate();

                }

            }

            instruccion.executeUpdate();
            instruccion = connection.prepareStatement(
                    "COMMIT"
            );
            instruccion.executeUpdate();
            instruccion.close();

        } catch (SQLException ex) {

            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return 1;
    }

    public int eliminarObra(Connection connection) {
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_obras "
                            + " SET Nombre = ?,  "
                            + " id_Tipo= ?,  "
                            + " id_Cultura = ?, "
                            + " Detalle= ?, "
                            + " CodigoQR = ?, "
                            + " idColeccion= ?,"
                            + " Reconocida= ?,"
                            + " status= ?"
                            + " WHERE id_Obra = ?"
                    );
            instruccion.setString(1, Nombre.get());
            instruccion.setInt(2, tipo_Obra.getCodigo());
            instruccion.setInt(3, tipo_Cultura.getCodigo());
            instruccion.setString(4, Detalle.get());
            instruccion.setString(5, Nombre.get() + "000");
            instruccion.setInt(6, coleccion.getCodigo());
            instruccion.setString(7, Reconocida.get());
            instruccion.setString(8, "disabled");
            instruccion.setInt(9, Codigo.get());
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizarObra(Connection connection, ArrayList<imgs_Obra> lista, Artista artista) {

        imgs_Obra.getImages(connection, Codigo.get(), listaImgs);
        boolean found = false;
        try {
            PreparedStatement instruccion
                    = connection.prepareStatement("START TRANSACTION");
            instruccion.execute();
            instruccion
                    = connection.prepareStatement(
                            "UPDATE tbl_obras "
                            + " SET Nombre = ?,  "
                            + " id_Tipo= ?,  "
                            + " id_Cultura = ?, "
                            + " Detalle= ?, "
                            + " CodigoQR = ?, "
                            + " idColeccion= ?,"
                            + " Reconocida= ?"
                            + " WHERE id_Obra = ?"
                    );
            instruccion.setString(1, Nombre.get());
            instruccion.setInt(2, tipo_Obra.getCodigo());
            instruccion.setInt(3, tipo_Cultura.getCodigo());
            instruccion.setString(4, Detalle.get());
            instruccion.setString(5, Nombre.get() + "000");
            instruccion.setInt(6, coleccion.getCodigo());
            instruccion.setString(7, Reconocida.get());
            instruccion.setInt(8, Codigo.get());
            instruccion.executeUpdate();
            for (int i = 0; i < listaImgs.size(); i++) {
                for (int j = 0; j < lista.size(); j++) {
                    if (listaImgs.get(i).getCodigo() == lista.get(j).getCodigo()) {
                        found = true;
                    }
                }
                if (found == false) {
                    try {
                        instruccion = connection.prepareStatement(
                                "DELETE FROM  tbl_imagenes_obra WHERE idImagen= ?"
                        );
                        instruccion.setInt(1, listaImgs.get(i).getCodigo());
                        instruccion.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e);
                        e.printStackTrace();

                    }
                }
                found = false;
            }
            for (int i = 0; i < imagenes.size(); i++) {

                instruccion = connection.prepareStatement("INSERT INTO tbl_imagenes_obra (Imagen, idObra) VALUES(?,?)");
                instruccion.setBinaryStream(1, imagenes.get(i), (int) fileImagenes.get(i).length());
                instruccion.setInt(2, Codigo.get());
                instruccion.executeUpdate();

            }
            if (artista != null) {
                instruccion
                        = connection.prepareStatement(
                                "UPDATE tbl_obras_artista "
                                + " SET idArtista = ?,  "
                                + " idObra = ?  "
                                + " WHERE idObra = ?"
                        );
                instruccion.setInt(1, artista.getCodigo());
                instruccion.setInt(2, Codigo.get());
                instruccion.setInt(3, Codigo.get());

                int i = instruccion.executeUpdate();
                if (i == 0) {
                    instruccion = connection.prepareStatement(
                            "INSERT INTO tbl_obras_artista(idArtista, idObra) VALUES (?,?)"
                    );
                    instruccion.setInt(1, artista.getCodigo());
                    instruccion.setInt(2, Codigo.get());

                    instruccion.executeUpdate();
                }

            } else {
                try {
                    instruccion = connection.prepareStatement(
                            "DELETE FROM  tbl_obras_artista WHERE idObra= ?"
                    );
                    instruccion.setInt(1, Codigo.get());
                    instruccion.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            instruccion = connection.prepareStatement("COMMIT");
            instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
        return 1;
    }

    public void AbrirFormulario(AnchorPane rootPrincipal, Window window, String Accion, Obra obra, FXMLDocumentController controller) {

        try {
            BoxBlur blur = new BoxBlur(3, 3, 3);

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/museocastillosforzesco/Obra.fxml"));
            Parent root = loader.load();
            ObraController obraController = (ObraController) loader.getController();
            obraController.setFuncionalidad(obra, Accion, controller, stage);
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
                if (obraController.getResult() == true) {
                    if (Accion.equals("Editar")) {
                        controller.MostrarAviso("Se actualizo exitosamente");
                    } else {
                        controller.MostrarAviso("Se agrego exitosamente");
                    }
                   
                }
                controller.OptionObras();
            });
        } catch (IOException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int AgregarValoracion(Connection connection, int Valoracion, String Reseña) {

        try {
            PreparedStatement instruccion = connection.prepareStatement("INSERT INTO tbl_catalogo_obras (id_Obra, Valoracion, Reseña) VALUES(?,?,?)");
            instruccion.setInt(1, Codigo.get());
            instruccion.setInt(2, Valoracion);
            instruccion.setString(3, Reseña);
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Obra.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    @Override
    public String toString() {
        return Nombre.get();
    }
}
