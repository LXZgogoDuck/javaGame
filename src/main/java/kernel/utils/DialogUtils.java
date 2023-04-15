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
    public static void tips(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("tips");
        ImageView menhera = new ImageView(Main.class.getResource("statics/images/systemUse/exception.png").toString());
        menhera.setFitHeight(100);
        menhera.setPreserveRatio(true);
        alert.setGraphic(menhera);
        alert.setHeaderText("some info is wrong");
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.show();
    }

    public static void good(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congrats");
        ImageView menhera = new ImageView(Main.class.getResource("statics/images/systemUse/xiaomai2.png").toString());
        menhera.setFitHeight(100);
        menhera.setPreserveRatio(true);
        alert.setGraphic(menhera);
        alert.setHeaderText("your wish is fulfilled");
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.show();
    }

    public static boolean confirm(Stage stage, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message ,new ButtonType("cancel", ButtonBar.ButtonData.NO),
                new ButtonType("confirm", ButtonBar.ButtonData.YES));
//        set tht title of the window
        alert.setTitle("confirm");
        ImageView menhera = new ImageView(Main.class.getResource("statics/images/systemUse/confirm.png").toString());
        menhera.setFitHeight(100);
        menhera.setPreserveRatio(true);
        alert.setGraphic(menhera);
        alert.setHeaderText("last chance");
//        设置对话框的 icon 图标，参数是主窗口的 stage
//        set the icon of the window, parameter is "stage" of the main window
        alert.initOwner(stage);
//        showAndWait() 将在对话框消失以前不会执行之后的代码
//        showAndWait() will execute after the window disappear
        Optional<ButtonType> buttonType = alert.showAndWait();
//        根据点击结果返回
//        return based on the clicking result
        if(buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            return true;
        } else {
            return false;
        }
    }
}
