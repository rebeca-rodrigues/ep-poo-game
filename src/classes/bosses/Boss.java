package src.classes.bosses;

import src.lib.GameLib;

import java.awt.Color;

import src.classes.Game;

public abstract class Boss {

    protected double life;
    protected double x, y;
    protected double radius;
    protected boolean active = true;
    protected boolean exploding = false;
    protected long explosionStart, explosionEnd;
    protected double finalY;

    public Boss(double life, double x, double y, double radius) {
        this.life = life;
        this.x = x;
        this.y = -80;
        this.radius = radius;
        this.finalY = y;
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
            GameLib.setColor(Color.RED);
            GameLib.fillRect(x+70, y - 30, this.life / 5, 10);
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
