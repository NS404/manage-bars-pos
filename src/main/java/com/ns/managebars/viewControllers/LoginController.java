package com.ns.managebars.viewControllers;


import com.ns.managebars.JFXApplication;
import com.ns.managebars.uiManager.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@FxmlView("/views/login-view.fxml")
public class LoginController {




    private final FxWeaver fxWeaver;
    @FXML
    private PasswordField passcodeField;

    private final UserManager userHandler ;

    public LoginController(UserManager userHandler,
                            FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
        this.userHandler = userHandler;
    }


    @FXML
    public void initialize() {


    }



    public void keyPadButtonAction(ActionEvent actionEvent){

        Button key =(Button) actionEvent.getSource();
        String one = key.getText();
        StringBuilder sequence = (StringBuilder) passcodeField.getCharacters();
        sequence.append(one);
        passcodeField.setText(String.valueOf(sequence));

    }

    public void deleteButton() {
        passcodeField.clear();
    }



    public void okButton() throws IOException {

        if(!passcodeField.getText().isBlank()){

            String passcode = passcodeField.getText().toLowerCase().trim();

            passcodeField.clear();

            if (userHandler.authenticate(passcode)) {

                switch (userHandler.getActualUser().getRole()) {
                    case WAITER ->
                            JFXApplication.mainStage.setScene(new Scene(fxWeaver.loadView(WaiterController.class)));
                    case MANAGER ->
                            JFXApplication.mainStage.setScene(new Scene(fxWeaver.loadView(ManagerController.class)));
                }
            }




        }


    }

}

