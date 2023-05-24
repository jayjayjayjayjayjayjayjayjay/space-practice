package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.SpriteStore;
import org.newdawn.spaceinvaders.Game;

public class CoinEntity extends Entity {
    private Game game;

    public CoinEntity(Game game, String sprite, int x, int y) {
        super(sprite, x, y);
        this.game = game;
    }

    public void move(long delta) {
        // 코인이 아래로 떨어지도록 dy 값을 설정
        setVerticalMovement(100);
        super.move(delta);

        if (y > game.getHeight()) {
            game.removeEntity(this);
        }
    }

    public void collidedWith(Entity other) {
        if (other instanceof ShipEntity) {
            game.removeEntity(this);
            game.increaseCoinCount();
        }


    }
}
