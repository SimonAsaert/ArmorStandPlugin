package tld.sima.armorstand.conversations;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import tld.sima.armorstand.Main;
import tld.sima.armorstand.inventories.mainMenuInventory;
import tld.sima.armorstand.inventories.optionsMenuInventory;

public class RotationConv extends StringPrompt {
	
	private Main plugin = Main.getPlugin(Main.class);
	private UUID uuid;
	private boolean invType;
	private enum rotationType {
		BODY,
		HX, HY, HZ,
		TX, TY, TZ,
		LAX, LAY, LAZ,
		RAX, RAY, RAZ,
		LLX, LLY, LLZ,
		RLX, RLY, RLZ
	};
	private rotationType typeUsed;
	
	public Prompt acceptInput(ConversationContext con, String message) {
		Player player = plugin.getServer().getPlayer(uuid);
		
		// If somehow input to function is incorrect, fallback to a main menu.
		if (typeUsed == null) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "Something somewhere went wrong");
			if(invType) {
				mainMenuInventory i = new mainMenuInventory();
				i.newInventory(player, plugin.getStandMap().get(player.getUniqueId()));
			}else {
				optionsMenuInventory i = new optionsMenuInventory();
				i.openInventory(player, plugin.getStandMap().get(player.getUniqueId()));
			}
			return null;
		}
		
		// Get angle between 360 and 0
		double degrees;
		try {
			degrees = Double.parseDouble(message);
		}catch(Exception e) {
			con.getForWhom().sendRawMessage(ChatColor.RED + "You need to put an number value here");
			if(invType) {
				mainMenuInventory i = new mainMenuInventory();
				i.newInventory(player, plugin.getStandMap().get(player.getUniqueId()));
			}else {
				optionsMenuInventory i = new optionsMenuInventory();
				i.openInventory(player, plugin.getStandMap().get(player.getUniqueId()));
			}
			return null;
			
		}
		if (degrees < 0){degrees = 360+degrees;}
		
		ArmorStand stand = plugin.getStandMap().get(player.getUniqueId());
		
		// Setting armorstand rotations depending on type.
		if (typeUsed.equals(rotationType.BODY)) {
			Location loc = stand.getLocation().clone();
			loc.setYaw((float) degrees);
			stand.teleport(loc);
			con.getForWhom().sendRawMessage(ChatColor.GOLD + "Body angle changed to: " + ChatColor.WHITE + degrees);
		}else {
			int degreesD = (int)degrees;
			degrees = degrees*Math.PI/180;
			if (typeUsed.equals(rotationType.HX)) {
				stand.setHeadPose(stand.getHeadPose().setX(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Head X angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.HY)) {
				stand.setHeadPose(stand.getHeadPose().setY(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Head Y angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.HZ)) {
				stand.setHeadPose(stand.getHeadPose().setZ(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Head Z angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.TX)) {
				stand.setBodyPose(stand.getBodyPose().setX(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Chest X angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.TY)) {
				stand.setBodyPose(stand.getBodyPose().setY(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Chest Y angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.TZ)) {
				stand.setBodyPose(stand.getBodyPose().setZ(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Chest Z angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.LAX)) {
				stand.setLeftArmPose(stand.getLeftArmPose().setX(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Arm X angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.LAY)) {
				stand.setLeftArmPose(stand.getLeftArmPose().setY(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Arm Y angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.LAZ)) {
				stand.setLeftArmPose(stand.getLeftArmPose().setZ(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Arm Z angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.RAX)) {
				stand.setRightArmPose(stand.getRightArmPose().setX(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Arm X angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.RAY)) {
				stand.setRightArmPose(stand.getRightArmPose().setY(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Arm Y angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.RAZ)) {
				stand.setRightArmPose(stand.getRightArmPose().setZ(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Arm Z angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.LLX)) {
				stand.setLeftLegPose(stand.getLeftLegPose().setX(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Leg X angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.LLY)) {
				stand.setLeftLegPose(stand.getLeftLegPose().setY(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Leg Y angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.LLZ)) {
				stand.setLeftLegPose(stand.getLeftLegPose().setZ(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Left Leg Z angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.RLX)) {
				stand.setRightLegPose(stand.getRightLegPose().setX(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Leg X angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.RLY)) {
				stand.setRightLegPose(stand.getRightLegPose().setY(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Leg Y angle changed to: " + ChatColor.WHITE + degreesD);
			}else if (typeUsed.equals(rotationType.RLZ)) {
				stand.setRightLegPose(stand.getRightLegPose().setZ(degrees));
				con.getForWhom().sendRawMessage(ChatColor.GOLD + "Right Leg Z angle changed to: " + ChatColor.WHITE + degreesD);
			}
		}
		

		if(invType) {
			mainMenuInventory i = new mainMenuInventory();
			i.newInventory(player, plugin.getStandMap().get(player.getUniqueId()));
		}else {
			optionsMenuInventory i = new optionsMenuInventory();
			i.openInventory(player, plugin.getStandMap().get(player.getUniqueId()));
		}
		return null;
	}

	public void setData(UUID uuid, boolean invType, String type) {
		this.uuid = uuid;
		this.invType = invType;
		typeUsed = rotationType.valueOf(type);
	}
	
	public String getPromptText(ConversationContext arg0) {
		String output = (ChatColor.GOLD + "Put in an angle in degrees, like: " + ChatColor.WHITE + "90");
		return output;
	}

}
