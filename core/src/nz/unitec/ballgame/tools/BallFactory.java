package nz.unitec.ballgame.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import nz.unitec.ballgame.BallGame;
import nz.unitec.ballgame.entity.components.AnimationComponent;
import nz.unitec.ballgame.entity.components.B2dBodyComponent;
import nz.unitec.ballgame.entity.components.BulletComponent;
import nz.unitec.ballgame.entity.components.CollisionComponent;
import nz.unitec.ballgame.entity.components.EnemyComponent;
import nz.unitec.ballgame.entity.components.PlayerComponent;
import nz.unitec.ballgame.entity.components.StateComponent;
import nz.unitec.ballgame.entity.components.TextureComponent;
import nz.unitec.ballgame.entity.components.TransformComponent;
import nz.unitec.ballgame.entity.components.TypeComponent;
import nz.unitec.ballgame.entity.systems.RenderingSystem;
import nz.unitec.ballgame.loader.B2dAssetManager;

public class BallFactory {
    private BodyFactory bodyFactory;
    public World world;
    private PooledEngine engine;
    public int currentLevel = 0;

    private final int WALL_WIDTH = 20;
    private final int FLOOR_HEIGHT = 60;
    private final int FLOOR_OFFSET_Y = -10;
    private final int PLAYER_RADIUS = 35;
    private final int PLAYER_OFFSET_Y = FLOOR_OFFSET_Y + FLOOR_HEIGHT + PLAYER_RADIUS / 2 - 30;
    private TextureRegion wallTex;
    private TextureRegion floorTex;
    private TextureRegion enemyTex;
    private TextureRegion waterTex;
    private TextureRegion platformTex;
    private TextureRegion bulletTex;
    private TextureAtlas atlas;
    private OpenSimplexNoise openSim;
    public Entity player;
    public B2dAssetManager assman;
    private Random  random=new Random();;

    private static final int MODE_EASY = 1;
    private static final int MODE_HARD = 2;

    public BallFactory(PooledEngine en, B2dAssetManager assMan, int i) {
        engine = en;
        this.atlas = assMan.manager.get("images/game.atlas", TextureAtlas.class);

        int wallWidth = WALL_WIDTH;
        int wallHeight = RenderingSystem.FRUSTUM_HEIGHT;
        wallTex = DFUtils.makeTextureRegion(wallWidth, wallHeight, "1F5F9FFF"); //TODO make some damn images for this stuff

        floorTex = atlas.findRegion("reallybadlydrawndirt");
        floorTex.setRegionHeight(FLOOR_HEIGHT);
        floorTex.setRegionWidth(RenderingSystem.FRUSTUM_WIDTH);

        //enemyTex = atlas.findRegion("enemy");
        this.assman = assMan;

        waterTex = atlas.findRegion("water");
       bulletTex = DFUtils.makeTextureRegion(10, 10, "444444FF");
        platformTex = atlas.findRegion("platform");
        if(i == MODE_EASY){
            world = new World(new Vector2(0, -9.8f), false);
        }else if(i == MODE_HARD){
            world = new World(new Vector2(0, -50f), false);
        }else{
            world = new World(new Vector2(0, -9.8f), false);
        }
        world.setContactListener(new B2dContactListener());
        bodyFactory = BodyFactory.getInstance(world);

        openSim = new OpenSimplexNoise(MathUtils.random(2000l));

//        pem = new ParticleEffectManager();
//        pem.addParticleEffect(ParticleEffectManager.FIRE, assMan.manager.get("particles/fire.pe",ParticleEffect.class),1f/128f);
//        pem.addParticleEffect(ParticleEffectManager.WATER, assMan.manager.get("particles/water.pe",ParticleEffect.class),1f/16f);
//        pem.addParticleEffect(ParticleEffectManager.SMOKE, assMan.manager.get("particles/smoke.pe",ParticleEffect.class),1f/64f);
        this.createBackground();
    }

    /**
     * Creates a ball per level up to yLevel
     *
     * @param timeSinceBegin
     */
    public void generateLevel(float timeSinceBegin) {
        generateSingleBall((int) timeSinceBegin);
//        createEnemy(enemyTex, 100, 100); // just for test
        currentLevel++;

//        float  x1 = genNForL(currentLevel,(int) timeSinceBegin);
//        System.out.println("x1 = " + x1);
//        float  x2= genNForL(currentLevel,(int) timeSinceBegin);
//        System.out.println("x2 = " + x2);
    }

    // generate noise for level
    private float genNForL(int level, int height) {
        return (float) openSim.eval(height, level);
    }

