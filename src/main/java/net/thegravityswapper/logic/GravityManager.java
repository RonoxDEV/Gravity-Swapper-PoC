package net.thegravityswapper.logic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.thegravityswapper.TheGravitySwapperMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.thegravityswapper.network.GravitySyncPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class GravityManager {
    private static final Map<UUID, Integer> invertedPlayers = new HashMap<>();

    public static void swapGravity(Player player, int ticks) {
        TheGravitySwapperMod.LOGGER.info(
                "GravityManager: Swapping gravity for " + player.getName().getString() + " for " + ticks + " ticks");
        invertedPlayers.put(player.getUUID(), ticks);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Player player = event.player;
            UUID uuid = player.getUUID();

            if (invertedPlayers.containsKey(uuid)) {
                int remaining = invertedPlayers.get(uuid);
                if (remaining > 0) {
                    Vec3 motion = player.getDeltaMovement();
                    player.setDeltaMovement(motion.x, 0.5, motion.z);
                    player.hurtMarked = true; // Forces motion synchronization to client
                    player.fallDistance = 0;

                    if (player instanceof ServerPlayer serverPlayer) {
                        TheGravitySwapperMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                                new GravitySyncPacket(motion.x, 0.5, motion.z));
                    }

                    invertedPlayers.put(uuid, remaining - 1);
                } else {
                    TheGravitySwapperMod.LOGGER
                            .info("GravityManager: Effect ended for " + player.getName().getString());
                    invertedPlayers.remove(uuid);
                }
            }
        }
    }
}
