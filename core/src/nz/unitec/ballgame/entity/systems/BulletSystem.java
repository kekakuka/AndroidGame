package nz.unitec.ballgame.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import nz.unitec.ballgame.entity.components.B2dBodyComponent;
import nz.unitec.ballgame.entity.components.BulletComponent;
import nz.unitec.ballgame.entity.components.Mapper;
import nz.unitec.ballgame.tools.BallFactory;

public class BulletSystem extends IteratingSystem{
	private BallFactory ballFactory;
	
	@SuppressWarnings("unchecked")
	public BulletSystem(BallFactory ballFactory){
		super(Family.all(BulletComponent.class).get());
		this.ballFactory = ballFactory;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		//get box 2d body and bullet components
		B2dBodyComponent b2body = Mapper.b2dCom.get(entity);
		BulletComponent bullet = Mapper.bulletCom.get(entity);
		
		// apply bullet velocity to bullet body
		b2body.body.setLinearVelocity(bullet.xVel, bullet.yVel);
		
		// get player pos
		B2dBodyComponent playerBodyComp = Mapper.b2dCom.get(ballFactory.player);
		float px = playerBodyComp.body.getPosition().x;
		float py = playerBodyComp.body.getPosition().y;
		
		//get bullet pos
		float bx = b2body.body.getPosition().x;
		float by = b2body.body.getPosition().y;
		
		// if bullet is 1000 units away from player on any axis then it is probably off screen
		if(bx - px > 1000 || by - py > 1000){
			bullet.isDead = true;
		}
		
		//check if bullet is dead
		if(bullet.isDead){
//			System.out.println("Bullet died");
			// Mapper.peCom.get(bullet.particleEffect).isDead = true;
			b2body.isDead = true;
		}
	}
}
