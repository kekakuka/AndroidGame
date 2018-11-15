package nz.unitec.ballgame;

public interface NativeDB {
    public void initDB();
    public int[] getScores();
    public void setScore(int score);
}
