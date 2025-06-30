package src.classes.enemys;

import java.awt.Color;
import src.lib.GameLib;
import src.classes.Game;

// EnemyType2
public class EnemyType2 extends Enemy {

    private double speed;
    private double angle;
    private double rotationSpeed;
    private boolean shootNow;

    public EnemyType2(double x, double y) {
        super(x, y, 12.0);
        speed = 0.42;
        angle = (3 * Math.PI) / 2;
        rotationSpeed = 0.0;
        shootNow = false;
    }

    @Override
    public void update(long delta, Game game) {
        if (exploding) {
            if (System.currentTimeMillis() > explosionEnd) active = false;
            return;
        }

        double prevY = y;

        x += speed * Math.cos(angle) * delta;
        y += speed * Math.sin(angle) * (-1.0) * delta;
        angle += rotationSpeed * delta;

        if (x < -10 || x > GameLib.WIDTH + 10) {
            active = false;
            return;
        }

        double threshold = GameLib.HEIGHT * 0.30;

        if (prevY < threshold && y >= threshold) {
            if (x < GameLib.WIDTH / 2) rotationSpeed = 0.003;
            else rotationSpeed = -0.003;
        }

        if (rotationSpeed > 0 && Math.abs(angle - 3 * Math.PI) < 0.05) {
            rotationSpeed = 0.0;
            angle = 3 * Math.PI;
            shootNow = true;
        }

        if (rotationSpeed < 0 && Math.abs(angle) < 0.05) {
            rotationSpeed = 0.0;
            angle = 0.0;
            shootNow = true;
        }

        if (shootNow) {
            double[] angles = {Math.PI / 2 + Math.PI / 8, Math.PI / 2, Math.PI / 2 - Math.PI / 8};
            for (double a : angles) {
                double randomAngle = a + Math.random() * Math.PI / 6 - Math.PI / 12;
                double vx = Math.cos(randomAngle) * 0.30;
                double vy = Math.sin(randomAngle) * 0.30;
                EnemyProjectile ep = new EnemyProjectile(x, y, vx, vy);
                game.addEnemyProjectile(ep);
            }
            shootNow = false;
        }
    }

    @Override
    protected void drawEnemy() {
        GameLib.setColor(Color.MAGENTA);
        GameLib.drawDiamond(x, y, radius);
    }
}

