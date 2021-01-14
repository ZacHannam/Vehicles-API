package nea.zachannam.vehicles.api.vehicles.models;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import nea.zachannam.vehicles.api.vehicles.Vehicle;
import nea.zachannam.vehicles.api.vehicles.components.frame.FrameComponent;
import nea.zachannam.vehicles.api.vehicles.components.inventory.InventoryComponent;
import nea.zachannam.vehicles.api.vehicles.components.seat.driverseat.DriverSeatComponent;
import nea.zachannam.vehicles.api.vehicles.components.steeringwheel.SteeringWheelComponent;
import nea.zachannam.vehicles.api.vehicles.components.wheelbase.WheelBaseComponent;
import net.minecraft.server.v1_16_R1.NBTTagCompound;

class Frame_Kart_1 extends FrameComponent {

	public Frame_Kart_1(Vehicle paramVehicle, ItemStack paramHelmetItem) {
		super(paramVehicle);
		
		this.applyHelmetItem(paramHelmetItem);
	}
	
	public Frame_Kart_1(Vehicle paramVehicle) {
		super(paramVehicle);
	}

	@Override
	public ItemStack HELMET_ITEM() {
		
		ItemStack item = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setUnbreakable(true);

        item.setItemMeta(itemMeta);

      	net.minecraft.server.v1_16_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        NBTTagCompound nmsData = nmsItem.getTag();

        nmsData.setInt("CustomModelData", 8); //8

        nmsItem.setTag(nmsData);

        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        item.setItemMeta(meta);
        
        return item;
	}
	
	@Override
	public Vector OFFSET() {
		return new Vector(0, -1.25, 0);
	}

	@Override
	public double HEIGHT() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public double WIDTH() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public double LENGTH() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public double YAW_OFFSET() {
		// TODO Auto-generated method stub
		return Math.PI;
	}
	
}

class DriverSeat_Kart_1 extends DriverSeatComponent {

	public DriverSeat_Kart_1(Vehicle paramVehicle) {
		super(paramVehicle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean SHOW_STEERING_DISPLAY() {
		return true;
	}

	@Override
	public int NUMBER_OF_CHARACTERS_ON_DISPLAY() {
		return 51;
	}

	@Override
	public Vector OFFSET() {
		return new Vector(0, 0, 0);
	}
}

class SteeringWheel_Kart_1 extends SteeringWheelComponent {

	public SteeringWheel_Kart_1(Vehicle paramVehicle, ItemStack paramHelmetItem) {
		super(paramVehicle);
		
		this.applyHelmetItem(paramHelmetItem);
	}
	
	public SteeringWheel_Kart_1(Vehicle paramVehicle) {
		super(paramVehicle);
	}

	@Override
	public ItemStack HELMET_ITEM() {
		ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setUnbreakable(true);

        item.setItemMeta(itemMeta);

      	net.minecraft.server.v1_16_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        NBTTagCompound nmsData = nmsItem.getTag();

        nmsData.setInt("CustomModelData", 129);

        nmsItem.setTag(nmsData);

        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        item.setItemMeta(meta);
		return item;
        
		//return new ItemStack(Material.BLACK_CARPET);
	}
	
	@Override
	public Vector OFFSET() {
		return new Vector (0, 0.55, -0.5);
	}
	
	@Override
	public double YAW_OFFSET() {
		return Math.PI / 2;
	}

	@Override
	public int STEERINGWHEEL_LOCKS() {
		return 1;
	}

	@Override
	public double STEERINGWHEEL_ANGLE() {
		// TODO Auto-generated method stub
		return Math.toRadians(42);
	}

}

class WheelBase_Kart_1 extends WheelBaseComponent {

	public WheelBase_Kart_1(Vehicle paramVehicle) {
		super(paramVehicle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Vector[] WHEEL_OFFSETS() {
		return new Vector[] {
				new Vector(1, 0, 0.5),
				new Vector(1, 0, -0.5),
				new Vector(-1, 0, -0.5),
				new Vector(-1, 0, 0.5)
			};
	}

	@Override
	public double MAX_SPEED() {
		return 0.6;
	}

	@Override
	public float NORMALISE_STEERING_AMOUNT() {
		return (float) (Math.PI / 360);
	}

	@Override
	public float MAX_STEERING_ROTATION() {
		// TODO Auto-generated method stub
		return (float) (Math.PI / 6);
	}

	@Override
	public float STEERING_SPEED() {
		// TODO Auto-generated method stub
		return (float) (Math.PI / 90);
	}

	@Override
	public double REVERSE_SPEED_MULTIPLIER() {
		return 0.2;
	}

	@Override
	public double POWER_ACCELERATION() {
		// TODO Auto-generated method stub
		return 0.004;
	}

	@Override
	public double IDLE_ACCELERATION() {
		// TODO Auto-generated method stub
		return 0.001;
	}

	@Override
	public double BRAKE_ACCELERATION() {
		// TODO Auto-generated method stub
		return 0.012;
	}
}

class Inventory_Kart_1 extends InventoryComponent {

	public Inventory_Kart_1(Vehicle paramVehicle) {
		super(paramVehicle);
		// TODO Auto-generated constructor stub
	}
}


public class Kart_1 extends Vehicle {

	public Kart_1(UUID paramUUID) {
		super(paramUUID);
		
		super.addComponent(new WheelBase_Kart_1(this));
		super.addComponent(new DriverSeat_Kart_1(this));
		super.addComponent(new Frame_Kart_1(this));
		super.addComponent(new SteeringWheel_Kart_1(this));
		super.addComponent(new Inventory_Kart_1(this));
	}
	
}
