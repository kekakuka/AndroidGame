package nz.unitec.ballgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AppPreferences {
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREFS_NAME = "BallGame";

    private BallGame parent;

    public AppPreferences(BallGame ballGame) {
        this.parent = ballGame;
    }

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public boolean isSoundEffectsEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        Preferences prefs = getPrefs();
        prefs.putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        prefs.flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean musicEnabled) {
        Preferences prefs = getPrefs();
        prefs.putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        prefs.flush();
        if(musicEnabled) {
            this.parent.bgMusic.play();
        } else {
            this.parent.bgMusic.stop();
        }
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public void setMusicVolume(float volume) {
        Preferences prefs = getPrefs();
        prefs.putFloat(PREF_MUSIC_VOLUME, volume);
        prefs.flush();
        parent.bgMusic.setVolume(volume);
    }

    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOL, 0.5f);
    }

    public void setSoundVolume(float volume) {
        Preferences prefs = getPrefs();
        prefs.putFloat(PREF_SOUND_VOL, volume);
        prefs.flush();
    }
}
