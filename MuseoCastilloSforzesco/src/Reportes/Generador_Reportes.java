/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import Mantenimientos.Museo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Elias
 */
public class Generador_Reportes {

    private StringProperty NombreTarjeta;
    private DoubleProperty Comision;
    private DoubleProperty Fecha;
    private StringProperty Museo;
    private StringProperty Coleccion;
    private StringProperty Obra;
    private IntegerProperty Puntaje;
    private DoubleProperty Porcentaje;

    public Generador_Reportes(String NombreTarjeta, Double Comision) {
        this.NombreTarjeta = new SimpleStringProperty(NombreTarjeta);
        this.Comision = new SimpleDoubleProperty(Comision);

    }

    public Generador_Reportes(String Museo, String Coleccion, String Obra,
            int Puntaje, double Porcentaje) {
        this.Museo = new SimpleStringProperty(Museo);
        this.Coleccion = new SimpleStringProperty(Coleccion);
        this.Obra = new SimpleStringProperty(Obra);
        this.Puntaje = new SimpleIntegerProperty(Puntaje);
        this.Porcentaje = new SimpleDoubleProperty(Porcentaje);
    }

    public String getNombreTarjeta() {
        return NombreTarjeta.get();
    }

    public void setNombreTarjeta(String NombreTarjeta) {
        this.NombreTarjeta = new SimpleStringProperty(NombreTarjeta);
    }

    public Double getComision() {
        return Comision.get();
    }

    public void setComision(Double Comision) {
        this.Comision = new SimpleDoubleProperty(Comision);
    }

    public Double getFecha() {
        return Fecha.get();
    }

    public void setFecha(Double Fecha) {
        this.Fecha = new SimpleDoubleProperty(Fecha);
    }

    public String getMuseo() {
        return Museo.get();
    }

    public void setMuseo(String Museo) {
        this.Museo = new SimpleStringProperty(Museo);
    }

    public String getColeccion() {
        return Coleccion.get();
    }

    public void setColeccion(String Coleccion) {
        this.Coleccion = new SimpleStringProperty(Coleccion);
    }

    public String getObra() {
        return Obra.get();
    }

    public void setObra(String Obra) {
        this.Obra = new SimpleStringProperty(Obra);
    }

    public int getPuntaje() {
        return Puntaje.get();
    }

    public void setPuntaje(int Puntaje) {
        this.Puntaje = new SimpleIntegerProperty(Puntaje);
    }

    public Double getPorcentaje() {
        return Porcentaje.get();
    }

    public void setPorcentaje(Double Porcentaje) {
        this.Porcentaje = new SimpleDoubleProperty(Porcentaje);
    }

    public static void ReporteComision(Connection connection, ObservableList<Generador_Reportes> lista, String Rango1, String Rango2) {

        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    " SELECT a.id_Tarjeta, a.Tipo_Tarjeta,SUM(b.Comision)Comision \n"
                    + "FROM  db_museo_castillo.tbl_tarjetas a  \n"
                    + "LEFT JOIN db_museo_castillo.tbl_entrada b ON (a.id_Tarjeta = b.Tarjeta) \n"
                    + "WHERE Fecha_Venta <=? and Fecha_Venta>=? \n"
                    + "GROUP BY a.id_Tarjeta, a.Tipo_Tarjeta"
            );
            instruccion.setString(1, Rango1);
            instruccion.setString(2, Rango2);
            ResultSet resultado = instruccion.executeQuery();
            Generador_Reportes reporte;
            while (resultado.next()) {
                reporte = new Generador_Reportes(resultado.getString(2), new BigDecimal(resultado.getDouble(3)).setScale(3, RoundingMode.UP).doubleValue());
                lista.add(reporte);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Generador_Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Reporte_Obras(Connection connection, ObservableList<Generador_Reportes> lista, ObservableList<Museo> museos, String tipo) {

        try {
            for (int i = 0; i < museos.size(); i++) {
                PreparedStatement instruccion = connection.prepareStatement(
                        "SELECT A.id_Museo, A.Nombre, B.Nombre, C.Nombre, SUM(D.Valoracion)Valoracion ,COUNT(D.id_Obra)Cantidad \n"
                        + "FROM db_museo_castillo.tbl_museos A \n"
                        + "LEFT JOIN db_museo_castillo.tbl_colecciones B ON(A.id_Museo = B.id_Museo) \n"
                        + "LEFT JOIN db_museo_castillo.tbl_obras C ON(B.id_Coleccion = C.idColeccion) \n"
                        + "LEFT JOIN db_museo_castillo.tbl_catalogo_obras D ON(C.id_Obra = D.id_Obra ) \n"
                        + "WHERE A.id_Museo = ?  and D.Valoracion>=? \n"
                        + "GROUP BY A.id_Museo,C.Nombre \n"
                        + "ORDER BY A.id_Museo, Valoracion " + tipo + " \n"
                        + "LIMIT 10");
                instruccion.setInt(1, museos.get(i).getCodigo());
                instruccion.setInt(2, 0);
                ResultSet resultado = instruccion.executeQuery();
                Generador_Reportes reporte;
                while (resultado.next()) {

                    reporte = new Generador_Reportes(resultado.getString(2), resultado.getString(3), resultado.getString(4), resultado.getInt(5), (double) resultado.getInt(5) / (double) resultado.getInt(6));
                    lista.add(reporte);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Generador_Reportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
