/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mantenimientos;

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
public class tipo_Obra {

    private IntegerProperty Codigo;
    private StringProperty Nombre;

    public tipo_Obra(int Codigo, String Nombre) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Nombre = new SimpleStringProperty(Nombre);
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

    public static void Tipo_Obras(Connection connection, ObservableList<tipo_Obra> tipos) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery("SELECT * FROM tbl_tipo_obras");

            while (resultado.next()) {
                tipos.add(new tipo_Obra(resultado.getInt(1), resultado.getString(2)));

            }
        } catch (SQLException ex) {
            Logger.getLogger(tipo_Obra.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String toString() {
        return Nombre.get();
    }
}
