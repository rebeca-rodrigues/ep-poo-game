package src.classes;

import src.classes.enemys.*;
import src.classes.power_ups.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import src.lib.GameLib;

public class Game { 
    //aqui eu crei as variáveis necessárias para o jogo
    //e criei o construtor do jogo, que inicializa os objetos principais

    public Player player; // o jogador
    List<Projectile> projectiles; //o que o jogador dispara
    List<Enemy> enemies;  // Lista de inimigos
    List<EnemyProjectile> enemyProjectiles; //o que os inimigos disparam
    List<PowerUp> powerUps; // Lista de power ups

    long currentTime;   // tempo atual do jogo
    // variáveis para controle de spawn de inimigos
    long nextEnemy1Spawn;
    long nextEnemy2Spawn;

    double enemy2SpawnX = GameLib.WIDTH * 0.2; // e aqui eu controlei a posição inicial do inimigo do tipo 2
    int enemy2Count = 0; //aqui eu controlei a quantidade de inimigos do tipo 2 que já foram criados

    long nextVelocityPowerUpSpawn;
    long velocityPowerUpduration;
    long nextMultipleProjectilesPowerUpSpawn;
    long multipleProjectilesPowerUpduration;


    public Game() {   // construtor do jogo
        GameLib.initGraphics();// isso é para iniciar o modo gráfico do jogo
        player = new Player(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90);// aqui eu criei o jogador na posição inicial
        projectiles = new ArrayList<>(); // lista de projéteis do jogador
        enemies = new ArrayList<>(); // criei a lista de inimigos
        enemyProjectiles = new ArrayList<>(); //  lista de projéteis inimigos
        powerUps = new ArrayList<>(); // criação da lista de power ups

        currentTime = System.currentTimeMillis(); //
        nextEnemy1Spawn = currentTime + 2000;/// aqui eu controlei o tempo de spawn do inimigo do tipo 1
        nextEnemy2Spawn = currentTime + 7000; // "" so que do inimigo do tipo 2

        nextVelocityPowerUpSpawn = currentTime + 10000;
        nextVelocityPowerUpSpawn = currentTime + 20000;
    }

    public void run() {  // esse é o loop principal do jogo
        // Esse loop controla a execução do jogo, atualizando o estado e desenhando os objetos
        boolean running = true; // variável para controlar o loop do jogo

        while (running) {
            long newTime = System.currentTimeMillis();
            long delta = newTime - currentTime;
            currentTime = newTime;

            // Atualizações
            player.update(delta, this);
            for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
                Projectile p = it.next();
                p.update(delta);
                if (!p.isActive()) it.remove();
            }
            spawnEnemies();
            spawnPowerUps();
            removePowerUps();

            for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();) {
                Enemy e = it.next();
                e.update(delta, this);
                if (!e.isActive()) it.remove();
            }

            for (Iterator<EnemyProjectile> it = enemyProjectiles.iterator(); it.hasNext();) {
                EnemyProjectile ep = it.next();
                ep.update(delta);
                if (!ep.isActive()) it.remove();
            }

            for (Iterator<PowerUp> it = powerUps.iterator(); it.hasNext();) {
                PowerUp pu = it.next();
                pu.update(delta, this);
                if (!pu.isActive()) it.remove();
            }

            // Colisões: player x projéteis inimigos
            for (EnemyProjectile ep : enemyProjectiles) {
                if (player.isActive() && player.collidesWith(ep)) {
                    player.explode(currentTime);
                }
            }

            // Colisões: projéteis player x inimigos
            for (Projectile p : projectiles) {
                for (Enemy e : enemies) {
                    if (p.isActive() && e.isActive() && p.collidesWith(e)) {
                        e.explode(currentTime);
                        p.deactivate();
                    }
                }
            }

            // Colisões: player x power up
            for (PowerUp pu: powerUps) {
                if (player.isActive() && player.getPowerUp(pu)) {
                    pu.efect(player);
                    pu.deactivate();
                    switch (pu.getType()) {
                        case "velocity":
                            velocityPowerUpduration = currentTime + 3000;
                            break;
                        case "multipleProjectiles":
                            multipleProjectilesPowerUpduration = currentTime + 6000;
                            break;
                    }
                }
            }

            // Desenhos
            player.draw();
            for (Projectile p : projectiles) p.draw();
            for (Enemy e : enemies) e.draw();
            for (EnemyProjectile ep : enemyProjectiles) ep.draw();
            for (PowerUp pu : powerUps) pu.draw();

            GameLib.display();

            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;

            busyWait(currentTime + 3);
        }

        System.exit(0);
    }

    private void spawnEnemies() {
        if (currentTime > nextEnemy1Spawn) {
            enemies.add(new EnemyType1(Math.random() * (GameLib.WIDTH - 20) + 10, -10));
            nextEnemy1Spawn = currentTime + 500;
        }

        if (currentTime > nextEnemy2Spawn) {
            enemies.add(new EnemyType2(enemy2SpawnX, -10));
            enemy2Count++;
            if (enemy2Count < 10) {
                nextEnemy2Spawn = currentTime + 120;
            } else {
                enemy2Count = 0;
                enemy2SpawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
                nextEnemy2Spawn = currentTime + 3000 + (long) (Math.random() * 3000);
            }
        }
    }

    private void spawnPowerUps() {
        if (currentTime > nextVelocityPowerUpSpawn) {
            powerUps.add(new VelocityPowerUp(Math.random() * (GameLib.WIDTH - 20) + 30, -30));
            nextVelocityPowerUpSpawn = currentTime + 10000;
        }

        if (currentTime > nextMultipleProjectilesPowerUpSpawn) {
            powerUps.add(new MultipleProjectilesPowerUp(Math.random() * (GameLib.WIDTH - 20) + 30, -30));
            nextMultipleProjectilesPowerUpSpawn = currentTime + 20000;
        }
    }

    private void removePowerUps() {
        if (currentTime > velocityPowerUpduration) {
            player.velocity = 0.25;
        }
        if (currentTime > multipleProjectilesPowerUpduration) {
            player.mutiplePorjectiles = false;
        }
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public void addEnemyProjectile(EnemyProjectile ep) {
        enemyProjectiles.add(ep);
    }

    private void busyWait(long time) {
        while (System.currentTimeMillis() < time) Thread.yield();
    }
}
