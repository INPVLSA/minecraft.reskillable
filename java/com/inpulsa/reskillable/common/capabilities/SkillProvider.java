package com.inpulsa.reskillable.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkillProvider implements ICapabilitySerializable<CompoundTag> {
    private final SkillModel skillModel;
    private final LazyOptional<SkillModel> optional;

    public SkillProvider(SkillModel skillModel) {
        this.skillModel = skillModel;
        this.optional = LazyOptional.of(() -> this.skillModel);
    }

    public void invalidate() {
        this.optional.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {

        return capability == SkillCapability.INSTANCE ? this.optional.cast() : LazyOptional.empty();
    }

    public CompoundTag serializeNBT() {
        return this.skillModel.serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.skillModel.deserializeNBT(nbt);
    }
}
