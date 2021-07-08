package tld.sima.armorstand.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import tld.sima.armorstand.Main;

import java.util.*;

public class VisualStand {

    private final Set<UUID> playersScanning;
    private final Set<UUID> currentlyLookedAtStands;
    private final Map<UUID, Pair<ArmorStand, Boolean>> playerStandMap;
    private final BukkitTask task;

    public VisualStand(){
        playerStandMap = new HashMap<>();
        currentlyLookedAtStands = new HashSet<>();
        playersScanning = Collections.synchronizedSet(new LinkedHashSet<>());

        task = new BukkitRunnable(){
            @Override
            public void run() {
                Iterator<UUID> iter = playersScanning.iterator();
                while(iter.hasNext()){
                    UUID pUUID = iter.next();
                    Pair<ArmorStand, Boolean> pair = playerStandMap.get(pUUID);
                    Player p = (Player) Bukkit.getEntity(pUUID);
                    if (p == null){
                        if (pair != null && pair.getLeft() != null){
                            currentlyLookedAtStands.remove(pair.getLeft().getUniqueId());
                            pair.getLeft().setGlowing(pair.getRight());
                        }
                        playerStandMap.remove(pUUID);
                        iter.remove();
                        continue;
                    }
                    RayTraceResult rayTraceResult = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getEyeLocation().getDirection(), 4, (entity) -> entity instanceof ArmorStand);

                    if (rayTraceResult != null){
                        final Entity e = rayTraceResult.getHitEntity();
                        if (e instanceof ArmorStand){
                            if(!currentlyLookedAtStands.contains(e.getUniqueId())) {
                                currentlyLookedAtStands.add(e.getUniqueId());
                                if (pair == null) {
                                    playerStandMap.put(pUUID, new Pair<ArmorStand, Boolean>((ArmorStand) e, e.isGlowing()));
                                    e.setGlowing(true);
                                } else if (!pair.getLeft().getUniqueId().equals(e.getUniqueId())) {
                                    if (pair.getLeft() != null) {
                                        pair.getLeft().setGlowing(pair.getRight());
                                        currentlyLookedAtStands.remove(pair.getLeft().getUniqueId());
                                    }
                                    playerStandMap.put(pUUID, new Pair<ArmorStand, Boolean>((ArmorStand) e, e.isGlowing()));
                                    e.setGlowing(true);
                                }
                            }
                            continue;
                        }
                    }
                    if (pair != null && pair.getLeft() != null){
                        currentlyLookedAtStands.remove(pair.getLeft().getUniqueId());
                        pair.getLeft().setGlowing(pair.getRight());
                    }
                    playerStandMap.remove(pUUID);
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 20, 5);
    }

    public boolean addRemovePlayer(UUID uuid){
        if(this.playersScanning.contains(uuid)){
            if (playerStandMap.get(uuid) != null && playerStandMap.get(uuid).getLeft() != null){
                playerStandMap.get(uuid).getLeft().setGlowing(playerStandMap.get(uuid).getRight());
            }
            playersScanning.remove(uuid);
            return false;
        }else{
            playersScanning.add(uuid);
            return true;
        }
    }

    public Collection<Pair<ArmorStand, Boolean>> getPairs(){
        return playerStandMap.values();
    }

    public boolean containsStand(UUID uuid){
        return currentlyLookedAtStands.contains(uuid);
    }

    public void delete(){


        this.task.cancel();
    }

}
