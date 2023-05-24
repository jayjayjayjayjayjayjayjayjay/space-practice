package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.Game;

public class BossAlienEntity extends Entity{
    /** The speed at which the alien moves horizontally */
    private double moveSpeed = 200;
    /** The game in which the entity exists */
    private Game game;

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param game The game in which this entity is being created
     * @param x The initial x location of this alien
     * @param y The initial y location of this alien
     */
    public BossAlienEntity(Game game, int x, int y) {
        super("sprites/bossAlien/bossAlien2.png", x, y);

        this.game = game;
        dx = -moveSpeed;
    }

    public void move(long delta) {
        // if we have reached the left hand side of the screen and
        // are moving left then request a logic update
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        // and vice vesa, if we have reached the right hand side of
        // the screen and are moving right, request a logic update
        if ((dx > 0) && (x > 590)) {
            game.updateLogic();
        }

        // proceed with normal move
        super.move(delta);
    }

    public void doLogic() {
        // swap over horizontal movement and move down the
        // screen a bit
        dx = -dx;
        y += 10;

        // if we've reached the bottom of the screen then the player
        // dies
        if (y > 400) {
            game.notifyDeath();
        }
    }

    @Override
    public void collidedWith(Entity other) {
        // collisions with aliens are handled elsewhere
    }
}
