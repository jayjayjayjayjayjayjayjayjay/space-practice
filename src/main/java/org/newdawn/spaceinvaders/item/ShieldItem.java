package org.newdawn.spaceinvaders.item;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.entity.ShipEntity;
import org.newdawn.spaceinvaders.user.Inventory;

public class ShieldItem extends Item{
    private ShipEntity shipEntity;
    private double activeShield = 10000; // 10sec
    public ShieldItem(Inventory inventory) {
        super(inventory);
        this.inventory = inventory;
        name = "ShieldItem";
        coins = 0;
    }

    @Override
    public void useItem(Game game) {
        if (inventory.getItemCount(this.getName()) > 0) {
            shipEntity = game.getShip();
            shipEntity.activateShield();
            inventory.removeItem(this.getName());
        }
    }
}
