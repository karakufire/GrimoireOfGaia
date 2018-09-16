package gaia.entity.projectile;

import gaia.entity.EntityAttributes;
import gaia.entity.monster.EntityGaiaWerecat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGaiaProjectileBubble extends EntityFireball {

	@SuppressWarnings("unused") // used in reflection
	public EntityGaiaProjectileBubble(World worldIn) {
		super(worldIn);
		setSize(0.3125F, 0.3125F);
	}

	public EntityGaiaProjectileBubble(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		setSize(0.3125F, 0.3125F);
	}

	@Override
	protected EnumParticleTypes getParticleType() {
		return EnumParticleTypes.WATER_BUBBLE;
	}

	@Override
	protected float getMotionFactor() {
		return isInvulnerable() ? 0.73F : super.getMotionFactor();
	}

	@Override
	public boolean isBurning() {
		return false;
	}

	/**
	 * @see EntityFireball
	 */
	@Override
	protected void onImpact(RayTraceResult movingObject) {
		if (!world.isRemote) {
			if (movingObject.entityHit != null) {
				movingObject.entityHit.attackEntityFrom(DamageSource.MAGIC, (EntityAttributes.ATTACK_DAMAGE_2 / 2));

				if (movingObject.entityHit instanceof EntityLivingBase) {
					int i = 0;

					if (world.getDifficulty() == EnumDifficulty.NORMAL) {
						i = 10;
					} else if (world.getDifficulty() == EnumDifficulty.HARD) {
						i = 20;
					}

					if (i > 0) {
						((EntityLivingBase) movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, i * 20, 1));
					}
				}
			}

			this.world.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, false);
			this.setDead();
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	private static final DataParameter<Integer> Vuln = EntityDataManager.createKey(EntityGaiaWerecat.class, DataSerializers.VARINT);

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(Vuln, 0);
	}

	private boolean isInvulnerable() {
		return dataManager.get(Vuln) == 1;
	}
}
