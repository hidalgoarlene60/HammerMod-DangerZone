package dangerzone.entities;

import org.lwjgl.opengl.GL11;

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;

public class ModelDesertRainFrog extends ModelBase {


	ModelRenderer body;
	ModelRenderer lfleg;
	ModelRenderer rfleg;
	ModelRenderer lrleg;
	ModelRenderer rrleg;

	public ModelDesertRainFrog()
	{
		body = new ModelRenderer(this, 0, 0);
		body.addBox(-4F, 0F, -5F, 8, 5, 11);
		body.setRotationPoint(0F, 17F, 0F);
		body.setTextureSize(64, 64);
		body.mirror = true;
		setRotation(body, -0.1396263F, 0F, 0F);
		lfleg = new ModelRenderer(this, 0, 17);
		lfleg.addBox(0F, 0F, 0F, 1, 4, 1);
		lfleg.setRotationPoint(3F, 20F, -4F);
		lfleg.setTextureSize(64, 64);
		lfleg.mirror = true;
		setRotation(lfleg, 0F, 0F, -0.1745329F);
		rfleg = new ModelRenderer(this, 5, 17);
		rfleg.addBox(-1F, 0F, 0F, 1, 4, 1);
		rfleg.setRotationPoint(-3F, 20F, -4F);
		rfleg.setTextureSize(64, 64);
		rfleg.mirror = true;
		setRotation(rfleg, 0F, 0F, 0.1745329F);
		lrleg = new ModelRenderer(this, 0, 23);
		lrleg.addBox(0F, 0F, 0F, 1, 2, 1);
		lrleg.setRotationPoint(3F, 22F, 4F);
		lrleg.setTextureSize(64, 64);
		lrleg.mirror = true;
		setRotation(lrleg, 0F, 0F, -0.1745329F);
		rrleg = new ModelRenderer(this, 5, 23);
		rrleg.addBox(-1F, 0F, 0F, 1, 2, 1);
		rrleg.setRotationPoint(-3F, 22F, 4F);
		rrleg.setTextureSize(64, 64);
		rrleg.mirror = true;
		setRotation(rrleg, 0F, 0F, 0.1745329F);
	}

	// f = lifetime ticker. Doing Math.toRadians() on it provides a nice smooth wave cycle.
	// f1 = entity velocity
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float deathfactor)
	{
		float newangle = 0;
		//System.out.printf("floats: %f,  %f, %f, %f, %f, %f\n", f, f1, f2, f3, f4, f5);
		if(f1 > 0.01){
			newangle = (float) (Math.cos(Math.toRadians(f*24.6f)) * (float)Math.PI * 0.30F * f1);
		}else{
			newangle = 0.0F;
		}

		this.rfleg.rotateAngleX = newangle;
		this.lfleg.rotateAngleX = -newangle;
		this.rrleg.rotateAngleX = -newangle;
		this.lrleg.rotateAngleX = newangle;

		lfleg.render(deathfactor);
		rfleg.render(deathfactor);
		body.render(deathfactor);
		lrleg.render(deathfactor);
		rrleg.render(deathfactor);

	}

	public void doScale(Entity ent){
		GL11.glScalef(0.35f, 0.35f, 0.35f);
		if(ent.isBaby()){
			GL11.glScalef(0.25f, 0.25f, 0.25f);
		}
	}
}
