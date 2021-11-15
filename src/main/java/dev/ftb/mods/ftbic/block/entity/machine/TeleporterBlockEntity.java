package dev.ftb.mods.ftbic.block.entity.machine;

import dev.ftb.mods.ftbic.FTBICConfig;
import dev.ftb.mods.ftbic.block.FTBICElectricBlocks;
import dev.ftb.mods.ftbic.block.entity.ElectricBlockEntity;
import dev.ftb.mods.ftbic.util.EnergyTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class TeleporterBlockEntity extends ElectricBlockEntity {
	public BlockPos linkedPos;
	public ResourceKey<Level> linkedLevel;
	public byte[] preview;

	public TeleporterBlockEntity() {
		super(FTBICElectricBlocks.TELEPORTER.blockEntity.get(), 0, 0);
	}

	@Override
	public void initProperties() {
		super.initProperties();
		inputEnergyTier = EnergyTier.IV;
		energyCapacity = FTBICConfig.TELEPORTER_CAPACITY;
	}

	@Override
	public void writeData(CompoundTag tag) {
		super.writeData(tag);

		if (linkedPos != null && linkedLevel != null) {
			tag.putInt("LinkedX", linkedPos.getX());
			tag.putInt("LinkedY", linkedPos.getY());
			tag.putInt("LinkedZ", linkedPos.getZ());
			tag.putString("LinkedLevel", linkedLevel.location().toString());
		}

		if (preview != null) {
			tag.putByteArray("Preview", preview);
		}
	}

	@Override
	public void readData(CompoundTag tag) {
		super.readData(tag);

		linkedPos = null;
		linkedLevel = null;

		if (tag.contains("LinkedLevel")) {
			linkedPos = new BlockPos(tag.getInt("LinkedX"), tag.getInt("LinkedY"), tag.getInt("LinkedZ"));
			linkedLevel = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString("LinkedLevel")));
		}

		preview = tag.contains("Preview") ? tag.getByteArray("Preview") : null;
	}

	@Override
	public void writeNetData(CompoundTag tag) {
		super.writeNetData(tag);

		if (preview != null) {
			tag.putByteArray("Preview", preview);
		}
	}

	@Override
	public void readNetData(CompoundTag tag) {
		super.readNetData(tag);
		preview = tag.contains("Preview") ? tag.getByteArray("Preview") : null;
	}

	@Override
	public void stepOn(ServerPlayer player) {
		active = true;
	}

	@Override
	public InteractionResult rightClick(Player player, InteractionHand hand, BlockHitResult hit) {
		return InteractionResult.SUCCESS;
	}
}