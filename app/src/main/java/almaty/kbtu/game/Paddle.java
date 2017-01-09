package almaty.kbtu.game;

import android.graphics.RectF;

/**
 * Created by RIA on 24.05.2015.
 */
public class Paddle {
    private RectF rect;
    // How long and high our paddle will be
    private float length;
    private float height;
    // X is the far left of the rectangle which forms our paddle
    private float x;
    // Y is the top coordinate
    private float y;
    private float paddleSpeed;
    // Which ways can the paddle move
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    // Is the paddle moving and in which direction
    private int paddleMoving = STOPPED;
    // This the the constructor method
    // When we create an object from this class we will pass
    // in the screen width and height
    public Paddle(int screenX, int screenY) {
        length = 130;
        height = 20;
        // Start paddle in roughly the sceen centre
        x = screenX / 2;
        y = screenY - 20;
        rect = new RectF(x, y, x + length, y + height);
        // How fast is the paddle in pixels per second
        paddleSpeed = 300;
    }
    // This is a getter method to make the rectangle that
    // defines our paddle available in BreakoutView class
    public RectF getRect() {
        return rect;
    }
    // This method will be used to change/set if the paddle is going left, right or nowhere
    public void setMovementState(int state) {
        paddleMoving = state;
    }
    public void update(long fps) {
        if (paddleMoving == LEFT) {
            x = x - paddleSpeed / fps;
        }
        if (paddleMoving == RIGHT) {
            x = x + paddleSpeed / fps;
        }
        rect.left = x;
        rect.right = x + length;
    }
}
