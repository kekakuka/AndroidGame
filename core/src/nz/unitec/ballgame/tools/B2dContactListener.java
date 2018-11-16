package nz.unitec.ballgame.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import nz.unitec.ballgame.entity.components.CollisionComponent;
import nz.unitec.ballgame.entity.components.Mapper;

public class B2dContactListener implements ContactListener {

	public B2dContactListener(){
	}
	
	@Override
	public void beginContact(Contact contact) {
		System.out.println("begin Contact...");
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		//System.out.println(fa.getBody().getType()+" has hit "+ fb.getBody().getType());
		
		if(fa.getBody().getUserData() instanceof Entity){
			Entity ent = (Entity) fa.getBody().getUserData();
			System.out.println("begin Contact..." + Mapper.typeCom.get(ent).type);
			entityCollision(ent,fb);
			return;
		}else if(fb.getBody().getUserData() instanceof Entity){
			Entity ent = (Entity) fb.getBody().getUserData();
			System.out.println("begin Contact..." + Mapper.typeCom.get(ent).type);
			entityCollision(ent,fa);
			return;
		}
		System.out.println("begin Contact---");
	}

	private void entityCollision(Entity ent, Fixture fb) {
		if(fb.getBody().getUserData() instanceof Entity){
			Entity colEnt = (Entity) fb.getBody().getUserData();
			
			CollisionComponent col = ent.getComponent(CollisionComponent.class);
			CollisionComponent colb = colEnt.getComponent(CollisionComponent.class);
			
			if(col != null){
				col.collisionEntity = colEnt;
			}else if(colb != null){
				colb.collisionEntity = ent;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		//System.out.println("Contact end");
	}
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {		
	}
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {		
	}

}
