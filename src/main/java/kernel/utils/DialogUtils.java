package kernel.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import kernel.Main;

import java.io.File;
import java.util.Optional;

public class DialogUtils {
    //When something get wrong_pop up the notice dialogue
    public static void tips(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("tips");
        ImageView imageView = new ImageView(Main.class.getResource("statics/images/systemUse/exception.png").toString());
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        alert.setGraphic(imageView);
        alert.setHeaderText("some info is wrong");
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.show();
    }
// appear after successfully registered
    public static void good(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congrats");
        ImageView imageView = new ImageView(Main.class.getResource("statics/images/systemUse/xiaomai2.png").toString());
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        alert.setGraphic(imageView);
        alert.setHeaderText("your wish is fulfilled");
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.show();
    }

}
