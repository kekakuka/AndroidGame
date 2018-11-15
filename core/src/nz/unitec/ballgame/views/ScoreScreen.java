package nz.unitec.ballgame.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import nz.unitec.ballgame.BallGame;

public class ScoreScreen implements Screen{

    private BallGame parent;
    private Stage stage;
    private Skin skin;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion background;

    private Label titleLabel;
    private Label R1;
    private Label R2;
    private Label R3;
    private Label R4;
    private Label R5;



    public ScoreScreen(BallGame ballGame){
        parent = ballGame;
        /// create stage and set it as input processor
        stage = new Stage(new ScreenViewport());

        parent.assMan.queueAddSkin();
        parent.assMan.manager.finishLoading();
        skin = parent.assMan.manager.get("skin/glassy-ui.json");
        atlas = parent.assMan.manager.get("images/loading.atlas");
        background = atlas.findRegion("flamebackground");

    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen. Everything else will go inside
        // this table.
        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        table.setBackground(new TiledDrawable(background));
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));

        // return to main screen button
        final TextButton backButton = new TextButton("Back", skin, "small");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(BallGame.MENU);

            }
        });

        titleLabel = new Label( "Ranks", skin );
        titleLabel.setFontScale(2.0f);
        R1 = new Label( "1", skin );
        R2 = new Label( "2", skin );
        R3 = new Label( "3", skin );
        R4 = new Label( "4", skin );
        R5 = new Label( "5", skin );

        table.add(titleLabel).colspan(2);
        table.row().pad(40,0,0,10);

        table.add(R1);
        table.row().pad(20,0,0,10);

        table.add(R2);
        table.row().pad(20,0,0,10);

        table.add(R3);

        table.row().pad(20,0,0,10);

        table.add(R4);

        table.row().pad(20,0,0,10);

        table.add(R5);

        table.row().pad(50,0,0,10);
        table.add(backButton).colspan(2);
    }

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        // change the stage's viewport when the screen size is changed
        stage.getViewport().update(width, height, true);
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
