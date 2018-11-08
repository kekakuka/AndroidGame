package nz.unitec.ballgame.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nz.unitec.ballgame.BallGame;
import nz.unitec.ballgame.controller.KeyboardController;
import nz.unitec.ballgame.entity.components.Mapper;
import nz.unitec.ballgame.entity.components.PlayerComponent;
import nz.unitec.ballgame.entity.systems.AnimationSystem;
import nz.unitec.ballgame.entity.systems.BulletSystem;
import nz.unitec.ballgame.entity.systems.CollisionSystem;
import nz.unitec.ballgame.entity.systems.EnemySystem;
import nz.unitec.ballgame.entity.systems.LevelGenerationSystem;
import nz.unitec.ballgame.entity.systems.PhysicsDebugSystem;
import nz.unitec.ballgame.entity.systems.PhysicsSystem;
import nz.unitec.ballgame.entity.systems.PlayerControlSystem;
import nz.unitec.ballgame.entity.systems.RenderingSystem;
import nz.unitec.ballgame.entity.systems.ScoreSystem;
import nz.unitec.ballgame.tools.BallFactory;
import nz.unitec.ballgame.tools.DFUtils;

public class MainScreen implements Screen {
    private BallGame parent;
    private OrthographicCamera cam;
    private KeyboardController controller;
    private SpriteBatch sb;
    private PooledEngine engine;
    private BallFactory ballFactory;

    //private Sound ping;
    //private Sound boing;
    private Entity player;

    /**
     * @param ballGame
     */
    public MainScreen(BallGame ballGame) {
        parent = ballGame;

        engine = new PooledEngine();
        ballFactory = new BallFactory(engine, parent.assMan);

        sb = new SpriteBatch();
        RenderingSystem renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();

//		ParticleEffectSystem particleSystem = new ParticleEffectSystem(sb,cam);
        sb.setProjectionMatrix(cam.combined);

        controller = new KeyboardController(cam);
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PhysicsSystem(ballFactory.world));
        engine.addSystem(renderingSystem);
        // not a fan of splitting batch into rendering and particles but I like the separation of the systems
//        engine.addSystem(particleSystem); // particle get drawns on top so should be placed after normal rendering
        engine.addSystem(new PhysicsDebugSystem(ballFactory.world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem(ballFactory));
//        engine.addSystem(new SteeringSystem());
        engine.addSystem(new PlayerControlSystem(controller, ballFactory));
        player = ballFactory.createPlayer(cam);
        engine.addSystem(new ScoreSystem(ballFactory, sb));
        engine.addSystem(new EnemySystem(ballFactory));
//      engine.addSystem(new WallSystem(ballFactory));
        engine.addSystem(new BulletSystem(ballFactory));
        engine.addSystem(new LevelGenerationSystem(ballFactory));


        ballFactory.createFloor();
//        ballFactory.createBackground();
        //lvlFactory.createSeeker(Mapper.sCom.get(player),20,15);
        ballFactory.createWalls();
    }

    // reset world or start world again
    public void resetWorld() {
        System.out.println("Resetting world");
        engine.removeAllEntities();
        ballFactory.resetWorld();

        player = ballFactory.createPlayer(cam);
        ballFactory.createFloor();
        ballFactory.createWalls();

        // reset controller controls (fixes bug where controller stuck on directrion if died in that position)
        controller.left = false;
        controller.right = false;
        controller.up = false;
        controller.down = false;
        controller.isMouse1Down = false;
        controller.isMouse2Down = false;
        controller.isMouse3Down = false;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);

        //check if player is dead. if so show end screen
        PlayerComponent pc = (player.getComponent(PlayerComponent.class));
        if (pc.isDead) {
            PlayerComponent pl = Mapper.playerCom.get(player);
            parent.lastScore = pl.score;
            parent.changeScreen(BallGame.ENDGAME);

            DFUtils.log("YOU DIED : back to menu you go!");
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        sb.dispose();
        engine.clearPools();
    }

}
