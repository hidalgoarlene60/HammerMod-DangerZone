package demomodcode;


import dangerzone.ModelBase;
import dangerzone.ModelRenderer;



public class ModelBeanie extends ModelBase
{

    ModelRenderer beaniebase;
    ModelRenderer beanieaxel;
    ModelRenderer beanieprop;

  
    public ModelBeanie()
    {


    	beaniebase = new ModelRenderer(this, 0, 9);
    	beaniebase.addBox(-4F, -11F, -4F, 8, 3, 8);
    	beaniebase.setRotationPoint(0F, 0F, 0F);
    	beaniebase.setTextureSize(64, 32);
    	beaniebase.mirror = true;
    	setRotation(beaniebase, 0F, 0F, 0F);
    	beanieaxel = new ModelRenderer(this, 0, 0);
    	beanieaxel.addBox(-0.5F, -13F, -0.5F, 1, 2, 1);
    	beanieaxel.setRotationPoint(0F, 0F, 0F);
    	beanieaxel.setTextureSize(64, 32);
    	beanieaxel.mirror = true;
    	setRotation(beanieaxel, 0F, 0F, 0F);
    	beanieprop = new ModelRenderer(this, 0, 6);
    	beanieprop.addBox(-3F, -13F, -0.5F, 6, 0, 1);
    	beanieprop.setRotationPoint(0F, 0F, 0F);
    	beanieprop.setTextureSize(64, 32);
    	beanieprop.mirror = true;
    	setRotation(beanieprop, 0F, 0F, 0F);
    }
  
 
  
}