    private void generateSingleBall(int i) {
        int offset = 20;
        if (genNForL(i, currentLevel) > -0.5f) {
            // only make enemies above level 5 (stops instant deaths)
            if (currentLevel > 5) {
                if (genNForL(i * 300, currentLevel) > -0.2f) {

                    TextureRegion    enemy;

;                   int enemyUnmber=  random.nextInt(18)+101;
                    enemy=atlas.findRegion(String.valueOf(enemyUnmber));
                    // add an enemy
                    createEnemy(enemy, genNForL(i * 100, currentLevel) * (BallGame.WIDTH / 2 - offset) + BallGame.WIDTH / 2, BallGame.HEIGHT);
                }
            }

            //only make cloud enemies above level 10 (stops insta deaths)
            if (currentLevel > 0) {
                if (genNForL(i * 400, currentLevel) > 0.3f) {
                    // add a cloud enemy
//                    createSeeker(genNForL(i * 100,currentLevel) * range + offset,currentLevel * 2 + 1);
                }
            }
        }
    }


    public void createFloor() {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);

//        position.position.set(-RenderingSystem.FRUSTUM_WIDTH / 2,RenderingSystem.FRUSTUM_HEIGHT / 2,0);
        position.position.set(RenderingSystem.FRUSTUM_WIDTH / 2, +FLOOR_OFFSET_Y-10, 0);
//        position.position.set(20,0,0);
        texture.region = floorTex;
        texture.offsetX = 2.5f;
        type.type = TypeComponent.SCENERY;
//        b2dbody.body = bodyFactory.makeBoxPolyBody(20,-16, 46, 32, BodyFactory.STONE, BodyDef.BodyType.StaticBody);
        b2dbody.body = bodyFactory.makeBoxPolyBody(RenderingSystem.FRUSTUM_WIDTH / 2, +FLOOR_OFFSET_Y-10, RenderingSystem.FRUSTUM_WIDTH, FLOOR_HEIGHT, BodyFactory.STEEL, BodyDef.BodyType.StaticBody);


