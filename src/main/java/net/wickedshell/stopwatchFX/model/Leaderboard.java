package net.wickedshell.stopwatchFX.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Leaderboard {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static Leaderboard fromFile(File file) throws IOException {
        Leaderboard leaderboard = new Leaderboard();
        Player[] playersFromFile = MAPPER.readValue(file, Player[].class);
        Arrays.stream(playersFromFile).forEach(player -> {
            leaderboard.players.add(player);
            leaderboard.sequence++;
        });
        leaderboard.refreshPlayerList();
        return leaderboard;
    }

    public static void toFile(File file, Leaderboard leaderboard) throws IOException {
        MAPPER.writeValue(file, leaderboard.players);
    }

    private final List<Player> players = new ArrayList<>();
    private final Set<LeaderboardChangedListener> listeners = new HashSet<>();
    private int sequence;

    public void addPlayer(String name) {
        Player player = new Player(sequence++, name);
        players.add(player);
        refreshPlayerList();
    }

    public void updateBestTime(int playerId, long bestTime) {
        players.stream().filter(player -> player.id == playerId).findFirst().ifPresent(player -> player.bestTime = bestTime);
        refreshPlayerList();
    }

    public void removePlayer(int playerId) {
        players.removeIf(player -> player.getId() == playerId);
        refreshPlayerList();
    }

    public int getPlayerRanking(int playerId){
        Optional<Player> playerOptional = players.stream().filter(playerProspect -> playerProspect.id == playerId).findFirst();
        return playerOptional.map(player -> players.indexOf(player) + 1).orElse(-1);
    }

    public List<Player> getPlayers(){
        return Collections.unmodifiableList(players);
    }

    public void addChangeListener(LeaderboardChangedListener listener) {
        listeners.add(listener);
    }

    private void refreshPlayerList() {
        players.sort(Comparator.comparingLong(Player::getBestTime));
        listeners.forEach(listener -> listener.onLeaderboardChange(players.stream().toList()));
    }

    public static class Player {
        private int id;
        private String name;
        private long bestTime;

        public Player() {
        }

        public Player(int id, String name) {
            this.id = id;
            this.name = name;
            this.bestTime = 99999999999L;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long getBestTime() {
            return bestTime;
        }

        public void setBestTime(long bestTime) {
            this.bestTime = bestTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface LeaderboardChangedListener {

        void onLeaderboardChange(List<Player> players);
    }

}
