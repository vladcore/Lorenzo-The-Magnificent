package it.polimi.ingsw.LM34.UI.GUI.GuiViews;

import javafx.scene.control.Alert;

public class EndTurnPopup {

    public void interactWithPlayer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End Turn Popup");
        alert.setHeaderText(null);
        alert.setContentText("Your turn has ended");

        alert.showAndWait();
    }
}