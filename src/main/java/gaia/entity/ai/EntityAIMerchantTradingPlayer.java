package gaia.entity.ai;

import gaia.entity.EntityMobMerchant;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

public class EntityAIMerchantTradingPlayer extends EntityAIBase {
	private final EntityMobMerchant merchant;

	public EntityAIMerchantTradingPlayer(EntityMobMerchant merchant) {
		this.merchant = merchant;
		this.setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		if (!merchant.isEntityAlive() && merchant.isInWater() && merchant.isInLava() && !merchant.onGround && merchant.velocityChanged) {
			return false;
		} else {
			EntityPlayer player = merchant.getCustomer();
			return Objects.nonNull(player) && merchant.getDistanceSq(player) <= 16.0d && Objects.nonNull(player.openContainer);
		}
	}

	@Override
	public void startExecuting() { merchant.getNavigator().clearPath(); }

	@Override
	public void resetTask() { merchant.setCustomer((EntityPlayer) null); }
}