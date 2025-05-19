package com.simibubi.create.content.kinetics.transmission;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class ComplexClutchBlock extends ClutchBlock implements IBE<ComplexClutchBlockEntity> {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public ComplexClutchBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<ComplexClutchBlockEntity> getBlockEntityClass() {
		return ComplexClutchBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends ComplexClutchBlockEntity> getBlockEntityType() {
		return AllBlockEntityTypes.COMPLEX_CLUTCH.get();
	}
}
