/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AutoComplete;

import Mantenimientos.Mantenimiento;
import com.jfoenix.controls.JFXComboBox;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author Elias
 */
public class AutoComplete {
    public AutoComplete(){}
    
    public void AutoCompleteCBX(JFXComboBox<Mantenimiento> comboBox){
    
        TextFields.bindAutoCompletion(comboBox.getEditor(), sr -> {
             return comboBox.getItems();
        });
    }
}
