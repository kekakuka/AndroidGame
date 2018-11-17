package nz.unitec.ballgame.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import nz.unitec.ballgame.BallGame;
import nz.unitec.ballgame.entity.components.B2dBodyComponent;
import nz.unitec.ballgame.entity.components.EnemyComponent;
import nz.unitec.ballgame.tools.BallFactory;

public class EnemySystem extends IteratingSystem {
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<B2dBodyComponent> bodm;
    private BallFactory ballFactory;
    private BallGame ballGame;

    private float lastTime = 0f;
    private float blowDelay = 1f;
    private boolean isBlow = false;

    @SuppressWarnings("unchecked")
    public EnemySystem(BallFactory ballFactory, BallGame ballGame) {
        super(Family.all(EnemyComponent.class).get());
        em = ComponentMapper.getFor(EnemyComponent.class);
        bodm = ComponentMapper.getFor(B2dBodyComponent.class);
        this.ballFactory = ballFactory;
        this.ballGame = ballGame;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (lastTime > blowDelay) {
            isBlow = true;
            lastTime -= blowDelay;
        } else {
            isBlow = false;
        }
        lastTime += deltaTime;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyCom = em.get(entity);        // get EnemyComponent
        B2dBodyComponent bodyCom = bodm.get(entity);    // get B2dBodyComponent

        if (ballGame.GAME_MODE == BallGame.MODE_HARD && !enemyCom.isDead && isBlow) {
            System.out.println("Blow en enemy");
            if(bodyCom.body.getPosition().y > 200) {
                bodyCom.body.setLinearVelocity(MathUtils.random(-100, 100), MathUtils.random(-200, -50));
            }
        }

        // check for dead enemies
        if (enemyCom.isDead) {
            bodyCom.isDead = true;
            ballGame.playSoundEnemyDead();
        }
    }
}
