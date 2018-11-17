package nz.unitec.ballgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nz.unitec.ballgame.loader.B2dAssetManager;
import nz.unitec.ballgame.views.EndScreen;
import nz.unitec.ballgame.views.LoadingScreen;
import nz.unitec.ballgame.views.MainScreen;
import nz.unitec.ballgame.views.MenuScreen;
import nz.unitec.ballgame.views.PreferencesScreen;
import nz.unitec.ballgame.views.ScoreScreen;

public class BallGame extends Game {
    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private MenuScreen menuScreen;
    private MainScreen mainScreen;
    private EndScreen endScreen;
    private ScoreScreen scoreScreen;
    public AppPreferences preferences;
    public B2dAssetManager assMan = new B2dAssetManager();
    public Music bgMusic;
    public Sound bgSound;
    public final static int WIDTH = 480; //
    public final static int HEIGHT = 720;
//    public final static int HEIGHT = Gdx.graphics.getHeight() * (Gdx.graphics.getWidth() / BallGame.WIDTH); // 720;

    public final static int MENU = 0;
    public final static int PREFERENCES = 1;
    public final static int APPLICATION = 2;
    public final static int ENDGAME = 3;
    public final static int SCORE = 4;
    public final static int APPLICATION_HARD = 5;

    private static final int MODE_EASY = 1;
    private static final int MODE_HARD = 2;

    public int lastScore = 0;

    public NativeDB nativeDB;

    public BallGame(NativeDB nativeDB){
        this.nativeDB = nativeDB;
    }


    public void playSoundEnemyDead(){
        Sound soundEnemyDead = assMan.manager.get(B2dAssetManager.boingSound);
        if(preferences.isSoundEffectsEnabled()) {
            soundEnemyDead.play(preferences.getSoundVolume());
        }
    }

    @Override
    public void create() {
        loadingScreen = new LoadingScreen(this);
        preferences = new AppPreferences(this);
        setScreen(loadingScreen);

        // tells our asset manger that we want to load the images set in loadImages method
        assMan.queueAddMusic();
        assMan.queueAddSounds();
        // tells the asset manager to load the images and wait until finished loading.
        assMan.manager.finishLoading();
        // loads the 2 sounds we use
        bgMusic = assMan.manager.get(B2dAssetManager.playingSong);

        bgMusic.setLooping(true);
        bgMusic.setVolume(preferences.getMusicVolume());
        if (preferences.isMusicEnabled()) {
            bgMusic.play();
        }


        // Place holder. Probably here to do initDB or later in ENDGAME.
        nativeDB.initDB();

    }

    public void changeScreen(int screen) {
        switch (screen) {
            case MENU:
                if (menuScreen == null) menuScreen = new MenuScreen(this);
                this.setScreen(menuScreen);
                break;
            case PREFERENCES:
                if (preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
                this.setScreen(preferencesScreen);
                break;
            case APPLICATION:
                // always make new game screen so game can't start midway
                if (mainScreen == null) {
                    mainScreen = new MainScreen(this, MODE_EASY);
                } else {
                    mainScreen.resetWorld(MODE_EASY);
                }

                this.setScreen(mainScreen);
                break;
            case ENDGAME:
                if (endScreen == null) endScreen = new EndScreen(this);
                this.setScreen(endScreen);
                break;
            case SCORE:
                if (scoreScreen == null) scoreScreen = new ScoreScreen(this);
                this.setScreen(scoreScreen);
                break;
            case APPLICATION_HARD:
                // always make new game screen so game can't start midway
                if (mainScreen == null) {
                    mainScreen = new MainScreen(this, MODE_HARD);
                } else {
                    mainScreen.resetWorld(MODE_HARD);
                }

                this.setScreen(mainScreen);
                break;
        }
    }

    public AppPreferences getPreferences() {
        return this.preferences;
    }

    @Override
    public void dispose() {
        bgMusic.dispose();
        assMan.manager.dispose();
    }
}
