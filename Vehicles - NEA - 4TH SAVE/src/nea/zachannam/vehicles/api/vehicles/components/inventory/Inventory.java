package nea.zachannam.vehicles.api.vehicles.components.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Inventory {

	public org.bukkit.inventory.Inventory getInventory();
	public void setInventory(org.bukkit.inventory.Inventory paramInventory);
	public void onInventoryClick(InventoryClickEvent event);
}
