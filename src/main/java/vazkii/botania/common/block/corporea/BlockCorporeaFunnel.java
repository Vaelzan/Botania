/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 16, 2015, 2:17:05 PM (GMT)]
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaFunnel;

import javax.annotation.Nonnull;

public class BlockCorporeaFunnel extends BlockCorporeaBase {

	public BlockCorporeaFunnel(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BotaniaStateProps.POWERED);

		if(power && !powered) {
			((TileCorporeaFunnel) world.getTileEntity(pos)).doRequest();
			world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, true), 4);
		} else if(!power && powered)
			world.setBlockState(pos, state.with(BotaniaStateProps.POWERED, false), 4);
	}

	@Nonnull
	@Override
	public TileCorporeaBase createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileCorporeaFunnel();
	}

}
