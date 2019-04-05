package com.ngdroidapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Stack;

import istanbul.gamelab.ngdroid.base.BaseCanvas;
import istanbul.gamelab.ngdroid.util.Log;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */


public class GameCanvas extends BaseCanvas {

    private static final int STATE_PLAY = 0;
    private static final int STATE_DEAD = 1;

    private static final int DIRECTION_NORTH = 0;
    private static final int DIRECTION_EAST = 1;
    private static final int DIRECTION_SOUTH = 2;
    private static final int DIRECTION_WEST = 3;

    private int state;
    private int direction;
    private int speed;

    private int headx, heady;
    private int snakewidth, snakeheight, blockspace;
    private Stack<Rect> snake;
    private Paint snakepaint;

    private Rect food, oldfood;
    private int foodx, foody;
    private int foodwidth, foodheight, foodmargin;
    private boolean eat;
    private Paint foodpaint;

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        state = 0;
        direction = 0;
        speed = 1;

        headx = getUnitWidth() >> 1;
        heady = getUnitHeight() >> 1;
        snakewidth = 30;
        snakeheight = 30;
        blockspace = 5 + 30;

        snake = new Stack<>();
        snake.push(new Rect(headx, heady, headx + snakewidth, heady + snakewidth));
        snakepaint = new Paint();
        snakepaint.setColor(Color.WHITE);

        foodmargin = 50;
        foodwidth = snakewidth >> 1;
        foodheight = snakeheight >> 1;
        food = new Rect();
        oldfood = new Rect();
        eat = false;
        foodpaint = new Paint();
        foodpaint.setColor(Color.RED);
        respawnFood();
    }

    public void update() {
        if(state == STATE_PLAY) {
            moveSnake();
            checkCollisions();
        } else if(state == STATE_DEAD) {
            snakepaint.setColor(Color.YELLOW);
        }

    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.scale(getWidth() / 1920.0f, getHeight() / 1080.0f);

        canvas.drawColor(Color.BLACK);

        if(!eat) canvas.drawRect(food, foodpaint);

        for(int i = 0; i < snake.size(); i++) {
            canvas.drawRect(snake.get(i), snakepaint);
        }

        canvas.restore();
    }

    private void moveSnake() {
        switch (direction) {
            case DIRECTION_NORTH:
                heady -= speed;
                heady -= blockspace;
                break;
            case DIRECTION_SOUTH:
                heady += speed;
                heady += blockspace;
                break;
            case DIRECTION_WEST:
                headx -= speed;
                headx -= blockspace;
                break;
            case DIRECTION_EAST:
                headx += speed;
                headx += blockspace;
                break;
        }
        snake.push(new Rect(headx, heady, headx + snakewidth, heady + snakeheight));
        snake.remove(0);
    }

    private void checkCollisions() {
        if(headx <= 0 || headx + snakewidth >= getUnitWidth() || heady <= 0 || heady + snakeheight >= getUnitHeight()) {
            state = STATE_DEAD;
        }

        boolean flag = false;
        for(int i = 0; i < snake.size() - 1; i++) {
            if(snake.peek().intersect(snake.get(i)) && !eat) {
                state = STATE_DEAD;
            }
            if(eat && oldfood.intersect(snake.get(i))) {
                flag = true;
            }
        }
        eat = flag;

        if(food.intersect(snake.peek()) && !eat) {
            eat = true;
            oldfood.set(food.left, food.top, food.right, food.bottom);
            respawnFood();
        }
    }

    private void respawnFood() {
        switch (direction) {
            case DIRECTION_NORTH:
                snake.push(new Rect(snake.peek().left, snake.peek().top + blockspace, snake.peek().right, snake.peek().top + blockspace + snakeheight));
                break;
            case DIRECTION_SOUTH:
                snake.push(new Rect(snake.peek().left, snake.peek().top - blockspace, snake.peek().right, snake.peek().top - blockspace + snakeheight));
                break;
            case DIRECTION_WEST:
                snake.push(new Rect(snake.peek().left + blockspace, snake.peek().top, snake.peek().left + blockspace + snakewidth, snake.peek().top + snakeheight));
                break;
            case DIRECTION_EAST:
                snake.push(new Rect(snake.peek().left - blockspace, snake.peek().top, snake.peek().left - blockspace + snakewidth, snake.peek().top + snakeheight));
                break;
        }

        foodx = (int)(Math.random() * (getUnitWidth() - foodmargin)) + foodmargin;
        foody = (int)(Math.random() * (getUnitHeight() - foodmargin)) + foodmargin;
        food.set(foodx, foody, foodx + foodwidth, foody + foodheight);
    }

    private void resetGame() {
        this.setup();
    }

    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y, int id) {
        if(state == STATE_PLAY) {
            if (++direction == 4) direction = 0;
        }
    }

    public void touchMove(int x, int y, int id) {
    }

    public void touchUp(int x, int y, int id) {
        if(state == STATE_DEAD) {
            resetGame();
        }
    }


    public void pause() {

    }


    public void resume() {

    }


    public void reloadTextures() {

    }


    public void showNotify() {
    }

    public void hideNotify() {
    }

}
