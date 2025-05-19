package com.simibubi.create.content.kinetics.transmission;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.CreateLang;

import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ComplexClutchBlockEntity extends ClutchBlockEntity {
    protected ScrollOptionBehaviour<PowerDirection> powerDirection;

    public ComplexClutchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        powerDirection = new ScrollOptionBehaviour<>(PowerDirection.class,
            CreateLang.translateDirect("complex_clutch.power_direction"), this, new SwitchBox());
        behaviours.add(powerDirection);
    }

	@Override
    public float getRotationSpeedModifier(Direction face) {
        Direction facing = getBlockState().getValue(ComplexClutchBlock.FACING);
        PowerDirection direction = PowerDirection.values()[powerDirection.getValue()];

        if (face == facing && direction == PowerDirection.FORWARD)
            return 1;
        if (face == facing.getOpposite() && direction == PowerDirection.BACKWARD)
            return 1;

        return 0;
    }

    public static enum PowerDirection implements INamedIconOptions {
        FORWARD(AllIcons.I_REFRESH),
        BACKWARD(AllIcons.I_ROTATE_CCW);

        private String translationKey;
        private AllIcons icon;

        private PowerDirection(AllIcons icon) {
            this.icon = icon;
            translationKey = "create.complex_clutch.power_direction." + Lang.asId(name());
        }

        @Override
        public AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }
    }

	class SwitchBox extends ValueBoxTransform.Sided {

		@Override
		protected Vec3 getSouthLocation() {
			return VecHelper.voxelSpace(8, 8, 12.5);
		}

		@Override
		public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
			Direction facing = state.getValue(ComplexClutchBlock.FACING);
			return super.getLocalOffset(level, pos, state).add(Vec3.atLowerCornerOf(facing.getNormal())
				.scale(-1 / 16f));
		}

		@Override
		public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
			super.rotate(level, pos, state, ms);
			Direction facing = state.getValue(ComplexClutchBlock.FACING);
			if (facing.getAxis() == Axis.Y)
				return;
			if (getSide() != Direction.UP)
				return;
			TransformStack.of(ms)
				.rotateZDegrees(-AngleHelper.horizontalAngle(facing) + 180);
		}

		@Override
		protected boolean isSideActive(BlockState state, Direction direction) {
			Direction facing = state.getValue(ComplexClutchBlock.FACING);
			if (facing.getAxis() != Axis.Y && direction == Direction.DOWN)
				return false;
			return direction.getAxis() != facing.getAxis();
		}
	}

}
