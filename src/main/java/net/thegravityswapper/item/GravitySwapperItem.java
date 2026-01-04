package net.thegravityswapper.item;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.thegravityswapper.logic.GravityManager;
import net.thegravityswapper.TheGravitySwapperMod;

public class GravitySwapperItem extends Item {
	public GravitySwapperItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		if (!world.isClientSide()) {
			TheGravitySwapperMod.LOGGER.info("Gravity Swapper used by: " + entity.getName().getString());
			GravityManager.swapGravity(entity, 100); // 100 ticks = 5 seconds
			world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP,
					SoundSource.PLAYERS, 1, 1);
			if (world instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY() + 1, entity.getZ(),
						20, 0.5, 0.5, 0.5, 0.1);
			}
		}
		return ar;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}
}