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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author t3hk0d3
 */
public class EntityListener extends ModifyworldListener {

	public EntityListener(Plugin plugin, ConfigurationSection config, PlayerInformer informer) {
		super(plugin, config, informer);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent entityDamageByEntityEvent) {
			Player player;
			if (entityDamageByEntityEvent.getDamager() instanceof Player) { // Prevent from damaging by player
				player = (Player) entityDamageByEntityEvent.getDamager();
				if (permissionDenied(player, "modifyworld.damage.deal", event.getEntity())) {
					cancelDamageEvent(player, event);
				}
			}
			if (entityDamageByEntityEvent.getEntity() instanceof Player) {
				player = (Player) entityDamageByEntityEvent.getEntity();
				if (entityDamageByEntityEvent.getDamager() != null && player.isOnline()) { // Prevent from taking damage by entity
					if (_permissionDenied(player, "modifyworld.damage.take", entityDamageByEntityEvent.getDamager())) {
						cancelDamageEvent(player, event);
					}
				}
			}

		} else if (event.getEntity() instanceof Player player) { // player are been damaged by enviroment

			if (_permissionDenied(player, "modifyworld.damage.take",  event.getCause().name().toLowerCase().replace("_", ""))) {
				cancelDamageEvent(player, event);
			}
		}
	}

	protected void cancelDamageEvent(Player player, EntityDamageEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTame(EntityTameEvent event) {
		if (!(event.getOwner() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getOwner();

		if (permissionDenied(player, "modifyworld.tame", event.getEntity())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
			if (_permissionDenied(player, "modifyworld.mobtarget",  event.getEntity())) {
				event.setCancelled(true);
			}
		}
	}

	
}
