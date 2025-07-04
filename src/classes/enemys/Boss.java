package src.classes.enemys;

import java.awt.Color;
import src.lib.GameLib;
import src.classes.Game;

// Boss
public class Boss extends Enemy {

    private double speed;
    private double angle;
    private double rotationSpeed;
    private long nextShoot;

    public Boss(double x, double y) {
        super(x, y, 9.0);
        speed = 0.20 + Math.random() * 0.15;
        angle = (3 * Math.PI) / 2;
        rotationSpeed = 0.0;
        nextShoot = System.currentTimeMillis() + 500;
    }

    @Override
    public void update(long delta, Game game) {
        if (exploding) {
            if (System.currentTimeMillis() > explosionEnd) active = false;
            return;
        }

        y += speed * Math.sin(angle) * (-1.0) * delta;
        x += speed * Math.cos(angle) * delta;
        angle += rotationSpeed * delta;

        if (y > GameLib.HEIGHT + 10) {
            active = false;
            return;
        }

        long now = System.currentTimeMillis();

        if (now > nextShoot && y < game.getPlayer().getY()) {
            double vx = Math.cos(angle) * 0.45;
            double vy = Math.sin(angle) * 0.45 * (-1.0);
            EnemyProjectile ep = new EnemyProjectile(x, y, vx, vy);
            game.addEnemyProjectile(ep);
            nextShoot = now + 200 + (long) (Math.random() * 500);
        }
    }

    @Override
    protected void drawEnemy() {
        GameLib.setColor(Color.CYAN);
        GameLib.drawCircle(x, y, radius);
    }
}