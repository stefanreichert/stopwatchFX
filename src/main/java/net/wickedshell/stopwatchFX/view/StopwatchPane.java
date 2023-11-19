package net.wickedshell.stopwatchFX.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import net.wickedshell.stopwatchFX.model.Stopwatch;
import net.wickedshell.stopwatchFX.view.serialize.TimeSerializer;

import java.util.ResourceBundle;

import static net.wickedshell.stopwatchFX.view.StopwatchPane.StopwatchLabel.StopwatchStyle.STYLE_RUNNING;
import static net.wickedshell.stopwatchFX.view.StopwatchPane.StopwatchLabel.StopwatchStyle.STYLE_STOPPED;

public class StopwatchPane extends VBox implements Stopwatch.StopwatchListener {

    private final StopwatchLabel stopwatchLabel;

    public StopwatchPane(Stopwatch stopwatch) {
        stopwatchLabel = new StopwatchLabel();
        getChildren().add(stopwatchLabel);
        stopwatch.addListener(this);
    }

    @Override
    public void onStart() {
        stopwatchLabel.updateStatus("label.stopwatch.running", STYLE_RUNNING);
    }

    @Override
    public void onStop() {
        stopwatchLabel.updateStatus("label.stopwatch.stopped", STYLE_STOPPED);
    }

    @Override
    public void onMillisecondsChange(int seconds) {
        stopwatchLabel.updateTime(seconds);
    }

    static class StopwatchLabel extends VBox {

        private static final ResourceBundle labelsBundle = ResourceBundle.getBundle("labels");

        private final Label timeLabel;
        private final Label statusLabel;

        public enum StopwatchStyle {
            STYLE_STOPPED("-fx-font-size: 16; -fx-text-fill: darkred;"),
            STYLE_RUNNING("-fx-font-size: 16; -fx-text-fill: green;");

            private final String styleDefinition;

            StopwatchStyle(String styleDefinition) {
                this.styleDefinition = styleDefinition;
            }
        }

        public StopwatchLabel() {
            super(5);
            timeLabel = new Label("00:00.000");
            timeLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

            statusLabel = new Label(labelsBundle.getString("label.stopwatch.stopped"));
            statusLabel.setStyle(STYLE_STOPPED.styleDefinition);

            Label legendLabel = new Label(labelsBundle.getString("label.stopwatch.legend"));
            legendLabel.setStyle("-fx-font-size: 10; -fx-font-style: italic;");

            getChildren().addAll(timeLabel, statusLabel, legendLabel);
            setAlignment(Pos.CENTER);
            setMinSize(300, 100);
            setFocusTraversable(false);
        }

        public void updateTime(int milliseconds) {
            timeLabel.setText(TimeSerializer.serialize(milliseconds));
        }

        public void updateStatus(String key, StopwatchStyle style) {
            statusLabel.setText(labelsBundle.getString(key));
            statusLabel.setStyle(style.styleDefinition);
        }
    }
}
