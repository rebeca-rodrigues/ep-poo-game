package src.classes.bosses;

import java.awt.Color;
import src.lib.GameLib;
import src.classes.Game;
import src.classes.enemys.EnemyProjectile;

public class BossType2 extends Boss{

    private double life;
    private double speed;
    private double angle;
    private double rotationSpeed;
    private long nextShoot;

    public BossType2(double life, double x, double y) {
        super(life, x, y, 15.00);
        speed = 0.3;
        angle = (3 * Math.PI) / 2;
        rotationSpeed = 0.0;
        nextShoot = System.currentTimeMillis() + 500;
    }

    @Override
    public void update(long delta, Game game) {
        if (exploding) {
            if (System.currentTimeMillis() > explosionEnd) {
                game.running = false; // termina a fase
            }
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
    protected void drawBoss() {
        GameLib.setColor(Color.CYAN);
        GameLib.drawCircle(x, y, radius);
    }

}
