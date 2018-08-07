package gaia.entity.monster;

import gaia.entity.EntityAttributes;
import gaia.entity.EntityMobHostileBase;
import gaia.init.GaiaBlocks;
import gaia.init.GaiaItems;
import gaia.init.Sounds;
import gaia.items.ItemShard;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S2160"})
public class EntityGaiaSphinx extends EntityMobHostileBase {

	private int spawnTime;

	@SuppressWarnings("WeakerAccess") //used in reflection
	public EntityGaiaSphinx(World worldIn) {
		super(worldIn);

		setSize(1.2F, 1.8F);
		experienceValue = EntityAttributes.EXPERIENCE_VALUE_3;
		stepHeight = 1.0F;
		isImmuneToFire = true;

		spawnTime = 0;
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAttackMelee(this, EntityAttributes.ATTACK_SPEED_3, true));
		tasks.addTask(2, new EntityAIWander(this, 1.0D));
		tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(3, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntityAttributes.MAX_HEALTH_3);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityAttributes.FOLLOW_RANGE);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityAttributes.MOVE_SPEED_3);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EntityAttributes.ATTACK_DAMAGE_3);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(EntityAttributes.RATE_ARMOR_3);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		return !(source instanceof EntityDamageSourceIndirect) && super.attackEntityFrom(source, Math.min(damage, EntityAttributes.BASE_DEFENSE_3));
	}

	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
		super.knockBack(xRatio, zRatio, EntityAttributes.KNOCKBACK_3);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (super.attackEntityAsMob(entityIn)) {
			if (entityIn instanceof EntityLivingBase) {
				byte byte0 = 0;

				if (world.getDifficulty() == EnumDifficulty.NORMAL) {
					byte0 = 20;
				} else if (world.getDifficulty() == EnumDifficulty.HARD) {
					byte0 = 30;
				}

				if (byte0 > 0) {
					((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, byte0 * 20, 0));
					((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, byte0 * 20, 0));
				}
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isAIDisabled() {
		return false;
	}

	@Override
	public void onLivingUpdate() {
		if (!onGround && motionY < 0.0D) {
			motionY *= 0.8D;
		}

		if (getHealth() < EntityAttributes.MAX_HEALTH_3 * 0.75F && getHealth() > EntityAttributes.MAX_HEALTH_3 * 0.25F) {
			if ((spawnTime > 0) && (spawnTime <= 200)) {
				++spawnTime;
			} else {
				world.setEntityState(this, (byte) 9);

				heal(EntityAttributes.MAX_HEALTH_3 * 0.10F);
				spawnTime = 1;
			}
		}

		if (!world.isRemote) {
			setBesideClimbableBlock(collidedHorizontally);
		}

		if (getHealth() <= 0.0F) {
			for (int i = 0; i < 2; ++i) {
				world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE,
						posX + (rand.nextDouble() - 0.5D) * width,
						posY + rand.nextDouble() * height,
						posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.onLivingUpdate();
		}
	}

	// ================= Climber data =================//
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(CLIMBING, (byte) 0);
	}

	protected PathNavigate getNewNavigator(World worldIn) {
		return new PathNavigateClimber(this, worldIn);
	}

	@Override
	public boolean isOnLadder() {
		return isBesideClimbableBlock();
	}

	private boolean isBesideClimbableBlock() {
		return (dataManager.get(CLIMBING) & 1) != 0;
	}

	private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntityGaiaSphinx.class, DataSerializers.BYTE);

	private void setBesideClimbableBlock(boolean climbing) {
		byte b0 = dataManager.get(CLIMBING);

		if (climbing) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}

		dataManager.set(CLIMBING, b0);
	}
	// ================================================//

	@Override
	protected SoundEvent getAmbientSound() {
		return Sounds.aggressive_say;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return Sounds.aggressive_hurt;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return Sounds.aggressive_death;
	}

	@Override
	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
		if (wasRecentlyHit) {
			if ((rand.nextInt(4) == 0 || rand.nextInt(1 + lootingModifier) > 0)) {
				dropItem(GaiaItems.FoodSmallAppleGold, 1);
			}

			// Nuggets/Fragments
			int var11 = rand.nextInt(3) + 1;

			for (int var12 = 0; var12 < var11; ++var12) {
				ItemShard.Drop_Nugget(this, 2);
			}

			int var13 = rand.nextInt(3) + 1;

			for (int var14 = 0; var14 < var13; ++var14) {
				ItemShard.Drop_Nugget(this, 3);
			}

			// Rare
			if ((rand.nextInt(EntityAttributes.RATE_RARE_DROP) == 0 || rand.nextInt(1 + lootingModifier) > 0)) {
				int i = rand.nextInt(3);
				if (i == 0) {
					dropItem(GaiaItems.BoxDiamond, 1);
				} else if (i == 1) {
					dropItem(Item.getItemFromBlock(GaiaBlocks.BUST_SPHINX), 1);
				} else if (i == 2) {
					entityDropItem(new ItemStack(GaiaItems.MiscRing, 1, 2), 0.0F);
				}
			}

			// Very Rare
			if ((rand.nextInt(EntityAttributes.RATE_RARE_DROP) == 0 || rand.nextInt(1) > 0)) {
				entityDropItem(new ItemStack(GaiaItems.Chest, 1, 2), 0.0F);
			}
		}
	}

	@Override
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
		//noop
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		IEntityLivingData ret = super.onInitialSpawn(difficulty, livingdata);

		ItemStack bootsSwimming = new ItemStack(Items.LEATHER_BOOTS);
		setItemStackToSlot(EntityEquipmentSlot.FEET, bootsSwimming);
		bootsSwimming.addEnchantment(Enchantments.DEPTH_STRIDER, 2);

		return ret;
	}

	// ================= Tier Immunities =================//
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		//noop
	}

	@Override
	public void setInWeb() {
		//noop
	}
	// ===================================================//

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	@Override
	public boolean getCanSpawnHere() {
		return posY > 60.0D && super.getCanSpawnHere();
	}
}
