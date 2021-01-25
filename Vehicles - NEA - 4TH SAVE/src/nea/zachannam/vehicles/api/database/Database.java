package nea.zachannam.vehicles.api.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

public class Database {
	
	private static HashMap<String, Database> databases = new HashMap<String, Database>();
	private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(this.getClass());
	private Connection connection;
	
	public static Database getDatabase(String paramName) {
		if(databases.containsKey(paramName)) {
			return databases.get(paramName);
		}
		return new Database(paramName);
	}
	
	public JavaPlugin getInstance() {
		if (plugin == null)
			try {
				throw new Exception();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return plugin;
	}
	
	public String getDataFolderPath() {
		File dir = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
		File d = new File(dir.getParentFile().getPath(), getInstance().getName());
		
		return d.getPath();
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	private Connection getConnection(String paramName) {
		
		try {
		
			String url = "jdbc:sqlite:" + getDataFolderPath() + File.separator + paramName + ".db";
			
			Class.forName("org.sqlite.JDBC");
			
			Connection conn = DriverManager.getConnection(url);
			
			return conn;
			
		} catch(Exception e) {
		
			e.printStackTrace();
			
		}
		return null;
        
	}
	
	public ResultSet executeQuery(String instruction) {
		Statement statement;
		try {
			statement = this.getConnection().createStatement();
			ResultSet result = statement.executeQuery(instruction);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public void executeUpdate(String instruction) {
		Statement statement;
		try {
			statement = this.getConnection().createStatement();
			statement.executeUpdate(instruction);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public DatabaseMetaData getMetaData() {
		DatabaseMetaData meta;
		try {
			meta = this.getConnection().getMetaData();
			return meta;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	public Database(String paramName) {
		databases.put(paramName, this);
		
		this.connection = getConnection(paramName);
	}
	
	public void halt() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
