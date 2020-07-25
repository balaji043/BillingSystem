package bam.billing.se.utils;

import bam.billing.se.Main;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class BillingSystemUtils {

    private static final String[] units = {"", "One", "Two", "Three", "Four",
            "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
            "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
            "Eighteen", "Nineteen"};
    private static final String[] tens = {
            "",          // 0
            "",          // 1
            "Twenty",    // 2
            "Thirty",    // 3
            "Forty",     // 4
            "Fifty",     // 5
            "Sixty",     // 6
            "Seventy",   // 7
            "Eighty",    // 8
            "Ninety"     // 9
    };
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static String convert(final int n) {
        if (n < 0) {
            return "Minus " + convert(-n);
        }
        if (n < 20) {
            return units[n];
        }
        if (n < 100) {
            return tens[n / 10] + ((n % 10 != 0) ? " " : "") + units[n % 10];
        }
        if (n < 1000) {
            return units[n / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
        }
        if (n < 100000) {
            return convert(n / 1000) + " Thousand" + ((n % 10000 != 0) ? " " : "") + convert(n % 1000);
        }
        if (n < 10000000) {
            return convert(n / 100000) + " Lakh" + ((n % 100000 != 0) ? " " : "") + convert(n % 100000);
        }
        return convert(n / 10000000) + " Crore" + ((n % 10000000 != 0) ? " " : "") + convert(n % 10000000);
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static String formatDateTimeString(Long time) {
        return DATE_TIME_FORMAT.format(new Date(time));
    }

    public static void setImageViewToButtons(String iconName, JFXButton jfxButton, int i) {
        jfxButton.setText("");
        ImageView view1 = null;
        try {
            view1 = new ImageView(new Image(Main.class.
                    getResourceAsStream(iconName)
                    , i + 10, i + 10, true, true));
        } catch (Exception e) {
            jfxButton.setText("");
            e.getMessage();
            //LOGGER.log(Level.SEVERE, e.getMessage());
        }
        if (view1 != null)
            jfxButton.setGraphic(view1);
        jfxButton.setPrefSize(i + 5, i + 5);

//        jfxButton.setTooltip(new Tooltip(ButtonToolTip.valueOf(iconName).toString()));

    }

    public static Image getImage(String iconPath) {
        return new Image(Main.class.getResourceAsStream(iconPath));
    }

    public static void setImageViewToButtons(String iconName, JFXButton jfxButton) {
        setImageViewToButtons(iconName, jfxButton, 20);
    }


    public static void setImageToImageViews(String iconName, ImageView mainImageView) {

        try {
            mainImageView.setImage(new Image(Main.class.
                    getResourceAsStream(iconName)));
        } catch (Exception e) {
            AlertMaker.showErrorMessage(e);
        }
    }

    public static FXMLLoader getFXMLLoader(String viewPath) {
        return new FXMLLoader(
                Main.class.getResource(viewPath)
        );
    }

    public static File chooseFile(Main main) {
        main.snackBar(Message.FILE_CHOOSE);
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll
                (new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        return fileChooser.showSaveDialog(main.getPrimaryStage());
    }
}