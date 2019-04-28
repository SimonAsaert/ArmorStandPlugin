package tld.sima.armorstand.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;

import tld.sima.armorstand.Main;

public class ProtectedStands {
	
	public static void saveList(HashSet<UUID> uuids) {
		try {
			FileOutputStream fileOut = new FileOutputStream(Main.getPlugin(Main.class).getDataFolder().toString() + File.separator + "Storage" + File.separator + "ProtectedStands.obj");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(uuids);
			objectOut.close();
			fileOut.close();
			Bukkit.getConsoleSender().sendMessage("File Created!");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashSet<UUID> getList(){
		HashSet<UUID> prot = new HashSet<UUID>();
		try {
			FileInputStream fis = new FileInputStream(Main.getPlugin(Main.class).getDataFolder().toString() + File.separator + "Storage" + File.separator + "ProtectedStands.obj");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			prot = (HashSet<UUID>) ois.readObject();
			
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		}
		
		return prot;
	}
}
