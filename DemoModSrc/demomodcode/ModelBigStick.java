package demomodcode;


import org.lwjgl.opengl.GL11;

import dangerzone.ModelBase;
import dangerzone.ModelRenderer;


public class ModelBigStick extends ModelBase
{

  //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;

  
    public ModelBigStick()
    {


    	Shape1 = new ModelRenderer(this, 12, 0);
    	Shape1.addBox(0F, 0F, 0F, 1, 28, 1);
    	Shape1.setRotationPoint(0F, -15F, 0F);
    	Shape1.setTextureSize(64, 64);
    	Shape1.mirror = true;
    	setRotation(Shape1, 0F, 0F, 0F);
    	Shape2 = new ModelRenderer(this, 0, 0);
    	Shape2.addBox(0F, 0F, 0F, 2, 11, 3);
    	Shape2.setRotationPoint(-2F, -15F, -1F);
    	Shape2.setTextureSize(64, 64);
    	Shape2.mirror = true;
    	setRotation(Shape2, 0F, 0F, 0F);
    	Shape3 = new ModelRenderer(this, 19, 0);
    	Shape3.addBox(0F, 0F, 0F, 1, 7, 1);
    	Shape3.setRotationPoint(-3F, -13F, 0F);
    	Shape3.setTextureSize(64, 64);
    	Shape3.mirror = true;
    	setRotation(Shape3, 0F, 0F, 0F);
    }
  
  public void render()
  {
	  GL11.glScalef(1.5f, 1.5f, 1.5f);
	  Shape1.render(1);
	  Shape2.render(1);
	  Shape3.render(1);
  }
  
  


}
