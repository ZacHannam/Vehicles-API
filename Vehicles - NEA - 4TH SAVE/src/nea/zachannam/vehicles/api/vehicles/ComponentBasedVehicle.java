package nea.zachannam.vehicles.api.vehicles;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import nea.zachannam.vehicles.api.vehicles.components.Component;

public interface ComponentBasedVehicle {

	// Returns a list of components
	public ArrayList<Component> getComponents();
	
	/**
	 * Calls onButtonPressW on all of the components
	 */
	default public void buttonPressW() {
		
		for(Component component : getComponents()) {
			component.onButtonPressW();
		}
		
	}
	
	/**
	 * Calls onButtonPressA on all of the components
	 */
	default public void buttonPressA() {
		
		for(Component component : getComponents()) {
			component.onButtonPressA();
		}
		
	}
	
	/**
	 * Calls onButtonPressS on all of the components
	 */
	default public void buttonPressS() {
		
		for(Component component : getComponents()) {
			component.onButtonPressS();
		}
		
	}
	
	/**
	 * Calls onButtonPressD on all of the components
	 */
	default public void buttonPressD() {
		
		for(Component component : getComponents()) {
			component.onButtonPressD();
		}
		
	}
	
	/**
	 * Calls onButtonPressSpace on all of the components
	 */
	default public void buttonPressSpace() {
		
		for(Component component : getComponents()) {
			component.onButtonPressSpace();
		}
		
	}
	
	/**
	 * Calls onButtonPressCtrl on all of the components
	 */
	default public void buttonPressCtrl() {
		
		for(Component component : getComponents()) {
			component.onButtonPressCtrl();
		}
	}
	
	/**
	 * Calls noButtonPressForward on all of the components
	 */
	default public void noButtonPressForward() {
		
		for(Component component : getComponents()) {
			component.noButtonPressForward();
		}
		
	}
	
	/**
	 * Calls noButtonPressSideways on all of the components
	 */
	default public void noButtonPressSideways() {
		
		for(Component component : getComponents()) {
			component.noButtonPressSideways();
		}
	}
	
	/**
	 * Calls halt on all of the components
	 */
	default public void haltComponents() {
		for(Component component : getComponents()) {
			component.halt();
		}
	}
	
	/**
	 * Calls onVehicleClick on all of the components
	 * @param
	 */
	default public void onVehicleClick(Player paramPlayer) {
		for(Component component : getComponents()) {
			component.onVehicleClick(paramPlayer);
		}
	}
	
	/**
	 * Calls onDismount on all of the components
	 * @param
	 */
	default public void onDismount(OfflinePlayer paramRider) {
		for(Component component : getComponents()) {
			component.onDismount(paramRider);
		}
	}
	
	/**
	 * Calls onMount on all of the components
	 * @param paramPlayer
	 */
	public default void onMount(Player paramRider) {
		for(Component component : getComponents()) {
			component.onMount(paramRider);
		}
	}
}