        entity.add(b2dbody);
        entity.add(texture);
        entity.add(position);
        entity.add(type);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);
    }

    public Entity createEnemy(TextureRegion tex, float x, float y) {
        System.out.println("Making Enemy -  x:" + x + ",y:" + y);
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);

        b2dbody.body = bodyFactory.makeCirclePolyBody(x, y, 40, BodyFactory.RUBBER, BodyDef.BodyType.DynamicBody, true);
        position.position.set(x, y, 0);
        texture.region = tex;
        enemy.xPosCenter = x;
        type.type = TypeComponent.ENEMY;
        b2dbody.body.setUserData(entity);

        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(enemy);
        entity.add(type);

        engine.addEntity(entity);

        return entity;
    }

    public Entity createPlayer(OrthographicCamera cam) {

        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
//        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
//        SteeringComponent scom = engine.createComponent(SteeringComponent.class);

        player.cam = cam;
        b2dbody.body = bodyFactory.makeCirclePolyBody(RenderingSystem.FRUSTUM_WIDTH / 2, PLAYER_OFFSET_Y-10, PLAYER_RADIUS, BodyFactory.STONE, BodyDef.BodyType.DynamicBody, true);
        b2dbody.body.setSleepingAllowed(false); // don't allow unit to sleep or it wil sleep through events if stationary too long
        // set object position (x,y,z) z used to define draw order 0 first drawn
//        Animation anim = new Animation(0.1f, atlas.findRegions("flame_a"));
//        //anim.setPlayMode(Animation.PlayMode.LOOP);
//        animCom.animations.put(StateComponent.STATE_NORMAL, anim);
//        animCom.animations.put(StateComponent.STATE_MOVING, anim);
//        animCom.animations.put(StateComponent.STATE_JUMPING, anim);
//        animCom.animations.put(StateComponent.STATE_FALLING, anim);
//        animCom.animations.put(StateComponent.STATE_HIT, anim);

        position.position.set(RenderingSystem.FRUSTUM_WIDTH / 2, PLAYER_OFFSET_Y-10, 0);
        texture.region = atlas.findRegion("player");
        texture.offsetY = 0.5f;
        type.type = TypeComponent.PLAYER;
        stateCom.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

//        scom.body = b2dbody.body;

        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
      //  entity.add(animCom);
        entity.add(player);
        entity.add(colComp);
        entity.add(type);
        entity.add(stateCom);
//        entity.add(scom);

        engine.addEntity(entity);
        this.player = entity;
        return entity;
    }

    public void createWalls() {

        for (int i = 0; i < 2; i++) {
            System.out.println("Making wall " + i);
            Entity entity = engine.createEntity();
            B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
            TransformComponent position = engine.createComponent(TransformComponent.class);
            TextureComponent texture = engine.createComponent(TextureComponent.class);
            TypeComponent type = engine.createComponent(TypeComponent.class);

            //make wall
            b2dbody.body = bodyFactory.makeBoxPolyBody(0 + (i * RenderingSystem.FRUSTUM_WIDTH), RenderingSystem.FRUSTUM_HEIGHT / 2, WALL_WIDTH, RenderingSystem.FRUSTUM_HEIGHT, BodyFactory.STONE, BodyDef.BodyType.KinematicBody, true);
            position.position.set(0 + (i * RenderingSystem.FRUSTUM_WIDTH), 0, 0);
            texture.region = wallTex;
            type.type = TypeComponent.SCENERY;

            entity.add(b2dbody);
            entity.add(position);
            entity.add(texture);
            entity.add(type);
            b2dbody.body.setUserData(entity);

            engine.addEntity(entity);
        }
    }


    public Entity createBullet(float x, float y, float xVel, float yVel, BulletComponent.Owner own) {
//        System.out.println("Making bullet" + x + ":" + y + ":" + xVel + ":" + yVel);
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        StateComponent stateCom = engine.createComponent(StateComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        BulletComponent bul = engine.createComponent(BulletComponent.class);

        bul.owner = own;

        b2dbody.body = bodyFactory.makeCirclePolyBody(x, y, 20f, BodyFactory.STONE, BodyDef.BodyType.DynamicBody, true);
        b2dbody.body.setBullet(true); // increase physics computation to limit body travelling through other objects
        bodyFactory.makeAllFixturesSensors(b2dbody.body); // make bullets sensors so they don't move player
        position.position.set(x, y, 0);
        texture.region = bulletTex;
        Animation<TextureRegion> anim = new Animation(0.05f, DFUtils.spriteSheetToFrames(atlas.findRegion("FlameSpriteAnimation"), 7, 1));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        animCom.animations.put(0, anim);

        type.type = TypeComponent.BULLET;
        b2dbody.body.setUserData(entity);
        bul.xVel = xVel;
        bul.yVel = yVel;

        entity.add(bul);
        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(animCom);
        entity.add(stateCom);
        entity.add(type);

        engine.addEntity(entity);

        return entity;
    }

    public void removeEntity(Entity ent) {
        engine.removeEntity(ent);
    }


//    public Entity createSeeker(float x, float y) {
//        Entity entity = engine.createEntity();
//        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
//        TransformComponent position = engine.createComponent(TransformComponent.class);
//        TextureComponent texture = engine.createComponent(TextureComponent.class);
//        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
//        TypeComponent type = engine.createComponent(TypeComponent.class);
//        StateComponent stateCom = engine.createComponent(StateComponent.class);
//        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
//        SteeringComponent scom = engine.createComponent(SteeringComponent.class);
//
//
//        b2dbody.body = bodyFactory.makeCirclePolyBody(x,y,1, BodyFactory.STONE, BodyDef.BodyType.DynamicBody,true);
//        b2dbody.body.setGravityScale(0f);  // no gravity for our floating enemy
//        b2dbody.body.setLinearDamping(0.3f); // setting linear dampening so the enemy slows down in our box2d world(or it can float on forever)
//
//        position.position.set(x,y,0);
//        texture.region = atlas.findRegion("enemy");
//        type.type = TypeComponent.ENEMY;
//        stateCom.set(StateComponent.STATE_NORMAL);
//        b2dbody.body.setUserData(entity);
//        // bodyFactory.makeAllFixturesSensors(b2dbody.body); // seeker  should fly about not fall
//        scom.body = b2dbody.body;
//        enemy.enemyType = EnemyComponent.Type.CLOUD;
//
//        // set out steering behaviour
//        scom.steeringBehavior  = SteeringPresets.getWander(scom);
//        //scom.setIndependentFacing(true); // stop clouds rotating
//        scom.currentMode = SteeringComponent.SteeringState.WANDER;
//
//        entity.add(b2dbody);
//        entity.add(position);
//        entity.add(texture);
//        entity.add(colComp);
//        entity.add(type);
//        entity.add(enemy);
//        entity.add(stateCom);
//        entity.add(scom);
//
//        engine.addEntity(entity);
//        return entity;
//
//    }


    public void resetWorld(int i) {
        currentLevel = 0;
        openSim = new OpenSimplexNoise(MathUtils.random(2000l));
        Array<Body> bods = new Array<Body>();
        world.getBodies(bods);
        for (Body bod : bods) {
            world.destroyBody(bod);
        }
        world.dispose();
        if(i == MODE_EASY){
            world = new World(new Vector2(0, -9.8f), false);
        }else if(i == MODE_HARD){
            world = new World(new Vector2(0, -50f), false);
        }else{
            world = new World(new Vector2(0, -9.8f), false);
        }
        world.setContactListener(new B2dContactListener());
        bodyFactory = BodyFactory.getInstance(world);

        openSim = new OpenSimplexNoise(MathUtils.random(2000l));
    }

    // makes the background item
    public void createBackground() {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);

        position.position.set(223, 357, -10);
        texture.region = atlas.findRegion("reallygoodback");

        type.type = TypeComponent.SCENERY;

        b2dbody.body = bodyFactory.makeBoxPolyBody(223, 357,RenderingSystem.FRUSTUM_WIDTH*2, RenderingSystem.FRUSTUM_HEIGHT*2, BodyFactory.STONE, BodyDef.BodyType.StaticBody,true);

        bodyFactory.makeAllFixturesSensors(b2dbody.body);


        entity.add(texture);
        entity.add(position);
        entity.add(type);
        entity.add(b2dbody);

        b2dbody.body.setUserData(entity);

        engine.addEntity(entity);
    }
}
