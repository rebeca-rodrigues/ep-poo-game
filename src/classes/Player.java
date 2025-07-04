package src.classes;

import src.classes.enemys.EnemyProjectile;
import src.classes.power_ups.PowerUp;
import src.lib.GameLib;
import java.awt.Color;

// Classe Player
public class Player { //essa classe representa o jogador no jogo

    double x, y;   // posição do jogador
    // variaves do jogador
    double radius = 12; 
    boolean active = true;
    boolean exploding = false;
    long explosionStart, explosionEnd;
    double life;

    long nextShot = 0; 
    public boolean mutiplePorjectiles = false;
    public double velocity = 0.25;

    public Player(double x, double y, double life) {
        this.x = x;
        this.y = y;
        this.life = life;
    }

    public void update(long delta, Game game) {
        if (exploding) {
            if (System.currentTimeMillis() > explosionEnd) {
                game.running = false; // termina o jogo quando a explosão acabar
            }
            return;
        }

        if (!active) return;

        // Movimentação
        if (GameLib.iskeyPressed(GameLib.KEY_UP)) y -= velocity * delta;
        if (GameLib.iskeyPressed(GameLib.KEY_DOWN)) y += velocity * delta;
        if (GameLib.iskeyPressed(GameLib.KEY_LEFT)) x -= velocity * delta;
        if (GameLib.iskeyPressed(GameLib.KEY_RIGHT)) x += velocity * delta;

        // Disparo
        if (GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
            long now = System.currentTimeMillis();
            if (now > nextShot) {
                Projectile p = new Projectile(x, (y - 2 * radius), 0, -1.0);
                game.addProjectile(p);
                if (mutiplePorjectiles) {
                    Projectile p2 = new Projectile(x, (y - 2 * radius), 0.25, -1.0);
                    Projectile p3 = new Projectile(x, (y - 2 * radius), -0.25, -1.0);
                    game.addProjectile(p2);
                    game.addProjectile(p3);
                }
                nextShot = now + 100;
            }
        }

        // Limites da tela
        if (x < 0) x = 0;
        if (x >= GameLib.WIDTH) x = GameLib.WIDTH - 1;
        if (y < 25) y = 25;
        if (y >= GameLib.HEIGHT) y = GameLib.HEIGHT - 1;
    }

    public void draw() { //desenha o jogador na tela
        if (exploding) {
            double alpha = (System.currentTimeMillis() - explosionStart) / (double) (explosionEnd - explosionStart);
            GameLib.drawExplosion(x, y, alpha);
        } else {
            GameLib.setColor(Color.BLUE);
            GameLib.drawPlayer(x, y, radius);
            GameLib.setColor(Color.RED);
            GameLib.fillRect(x, y + 25, life, 1);
        }
    }

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

    public boolean isActive() {
        return active && !exploding;
    }


    public boolean collidesWith(EnemyProjectile ep) {
        double dx = ep.x - this.x;
        double dy = ep.y - this.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < (radius + ep.radius) * 0.8;
    }

    public boolean getPowerUp(PowerUp pu) {
        double dx = pu.x - this.x;
        double dy = pu.y - this.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < (radius + pu.radius) * 0.8;
    }

    public double getY() {
        return y;
    }
}
