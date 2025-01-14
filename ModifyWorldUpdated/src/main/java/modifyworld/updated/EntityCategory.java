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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.*;


public enum EntityCategory {
	PLAYER("player", Player.class),
	ITEM("item", Item.class),
	ANIMAL("animal", Animals.class, Squid.class),
	MONSTER("monster", Monster.class, Slime.class, EnderDragon.class, Ghast.class ),
	NPC("npc", NPC.class),
	PROJECTILE("projectile", Projectile.class);
	
	private final String name;
	private final Class<? extends Entity>[] classes;
	
	private static final Map<Class<? extends Entity>, EntityCategory> map = new HashMap<>();
	
	static {
		for (EntityCategory cat : EntityCategory.values()) {
			for (Class<? extends Entity> catClass : cat.getClasses()) {
				map.put(catClass, cat);
			}
		}
	}
	
	@SafeVarargs
	private EntityCategory(String name, Class<? extends Entity>... classes) {
		this.name = name;
		this.classes = classes;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNameDot() {
		return this.getName() + ".";
	}
	
	public Class<? extends Entity>[] getClasses() {
		return this.classes;
	}
	
	public static EntityCategory fromEntity(Entity entity) {
		for (Map.Entry<Class<? extends Entity>, EntityCategory> entitySetClass : map.entrySet()) {
			if (entitySetClass.getKey().isAssignableFrom(entity.getClass())) {
				return entitySetClass.getValue();
			}
		}
		
		return null;
	}
	
}
