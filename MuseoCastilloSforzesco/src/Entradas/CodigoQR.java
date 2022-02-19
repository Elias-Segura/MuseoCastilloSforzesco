/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entradas;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author Elias
 */
public class CodigoQR {

    public CodigoQR() {
    }

    public String LeerQR(ImageView image) throws IOException, FileNotFoundException, NotFoundException {
        String directorio;
        directorio = new File("").getAbsolutePath();
        File File = new File(directorio);
        if (!File.isDirectory()) {
            File.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(directorio));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("png files(*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(extensionFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image Image = new Image(new FileInputStream(file.getAbsoluteFile()));
            image.setImage(Image);
            String charset = "UTF-8";
            Map map = new HashMap();
            map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            String ReadQR = ReadQRCode(file.getAbsolutePath(), charset, map);

            return ReadQR;
        } else {
            return null;
        }
    }

    public String ReadQRCode(String filePath, String charset, Map map) throws FileNotFoundException, IOException, NotFoundException {

        if (ImageIO.read(new FileInputStream(filePath)) == null) {
            return null;

        } else {

            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))
            ));

            try {
                Result QRresult = new MultiFormatReader().decode(binaryBitmap, map);
                return QRresult.getText();
            } catch (NotFoundException e) {

                return null;
            }
        }

    }
}
