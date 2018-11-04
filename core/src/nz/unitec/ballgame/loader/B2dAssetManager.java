package nz.unitec.ballgame.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class B2dAssetManager {
    public final AssetManager manager = new AssetManager();

    // Textures
    public static final String playerImage = "images/player.png";
    public static final String enemyImage = "images/enemy.png";
    public static final String loadingImages = "images/loading.atlas";
    public static final String gameImages = "images/game.atlas";

    // Sounds
    public static final String boingSound = "sounds/boing.wav";
    public static final String pingSound = "sounds/ping.wav";

    // Music
    public static final String playingSong = "music/bg.mp3";

    // Skin
    public static final String skin = "skin/glassy-ui.json";
    static final String skinAtlas = "skin/glassy-ui.atlas";

    // Particle Effects
    public static final String smokeEffect = "particles/smoke.pe";
    public static final String waterEffect = "particles/water.pe";
    public static final String fireEffect = "particles/fire.pe";

    public void queueAddImages() {
        manager.load(playerImage, Texture.class);
        manager.load(enemyImage, Texture.class);

        manager.load(gameImages, TextureAtlas.class);
    }

    public void queueAddLoadingImages() {
        manager.load(loadingImages, TextureAtlas.class);
    }

    public void queueAddSounds() {
        manager.load(boingSound, Sound.class);
        manager.load(pingSound, Sound.class);
    }

    public void queueAddMusic() {
        manager.load(playingSong, Music.class);
    }

    public void queueAddSkin() {
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter(skinAtlas);
        manager.load(skin, Skin.class, params);
    }

    public void queueAddFonts() {

    }

    public void queueAddParticleEffects() {
        ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();
        pep.atlasFile = "images/game.atlas";
        manager.load(smokeEffect, ParticleEffect.class, pep);
        manager.load(waterEffect, ParticleEffect.class, pep);
        manager.load(fireEffect, ParticleEffect.class, pep);
    }
}
