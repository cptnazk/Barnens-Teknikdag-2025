package model.hero;

import manager.Camera;
import manager.GameEngine;
import view.Animation;
import model.GameObject;
import view.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
/*
Saker du kan prova ändra
- marios hastighet
- marios hopphöjd
- antal liv du börjar med
- fiendernas hastighet (hittas i enemy/Goomba respektive KoopaTroopa)
- ändra tidsgräns för ett spel (hittas i MapManager)
- bli odödlig (hittas i MarioForm)
 */
public class Mario extends GameObject{

    private int remainingLives;
    private int coins;
    private int points;
    private double invincibilityTimer;
    private MarioForm marioForm;
    private boolean toRight = true;

    public Mario(double x, double y){
        super(x, y, null);
        setDimension(48,48);

        remainingLives = 3; // Ändra värdet för att börja med mera eller mindre liv
        points = 0;
        coins = 0;
        invincibilityTimer = 0;

        ImageLoader imageLoader = new ImageLoader();
        BufferedImage[] leftFrames = imageLoader.getLeftFrames(MarioForm.SMALL);
        BufferedImage[] rightFrames = imageLoader.getRightFrames(MarioForm.SMALL);

        Animation animation = new Animation(leftFrames, rightFrames);
        marioForm = new MarioForm(animation, false, false);
        setStyle(marioForm.getCurrentStyle(toRight, false, false));
    }

    @Override
    public void draw(Graphics g){
        boolean movingInX = (getVelX() != 0);
        boolean movingInY = (getVelY() != 0);

        setStyle(marioForm.getCurrentStyle(toRight, movingInX, movingInY));

        super.draw(g);
    }

    public void jump(GameEngine engine) {
        if(!isJumping() && !isFalling()){
            setJumping(true);
            setVelY(10); // Ändra vördet för högre hopp. Små förändringar ger stora skillnader i höjd (prova först med ex. 15)
            engine.playJump();
        }
    }

    public void move(boolean toRight, Camera camera) {
        if(toRight){
            setVelX(5); // Ändra värdet för att röra dig framåt snabbare eller långsammare
        }
        else if(camera.getX() < getX()){
            setVelX(-5); // Ändre värdet för att röra dig bakåt snabbare eller långsammare (måste vara negativt för att röra sig bakåt)
        }

        this.toRight = toRight;
    }

    public boolean onTouchEnemy(GameEngine engine){

        if(!marioForm.isSuper() && !marioForm.isFire()){
            remainingLives--;
            engine.playMarioDies();
            return true;
        }
        else{
            engine.shakeCamera();
            marioForm = marioForm.onTouchEnemy(engine.getImageLoader());
            setDimension(48, 48);
            return false;
        }
    }

    public Fireball fire(){
        return marioForm.fire(toRight, getX(), getY());
    }

    public void acquireCoin() {
        coins++;
    }

    public void acquirePoints(int point){
        points = points + point;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

    public void setRemainingLives(int remainingLives) {
        this.remainingLives = remainingLives;
    }

    public int getPoints() {
        return points;
    }

    public int getCoins() {
        return coins;
    }

    public MarioForm getMarioForm() {
        return marioForm;
    }

    public void setMarioForm(MarioForm marioForm) {
        this.marioForm = marioForm;
    }

    public boolean isSuper() {
        return marioForm.isSuper();
    }

    public boolean getToRight() {
        return toRight;
    }

    public void resetLocation() {
        setVelX(0);
        setVelY(0);
        setX(50);
        setJumping(false);
        setFalling(true);
    }
}
