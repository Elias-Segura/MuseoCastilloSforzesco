/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museocastillosforzesco;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class ValoracionController implements Initializable {
    int valoracion = 0;
    ObservableList<Label> instalaciones = FXCollections.observableArrayList();
    private JFXButton btn_Prueba;
    @FXML
    private JFXButton star_1;
    @FXML
    private JFXButton star_2;
    @FXML
    private JFXButton star_3;
    @FXML
    private JFXButton star_4;
    @FXML
    private JFXButton star_5;
    ObservableList<JFXButton> btnStars = FXCollections.observableArrayList();
    boolean seleccion = false;
    @FXML
    private FontAwesomeIcon btn_Cerrar;

    /**
     * Initializes the controller class.
     */
    public int getValoracion(){
    return  valoracion;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnStars.add(star_1);
        btnStars.add(star_2);
        btnStars.add(star_3);
        btnStars.add(star_4);
        btnStars.add(star_5);
    }

    @FXML
    private void selectedbutton(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();
        System.out.println(btn.getText() + "" + btn.getId() + " " + btnStars.get(0).getId());
        if (seleccion == false) {
            SeleccionarValoracion(btn);
        } else {
            DeseleccionarValoracion(btn);
        }

    }

    public void SeleccionarValoracion(JFXButton btn) {
        valoracion=0;
        Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR, "2em");;
        Icon.setFill(Color.YELLOW);
        for (int i = 0; i < btnStars.size(); i++) {
            Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR, "2em");;
            Icon.setFill(Color.YELLOW);
            btnStars.get(i).setGraphic(Icon);
            valoracion+=1;
            if (btn.getId().equals(btnStars.get(i).getId())) {
                Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR, "2em");;
                Icon.setFill(Color.YELLOW);
                btnStars.get(i).setGraphic(Icon);
               
                break;
            }

        }
        seleccion = true;

    }

    public void DeseleccionarValoracion(JFXButton btn) {
        Text Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR_ALT, "2em");;
        Icon.setFill(Color.YELLOW);
        for (int i = btnStars.size() - 1; i >= 0; i--) {
            Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR_ALT, "2em");;
            Icon.setFill(Color.YELLOW);
            btnStars.get(i).setGraphic(Icon);
            valoracion-=1;
            if (btn.getId().equals(btnStars.get(i).getId())) {
                Icon = GlyphsDude.createIcon(FontAwesomeIcons.STAR_ALT, "2em");;
                Icon.setFill(Color.YELLOW);
                btnStars.get(i).setGraphic(Icon);
             
                break;
            }

        }
        seleccion = false;
    }

    @FXML
    private void CerrarValoracion(MouseEvent event) {
    ((FontAwesomeIcon) (btn_Cerrar)).getScene().getWindow().hide();
  
    }

}
