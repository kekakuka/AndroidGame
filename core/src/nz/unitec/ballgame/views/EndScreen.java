package nz.unitec.ballgame.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import nz.unitec.ballgame.BallGame;
import nz.unitec.ballgame.tools.DFUtils;

public class EndScreen implements Screen {

	private BallGame parent;
	private Skin skin;
	private Stage stage;
	private TextureAtlas atlas;
	private AtlasRegion background;
	
	public EndScreen(BallGame ballGame){
		parent = ballGame;
	}
	
	@Override
	public void show() {
		// get skin
		skin = parent.assMan.manager.get("skin/glassy-ui.json");
		atlas = parent.assMan.manager.get("images/loading.atlas");
		background = atlas.findRegion("flamebackground");
		
		// create button to go back to manu
		TextButton menuButton = new TextButton("Back", skin, "small");
		
		// create button listener
		menuButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				DFUtils.log("To the MENU");
				parent.changeScreen(BallGame.MENU);
			}
		});
		
		// create stage and set it as input processor
		stage = new Stage(new StretchViewport(BallGame.WIDTH, BallGame.HEIGHT));
		Gdx.input.setInputProcessor(stage); 
		
		// create table to layout iutems we will add
		Table table = new Table();
		table.setFillParent(true);
//        table.setDebug(true);
        table.setBackground(new TiledDrawable(background));
		
		//create a Labels showing the score and some credits
		Label labelScore = new Label("You score was "+parent.lastScore+"", skin);
		Label labelCredits = new Label("Credits:", skin);
		Label labelCredits1 = new Label("Shichang Qi", skin);
		Label labelCredits2 = new Label("Lei Li", skin);
		Label labelCredits3 = new Label("Yuyue Wang", skin);
		
		// add items to table
		table.add(labelScore);
		table.row().padTop(20);
		table.add(labelCredits);
		table.row().padTop(10);
		table.add(labelCredits1);
		table.row().padTop(10);
		table.add(labelCredits2);
		table.row().padTop(10);
		table.add(labelCredits3);
		table.row().padTop(50);
		table.add(menuButton).colspan(2);
		
		//add table to stage
		stage.addActor(table);
	}

	@Override
	public void render(float delta) {
		// clear the screen ready for next set of images to be drawn
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		parent.nativeDB.setScore(parent.lastScore);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// change the stage's viewport when teh screen size is changed
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {}

}
