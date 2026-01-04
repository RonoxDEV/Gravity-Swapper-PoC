package net.thegravityswapper.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class GravitySyncPacket {
    private final double x, y, z;

    public GravitySyncPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(GravitySyncPacket msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
    }

    public static GravitySyncPacket decode(FriendlyByteBuf buffer) {
        return new GravitySyncPacket(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(GravitySyncPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                ClientPacketHandler.handle(msg);
            }
        });
        context.setPacketHandled(true);
    }

    private static class ClientPacketHandler {
        private static void handle(GravitySyncPacket msg) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.setDeltaMovement(new Vec3(msg.x, msg.y, msg.z));
            }
        }
    }
}
