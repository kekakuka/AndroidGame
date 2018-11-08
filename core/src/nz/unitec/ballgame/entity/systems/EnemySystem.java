package nz.unitec.ballgame.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import nz.unitec.ballgame.entity.components.B2dBodyComponent;
import nz.unitec.ballgame.entity.components.EnemyComponent;
import nz.unitec.ballgame.tools.BallFactory;

public class EnemySystem extends IteratingSystem {
    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<B2dBodyComponent> bodm;
    private BallFactory ballFactory;

    @SuppressWarnings("unchecked")
    public EnemySystem(BallFactory ballFactory) {
        super(Family.all(EnemyComponent.class).get());
        em = ComponentMapper.getFor(EnemyComponent.class);
        bodm = ComponentMapper.getFor(B2dBodyComponent.class);
        this.ballFactory = ballFactory;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyCom = em.get(entity);        // get EnemyComponent
        B2dBodyComponent bodyCom = bodm.get(entity);    // get B2dBodyComponent

        // check for dead enemies
        if (enemyCom.isDead) {
            bodyCom.isDead = true;
        }
    }
}
