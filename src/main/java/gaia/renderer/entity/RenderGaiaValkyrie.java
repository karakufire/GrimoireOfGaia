package gaia.renderer.entity;

import gaia.GaiaReference;
import gaia.model.ModelGaiaValkyrie;
import gaia.renderer.entity.layers.LayerAuraValkyrie;
import gaia.renderer.entity.layers.LayerGaiaHeldItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGaiaValkyrie extends RenderLiving<EntityLiving> {
	private static final ResourceLocation texture = new ResourceLocation(GaiaReference.MOD_ID, "textures/entity/valkyrie.png");

	public RenderGaiaValkyrie(RenderManager renderManager, float shadowSize) {
		super(renderManager, new ModelGaiaValkyrie(0.0F), shadowSize);
		addLayer(LayerGaiaHeldItem.right(this, getModel().getRightArm()));
		addLayer(LayerGaiaHeldItem.left(this, getModel().getLeftArm()));
        addLayer(new LayerAuraValkyrie(this));
	}

	private ModelGaiaValkyrie getModel() {
		return (ModelGaiaValkyrie) getMainModel();
	}

	@Override
	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return texture;
	}
}
