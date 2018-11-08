package nz.unitec.ballgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nz.unitec.ballgame.BallGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = BallGame.HEIGHT;
		config.width = BallGame.HEIGHT;
		config.resizable = false;
		new LwjglApplication(new BallGame(), config);
	}
}
