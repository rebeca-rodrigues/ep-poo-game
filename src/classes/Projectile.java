package src.classes;

import src.classes.bosses.Boss;
import src.classes.enemys.Enemy;
import src.lib.GameLib;
import java.awt.Color;


// Classe Projectile (projéteis do player)
public class Projectile {

    double x, y;
    double vx, vy;
    boolean active = true;

    public Projectile(double x, double y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public void update(long delta) {
        if (!active) return;

        x += vx * delta;
        y += vy * delta;

        if (y < 0) active = false;
    }

    public void draw() {
        if (!active) return;

        GameLib.setColor(Color.GREEN);
        GameLib.drawLine(x, y - 5, x, y + 5);
        GameLib.drawLine(x - 1, y - 3, x - 1, y + 3);
        GameLib.drawLine(x + 1, y - 3, x + 1, y + 3);
    }

    public boolean isActive() {
        return active;
    }

    public boolean collidesWith(Enemy e) {
        double dx = e.getX() - x;
        double dy = e.getY() - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < e.getRadius();
    }

    public boolean collidesWith(Boss b) {
        double dx = b.getX() + 80 - x;
        double dy = b.getY() + 30 - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < b.getRadius() * 6;
    }

    public void deactivate() {
        active = false;
    }
}

