package org.newdawn.spaceinvaders.item;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.user.Inventory;

public class SpeedUpItem extends Item{
    public SpeedUpItem(Inventory inventory) {
        super(inventory);
        this.inventory = inventory;
        name = "SpeedUpItem";
        coins = 0;
    }

    public void useItem(Game game){
        if (inventory.getItemCount(this.getName()) > 0) {
            game.setSpeed(game.getSpeed()*1.2);
            inventory.removeItem(this.getName());
        }
    }
}
