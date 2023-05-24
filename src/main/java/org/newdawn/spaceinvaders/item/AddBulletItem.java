package org.newdawn.spaceinvaders.item;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.user.Inventory;

public class AddBulletItem extends Item {

    public AddBulletItem(Inventory inventory) {
        super(inventory);
        this.inventory = inventory;
        name = "AddBulletItem";
        coins = 0;
    }


    @Override
    public void useItem(Game game) {
        if (inventory.getItemCount(this.getName()) > 0) {
            game.increaseBulletCount();
            inventory.removeItem(this.getName());
        }
    }
}
