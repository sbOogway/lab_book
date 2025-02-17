package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {
    @FXML
    private Label label;

    @FXML
    private void handleOption1() {
        label.setText("You selected Option 1");
    }

    @FXML
    private void handleOption2() {
        label.setText("You selected Option 2");
    }

    @FXML
    private void handleOption3() {
        label.setText("You selected Option 3");
    }
    
}
