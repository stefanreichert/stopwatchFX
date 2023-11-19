package net.wickedshell.stopwatchFX.controller;

import net.wickedshell.stopwatchFX.model.Leaderboard;
import net.wickedshell.stopwatchFX.model.Stopwatch;
import net.wickedshell.stopwatchFX.view.LeaderboardPane.LeaderboardPaneEventHandler;

public class LeaderboardController implements LeaderboardPaneEventHandler {

    private final Leaderboard leaderboard;
    private final Stopwatch stopwatch;

    public LeaderboardController(Leaderboard leaderboard, Stopwatch stopwatch) {
        this.leaderboard = leaderboard;
        this.stopwatch = stopwatch;
    }

    @Override
    public void onAddPlayerClicked(String playerName) {
        System.out.println("geklickt: Spieler Hinzufügen");
        leaderboard.addPlayer(playerName);

    }

    @Override
    public void onRemovePlayerClicked(int playerId) {
        System.out.println("geklickt: Spieler Löschen");
        leaderboard.removePlayer(playerId);
    }

    @Override
    public void onUpdateBestTime(int playerId) {
        System.out.println("geklickt: Bestzeit Anpassen");
        leaderboard.updateBestTime(playerId, stopwatch.getMilliseconds());
    }
}
