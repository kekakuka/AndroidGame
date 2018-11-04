package nz.unitec.ballgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nz.unitec.ballgame.loader.B2dAssetManager;
import nz.unitec.ballgame.views.EndScreen;
import nz.unitec.ballgame.views.LoadingScreen;
import nz.unitec.ballgame.views.MainScreen;
import nz.unitec.ballgame.views.MenuScreen;
import nz.unitec.ballgame.views.PreferencesScreen;

public class BallGame extends Game {
    private LoadingScreen loadingScreen;
    private PreferencesScreen preferencesScreen;
    private MenuScreen menuScreen;
    private MainScreen mainScreen;
    private EndScreen endScreen;
    private AppPreferences preferences;
    public B2dAssetManager assMan = new B2dAssetManager();
    public Music bgMusic;

    public final static int WIDTH = 720;
    public final static int HEIGHT = 480;

    public final static int MENU = 0;
    public final static int PREFERENCES = 1;
    public final static int APPLICATION = 2;
    public final static int ENDGAME = 3;

    public int lastScore = 0;

    @Override
    public void create() {
        loadingScreen = new LoadingScreen(this);
        preferences = new AppPreferences(this);
        setScreen(loadingScreen);

        // tells our asset manger that we want to load the images set in loadImages method
        assMan.queueAddMusic();
        // tells the asset manager to load the images and wait until finished loading.
        assMan.manager.finishLoading();
        // loads the 2 sounds we use
        bgMusic = assMan.manager.get(B2dAssetManager.playingSong);
        bgMusic.setLooping(true);
        bgMusic.setVolume(preferences.getMusicVolume());
        if (preferences.isMusicEnabled()) {
            bgMusic.play();
        }
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
                    mainScreen = new MainScreen(this);
                } else {
                    mainScreen.resetWorld();
                }

                this.setScreen(mainScreen);
                break;
            case ENDGAME:
                if (endScreen == null) endScreen = new EndScreen(this);
                this.setScreen(endScreen);
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
