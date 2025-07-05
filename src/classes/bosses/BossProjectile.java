package src.classes.bosses;

import src.lib.GameLib;
import java.awt.Color;

// Classe EnemyProjectile (projéteis inimigos)
public class BossProjectile {

    public double x, y;
    double vx, vy;
    public double radius = 2.0;
    boolean active = true;

    public BossProjectile(double x, double y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public void update(long delta) {
        if (!active) return;

        x += vx * delta;
        y += vy * delta;

        if (y > GameLib.HEIGHT) active = false;
    }

    public void draw() {
        if (!active) return;

        GameLib.setColor(Color.RED);
        GameLib.drawCircle(x, y, radius);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }
}
