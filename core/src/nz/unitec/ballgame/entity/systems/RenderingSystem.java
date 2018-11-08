package nz.unitec.ballgame.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

import nz.unitec.ballgame.BallGame;
import nz.unitec.ballgame.entity.components.TextureComponent;
import nz.unitec.ballgame.entity.components.TransformComponent;

public class RenderingSystem extends SortedIteratingSystem {
    // debug stuff
    private boolean shouldRender = true;

    public static final int FRUSTUM_WIDTH = BallGame.WIDTH;
    public static final int FRUSTUM_HEIGHT = BallGame.HEIGHT;

    private static Vector2 meterDimensions = new Vector2();
    private static Vector2 pixelDimensions = new Vector2();

    private SpriteBatch batch;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    private OrthographicCamera cam;

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;

    @SuppressWarnings("unchecked")
    public RenderingSystem(SpriteBatch batch) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());

        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);

        renderQueue = new Array<Entity>();

        this.batch = batch;

        comparator = new ZComparator();

        cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        cam.position.set(FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2, 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderQueue.sort(comparator);

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        if (shouldRender) {
            batch.begin();

            for (Entity entity : renderQueue) {
                TextureComponent tex = textureM.get(entity);
                TransformComponent t = transformM.get(entity);

                if (tex.region == null || t.isHidden) {
                    continue;
                }

                float width = tex.region.getRegionWidth();
                float height = tex.region.getRegionHeight();

                float originX = width / 2f;
                float originY = height / 2f;

                batch.draw(tex.region,
                        t.position.x - originX + tex.offsetX,
                        t.position.y - originY + tex.offsetY,
                        originX, originY,
                        width, height,
                        t.scale.x, t.scale.y,
                        t.rotation);
            }
            batch.end();
        }
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }
}