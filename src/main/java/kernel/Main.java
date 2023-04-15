package kernel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kernel.views.loginController;

import java.io.IOException;

public class Main extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.stage.setTitle("Xuanzhuo's game room");
        showLoginView();
    }
    public void showLoginView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            System.out.println(Main.class.getResource("views/LoginView.fxml"));
            fxmlLoader.setLocation(Main.class.getResource("views/LoginView.fxml"));
            Parent root = fxmlLoader.load();
            loginController loginController = fxmlLoader.getController();
            loginController.setMain(this);
            stage.setScene(new Scene(root, 1152, 640));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
