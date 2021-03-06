//auto-generated model template
//template generated by MEIM
//template v 1.0
//author Shadowmage45 (shadowage_catapults@hotmail.com)
 
package foo.bad.pkg.set.yours.here;
 
 
public class ModelArrow.java extends ModelBase
{
 
ModelRendererCustom shaft;
ModelRendererCustom head1;
ModelRendererCustom head2;
ModelRendererCustom vein2;
ModelRendererCustom vein1;
ModelRendererCustom vein3;
ModelRendererCustom vein4;
ModelRendererCustom head3;
ModelRendererCustom head4;
public ModelArrow.java(){
  shaft = new ModelRendererCustom(this,"shaft");
  shaft.setTextureOffset(0,0);
  shaft.setTextureSize(256,256);
  shaft.setRotationPoint(0.0f, -1.0f, 0.0f);
  setPieceRotation(shaft,0.0f, 0.0f, 0.0f);
  shaft.addBox(-0.5f,-0.5f,-8.0f,1,1,16);
  head1 = new ModelRendererCustom(this,"head1");
  head1.setTextureOffset(35,0);
  head1.setTextureSize(256,256);
  head1.setRotationPoint(0.0f, -1.0f, 6.0f);
  setPieceRotation(head1,0.0f, 0.0f, 0.0f);
  head1.addBox(-0.5f,-0.5f,-0.5f,1,3,1);
  shaft.addChild(head1);
  head2 = new ModelRendererCustom(this,"head2");
  head2.setTextureOffset(41,0);
  head2.setTextureSize(256,256);
  head2.setRotationPoint(-1.0f, 0.0f, 6.0f);
  setPieceRotation(head2,-1.0402973E-9f, 0.0f, 0.0f);
  head2.addBox(-0.5f,-0.5f,-0.5f,3,1,1);
  shaft.addChild(head2);
  vein2 = new ModelRendererCustom(this,"vein2");
  vein2.setTextureOffset(0,17);
  vein2.setTextureSize(256,256);
  vein2.setRotationPoint(0.0f, 1.0f, -8.0f);
  setPieceRotation(vein2,0.0f, 0.0f, 0.0f);
  vein2.addBox(-0.5f,-0.5f,-0.5f,1,1,3);
  shaft.addChild(vein2);
  vein1 = new ModelRendererCustom(this,"vein1");
  vein1.setTextureOffset(0,17);
  vein1.setTextureSize(256,256);
  vein1.setRotationPoint(0.0f, -1.0f, -8.0f);
  setPieceRotation(vein1,0.0f, 0.0f, 0.0f);
  vein1.addBox(-0.5f,-0.5f,-0.5f,1,1,3);
  shaft.addChild(vein1);
  vein3 = new ModelRendererCustom(this,"vein3");
  vein3.setTextureOffset(0,17);
  vein3.setTextureSize(256,256);
  vein3.setRotationPoint(1.0f, 0.0f, -8.0f);
  setPieceRotation(vein3,0.0f, 0.0f, 0.0f);
  vein3.addBox(-0.5f,-0.5f,-0.5f,1,1,3);
  shaft.addChild(vein3);
  vein4 = new ModelRendererCustom(this,"vein4");
  vein4.setTextureOffset(0,17);
  vein4.setTextureSize(256,256);
  vein4.setRotationPoint(-1.0f, 0.0f, -8.0f);
  setPieceRotation(vein4,0.0f, 0.0f, 0.0f);
  vein4.addBox(-0.5f,-0.5f,-0.5f,1,1,3);
  shaft.addChild(vein4);
  head3 = new ModelRendererCustom(this,"head3");
  head3.setTextureOffset(41,2);
  head3.setTextureSize(256,256);
  head3.setRotationPoint(-0.5f, 0.0f, 6.5f);
  setPieceRotation(head3,0.0f, 0.0f, 0.0f);
  head3.addBox(-0.5f,-0.5f,-0.5f,2,1,1);
  shaft.addChild(head3);
  head4 = new ModelRendererCustom(this,"head4");
  head4.setTextureOffset(35,4);
  head4.setTextureSize(256,256);
  head4.setRotationPoint(0.0f, -0.5f, 6.5f);
  setPieceRotation(head4,0.0f, 0.0f, 0.0f);
  head4.addBox(-0.5f,-0.5f,-0.5f,1,2,1);
  shaft.addChild(head4);
  }
 
@Override
public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float f6)
  {
  super.render(entity, f1, f2, f3, f4, f5, f6);
  setRotationAngles(f1, f2, f3, f4, f5, f6, entity);
  shaft.bindParentTexture(entity.getTexture());
  shaft.render(f6);
  }
 
public void setPieceRotation(ModelRenderer model, float x, float y, float z)
  {
  model.rotateAngleX = x;
  model.rotateAngleY = y;
  model.rotateAngleZ = z;
  }
}
