package net.wickedshell.stopwatchFX.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.wickedshell.stopwatchFX.model.Leaderboard;
import net.wickedshell.stopwatchFX.model.Leaderboard.Player;
import net.wickedshell.stopwatchFX.view.serialize.TimeSerializer;

import java.util.*;

public class LeaderboardPane extends VBox {
    private static final ResourceBundle labelsBundle = ResourceBundle.getBundle("labels");
    private final Leaderboard leaderboard;

    private final ObservableList<LeaderboardEntry> leaderboardEntries;
    private final Set<LeaderboardPaneEventHandler> eventHandlers = new HashSet<>();

    private final Button addPlayerButton;
    private final Button removePlayerButton;
    private final Button updateBestTimeButton;

    public LeaderboardPane(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
        leaderboardEntries = FXCollections.observableArrayList();

        Label captionLabel = new Label(labelsBundle.getString("title.leaderboard"));
        captionLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        VBox.setMargin(captionLabel, new javafx.geometry.Insets(0, 0, 10, 0));

        TableView<LeaderboardEntry> tableView = new TableView<>();

        TableColumn<LeaderboardEntry, Number> rankingColumn = new TableColumn<>(labelsBundle.getString("label.leaderboard.column.ranking"));
        rankingColumn.setCellValueFactory(cellData -> cellData.getValue().rankingProperty());

        TableColumn<LeaderboardEntry, String> nameColumn = new TableColumn<>(labelsBundle.getString("label.leaderboard.column.name"));
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<LeaderboardEntry, String> timeColumn = new TableColumn<>(labelsBundle.getString("label.leaderboard.column.besttime"));
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(rankingColumn, nameColumn, timeColumn);
        tableView.addEventHandler(KeyEvent.ANY, event -> tableView.getParent().fireEvent(event));
        tableView.setItems(leaderboardEntries);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        addPlayerButton = new Button(labelsBundle.getString("label.leaderboard.button.addplayer"));
        HBox.setMargin(addPlayerButton, new javafx.geometry.Insets(10, 10, 10, 0));
        addPlayerButton.setFocusTraversable(false);
        addPlayerButton.addEventHandler(KeyEvent.ANY, event -> tableView.getParent().fireEvent(event));
        addPlayerButton.setOnAction(event -> this.showAddPlayerDialog());

        removePlayerButton = new Button(labelsBundle.getString("label.leaderboard.button.removeplayer"));
        HBox.setMargin(removePlayerButton, new javafx.geometry.Insets(10, 10, 10, 0));
        removePlayerButton.setDisable(true);
        removePlayerButton.setFocusTraversable(false);
        removePlayerButton.addEventHandler(KeyEvent.ANY, event -> tableView.getParent().fireEvent(event));
        removePlayerButton.setOnAction(event -> eventHandlers.forEach(eventHandler -> eventHandler.onRemovePlayerClicked(tableView.getSelectionModel().getSelectedItem().id)));

        updateBestTimeButton = new Button(labelsBundle.getString("label.leaderboard.button.updatebesttime"));
        HBox.setMargin(updateBestTimeButton, new javafx.geometry.Insets(10, 0, 10, 0));
        updateBestTimeButton.setDisable(true);
        updateBestTimeButton.setFocusTraversable(false);
        updateBestTimeButton.addEventHandler(KeyEvent.ANY, event -> tableView.getParent().fireEvent(event));
        updateBestTimeButton.setOnAction(event -> eventHandlers.forEach(eventHandler -> eventHandler.onUpdateBestTime(tableView.getSelectionModel().getSelectedItem().id)));

        HBox buttonsBox = new HBox(10, addPlayerButton, removePlayerButton, updateBestTimeButton);
        buttonsBox.setAlignment(Pos.CENTER);

        getChildren().addAll(captionLabel, tableView, buttonsBox);
        setAlignment(Pos.CENTER);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isRowSelected = newValue != null;
            removePlayerButton.setDisable(!isRowSelected);
            updateBestTimeButton.setDisable(!isRowSelected);
        });

        tableView.setPlaceholder(new Label(labelsBundle.getString("label.leaderboard.empty")));
        setMaxHeight(250);

        leaderboard.addChangeListener(this::setEntries);
        setEntries(leaderboard.getPlayers());
    }

    public void addEventHandler(LeaderboardPaneEventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    private void showAddPlayerDialog() {
        // Open a dialog to get the new player's name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(labelsBundle.getString("title.leaderboard.dialog.addplayer"));
        dialog.setHeaderText(null);
        dialog.setContentText(labelsBundle.getString("label.leaderboard.dialog.addplayer"));

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String playerName = result.get().trim();
            eventHandlers.forEach(listener -> listener.onAddPlayerClicked(playerName));
        }
    }

    private void setEntries(List<Player> players) {
        List<LeaderboardEntry> leaderboardEntriesSet = players.stream()
                .map(player -> new LeaderboardEntry(player.getId(), leaderboard.getPlayerRanking(player.getId()), player.getName(), player.getBestTime()))
                .toList();
        leaderboardEntries.setAll(leaderboardEntriesSet);
    }

    public interface LeaderboardPaneEventHandler {
        void onAddPlayerClicked(String playerName);

        void onRemovePlayerClicked(int playerId);

        void onUpdateBestTime(int playerId);
    }

    private static class LeaderboardEntry {
        private final int id;
        private final IntegerProperty ranking;
        private final StringProperty name;
        private final StringProperty time;

        public LeaderboardEntry(int id, int ranking, String name, long milliseconds) {
            this.id = id;
            this.ranking = new SimpleIntegerProperty(ranking);
            this.name = new SimpleStringProperty(name);
            this.time = new SimpleStringProperty(TimeSerializer.serialize(milliseconds));
        }


        public StringProperty nameProperty() {
            return name;
        }

        public StringProperty timeProperty() {
            return time;
        }

        public IntegerProperty rankingProperty() {
            return ranking;
        }
    }
}