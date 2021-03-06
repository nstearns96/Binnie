package binnie.core.machines.inventory;

import binnie.core.Constants;
import binnie.core.ModId;
import binnie.core.util.I18N;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class TankSlot extends BaseSlot<FluidStack> {
	private final FluidTank tank;

	@Deprecated
	public TankSlot(final int index, final String name, final int capacity) {
		this(index, new ResourceLocation(Constants.CORE_MOD_ID, "gui.tank." + name), capacity);
	}

	public TankSlot(final int index, final ResourceLocation name, final int capacity) {
		super(index, name);
		this.tank = new FluidTank(capacity);
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		final FluidStack liquid = FluidStack.loadFluidStackFromNBT(compound);
		this.setContent(liquid);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		if (this.getContent() != null) {
			this.getContent().writeToNBT(compound);
		}
		return compound;
	}

	@Override
	public FluidStack getContent() {
		return this.tank.getFluid();
	}

	private void setContent(@Nullable FluidStack stack) {
		this.tank.setFluid(stack);
	}

	public FluidTank getTank() {
		return this.tank;
	}

	@Override
	public String getName() {
		Validator<FluidStack> validator = getValidator();
		if (validator != null) {
			return I18N.localise(ModId.CORE, "gui.tank.validated", validator.getTooltip());
		}
		if (this.unlocLocation == null) {
			return "";
		}
		return I18N.localise(this.unlocLocation);
	}
}
