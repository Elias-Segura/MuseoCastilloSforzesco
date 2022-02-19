/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mantenimientos;

import com.mysql.cj.protocol.Resultset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Elias
 */
public class tipo_Museo {

    private IntegerProperty Codigo;
    private StringProperty Tipo;

    public tipo_Museo() {
    }

    public tipo_Museo(int Codigo, String Tipo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Tipo = new SimpleStringProperty(Tipo);
    }
    public int getCodigo() {
        return Codigo.get();
    }

    public void setCodigo(int Codigo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
    }

    public String getTipo() {
        return Tipo.get();
    }

    public void setTipo(String Tipo) {
        this.Tipo = new SimpleStringProperty(Tipo);
    }


    public static void Tipo_Museos(Connection connection, ObservableList<tipo_Museo> tipos) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM tbl_tipo_museo");

            while (resultado.next()) {
                tipos.add(new tipo_Museo(resultado.getInt(1), resultado.getString(2)));

            }

        } catch (SQLException ex) {
            Logger.getLogger(tipo_Museo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String toString() {
        return Tipo.get();
    }
}
