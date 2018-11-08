package nz.unitec.ballgame.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import nz.unitec.ballgame.controller.KeyboardController;
import nz.unitec.ballgame.entity.components.B2dBodyComponent;
import nz.unitec.ballgame.entity.components.BulletComponent;
import nz.unitec.ballgame.entity.components.PlayerComponent;
import nz.unitec.ballgame.entity.components.StateComponent;
import nz.unitec.ballgame.tools.BallFactory;
import nz.unitec.ballgame.tools.DFUtils;

public class PlayerControlSystem extends IteratingSystem {

    private BallFactory ballFactory;
    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<B2dBodyComponent> bodm;
    ComponentMapper<StateComponent> sm;
    KeyboardController controller;


    @SuppressWarnings("unchecked")
    public PlayerControlSystem(KeyboardController keyCon, BallFactory ballFactory) {
        super(Family.all(PlayerComponent.class).get());
        controller = keyCon;
        this.ballFactory = ballFactory;
        pm = ComponentMapper.getFor(PlayerComponent.class);
        bodm = ComponentMapper.getFor(B2dBodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2dBodyComponent b2body = bodm.get(entity);
        StateComponent state = sm.get(entity);
        PlayerComponent player = pm.get(entity);


        //player.cam.position.y = b2body.body.getPosition().y;


        if (b2body.body.getLinearVelocity().y > 0 && state.get() != StateComponent.STATE_FALLING) {
            state.set(StateComponent.STATE_FALLING);
            System.out.println("setting to Falling");
        }

        if (b2body.body.getLinearVelocity().y == 0) {
            if (state.get() == StateComponent.STATE_FALLING) {
                state.set(StateComponent.STATE_NORMAL);
                System.out.println("setting to normal");
            }
            if (b2body.body.getLinearVelocity().x != 0 && state.get() != StateComponent.STATE_MOVING) {
                state.set(StateComponent.STATE_MOVING);
                System.out.println("setting to moving");
            }
        }
        // old function for testing platform ghosting
        //if(b2body.body.getLinearVelocity().y < 0 && state.get() == StateComponent.STATE_FALLING){
        // player is actually falling. check if they are on platform
        //if(player.onPlatform){
        //overwrite old y value with 0 t stop falling but keep x vel
        //b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0f);
        //}
        //}

        if (controller.left) {
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, -30f, 1f), b2body.body.getLinearVelocity().y);
        }
        if (controller.right) {
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 30f, 1f), b2body.body.getLinearVelocity().y);
        }

        if (player.timeSinceLastShot >= player.shootDelay) {
            Vector2 aim = new Vector2(0.0f, 500f); // just shoot up
            aim.scl(5f);
            // create a bullet
            ballFactory.createBullet(b2body.body.getPosition().x,
                    b2body.body.getPosition().y,
                    aim.x,
                    aim.y,
                    BulletComponent.Owner.PLAYER);
            player.timeSinceLastShot -= player.shootDelay;

        } else {
            player.timeSinceLastShot += deltaTime;
        }
    }
}
