package shadowmage.meim.client.modelrenderer;

import java.util.ArrayList;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import shadowmage.meim.common.config.MEIMConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * hackey as hell, pretty much recreating vanilla renderer to un-private some fucking fields
 * goddamn private shit..... also...recreate ModelBox class for pretty much the same thing...
 * lame as hell...but....W/E
 * @author Shadowmage
 *
 */
public class MEIMModelRenderer
{

/** The size of the texture file's width in pixels. */
public float textureWidth;

/** The size of the texture file's height in pixels. */
public float textureHeight;

/** The X offset into the texture used for displaying this model */
public int textureOffsetX;

/** The Y offset into the texture used for displaying this model */
public int textureOffsetY;

/**
 * point on parent piece (or world) that this piece will rotate around
 * when rendered.
 * All child pieces will use this point as their 0,0,0
 */
public float rotationPointX;
public float rotationPointY;
public float rotationPointZ;

/**
 * the angles to use while rotating this piece IN RADIANS
 */
public float rotateAngleX;
public float rotateAngleY;
public float rotateAngleZ;
public boolean compiled;

/** The GL display list rendered by the Tessellator for this model */
public int displayList;
public boolean mirror;
public boolean showModel;

/** Hides the model. */
public boolean isHidden;
public ArrayList<MEIMModelBox> cubeList;
public ArrayList<MEIMModelRenderer> childModels;
public String boxName;
public ModelBase baseModel;
public float baseOffsetX;
public float baseOffsetY;
public float baseOffsetZ;


public MEIMModelRenderer parent;
private boolean isCurrentSelection = false;
private int currentSelectedBoxNumber = 0;

public float customScale = 1.f;

public int pieceName = 0;

private static int pieceCount = 1;

public MEIMModelRenderer(ModelBase par1ModelBase, String par2Str, MEIMModelRenderer parent)
  {
  this.textureWidth = 64.0F;
  this.textureHeight = 32.0F;
  this.compiled = false;
  this.displayList = 0;
  this.mirror = false;
  this.showModel = true;
  this.isHidden = false;
  this.cubeList = new ArrayList();
  this.baseModel = par1ModelBase;
  par1ModelBase.boxList.add(this);
  this.boxName = par2Str;
  this.setTextureSize(par1ModelBase.textureWidth, par1ModelBase.textureHeight);
  this.parent = parent;  
  this.rotationPointBox = new MEIMModelBox(this, this.textureOffsetX, this.textureOffsetY, -.5f, -.5f, -.5f, 1, 1, 1, 0.f);

  /**
   * GL picking/selection box-naming......should give a unique number to every modelRenderer that is created
   */
  this.pieceName = pieceCount;
  pieceCount++;
  }

public MEIMModelBox replaceCurrentBox(MEIMModelBox box)
  {
  if(currentSelectedBoxNumber<0 || currentSelectedBoxNumber>= this.cubeList.size() || box==null)
    {
    return null;
    }
  this.compiled = false;
  this.cubeList.set(currentSelectedBoxNumber, box);
  return box;
  }

public void reinitCubeList()
  {
  ArrayList<MEIMModelBox> boxes = new ArrayList<MEIMModelBox>();
  for(MEIMModelBox bx : this.cubeList)
    {
    boxes.add(new MEIMModelBox(this, this.textureOffsetX, this.textureOffsetY, bx.posX1, bx.posY1, bx.posZ1, bx.width, bx.height, bx.length, 0.f));
    }
  this.cubeList = boxes;
  this.compiled = false;
  }

public void setCurrent()
  {
  this.isCurrentSelection = true;
  }

public void clearCurrent()
  {
  this.isCurrentSelection = false;
  }

public void toggleCurrent()
  {
  this.isCurrentSelection = !this.isCurrentSelection;
  }

public void swapFor(MEIMModelRenderer newPart)
  {
  this.isCurrentSelection = false;
  this.currentSelectedBoxNumber = 0;
  if(newPart!=null)
    {
    newPart.isCurrentSelection = true;
    newPart.currentSelectedBoxNumber = 0;
    }
  }

public MEIMModelBox getFirstBox()
  {
  this.currentSelectedBoxNumber = 0;
  if(this.cubeList.size()>=1)
    {    
    return this.cubeList.get(0);
    }  
  return null;
  }

public MEIMModelRenderer getPrevChild(MEIMModelRenderer currentChild)
  {
  if(this.childModels==null || this.childModels.size()<=1)
    {
    return currentChild;
    }
  for(int i = 0; i < this.childModels.size(); i++)
    {
    if(this.childModels.get(i)==currentChild)
      {
      if(i-1>=0)
        {
        return this.childModels.get(i-1);
        }
      else
        {
        return this.childModels.get(this.childModels.size()-1);
        }
      }
    }  
  return null;
  }

public MEIMModelRenderer getNextChild(MEIMModelRenderer currentChild)
  {
  if(this.childModels==null || this.childModels.size()<=1)
    {
    return currentChild;
    }
  for(int i = 0; i < this.childModels.size(); i++)
    {
    if(this.childModels.get(i)==currentChild)
      {
      if(i+1<this.childModels.size())
        {
        return this.childModels.get(i+1);
        }
      else
        {
        return this.childModels.get(0);
        }
      }
    }  
  return null;
  }

public MEIMModelBox getPrevBox(MEIMModelBox currentBox)
  {
  if(this.cubeList==null || this.cubeList.size()<=1)
    {
    this.currentSelectedBoxNumber =-1;
    return currentBox;
    }
  for(int i = 0; i < this.cubeList.size(); i++)
    {
    if(this.cubeList.get(i)==currentBox)
      {
      if(i-1>=0)
        {
        this.currentSelectedBoxNumber = i-1;
        return this.cubeList.get(i-1);
        }
      else
        {
        this.currentSelectedBoxNumber = this.cubeList.size()-1;
        return this.cubeList.get(this.cubeList.size()-1);
        }
      }
    }  
  return null;
  }

/**
 * also resets currentbox number if passed in a null box
 * @param currentBox
 * @return
 */
public MEIMModelBox getNextBox(MEIMModelBox currentBox)
  {
  if(this.cubeList==null || this.cubeList.size()<=1)
    {
    this.currentSelectedBoxNumber = -1;
    return currentBox;
    }
  if(currentBox == null)
    {
    this.currentSelectedBoxNumber = 0;
    return this.cubeList.get(0);
    }
  for(int i = 0; i < this.cubeList.size(); i++)
    {
    if(this.cubeList.get(i)==currentBox)
      {
      if(i+1<this.cubeList.size())
        {
        this.currentSelectedBoxNumber = i +1;
        return this.cubeList.get(i+1);
        }
      else
        {
        this.currentSelectedBoxNumber = 0;
        return this.cubeList.get(0);
        }
      }
    }  
  return null;
  }

/**
 * Sets the current box's rotation points and rotation angles to another box.
 */
public void addChild(MEIMModelRenderer par1ModelRenderer)
  {
  if (this.childModels == null)
    {
    this.childModels = new ArrayList();
    }

  this.childModels.add(par1ModelRenderer);
  }

public MEIMModelRenderer setTextureOffset(int par1, int par2)
  {
  this.textureOffsetX = par1;
  this.textureOffsetY = par2;
  return this;
  }

public MEIMModelRenderer addBox(String par1Str, float par2, float par3, float par4, int par5, int par6, int par7)
  {
  par1Str = this.boxName + "." + par1Str;
  TextureOffset var8 = this.baseModel.getTextureOffset(par1Str);
  this.setTextureOffset(var8.textureOffsetX, var8.textureOffsetY);
  this.cubeList.add((new MEIMModelBox(this, this.textureOffsetX, this.textureOffsetY, par2, par3, par4, par5, par6, par7, 0.0F)).func_78244_a(par1Str));
  this.currentSelectedBoxNumber = this.cubeList.size()-1;
  return this;
  }

public MEIMModelRenderer addBox(float par1, float par2, float par3, int par4, int par5, int par6)
  {
  this.cubeList.add(new MEIMModelBox(this, this.textureOffsetX, this.textureOffsetY, par1, par2, par3, par4, par5, par6, 0.0F));
  this.currentSelectedBoxNumber = this.cubeList.size()-1;
  return this;
  }

/**
 * Creates a textured box. Args: originX, originY, originZ, width, height, depth, scaleFactor.
 */
public void addBox(float par1, float par2, float par3, int par4, int par5, int par6, float par7)
  {
  this.cubeList.add(new MEIMModelBox(this, this.textureOffsetX, this.textureOffsetY, par1, par2, par3, par4, par5, par6, par7));
  this.currentSelectedBoxNumber = this.cubeList.size()-1;
  }

public void setRotationPoint(float par1, float par2, float par3)
  {
  this.rotationPointX = par1;
  this.rotationPointY = par2;
  this.rotationPointZ = par3;
  }


@SideOnly(Side.CLIENT)
public void render(float renderScale)
  {
  renderScale = this.customScale * 0.0625f;
  if (!this.isHidden)
    {
    if (this.showModel)
      {
      if (!this.compiled)
        {
        this.compileDisplayList(renderScale);
        }

      GL11.glTranslatef(this.baseOffsetX, this.baseOffsetY, this.baseOffsetZ);
      int index;

      if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
        {
        if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
          {
          if(this.isCurrentSelection)
            {
            GL11.glColor4f(1.f, 0.3f, 0.3f, 1.f);
            GL11.glCallList(this.displayList);
            if(this.rotationPointBox!=null)
              {
              GL11.glColor4f(1.f, 1.f, 0.3f, 1.f);
              GL11.glCallList(this.rotationBoxDL);
              }
            GL11.glColor4f(1, 1, 1, 1);
            if (this.childModels != null)
              {
              GL11.glColor4f(.3f, .3f, 1.f, 1.f);
              for (index = 0; index < this.childModels.size(); ++index)
                {                 
                this.childModels.get(index).render(renderScale);
                }
              GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
              }
            }
          else
            {
            GL11.glCallList(this.displayList);
            if (this.childModels != null)
              {
              for (index = 0; index < this.childModels.size(); ++index)
                {
                this.childModels.get(index).render(renderScale);
                }
              }
            }
          }
        else
          {
          GL11.glTranslatef(this.rotationPointX * renderScale, this.rotationPointY * renderScale, this.rotationPointZ * renderScale);
          if(this.isCurrentSelection)
            {
            GL11.glColor4f(1.f, 0.3f, 0.3f, 1.f);
            GL11.glCallList(this.displayList);
            if(this.rotationPointBox!=null)
              {
              GL11.glColor4f(1.f, 1.f, 0.3f, 1.f);
              GL11.glCallList(this.rotationBoxDL);
              }
            GL11.glColor4f(1, 1, 1, 1);
            if (this.childModels != null)
              {
              GL11.glColor4f(.3f, .3f, 1.f, 1.f);
              for (index = 0; index < this.childModels.size(); ++index)
                {                 
                this.childModels.get(index).render(renderScale);
                }
              GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
              }
            }
          else
            {
            GL11.glCallList(this.displayList);
            if (this.childModels != null)
              {
              for (index = 0; index < this.childModels.size(); ++index)
                {
                this.childModels.get(index).render(renderScale);
                }
              }
            }

          GL11.glTranslatef(-this.rotationPointX * renderScale, -this.rotationPointY * renderScale, -this.rotationPointZ * renderScale);
          }
        }
      else
        {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.rotationPointX * renderScale, this.rotationPointY * renderScale, this.rotationPointZ * renderScale);

        if (this.rotateAngleZ != 0.0F)
          {
          GL11.glRotatef(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
          }

        if (this.rotateAngleY != 0.0F)
          {
          GL11.glRotatef(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
          }

        if (this.rotateAngleX != 0.0F)
          {
          GL11.glRotatef(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
          }

        if(this.isCurrentSelection)
          {
          GL11.glColor4f(1.f, 0.3f, 0.3f, 1.f);
          GL11.glCallList(this.displayList);
          if(this.rotationPointBox!=null)
            {
            GL11.glColor4f(1.f, 1.f, 0.3f, 1.f);
            GL11.glCallList(this.rotationBoxDL);
            }
          GL11.glColor4f(1, 1, 1, 1);

          if (this.childModels != null)
            {
            GL11.glColor4f(.3f, .3f, 1.f, 1.f);
            for (index = 0; index < this.childModels.size(); ++index)
              {                 
              this.childModels.get(index).render(renderScale);
              }
            GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
            }
          }
        else
          {
          GL11.glCallList(this.displayList);
          if (this.childModels != null)
            {
            for (index = 0; index < this.childModels.size(); ++index)
              {
              this.childModels.get(index).render(renderScale);
              }
            }
          }

        GL11.glPopMatrix();
        }

      GL11.glTranslatef(-this.baseOffsetX, -this.baseOffsetY, -this.baseOffsetZ);
      }
    }

  }

@SideOnly(Side.CLIENT)
public void renderWithRotation(float renderScale)
  {
  if (!this.isHidden)
    {
    if (this.showModel)
      {
      if (!this.compiled)
        {
        this.compileDisplayList(renderScale);
        }

      GL11.glPushMatrix();
      GL11.glTranslatef(this.rotationPointX * renderScale, this.rotationPointY * renderScale, this.rotationPointZ * renderScale);

      if (this.rotateAngleY != 0.0F)
        {
        GL11.glRotatef(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
        }

      if (this.rotateAngleX != 0.0F)
        {
        GL11.glRotatef(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
        }

      if (this.rotateAngleZ != 0.0F)
        {
        GL11.glRotatef(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
        }

      GL11.glCallList(this.displayList);
      GL11.glPopMatrix();
      }
    }
  }

/**
 * Allows the changing of Angles after a box has been rendered
 */
@SideOnly(Side.CLIENT)
public void postRender(float renderScale)
  {
  if (!this.isHidden)
    {
    if (this.showModel)
      {
      if (!this.compiled)
        {
        this.compileDisplayList(renderScale);
        }

      if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
        {
        if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
          {
          GL11.glTranslatef(this.rotationPointX * renderScale, this.rotationPointY * renderScale, this.rotationPointZ * renderScale);
          }
        }
      else
        {
        GL11.glTranslatef(this.rotationPointX * renderScale, this.rotationPointY * renderScale, this.rotationPointZ * renderScale);

        if (this.rotateAngleZ != 0.0F)
          {
          GL11.glRotatef(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
          }

        if (this.rotateAngleY != 0.0F)
          {
          GL11.glRotatef(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
          }

        if (this.rotateAngleX != 0.0F)
          {
          GL11.glRotatef(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
          }
        }
      }
    }
  }

public void renderForPicking()
  {
  float renderScale = 0.0625f;
  int index;
  GL11.glTranslatef(this.baseOffsetX, this.baseOffsetY, this.baseOffsetZ);
  GL11.glPushMatrix();  
  GL11.glTranslatef(this.rotationPointX * renderScale, this.rotationPointY * renderScale, this.rotationPointZ * renderScale);
  if (this.rotateAngleZ != 0.0F)
    {
    GL11.glRotatef(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
    }
  if (this.rotateAngleY != 0.0F)
    {
    GL11.glRotatef(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
    }
  if (this.rotateAngleX != 0.0F)
    {
    GL11.glRotatef(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
    }  
  int r = (this.pieceName % 256);
  int g = (this.pieceName/256)%256;
  int b = ((this.pieceName/256)/256)%256;
  MEIMConfig.logDebug("piece num: "+this.pieceName);
  MEIMConfig.logDebug("setting piece colors to: "+r+","+g+","+b);
  //GL11.glColor3b((byte)r, (byte)g, (byte)b);
  GL11.glColor3ub((byte)r, (byte)g, (byte)b);  
  GL11.glCallList(this.displayList);
  if (this.childModels != null)
    {
    for (index = 0; index < this.childModels.size(); ++index)
      {
      this.childModels.get(index).renderForPicking();
      }
    }
  GL11.glPopMatrix();
  GL11.glTranslatef(-this.baseOffsetX, -this.baseOffsetY, -this.baseOffsetZ);
  }

MEIMModelBox rotationPointBox;
int rotationBoxDL;

/**
 * Compiles a GL display list for this model
 */
@SideOnly(Side.CLIENT)
public void compileDisplayList(float renderScale)
  {
  this.displayList = GLAllocation.generateDisplayLists(1);
  GL11.glNewList(this.displayList, GL11.GL_COMPILE);
  Tessellator tess = Tessellator.instance;
  for (int index = 0; index < this.cubeList.size(); ++index)
    {
    this.cubeList.get(index).render(tess, renderScale);
    }

  GL11.glEndList();

  this.rotationBoxDL = GLAllocation.generateDisplayLists(1);
  GL11.glNewList(this.rotationBoxDL, GL11.GL_COMPILE);
  if(this.rotationPointBox!=null)
    {
    this.rotationPointBox.render(tess, renderScale);
    }
  GL11.glEndList();
  this.compiled = true;
  }

/**
 * Returns the model renderer with the new texture parameters.
 */
public MEIMModelRenderer setTextureSize(int xSize, int zSize)
  {
  this.textureWidth = (float)xSize;
  this.textureHeight = (float)zSize;
  return this;
  }


}
