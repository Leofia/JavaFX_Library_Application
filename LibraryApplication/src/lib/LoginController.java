package lib;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
          usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });
    }


    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user= userDAO.findUserByUsernameAndPassword(username,password);
        if (user != null) {
            messageLabel.setText("Login Successful!");
            messageLabel.setStyle("-fx-text-fill: green;");

            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
                Parent mainScreenRoot = loader.load();
                MainScreenController controller = loader.getController();
                controller.setLoggedInUser(user);
                Lib.setLoggedInUser(user);
                Stage stage = (Stage) messageLabel.getScene().getWindow();
                stage.setScene(new Scene(mainScreenRoot));

            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Invalid username or password!");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}