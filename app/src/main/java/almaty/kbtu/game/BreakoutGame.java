package almaty.kbtu.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by RIA on 22.05.2015.
 */
public class BreakoutGame extends Activity {
    BreakoutView breakoutView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize gameView and set it as the view
        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);
    }
    // Notice we implement runnable so we have
    // A thread and can override the run method.
    public class BreakoutView extends SurfaceView implements Runnable {
        // This is our thread
        Thread gameThread = null;
        SurfaceHolder ourHolder;
        // A boolean which we will set and unset
        // when the game is running- or not.
        volatile boolean playing;
        // Game is paused at the start
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        long fps;
        private long timeThisFrame;
        // The size of the screen in pixels
        int screenX;
        int screenY;
        // The players paddle
        Paddle paddle;
        // A ball
        Ball ball;
        // Up to 200 bricks
        Brick[] bricks = new Brick[200];
        int numBricks = 0;
        // The score
        int pscore = 0;
        // Lives
        int lives = 3;
        public BreakoutView(Context context) {
            super(context);
            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();
            // Get a Display object to access screen details
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;
            paddle = new Paddle(screenX, screenY);
            // Create a ball
            ball = new Ball(screenX, screenY);
            createBricksAndRestart();
        }
        public void createBricksAndRestart(){
            // Put the ball back to the start
            ball.reset(screenX, screenY);
            int brickWidth = screenX / 8;
            int brickHeight = screenY / 10;
            // Build a wall of bricks
            numBricks = 0;
            for (int column = 0; column < 8; column++) {
                for (int row = 0; row < 3; row++) {
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    numBricks++;
                }
            }
            // if game over reset scores and lives
            if (lives == 0) {
                pscore = 0;
                lives = 3;
            }
        }
        @Override
        public void run() {
            while (playing) {
                long startFrameTime = System.currentTimeMillis();
                if (!paused) {
                    update();
                }
                draw();
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }
        // Everything that needs to be updated goes in here
        // Movement, collision detection etc.
        public void update() {
            // Move the paddle if required
            paddle.update(fps);
            ball.update(fps);
            // Check for ball colliding with a brick
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bricks[i].getRect(), ball.getRect())) {
                        bricks[i].setInvisible();
                        ball.reverseYVelocity();
                        pscore = pscore + 10;
                    }
                }
            }
            // Check for ball colliding with paddle
            if (RectF.intersects(paddle.getRect(), ball.getRect())) {
                ball.setRandomXVelocity();
                ball.reverseYVelocity();
                ball.clearObstacleY(paddle.getRect().top - 2);
            }
            // Bounce the ball back when it hits the bottom of screen
            if (ball.getRect().bottom > screenY) {
                ball.reverseYVelocity();
                ball.clearObstacleY(screenY - 2);
                // Lose a life
                lives--;
                if (lives == 0) {
                    paused = true;
                    createBricksAndRestart();
                }
            }
            // Bounce the ball back when it hits the top of screen
            if (ball.getRect().top < 0) {
                ball.reverseYVelocity();
                ball.clearObstacleY(12);
            }
            // If the ball hits left wall bounce
            if (ball.getRect().left < 0) {
                ball.reverseXVelocity();
                ball.clearObstacleX(2);
            }
            // If the ball hits right wall bounce
            if (ball.getRect().right > screenX - 10) {
                ball.reverseXVelocity();
                ball.clearObstacleX(screenX - 22);
            }
            // Pause if cleared screen
            if (pscore == numBricks * 10) {
                paused = true;
                createBricksAndRestart();
            }
        }
        // Draw the newly updated scene
        public void draw() {
            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();
                // Draw the background color
                canvas.drawColor(Color.argb(255, 26, 128, 182));
                // Choose the brush color for drawing
                paint.setColor(Color.argb(255, 255, 255, 255));
                // Draw the paddle
                canvas.drawRect(paddle.getRect(), paint);
                // Draw the ball
                canvas.drawRect(ball.getRect(), paint);
                // Change the brush color for drawing
                paint.setColor(Color.argb(255, 249, 129, 0));
                // Draw the bricks if visible
                for (int i = 0; i < numBricks; i++) {
                    if (bricks[i].getVisibility()) {
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                }
                // Choose the brush color for drawing
                paint.setColor(Color.argb(255, 255, 255, 255));
                // Draw the score
                paint.setTextSize(40);
                canvas.drawText("Score: " + pscore + "   Lives: " + lives, 10, 50, paint);
                if (pscore == numBricks * 10) {
                    paint.setTextSize(90);
                    canvas.drawText("YOU WIN!", 10, screenY / 2, paint);
                }
                if (lives <= 0) {
                    paint.setTextSize(90);
                    canvas.drawText("YOU LOSE!", 10, screenY / 2, paint);
                }
                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    paused = false;
                    if (motionEvent.getX() > screenX / 2) {
                        paddle.setMovementState(paddle.RIGHT);
                    } else {
                        paddle.setMovementState(paddle.LEFT);
                    }
                    break;
                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:
                    paddle.setMovementState(paddle.STOPPED);
                    break;
            }
            return true;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        breakoutView.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Tell the gameView pause method to execute
        breakoutView.pause();
    }
}

