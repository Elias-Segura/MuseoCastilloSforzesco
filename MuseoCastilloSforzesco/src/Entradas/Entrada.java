/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entradas;

import Mantenimientos.Comision;
import Mantenimientos.Museo;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

/**
 *
 * @author Elias
 */
public class Entrada {

    private StringProperty CodigoQR;
    private StringProperty Tipo_Tarjeta;
    private DoubleProperty Comision;
    private Museo museo;
    private Comision comision;
    private StringProperty Visitante;
    private Date fecha;

    public Entrada(String CodigoQR, Double Comision, String Visitante, Museo museo, Comision comision) {
        this.CodigoQR = new SimpleStringProperty(CodigoQR);
        this.Comision = new SimpleDoubleProperty(Comision);
        this.Visitante = new SimpleStringProperty(Visitante);
        this.museo = museo;
        this.comision = comision;
    }

    public Museo getMuseo() {
        return museo;
    }

    public void setMuseo(Museo museo) {
        this.museo = museo;
    }

    public Comision getComisionTarget() {
        return comision;
    }

    public void setComision(Comision comision) {
        this.comision = comision;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getVisitante() {
        return Visitante.get();
    }

    public void setVisitante(String Visitante) {
        this.Visitante = new SimpleStringProperty(Visitante);
    }

    public String getCodigoQR() {
        return CodigoQR.get();
    }

    public void setCodigoQR(String CodigoQR) {
        this.CodigoQR = new SimpleStringProperty(CodigoQR);
    }

    public String getTipo_Tarjeta() {
        return Tipo_Tarjeta.get();
    }

    public void setTipo_Tarjeta(String Tipo_Tarjeta) {
        this.Tipo_Tarjeta = new SimpleStringProperty(Tipo_Tarjeta);
    }

    public Double getComision() {
        return Comision.get();
    }

    public void setComision(Double Comision) {
        this.Comision = new SimpleDoubleProperty(Comision);
    }



    public int VenderEntrada(Connection connection, ObservableList<Listado> lista, Date date, double total) {
        int resultado = 0;
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "INSERT INTO tbl_entrada(CodigoQR, Comision, Visitante,Tarjeta,Fecha_Venta, Total) "
                    + "VALUES (?, ?, ?, ?,?,?)"
            );
            instruccion.setString(1, CodigoQR.get());
            instruccion.setDouble(2, Comision.get());
            instruccion.setString(3, Visitante.get());
            instruccion.setInt(4, getComisionTarget().getCodigo());
            instruccion.setDate(5, date);
            instruccion.setDouble(6, total);
            resultado = instruccion.executeUpdate();
            for (int i = 0; i < lista.size(); i++) {
                try {
                    instruccion = connection.prepareStatement(
                            "INSERT INTO tbl_entradas_visitante(Fecha_Entrada, Museo, CodigoQR) "
                            + "VALUES (?, ?, ?)"
                    );
                    instruccion.setDate(1, lista.get(i).getFecha());
                    instruccion.setDouble(2, lista.get(i).getCodigo());
                    instruccion.setString(3, CodigoQR.get());
                    resultado = instruccion.executeUpdate();//retorna 1 si se inserto y 0 sino se inserto
                } catch (SQLException ex) {
                    Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //retorna 1 si se inserto y 0 sino se inserto
        } catch (SQLException ex) {
            Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    public static void ValidarEntrada(Connection connection, String CodigoQR, Date date, TextArea textArea) {
        String Entrada = "";
        try {
            Statement instruccion = connection.createStatement();
            ResultSet resultado = instruccion.executeQuery(
                    "SELECT \n"
                    + " A.Fecha_Entrada, \n"
                    + " A.Museo, \n"
                    + " A.CodigoQR, \n"
                    + " B.Nombre\n"
                    + " FROM db_museo_castillo.tbl_entradas_visitante A \n"
                    + " INNER JOIN db_museo_castillo.tbl_museos B ON(A.Museo =  B.id_Museo)\n"
            );
            while (resultado.next()) {
                String FECHA1 = date.toString();
                String FECHA2 = resultado.getDate(1).toString();
                if (CodigoQR.equals(resultado.getString(3)) && FECHA1.equals(FECHA2)) {
                    Entrada += resultado.getString(4) + " \n";
                    textArea.setText(Entrada);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
