package nz.unitec.ballgame.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import nz.unitec.ballgame.entity.components.PlayerComponent;
import nz.unitec.ballgame.entity.components.TransformComponent;
import nz.unitec.ballgame.tools.BallFactory;

public class LevelGenerationSystem extends IteratingSystem {

    // get transform component so we can check players height
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);
    private BallFactory ballFactory;

    private float generatedDelay = 1f;
    private float timeSinceLastGenerated = 0f;
    private float timeSinceBegin = 0f;

    @SuppressWarnings("unchecked")
    public LevelGenerationSystem(BallFactory ballFactory) {
        super(Family.all(PlayerComponent.class).get());
        this.ballFactory = ballFactory;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent trans = tm.get(entity);
        timeSinceBegin += deltaTime;

        if (timeSinceBegin / 15 > 0 && generatedDelay == 1f) {
            generatedDelay = 0.5f;
        }

        if (timeSinceLastGenerated >= generatedDelay) {
            timeSinceLastGenerated -= generatedDelay;
//            System.out.println("May generate an enemy");
            ballFactory.generateLevel(timeSinceBegin);
        } else {
            timeSinceLastGenerated += deltaTime;
        }
    }
}
