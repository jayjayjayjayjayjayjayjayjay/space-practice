package org.newdawn.spaceinvaders.item;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.user.Inventory;

public class ReLoadSpeedUpItem extends Item{
    public ReLoadSpeedUpItem(Inventory inventory){
        super(inventory);
        this.inventory = inventory;
        name = "ReLoadSpeedUpItem";
        coins = 0;
    }
    @Override
    public void useItem(Game game) {
        if (inventory.getItemCount(this.getName()) > 0) {
            game.setFireSpeed(game.getFireSpeed()*(0.75));
            inventory.removeItem(this.getName());
        }
    }
}
