package me.nerdfuryz.grenader;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Grenader extends JavaPlugin
  implements Listener
{
  public static final Logger logger = Logger.getLogger("Minecraft");
  public static ArrayList<Player> eggp = new ArrayList<Player>();
  public static ArrayList<File> filep = new ArrayList<File>();
  public static Grenader instance = null;
  public static File config;
  public static FileConfiguration configFile;

  public void onEnable()
  {
    instance = this;
    filep.add(config);
    getServer().getPluginManager().registerEvents(this, this);
    Permissions.setup();
    config = new File(getDataFolder(), "config.yml");
    configFile = new YamlConfiguration();
    ConfigHandler.checkForConfig(config);
    ConfigHandler.loadConfig(configFile, config);
    ConfigHandler.saveConfig(configFile, config);
    logger.info("[Grenades] Is now enabled!");
    if (filep.contains(config)) {
      int i = configFile.getInt("Granade.Damage");
      configFile.set("Granade.Damage", Integer.valueOf(i));
    }
    if (filep.contains(config)) {
      int i = configFile.getInt("GL.Damage");
      configFile.set("GL.Damage", Integer.valueOf(i));
    }
  }

  public void onDisable() {
    saveConfig();
    filep.remove(config);
    logger.info("[Grenades] Is now disabled!");
  }

  public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
    Player p = (Player)s;
    if (l.equalsIgnoreCase("gl")) {
      if ((!p.isOp()) && (!Permissions.has(p, "grenades.use.gl"))) {
        p.sendMessage(Permissions.noperm);
      } else {
        int gldmg = getConfig().getInt("GL.Damage");
        Block tb = p.getTargetBlock(null, 50);
        Location loc = tb.getLocation();
        p.getWorld().createExplosion(loc, gldmg);
      }
    }
    else if (l.equalsIgnoreCase("ac130")) {
      if ((!p.isOp()) && (!Permissions.has(p, "grenades.use.ac130"))) {
        p.sendMessage(Permissions.noperm);
      } else {
        Block tb = p.getTargetBlock(null, 50);
        Location eloc = tb.getLocation();
        World w = p.getWorld();

        p.sendMessage(ChatColor.GRAY + "[AC-130] " + ChatColor.YELLOW + "Firing to the location in: ");
        p.sendMessage(ChatColor.YELLOW + "5");
        waitSec(1);
        p.sendMessage(ChatColor.YELLOW + "4");
        waitSec(1);
        p.sendMessage(ChatColor.YELLOW + "3");
        waitSec(1);
        p.sendMessage(ChatColor.YELLOW + "2");
        waitSec(1);
        p.sendMessage(ChatColor.YELLOW + "1");
        waitSec(1);
        p.sendMessage(ChatColor.RED + "FIRE!");
        waitSec(1);
        w.createExplosion(eloc, 15.0F);
        eloc.add(0.0D, 0.0D, 15.0D);
        waitSec(1);
        w.createExplosion(eloc, 5.0F);
        eloc.add(0.0D, 0.0D, -15.0D);
        waitSec(1);
        w.createExplosion(eloc, 5.0F);
        eloc.add(15.0D, 0.0D, 0.0D);
        waitSec(1);
        w.createExplosion(eloc, 5.0F);
        eloc.add(-15.0D, 0.0D, 0.0D);
        waitSec(1);
        w.createExplosion(eloc, 5.0F);
      }
    }
    return false;
  }
  @EventHandler
  public void onEgg(PlayerEggThrowEvent e) { Player p = e.getPlayer();
    Egg eg = e.getEgg();
    if ((!p.isOp()) || (!Permissions.has(p, "grenades.use"))) {
      return;
    }
    p.sendMessage(ChatColor.GREEN + "Grenade!");
    if (getConfig().getBoolean("Grenade.BlockDamage", true)) {
      int dmg = getConfig().getInt("Grenade.Damage");
      e.getEgg().remove();
      e.setHatching(true);
      eg.getWorld().createExplosion(eg.getLocation(), dmg);
    } }

  @EventHandler
  public void onDamage(EntityChangeBlockEvent e) {
    if ((e.getEntity() instanceof Player)) {
      Player p = (Player)e.getEntity();
      if (eggp.contains(p))
        e.setCancelled(true); 
    }
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    if (!p.isOp()) {
      return;
    }
    p.sendMessage(ChatColor.GRAY + "[AC-130]" + ChatColor.YELLOW + " Is ready for deployment!");
  }

  public static void waitSec(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static Grenader getInstance() {
    return instance;
  }
}