package src.classes.power_ups;

import java.awt.Color;

import src.classes.Game;
import src.classes.Player;
import src.lib.GameLib;

public class VelocityPowerUp extends PowerUp {

    private final double speed;
    private double angle;
    private final double rotationSpeed;

    public VelocityPowerUp(double x, double y) {
            super(x, y, 10, "velocity");
            speed = 0.10 + Math.random() * 0.15;
            angle = (3 * Math.PI) / 2;
            rotationSpeed = 0.0;
    }
    
    @Override
    public void update(long delta, Game game) {
        y += speed * Math.sin(angle) * (-1.0) * delta;
        x += speed * Math.cos(angle) * delta;
        angle += rotationSpeed * delta;

        if (y > GameLib.HEIGHT + 10) {
            active = false;
        }
    }

    @Override
    public void draw() {
        GameLib.setColor(Color.GREEN);
        GameLib.drawDiamond(x, y, radius);
    }

    @Override
    public void efect(Player player) {
        player.velocity += 0.30;
    }
}
