package binnie.botany.blocks;

import binnie.botany.CreativeTabBotany;
import binnie.botany.api.genetics.EnumFlowerColor;
import binnie.botany.tile.TileCeramic;
import binnie.core.util.TileUtil;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import forestry.core.blocks.IColoredBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockCeramic extends Block implements IColoredBlock, IItemModelRegister {
	public BlockCeramic() {
		super(Material.ROCK);
		setHardness(1.0f);
		setResistance(5.0f);
		setRegistryName("ceramic");
		setCreativeTab(CreativeTabBotany.INSTANCE);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel(Item item, IModelManager manager) {
		for (EnumFlowerColor c : EnumFlowerColor.values()) {
			manager.registerItemModel(item, c.ordinal());
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, @Nullable EntityPlayer player, boolean willHarvest) {
		List<ItemStack> drops = new ArrayList<>();
		TileCeramic ceramic = TileUtil.getTile(world, pos, TileCeramic.class);
		if (ceramic != null) {
			drops = Collections.singletonList(ceramic.getStack());
		}
		boolean hasBeenBroken = world.setBlockToAir(pos);
		if (hasBeenBroken && !world.isRemote && drops.size() > 0 && (player == null || !player.capabilities.isCreativeMode)) {
			for (ItemStack drop : drops) {
				spawnAsEntity(world, pos, drop);
			}
		}
		return hasBeenBroken;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileCeramic();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileCeramic ceramic = TileUtil.getTile(worldIn, pos, TileCeramic.class);
		if (ceramic != null) {
			ceramic.setColor(EnumFlowerColor.VALUES[stack.getItemDamage()]);
		}
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> itemList) {
		for (EnumFlowerColor c : EnumFlowerColor.values()) {
			itemList.add(new ItemStack(this, 1, c.getFlowerColorAllele().getID()));
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		TileCeramic ceramic = TileUtil.getTile(world, pos, TileCeramic.class);
		if (ceramic != null) {
			return ceramic.getStack();
		}
		return super.getPickBlock(state, target, world, pos, player);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
		if (world != null && pos != null) {
			TileCeramic ceramic = TileUtil.getTile(world, pos, TileCeramic.class);
			if (ceramic != null) {
				return ceramic.getColor().getFlowerColorAllele().getColor(false);
			}
		}
		return EnumFlowerColor.get(0).getFlowerColorAllele().getColor(false);
	}
}
