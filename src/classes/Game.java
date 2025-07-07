package src.classes;

import src.lib.GameLib;
import src.classes.config.*;
import src.classes.config.EventoFactory.*;
import src.classes.enemys.*;
import src.classes.power_ups.*;
import src.classes.bosses.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Game { 
    //aqui eu crei as variáveis necessárias para o jogo
    //e criei o construtor do jogo, que inicializa os objetos principais
    private Config config;
    private Player player; // o jogador

    private List<Fase> fases;         // lista de fases completas, carregadas antecipadamente
    private int faseAtualIndex = 0; 
    private long tempoInicioFase;
    private Boss boss;

    List<Projectile> projectiles; //o que o jogador dispara
    List<Enemy> enemies;  // Lista de inimigos
    List<EnemyProjectile> enemyProjectiles; //o que os inimigos disparam
    List<BossProjectile> bossProjectiles; //o que os inimigos disparam
    List<PowerUp> powerUps; // Lista de power ups
  
    long currentTime;   // tempo atual do jogo

    long nextVelocityPowerUpSpawn;
    long velocityPowerUpduration;
    long nextMultipleProjectilesPowerUpSpawn;
    long multipleProjectilesPowerUpduration;

    public boolean running = true; // variável para controlar o loop das fases

    public Game() {   // construtor do jogo

        //Configuração do jogo - vida do jogador, numero de fases, arquivos de fases
        try {
            config = new Config("src/fases/config.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        fases = new ArrayList<>();
        for (String nomeFase : config.getArquivosFases()) {
            try {
                Fase fase = new Fase("src/fases/" + nomeFase);
                fases.add(fase);
            } catch (IOException e) {
                System.err.println("Erro ao carregar fase: " + nomeFase);
                e.printStackTrace();
            }
        }

        // Inicia gráficos
        GameLib.initGraphics();

        //Inicia player com vida igual a arquivo de configuração (100.000)
        player = new Player(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, config.getVidaJogador());// aqui eu criei o jogador na posição inicial
        projectiles = new ArrayList<>(); // lista de projéteis do jogador
        enemies = new ArrayList<>();
        enemyProjectiles = new ArrayList<>();
        bossProjectiles = new ArrayList<>();
        powerUps = new ArrayList<>(); // criação da lista de power ups

        currentTime = System.currentTimeMillis();

        nextVelocityPowerUpSpawn = currentTime + 10000;
        nextMultipleProjectilesPowerUpSpawn = currentTime + 20000;

    }

    public void run() {  // esse é o loop principal do jogo

        for (faseAtualIndex = 0; faseAtualIndex < fases.size(); faseAtualIndex++) {
            Fase faseAtual = fases.get(faseAtualIndex);
            System.out.println("Iniciando Fase " + (faseAtualIndex + 1));

            rodarFase(faseAtual);
            if (!player.isActive()) {
                List<Evento> eventos = faseAtual.getEventos();
                for (Evento evento : eventos) evento.setDisparado(false);
                faseAtualIndex--;
            }
            else System.out.println("Parabéns! Você finalizou a fase " + (faseAtualIndex + 1));
            this.running = true;
            player = new Player(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, config.getVidaJogador());// aqui eu criei o jogador na posição inicial
            projectiles = new ArrayList<>(); // lista de projéteis do jogador
            enemies = new ArrayList<>();
            enemyProjectiles = new ArrayList<>();
            bossProjectiles = new ArrayList<>();
            powerUps = new ArrayList<>();
            boss = null; 
            
            
            
        }

        System.out.println("Fim de jogo.");
        System.exit(0);
    }

    private void rodarFase(Fase fase) {
        List<Evento> eventos = fase.getEventos();
    
        tempoInicioFase = System.currentTimeMillis();
    
        while (running) {
            long newTime = System.currentTimeMillis();
            long delta = newTime - currentTime;
            currentTime = newTime;
    
            long tempoRelativo = currentTime - tempoInicioFase;
    
            // Dispara eventos
            for (Evento evento : eventos) {
                if (!evento.isDisparado() && tempoRelativo >= evento.getTempo()) {
                    executarEvento(evento);
                    evento.setDisparado(true);
                }
            }
            removePowerUps();

            // Atualizações
            player.update(delta, this);
            for (Iterator<Projectile> it = projectiles.iterator(); it.hasNext();) {
                Projectile p = it.next();
                p.update(delta);
                if (!p.isActive()) it.remove();
            }

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
            
            if (boss != null) boss.update(delta, this);

            for (Iterator<BossProjectile> it = bossProjectiles.iterator(); it.hasNext();) {
                BossProjectile ep = it.next();
                ep.update(delta);
                if (!ep.isActive()) it.remove();
            }

            // Colisões: player x projéteis inimigos
            for (EnemyProjectile ep : enemyProjectiles) {
                if (player.isActive() && player.collidesWith(ep)) {
                    player.damage(currentTime);
                    ep.deactivate();
                }
            }

            // Colisões: player x projéteis inimigos
            for (BossProjectile bp : bossProjectiles) {
                if (player.isActive() && player.collidesWith(bp)) {
                    player.damage(currentTime);
                    bp.deactivate();
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

            // Colisões: projéteis player x chefe
            for (Projectile p : projectiles) {
                if (p.isActive() && boss != null && boss.isActive() && p.collidesWith(boss)) {
                    boss.damage(currentTime);
                    p.deactivate();
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
            if (boss != null) {boss.draw();}
            for (BossProjectile bp : bossProjectiles) bp.draw();
            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                System.exit(0);
            }
    
            GameLib.display();
            busyWait(currentTime + 3);
        }
    }

    private void executarEvento(Evento evento) {
        switch (evento.getTipo()) {
            case "INIMIGO" -> spawnEnemy(evento);
            case "POWERUP" -> spawnPowerUps(evento);
            case "CHEFE" -> spawnBoss(evento);
        }
    }

    private void spawnEnemy (Evento e) {        
        if (e.getSubtipo() == 1) {
            enemies.add(new EnemyType1(e.getX(), e.getY()));
        }
        if (e.getSubtipo() == 2) {
            int enemy2Count = 0;
            double distance = 23;
            double angle = (3 * Math.PI) / 2;
            double x = e.getX();
            double y = e.getY();
            while (enemy2Count < 10) {
                enemies.add(new EnemyType2(x, y));
                x += distance * Math.cos(angle);
                y += distance * Math.sin(angle) * (-1.0);
                enemy2Count++;
            }
        }
    }

    private void spawnPowerUps(Evento e) {
        if (e.getSubtipo() == 1) {
            powerUps.add(new VelocityPowerUp(e.getX(), e.getY()));
        }

        if (e.getSubtipo() == 2) {
            powerUps.add(new MultipleProjectilesPowerUp(e.getX(), e.getY()));
        }
    }

    private void spawnBoss(Evento e) { 
        EventoChefe evento = (EventoChefe) e;         
        if (evento.getSubtipo() == 1) {
            boss = new BossType1(evento.getVida(), evento.getX(), evento.getY());
        }

        if (evento.getSubtipo() == 2) {
            boss = new BossType2(evento.getVida(), evento.getX(), evento.getY());
        }
    }


    public Player getPlayer() {
        return player;
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public void addEnemyProjectile(EnemyProjectile ep) {
        enemyProjectiles.add(ep);
    }

    public void addBossProjectile(BossProjectile bp) {
        bossProjectiles.add(bp);
    }

    public void addEnemyType2 (double x, double y) {
        int enemy2Count = 0;
        double distance = 23;
        double angle = (3 * Math.PI) / 2;
        while (enemy2Count < 10) {
            enemies.add(new EnemyType2(x, y));
            x += distance * Math.cos(angle);
            y += distance * Math.sin(angle) * (-1.0);
            enemy2Count++;
        }
    }

    private void busyWait(long time) {
        while (System.currentTimeMillis() < time) Thread.yield();
    }
    
    private void removePowerUps() {
        if (currentTime > velocityPowerUpduration) {
            player.velocity = 0.25;
        }
        if (currentTime > multipleProjectilesPowerUpduration) {
            player.mutiplePorjectiles = false;
        }
    }
}    




        /*
        


    private void removePowerUps() {
        if (currentTime > velocityPowerUpduration) {
            player.velocity = 0.25;
        }
        if (currentTime > multipleProjectilesPowerUpduration) {
            player.mutiplePorjectiles = false;
        }
    }


        // Esse loop controla a execução do jogo, atualizando o estado e desenhando os objetos
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
                    player.damage(currentTime);
                    ep.deactivate();
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
        */
