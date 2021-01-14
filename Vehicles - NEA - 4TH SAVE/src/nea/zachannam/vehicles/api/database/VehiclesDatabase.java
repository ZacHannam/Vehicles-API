package nea.zachannam.vehicles.api.database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import nea.zachannam.vehicles.api.vehicles.Vehicle;

public class VehiclesDatabase extends Database{

	private static final String TABLE_NAME = "vehicles";
	
	public VehiclesDatabase() {
		super("Vehicles");
		
		this.createDatabase();
	}
	
	private void createDatabase() {
		
		DatabaseMetaData meta = super.getMetaData();
		try {
			
			ResultSet tables = meta.getTables(null, null, TABLE_NAME, null);
			
			if(!tables.next()) {
				
				this.executeUpdate("CREATE TABLE " + TABLE_NAME + " (vehicleUUID char(32), type int, meta String)");
				
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void saveVehicle(Vehicle paramVehicle) {
		String meta = paramVehicle.getMeta().toString();
		Bukkit.broadcastMessage(meta);
	}
}
