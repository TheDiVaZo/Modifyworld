/*
 * Modifyworld - PermissionsEx ruleset plugin for Bukkit
 * Copyright (C) 2011 t3hk0d3 http://www.tehkode.ru
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package modifyworld.updated;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import co.aikar.commands.PaperCommandManager;
import modifyworld.updated.command.AdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author t3hk0d3
 */
public class Modifyworld extends JavaPlugin {

	private static Modifyworld instance;

	protected final static Class<? extends ModifyworldListener>[] LISTENERS = new Class[]{
		PlayerListener.class,
		EntityListener.class,
		BlockListener.class,
		VehicleListener.class
	};
	protected List<ModifyworldListener> listeners = new ArrayList<ModifyworldListener>();
	protected PlayerInformer informer;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		this.informer = new PlayerInformer(getConfig());

		this.registerListeners();
		this.getLogger().info("Modifyworld enabled!");
		registerCommands();
		reloadPlugin();
		if(instance == null) instance = this;
	}

	private void registerCommands() {
		PaperCommandManager manager = new PaperCommandManager(this);
		manager.registerCommand(new AdminCommand());

		manager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t)-> {
			getLogger().warning("Error occurred while executing command "+command.getName());
			return true;
		});
		Bukkit.getLogger().info("Command has been register");
	}

	public static Modifyworld getInstance() {
		return instance;
	}

	public void reloadPlugin() {
		reloadConfig();
        saveConfig();
		for (ModifyworldListener listener : listeners) {
			listener.setConfig(getConfig().getConfigurationSection("settings"));
		}
		informer.setConfig(getConfig().getConfigurationSection("messages"));
    }

	@Override
	public void onDisable() {
		this.listeners.clear();
		this.getLogger().info("Modifyworld successfully disabled!");
	}

	protected void initializeConfiguration(FileConfiguration config) {
		// Flags
		config.set("item-restrictions", false);
		config.set("inform-players", true);
		config.set("whitelist", false);
		config.set("use-material-names", true);
		config.set("drop-restricted-item", false);
		config.set("item-use-check", false);
		config.set("check-metadata", true);
	}

	protected void registerListeners() {
		for (Class listenerClass : LISTENERS) {
			try {
				Constructor constructor = listenerClass.getConstructor(Plugin.class, ConfigurationSection.class, PlayerInformer.class);
				ModifyworldListener listener = (ModifyworldListener) constructor.newInstance(this, this.getConfig(), this.informer);
				this.listeners.add(listener);
			} catch (Throwable e) {
				this.getLogger().warning("Failed to initialize \"" + listenerClass.getName() + "\" listener");
				e.printStackTrace();
			}
		}
	}
}
