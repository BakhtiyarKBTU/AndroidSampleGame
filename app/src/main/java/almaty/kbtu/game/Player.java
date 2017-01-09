package almaty.kbtu.game;

/**
 * Created by RIA on 28.05.2015.
 */
public class Player {
    private long id;
    private String player;
    private long score;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }
    public long getScore() {
        return score;
    }
    public void setScore(long score) {
        this.score = score;
    }
    @Override
    public String toString() {
        return "Player: " + player + ", score: " + String.valueOf(score);
    }
}
