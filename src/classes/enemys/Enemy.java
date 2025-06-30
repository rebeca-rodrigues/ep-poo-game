package src.classes.enemys;

import src.lib.GameLib;
import src.classes.Game;

// Classe abstrata Enemy e subclasses inimigo tp 1 e Inimigo tipo 2
public abstract class Enemy {

    protected double x, y;
    protected double radius;
    protected boolean active = true;
    protected boolean exploding = false;
    protected long explosionStart, explosionEnd;

    public Enemy(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public abstract void update(long delta, Game game);

    public void draw() {
        if (exploding) {
            double alpha = (System.currentTimeMillis() - explosionStart) / (double) (explosionEnd - explosionStart);
            GameLib.drawExplosion(x, y, alpha);
        } else {
            drawEnemy();
        }
    }

    protected abstract void drawEnemy();

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

