package nz.unitec.ballgame.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import nz.unitec.ballgame.BallGame;
import nz.unitec.ballgame.entity.components.B2dBodyComponent;
import nz.unitec.ballgame.entity.components.EnemyComponent;
import nz.unitec.ballgame.entity.components.Mapper;
import nz.unitec.ballgame.entity.components.PlayerComponent;
import nz.unitec.ballgame.loader.B2dAssetManager;
import nz.unitec.ballgame.tools.BallFactory;


public class ScoreSystem extends IteratingSystem {
    private ComponentMapper<EnemyComponent> em;
    private BallFactory ballFactory;
    private SpriteBatch batch;
    private OrthographicCamera cam;

    @SuppressWarnings("unchecked")
    public ScoreSystem(BallFactory ballFactory, SpriteBatch sb) {
        super(Family.all(EnemyComponent.class).get());
        em = ComponentMapper.getFor(EnemyComponent.class);
        this.ballFactory = ballFactory;
        this.batch = sb;
        cam = new OrthographicCamera(BallGame.WIDTH, BallGame.HEIGHT);
        cam.position.set(0, 0, 0);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyCom = em.get(entity);        // get EnemyComponent
        // check for dead enemies
        if (enemyCom.isDead) {
            // player got 1 score
            PlayerComponent pl = Mapper.playerCom.get(ballFactory.player);
            pl.score++;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        PlayerComponent pl = Mapper.playerCom.get(ballFactory.player);
        String score = pl.score + "";

        BitmapFont scoreFont = ballFactory.assman.manager.get(B2dAssetManager.scoreFont);
        GlyphLayout layout = new GlyphLayout();
        layout.setText(scoreFont, score);

        batch.begin();
        scoreFont.draw(batch, layout, BallGame.WIDTH / 2 - layout.width - 10, BallGame.HEIGHT / 2 - layout.height - 2);
//        scoreFont.draw(batch, layout, 10, 10);
        batch.end();
    }
}
