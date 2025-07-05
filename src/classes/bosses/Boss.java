package src.classes.bosses;

import src.lib.GameLib;
import src.classes.Game;

public abstract class Boss {

    protected double life;
    protected double x, y;
    protected double radius;
    protected boolean active = true;
    protected boolean exploding = false;
    protected long explosionStart, explosionEnd;


    public Boss(double life, double x, double y, double radius) {
        this.life = life;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public abstract void update(long delta, Game game);

    public void damage(long currentTime) {
        if (!exploding && active) {
            this.life -= 10;
            if (this.life <= 0.0) {
                this.life = 0.0;
                active = false;
                exploding = true;
                explosionStart = currentTime;
                explosionEnd = currentTime + 2000;
            }
        }
    }

    public void draw() {
        if (exploding) {
            double alpha = (System.currentTimeMillis() - explosionStart) / (double) (explosionEnd - explosionStart);
            GameLib.drawExplosion(x, y, alpha);
        } else {
            drawBoss();
        }
    }

    protected abstract void drawBoss();

    public void explode(long currentTime) {
        exploding = true;
        explosionStart = currentTime;
        explosionEnd = currentTime + 500;
    }

    public boolean isActive() {
        return active && !exploding;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }

}
