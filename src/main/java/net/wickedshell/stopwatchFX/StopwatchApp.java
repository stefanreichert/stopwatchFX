package net.wickedshell.stopwatchFX;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.wickedshell.stopwatchFX.controller.LeaderboardController;
import net.wickedshell.stopwatchFX.controller.SceneController;
import net.wickedshell.stopwatchFX.model.Leaderboard;
import net.wickedshell.stopwatchFX.model.Stopwatch;
import net.wickedshell.stopwatchFX.view.LeaderboardPane;
import net.wickedshell.stopwatchFX.view.LogoPane;
import net.wickedshell.stopwatchFX.view.StopwatchPane;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class StopwatchApp extends Application {

    private static final ResourceBundle labelsBundle = ResourceBundle.getBundle("labels");
    private static final File STOPWATCH_FILE_PATH = Path.of(FileUtils.getUserDirectoryPath(), "Temp", "stopwatch.json").toFile();

    private Leaderboard leaderboard;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // setup model & controller
        Stopwatch stopwatch = new Stopwatch();
        try {
            leaderboard = Leaderboard.fromFile(STOPWATCH_FILE_PATH);
        } catch (IOException e) {
            leaderboard = new Leaderboard();
        }
        LeaderboardController leaderboardController = new LeaderboardController(leaderboard, stopwatch);
        SceneController sceneController = new SceneController(stopwatch);

        // setup view
        VBox appPane = new VBox(10);
        Scene scene = new Scene(appPane);

        StopwatchPane stopwatchPane = new StopwatchPane(stopwatch);
        LeaderboardPane leaderboardPane = new LeaderboardPane(leaderboard);

        HBox topPane = new HBox(10, stopwatchPane, new LogoPane());
        topPane.setAlignment(Pos.CENTER_RIGHT);

        Separator separator = new Separator();
        separator.setMaxWidth(Double.MAX_VALUE);

        appPane.getChildren().addAll(topPane, separator, leaderboardPane);

        // setup scene handling
        leaderboardPane.addEventHandler(leaderboardController);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, sceneController);

        primaryStage.setTitle(labelsBundle.getString("title.application"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Leaderboard.toFile(STOPWATCH_FILE_PATH, leaderboard);
        super.stop();
    }
}

