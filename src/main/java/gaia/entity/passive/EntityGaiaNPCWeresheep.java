package gaia.entity.passive;

import gaia.entity.EntityMobMerchant;
import gaia.entity.GaiaTrade;
import gaia.init.GaiaItems;
import gaia.init.Sounds;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class EntityGaiaNPCWeresheep extends EntityMobMerchant {

	public EntityGaiaNPCWeresheep(World var1) {
		super(var1);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return Sounds.passive_say;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return Sounds.passive_hurt;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return Sounds.passive_death;
	}

	@Override
	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
		if (wasRecentlyHit && (rand.nextInt(1) == 0 || rand.nextInt(1 + lootingModifier) > 0)) {
			entityDropItem(new ItemStack(GaiaItems.SpawnWeresheep, 1, 0), 0.0F);
		}
	}

	@Override
	public void addRecipies(MerchantRecipeList recipes) {
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 0), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));

		// Buy List
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 0)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 1)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 2)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 4)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 5)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 6)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 7)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 8)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 9)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 10)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 11)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 12)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 13)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 14)));
		recipes.add(new GaiaTrade(new ItemStack(GaiaItems.MiscCurrency, 1, 3), new ItemStack(Blocks.WOOL, 1, 15)));

		// Sell List
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 0), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 1), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 2), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 3), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 4), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 5), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 6), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 7), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 8), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 9), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 10), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 11), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 12), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 13), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 14), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
		recipes.add(new GaiaTrade(new ItemStack(Blocks.WOOL, 1, 15), new ItemStack(GaiaItems.MiscCurrency, 1, 3)));
	}
}
