package me.nerdfuryz.grenader;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Permissions
{
  public static String noperm = ChatColor.RED + "You haven't got permission to use that!";
  private static Permission permission;

  public static void setup()
  {
    try
    {
      Class.forName("net.milkbowl.vault.permission.Permission");
      RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
      permission = (Permission)permissionProvider.getProvider(); } catch (ClassNotFoundException localClassNotFoundException) {
    }
    if (permission == null) {
      Grenader.logger.info("[Grenader] Failed to hook into Vault. Defaulting to OP-Permissions.");
      return;
    }
    Grenader.logger.info("Hooked into Vault.");
  }
  public static boolean has(CommandSender sender, String node) {
    if (sender.isOp())
      return true;
    if (permission != null)
      return permission.has(sender, node);
    return false;
  }
}