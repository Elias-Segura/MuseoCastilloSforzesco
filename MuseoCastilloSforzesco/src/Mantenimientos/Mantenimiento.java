/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mantenimientos;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Elias
 */
public class Mantenimiento {

    private IntegerProperty Codigo;
    private StringProperty Columna1;
    private StringProperty Columna2;
    private StringProperty Columna3;
    private StringProperty Columna4;

    public Mantenimiento(int Codigo, String Columna1, String Columna2,
            String Columna3, String Columna4) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
        this.Columna1 = new SimpleStringProperty(Columna1);
        this.Columna2 = new SimpleStringProperty(Columna2);
        this.Columna3 = new SimpleStringProperty(Columna3);
        this.Columna4 = new SimpleStringProperty(Columna4);
    }

    public int getCodigo() {
        return Codigo.get();
    }

    public void setCodigo(int Codigo) {
        this.Codigo = new SimpleIntegerProperty(Codigo);
    }

    public String getColumna1() {
        return Columna1.get();
    }

    public void setColumna1(String Columna1) {
        this.Columna1 = new SimpleStringProperty(Columna1);
    }

    public String getColumna2() {
        return Columna2.get();
    }

    public void setColumna2(String Columna2) {
        this.Columna2 = new SimpleStringProperty(Columna2);
    }

    public String getColumna3() {
        return Columna3.get();
    }

    public void setColumna3(String Columna3) {
        this.Columna3 = new SimpleStringProperty(Columna3);
    }

    public String getColumna4() {
        return Columna4.get();
    }

    public void setColumna4(String Columna4) {
        this.Columna4 = new SimpleStringProperty(Columna4);
    }

    @Override
    public String toString() {
        return Columna1.get();
    }
}
