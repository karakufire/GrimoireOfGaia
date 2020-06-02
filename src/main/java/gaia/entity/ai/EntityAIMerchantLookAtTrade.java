package gaia.entity.ai;

import gaia.entity.EntityMobMerchant;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIMerchantLookAtTrade extends EntityAIWatchClosest {
	private final EntityMobMerchant merchant;

	public EntityAIMerchantLookAtTrade(EntityMobMerchant merchant) {
		super(merchant, EntityPlayer.class, 8.0f);
		this.merchant = merchant;
	}

	@Override
	public boolean shouldExecute() {
		if (merchant.isTrading()) {
			this.closestEntity = merchant.getCustomer();
			return true;
		} else return false;
	}
}
