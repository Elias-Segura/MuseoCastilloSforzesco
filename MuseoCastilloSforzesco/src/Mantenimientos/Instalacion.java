/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mantenimientos;

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
import javafx.scene.control.Label;

/**
 *
 * @author Elias
 */
public class Instalacion {

    private IntegerProperty Codigo;
    private IntegerProperty id_Instalacion_Museo;
    private StringProperty Nombre;
    private Label label = new Label();

    public Instalacion() {
    }

    public Instalacion(int Codigo, String Nombre) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Nombre = new SimpleStringProperty(Nombre);
        label.setText(Nombre);
    }

    public Instalacion(int Codigo, String Nombre, int id_Instalacion_Museo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Nombre = new SimpleStringProperty(Nombre);
        label.setText(Nombre);
        this.id_Instalacion_Museo = new SimpleIntegerProperty(id_Instalacion_Museo);
    }

    public int getIDInstalacionMuseo() {
        return id_Instalacion_Museo.get();
    }

    public Label getLabelName() {
        return label;
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

    public static void getInstalaciones(Connection connection, ArrayList<Instalacion> lista, int codigo, String status) {
        try {

            PreparedStatement instruccion = connection.prepareStatement(
                    "SELECT * FROM db_museo_castillo.tbl_instalaciones_museos A \n"
                    + "INNER JOIN db_museo_castillo.tbl_instalaciones B ON(A.id_Instalacion =B.id_Instalacion) \n"
                    + "WHERE A.id_Museo= ? and A.status= ? ");
            instruccion.setInt(1, codigo);
            instruccion.setString(2, status);
            ResultSet resultado = instruccion.executeQuery();

            lista.clear();
            while (resultado.next()) {
                lista.add(new Instalacion(resultado.getInt(5), resultado.getString(6), resultado.getInt(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Instalacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void Instalaciones(Connection connection, ObservableList<Instalacion> lista) {
        try {
            PreparedStatement instruccion = connection.prepareStatement("SELECT * FROM tbl_instalaciones");
            ResultSet resultado = instruccion.executeQuery();
            while (resultado.next()) {
                lista.add(new Instalacion(resultado.getInt(1), resultado.getString(2)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Instalacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return Nombre.get();
    }
}
