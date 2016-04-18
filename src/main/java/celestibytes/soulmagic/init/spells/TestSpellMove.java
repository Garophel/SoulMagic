package celestibytes.soulmagic.init.spells;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import celestibytes.soulmagic.api.spell.IInstantSpell;
import celestibytes.soulmagic.api.spell.ISpellHandler;
import celestibytes.soulmagic.api.spell.ext.IInstantSpellExt;

public class TestSpellMove implements IInstantSpell {
	
	private Vec3d pos = Vec3d.ZERO;

	@Override
	public Vec3d getPosition() {
		return pos;
	}

	@Override
	public void onCast(World world, EntityLivingBase caster, ISpellHandler magic, IInstantSpellExt ext) {}

	@Override
	public void onCastNothing(World world, EntityLivingBase caster, ISpellHandler magic, IInstantSpellExt ext) {}

	@Override
	public void onCastLiving(EntityLivingBase target, World world, EntityLivingBase caster, ISpellHandler magic, IInstantSpellExt ext) {}

	@Override
	public void onCastBlock(BlockPos pos, EnumFacing side, World world, EntityLivingBase caster, ISpellHandler magic, IInstantSpellExt ext) {
		this.pos = new Vec3d(pos);
		IBlockState block = world.getBlockState(pos);
		BlockPos move = pos.offset(side);
		
		if(world.isAirBlock(move) && world.getTileEntity(pos) == null) {
			world.setBlockState(move, block);
			world.setBlockToAir(pos);
			magic.consumeMana(100);
//			caster.addChatMessage(new TextComponentString("current mana: " + magic.getAvailableMana()));
		}
	}

}
