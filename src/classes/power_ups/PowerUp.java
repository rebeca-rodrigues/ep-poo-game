package src.classes.power_ups;

import src.classes.Game;
import src.classes.Player;

public abstract class PowerUp {
    public double x, y;
    public double radius;
    protected boolean active = true;
    public String type;

    public PowerUp(double x, double y, double radius, String type) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.type = type;
    }

    public abstract void update(long delta, Game game);

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public abstract void draw();
    public abstract void efect(Player player);
    
    public String getType() {
        return this.type;
    };

    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }

}
