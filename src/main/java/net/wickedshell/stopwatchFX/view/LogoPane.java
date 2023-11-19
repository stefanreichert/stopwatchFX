package net.wickedshell.stopwatchFX.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class LogoPane extends VBox {

    private final ImageView imageView;

    public LogoPane() {
        Image hsvLogo = new Image("hsv_logo.png");
        imageView = new ImageView(hsvLogo);
        imageView.setFitWidth(100); // Adjust the width as needed
        imageView.setPreserveRatio(true);

        getChildren().add(imageView);
        setAlignment(Pos.CENTER);
    }
}
