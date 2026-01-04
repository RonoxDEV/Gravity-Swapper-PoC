/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.thegravityswapper.init;

import net.thegravityswapper.item.GravitySwapperItem;
import net.thegravityswapper.TheGravitySwapperMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

public class TheGravitySwapperModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, TheGravitySwapperMod.MODID);
	public static final RegistryObject<Item> GRAVITY_SWAPPER;
	static {
		GRAVITY_SWAPPER = REGISTRY.register("gravity_swapper", GravitySwapperItem::new);
	}
	// Start of user code block custom items
	// End of user code block custom items
}