package nz.unitec.ballgame.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PlayerComponent implements Component, Poolable{
	public OrthographicCamera cam = null;
	public boolean onPlatform = false;
	public boolean onSpring = false;
	public boolean isDead = false;
	public int score = 0;
	public float shootDelay = 0.5f;
	public float timeSinceLastShot = 0f;
	@Override
	public void reset() {
		cam = null;
		onPlatform = false;
		onSpring = false;
		isDead = false;
		score = 0;
		shootDelay = 0.5f;
		timeSinceLastShot = 0f;
	}	
}
