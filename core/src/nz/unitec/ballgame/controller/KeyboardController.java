package nz.unitec.ballgame.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class KeyboardController implements InputProcessor {
    private OrthographicCamera cam;
    public boolean left, right, up, down;
    public boolean isMouse1Down, isMouse2Down, isMouse3Down;
    public boolean isDragged;
    public Vector2 mouseLocation = new Vector2(0, 0);

    public KeyboardController(OrthographicCamera cam) {
        this.cam = cam;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;
        switch (keycode) // switch code base on the variable keycode
        {
            case Keys.LEFT:    // if keycode is the same as Keys.LEFT a.k.a 21
                left = true;    // do this
                keyProcessed = true;    // we have reacted to a keypress
                break;
            case Keys.RIGHT:    // if keycode is the same as Keys.LEFT a.k.a 22
                right = true;    // do this
                keyProcessed = true;    // we have reacted to a keypress
                break;
            case Keys.UP:        // if keycode is the same as Keys.LEFT a.k.a 19
                up = true;        // do this
                keyProcessed = true;    // we have reacted to a keypress
                break;
            case Keys.DOWN:    // if keycode is the same as Keys.LEFT a.k.a 20
                down = true;    // do this
                keyProcessed = true;    // we have reacted to a keypress
        }
        return keyProcessed;    //  return our peyProcessed flag
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyProcessed = false;
        switch (keycode) // switch code base on the variable keycode
        {
            case Keys.LEFT:    // if keycode is the same as Keys.LEFT a.k.a 21
                left = false;    // do this
                keyProcessed = true;    // we have reacted to a keypress
                break;
            case Keys.RIGHT:    // if keycode is the same as Keys.LEFT a.k.a 22
                right = false;    // do this
                keyProcessed = true;    // we have reacted to a keypress
                break;
            case Keys.UP:        // if keycode is the same as Keys.LEFT a.k.a 19
                up = false;        // do this
                keyProcessed = true;    // we have reacted to a keypress
                break;
            case Keys.DOWN:    // if keycode is the same as Keys.LEFT a.k.a 20
                down = false;    // do this
                keyProcessed = true;    // we have reacted to a keypress
        }
        return keyProcessed;    //  return our peyProcessed flag
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            isMouse1Down = true;
        } else if (button == 1) {
            isMouse2Down = true;
        } else if (button == 2) {
            isMouse3Down = true;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragged = false;
        //System.out.println(button);
        if (button == 0) {
            isMouse1Down = false;
        } else if (button == 1) {
            isMouse2Down = false;
        } else if (button == 2) {
            isMouse3Down = false;
        }
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragged = true;
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseLocation.x = screenX;
        mouseLocation.y = screenY;
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public boolean isTouched() {
        return Gdx.input.isTouched();
    }

    public Vector2 getTouchedPosition() {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        this.cam.unproject(touchPos);
        return new Vector2(touchPos.x, touchPos.y);
    }

    public Vector2 getAcceleration() {
        float mSensorX = 0;
        float mSensorY = 0;
        switch (Gdx.input.getRotation()) {
            case 0:
                mSensorX = Gdx.input.getAccelerometerX();
                mSensorY = Gdx.input.getAccelerometerY();
            case 90:
                mSensorX = Gdx.input.getAccelerometerY();
                mSensorY = -Gdx.input.getAccelerometerX();
                break;
            case 180:
                mSensorX = -Gdx.input.getAccelerometerX();
                mSensorY = -Gdx.input.getAccelerometerY();
                break;
            case 270:
                mSensorX = Gdx.input.getAccelerometerY();
                mSensorY = -Gdx.input.getAccelerometerX();
                break;
        }
        mSensorX = Gdx.input.getAccelerometerY();
        mSensorY = -Gdx.input.getAccelerometerX();
        Vector2 acceleration = new Vector2(mSensorX, mSensorY);
        acceleration.scl(0.3f);
        return acceleration;
    }
}
