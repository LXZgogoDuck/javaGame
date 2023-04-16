package kernel.views;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import kernel.Main;
import kernel.main.Game;
import kernel.main.cheatingGame;
import kernel.utils.DialogUtils;
import kernel.utils.jdbcUtils;

import java.util.ArrayList;
import java.util.List;

public class loginController {
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private CheckBox isAdmin;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private VBox loginBox;
    @FXML
    private VBox signUpBox;
    @FXML
    private Main mainApp;
    @FXML
    private TextField commonUserUsername;
    @FXML
    private TextField commonUserPassword;
    @FXML
    private TextField commonUserTruename;
    @FXML
    private TextField commonUserSid;
    @FXML
    private TextField commonUserTel;
    @FXML
    private TextField commonUserAddress;
    @FXML
    private Button saveToDataBaseButton;
    @FXML
    private Label loginBoxLabel;

    public void setMain(Main mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    public void handleLogin(){
        if (verify(usernameField.getText(), passwordField.getText())) {
            System.out.println("User checked!");
            if(isAdmin.isSelected()){
                //cheating mode implementation
                new cheatingGame();
                return;
            }
            //if "cheating mode" box is not selected, it will be common game
            new Game();
        } else {
            DialogUtils.tips(mainApp.getPrimaryStage(),"oooh~incorrect username or password.please check again!");
        }
    }
    @FXML
    public void handleSignUp() {
        loginBox.setVisible(false);
        signUpBox.setVisible(true);
    }
    @FXML
    private void handleNewUser() {
        //if the user already exists, then pop up the alarm
        if (exists(commonUserUsername.getText())) {
            DialogUtils.tips(mainApp.getPrimaryStage(), "User already exist~");
        } else {
         //if not exists, then pop up "successfully registered" information accordingly
            signUp(commonUserUsername.getText(), commonUserPassword.getText());
            DialogUtils.good(mainApp.getPrimaryStage(),"successfully registered!");
            }

    }
    // if click "back" button, then back to the user login surface
    @FXML
    private void handleBack() {
        signUpBox.setVisible(false);
        loginBox.setVisible(true);
    }
    //verify functon__used to verify whether the username and password is correct or not
    private boolean verify(String username, String password) {
        jdbcUtils jdbcUtils = new jdbcUtils();
        jdbcUtils.getConnection();

        String sql = "select count(1) count from `commonuser` where username = ? and password = ?";
        List<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        try {
            return jdbcUtils.count(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // check if the user already exist in the database
    private boolean exists(String username) {
        jdbcUtils jdbcUtils = new jdbcUtils();
        jdbcUtils.getConnection();

        String sql = "select count(1) count from `commonuser` where username = ?";
        List<String> params = new ArrayList<>();
        params.add(username);
        try {
            return jdbcUtils.count(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //sign up__use to add new users including their name and pwd
    private boolean signUp(String username, String password) {
        jdbcUtils jdbcUtils = new jdbcUtils();
        jdbcUtils.getConnection();

        String sql = "insert into `commonuser` (username,password) values (?,?)";
        List<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        try {
            return jdbcUtils.insert(sql, params) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
