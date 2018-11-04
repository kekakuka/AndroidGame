package nz.unitec.ballgame.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import nz.unitec.ballgame.BallGame;

public class MainScreen implements Screen {
	private BallGame parent;
	private OrthographicCamera cam;


	/**
	 * @param ballGame
	 */
	public MainScreen(BallGame ballGame) {
		parent = ballGame;

	}
	
	// reset world or start world again
	public void resetWorld(){
		System.out.println("Resetting world");

		
	}
	

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.4f, 0.4f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	}

	@Override
	public void resize(int width, int height) {		
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {		
	}

	@Override
	public void dispose() {

	}

}
