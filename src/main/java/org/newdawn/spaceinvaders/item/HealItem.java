package org.newdawn.spaceinvaders.item;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.entity.ShipEntity;
import org.newdawn.spaceinvaders.user.Inventory;

public class HealItem extends Item{
    public HealItem(Inventory inventory) {
        super(inventory);
        this.inventory = inventory;
        coins = 0;
        name = "HealItem";
    }

    @Override
    public void useItem(Game game) {
        if (inventory.getItemCount(this.getName()) > 0) {
            game.getEntity().setHP(game.getEntity().getHP()+1);
            inventory.removeItem(this.getName());
        }
    }
}
