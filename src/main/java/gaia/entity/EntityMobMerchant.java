package gaia.entity;

import gaia.entity.ai.EntityAIMerchantLookAtTrade;
import gaia.entity.ai.EntityAIMerchantTradingPlayer;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class EntityMobMerchant extends EntityAgeable implements INpc, IMerchant {
	private static final String OFFERS_TAG = "Offers";
	private static final int MAX_RECIPES_TO_ADD = 75;
	private MerchantRecipeList buyingList;
	private int wealth;

	private EntityPlayer customer;
	private final InventoryBasic merchantInventory;

	public EntityMobMerchant(World worldIn) {
		super(worldIn);
		this.setSize(0.6f, 1.9f);
		this.merchantInventory = new InventoryBasic("Items", false, 8);
	}


	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
//		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
//		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
//		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
//		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
		this.tasks.addTask(1, new EntityAIMerchantTradingPlayer(this));
		this.tasks.addTask(1, new EntityAIMerchantLookAtTrade(this));
		this.tasks.addTask(2, new EntityAIMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));

		this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
//		super.initEntityAI();
	}

	/**
	 * Used when isRiding is triggered.
	 * Makes the entity being ridden face the same direction of the rider.
	 *
	 * @see EntitySkeleton
	 */
	public void updateRidden() {
		super.updateRidden();

		if (this.getRidingEntity() instanceof EntityCreature) {
			EntityCreature entitycreature = (EntityCreature) this.getRidingEntity();
			this.renderYawOffset = entitycreature.renderYawOffset;
		}
	}

	/**
	 * Used for isRiding.
	 * Used to offset the entity.
	 *
	 * @see EntitySkeleton
	 */
	public double getYOffset() {
		return -0.6D;
	}

	@Override
	public ITextComponent getDisplayName() {
		if (hasCustomName()) {
			return super.getDisplayName();
		}

		TextComponentString nameString = new TextComponentString(getName());
		nameString.getStyle().setHoverEvent(getHoverEvent());
		nameString.getStyle().setInsertion(getCachedUniqueIdString());
		return nameString;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntityAttributes.MAX_HEALTH_1);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityAttributes.MOVE_SPEED_1);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityAttributes.FOLLOW_RANGE);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		final boolean flag = stack != ItemStack.EMPTY && stack.getItem() == Items.SPAWN_EGG;

		if (!flag && isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking()) {
			if (!world.isRemote && (buyingList == null || !buyingList.isEmpty())) {
				setCustomer(player);
				player.displayVillagerTradeGui(this);
			}

			player.addStat(StatList.TALKED_TO_VILLAGER);
			return true;
		} else {
			return super.processInteract(player, hand);
		}
	}

	public abstract void addRecipies(MerchantRecipeList list);

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setInteger("Riches", wealth);

		if (buyingList != null) {
			compound.setTag(OFFERS_TAG, buyingList.getRecipiesAsTags());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		wealth = compound.getInteger("Riches");

		if (compound.hasKey(OFFERS_TAG)) {
			NBTTagCompound offerCompound = compound.getCompoundTag(OFFERS_TAG);
			buyingList = new GaiaTradeList(offerCompound);
		}
	}

	@Override
	protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
//		super.dropLoot(wasRecentlyHit, lootingModifier, source);
		dropFewItems(wasRecentlyHit, lootingModifier);
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
		recipe.incrementToolUses();
		livingSoundTime = -getTalkInterval();
		int i = 3 + rand.nextInt(4);

		if (recipe.getToolUses() == 1 || rand.nextInt(5) == 0) {
			// TODO Will need to come back to this, mating code got changed
			// TODO isWillingToMate = true;

			i += 5;
		}

		if (recipe.getItemToBuy().getItem() == Items.EMERALD) {
			wealth += recipe.getItemToBuy().getMaxStackSize();
		}

		if (recipe.getRewardsExp()) {
			world.spawnEntity(new EntityXPOrb(world, posX, posY + 0.5D, posZ, i));
		}
	}

	/**
	 * Overridden due to Villager sounds
	 */
	@Override
	public void verifySellingItem(ItemStack stack) {
		if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
			this.livingSoundTime = -this.getTalkInterval();
		}
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer var1) {
		if (buyingList == null) {
			addDefaultEquipmentAndRecipies(MAX_RECIPES_TO_ADD);
		}

		return buyingList;
	}

	private void addDefaultEquipmentAndRecipies(int maxRecipesToAdd) {
		MerchantRecipeList rec = new MerchantRecipeList();
		addRecipies(rec);
		if (buyingList == null) {
			buyingList = new MerchantRecipeList();
		}

		for (int i = 0; i < maxRecipesToAdd && i < rec.size(); ++i) {
			buyingList.add(rec.get(i));
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		return posY < 0.0D && super.getCanSpawnHere();
	}

	@Override
	public void setCustomer(@Nullable EntityPlayer player) { this.customer = player; }

	@Nullable
	@Override
	public EntityPlayer getCustomer() { return this.customer; }

	public boolean isTrading() { return Objects.nonNull(this.customer); }

	@Override
	public void setRecipes(@Nullable MerchantRecipeList recipeList) { }

	@Override
	public World getWorld() { return this.world; }

	@Override
	public BlockPos getPos() { return new BlockPos(this); }

	@Nullable
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) { return null; }

	@Override
	public float getEyeHeight() {
		return this.height * 0.85f;
	}
}
