package src.classes.bosses;

import java.awt.Color;
import src.lib.GameLib;
import src.classes.Game;
//import src.classes.Projectile;

public class BossType2 extends Boss{

    private double speed;
    private String direction = "LEFT";
    private long nextShoot;
    private long bigShoot;
    double angle = (3 * Math.PI) / 2;
    double vx = Math.cos(angle) * 0.45;
    double vy = Math.sin(angle) * 0.45 * (-1.0);
    private final int[][] sprite = {
        {0,0,1,1,1,1,1,1,0,0},
        {0,1,1,0,1,1,0,1,1,0},
        {1,1,0,0,1,1,0,0,1,1},
        {1,1,1,1,1,1,1,1,1,1},
        {1,0,1,0,1,1,0,1,0,1},
        {1,0,1,0,0,0,0,1,0,1},
        {0,0,0,1,0,0,1,0,0,0},
        {0,0,1,0,0,0,0,1,0,0}
    };

    public BossType2(double life, double x, double y) {
        super(life, x, y, 15.00);
        speed = 0.3;
        nextShoot = System.currentTimeMillis() + 500;
        bigShoot = System.currentTimeMillis() + 5000;
    }

    @Override
    public void update(long delta, Game game) {
        if (exploding) {
            if (System.currentTimeMillis() > explosionEnd) {
                game.running = false;
            }
            return;
        }
        if (direction == "LEFT") x -= speed;
        if (direction == "RIGHT") x += speed;
        if (y < finalY) y += speed;
        if (x < 0){x = 0; direction = "RIGHT";}
        if (x + 150 >= GameLib.WIDTH){x = GameLib.WIDTH - 151; direction = "LEFT";}

        long now = System.currentTimeMillis();

        if (now > nextShoot) {
            BossProjectile bp1 = new BossProjectile(x+70, (y + 6 * radius), 0.25, 1.0);
            BossProjectile bp2 = new BossProjectile(x+70, (y + 6 * radius), -0.25, 1.0);
            BossProjectile bp3 = new BossProjectile(x+70, (y + 6 * radius), 0, 1.0);
            game.addBossProjectile(bp1);
            game.addBossProjectile(bp2);
            game.addBossProjectile(bp3);
            nextShoot = now + 300;
        }
        
        if (now > bigShoot) {
            game.addEnemyType2(x, y);
            bigShoot = now + 6000;
        }
    }

    @Override
    protected void drawBoss() {
        GameLib.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < sprite.length; i++) {
            for (int j = 0; j < sprite[i].length; j++) {
                if (sprite[i][j] == 1) {
                    int pj = (int) (x + j * radius);
                    int pi = (int) (y + i * radius);
                    GameLib.fillRect(pj, pi, radius, radius);
                }
            }
        }
    }

}
