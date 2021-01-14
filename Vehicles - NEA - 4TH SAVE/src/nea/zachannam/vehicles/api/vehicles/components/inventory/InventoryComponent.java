package nea.zachannam.vehicles.api.vehicles.components.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import lombok.Getter;
import lombok.Setter;
import nea.zachannam.vehicles.api.main.MessagesEnum;
import nea.zachannam.vehicles.api.main.VehiclesAPI;
import nea.zachannam.vehicles.api.user.User;
import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.components.Component;
import nea.zachannam.vehicles.api.vehicles.components.ComponentName;

public abstract class InventoryComponent extends Component implements nea.zachannam.vehicles.api.vehicles.components.inventory.Inventory  {

	@Setter
	@Getter
	private org.bukkit.inventory.Inventory inventory;
	
	@SuppressWarnings("unchecked")
	public JSONObject getMeta() {
		JSONObject inventoryMeta = new JSONObject();
		
		for(int slot = 0; slot < this.getInventory().getSize(); slot++) {
			if(this.getInventory().getItem(slot) != null) {
				inventoryMeta.put(slot, this.getInventory().getItem(slot).serialize());
			}
		}
		
		return inventoryMeta;
	}
	
	public void onVehicleClick(Player paramPlayer) {
		User user = VehiclesAPI.getUserManager().getUser(paramPlayer.getUniqueId());
		user.openVehicleInventory(super.getVehicle());
	}
	
	public void onInventoryClick(InventoryClickEvent event) {
		
		event.setCancelled(true);
		
		User user = VehiclesAPI.getUserManager().getUser(event.getWhoClicked().getUniqueId());
		
		if(event.getRawSlot() == 13) {
			
			if(this.getDriverSeat().hasRider()) {
				MessagesEnum.SEAT_OCCUPIED.sendMessage(user.getPlayer());
				return;
			}
			
			if(user.inSeat()) {
				MessagesEnum.USER_IN_SEAT.sendMessage(user.getPlayer());
				return;
			}
			
			ItemStack inSeat = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			ItemMeta inSeatMeta = inSeat.getItemMeta();
			inSeatMeta.setDisplayName(ChatColor.RED + "Seat Occupied!");
			inSeat.setItemMeta(inSeatMeta);
			
			this.getInventory().setItem(13, inSeat);
		}
		
		user.quickSetDrive(super.getVehicle());
		user.getPlayer().closeInventory();
	}
	
	public InventoryComponent(Vehicle paramVehicle) {
		super(paramVehicle);
		
		this.buildInventory();
	}
	
	private void buildInventory() {
		
		org.bukkit.inventory.Inventory inventory = Bukkit.createInventory(null, 3*9, ChatColor.BLUE + "Vehicle");
		
		ItemStack fillerItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta fillerItemMeta = fillerItem.getItemMeta();
		fillerItemMeta.setDisplayName(ChatColor.RED + " ");
		fillerItem.setItemMeta(fillerItemMeta);
		
		for(int i = 0; i < 3* 9; i++ ) {
			if(i == 13) {
				ItemStack seatUnoccupied = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
				ItemMeta seatUnoccupiedMeta = seatUnoccupied.getItemMeta();
				seatUnoccupiedMeta.setDisplayName(ChatColor.RED + "Click to sit in vehicle");
				seatUnoccupied.setItemMeta(seatUnoccupiedMeta);
				
				inventory.setItem(i, seatUnoccupied);
			} else {
				inventory.setItem(i, fillerItem);
			}
		}
		
		this.setInventory(inventory);
		
	}
	
	public void onDriverDismount(Player paramRider) {
		ItemStack seatUnoccupied = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta seatUnoccupiedMeta = seatUnoccupied.getItemMeta();
		seatUnoccupiedMeta.setDisplayName(ChatColor.RED + "Click to sit in vehicle");
		seatUnoccupied.setItemMeta(seatUnoccupiedMeta);

		this.getInventory().setItem(13, seatUnoccupied);
	}

	@Override
	public String getComponentName() {
		return ComponentName.INVENTORY.getName();
	}

	@Override
	public String[] getRequiredComponents() {
		return new String[] {};
	}
}
