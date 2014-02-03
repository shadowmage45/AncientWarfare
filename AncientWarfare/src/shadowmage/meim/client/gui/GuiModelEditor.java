/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
   Please see COPYING for precise license information.

   This file is part of Ancient Warfare.

   Ancient Warfare is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Ancient Warfare is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadowmage.meim.client.gui;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.IFileSelectCallback;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.model.ModelBaseAW;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.client.model.Primitive;
import shadowmage.ancient_framework.client.model.PrimitiveBox;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_framework.common.utils.Trig;
import shadowmage.meim.client.texture.TextureManager;

public class GuiModelEditor extends GuiContainerAdvanced implements IFileSelectCallback
{

static final int SELECT_LOAD = 0;
static final int SELECT_SAVE = 1;
static final int SELECT_IMPORT_PIECE = 2;
static final int SELECT_LOAD_TEXTURE = 3;
private static final int SELECT_SAVE_TEXTURE = 4;
private static final int SELECT_EXPORT_UV_MAP = 5;

static ModelBaseAW model;

int selectionMode = -1;

private ModelPiece selectedPiece;
private Primitive selectedPrimitive;

/**
 * stored/calc'd values
 */
float viewPosX, viewPosY, viewPosZ, viewTargetX, viewTargetY, viewTargetZ;
private GuiModelEditorSetup setup;

public GuiModelEditor(ContainerBase container)
  {
  super(container);
  this.setup = new GuiModelEditorSetup(this);
  this.shouldCloseOnVanillaKeys = true;
  viewPosZ = 5;
  }

public void initModel()
  {
  model = new ModelBaseAW();
  model.setTextureSize(256, 256);
  ModelPiece piece = new ModelPiece(model, "part1", 0, 0, 0, 0, 0, 0, null);
  PrimitiveBox box = new PrimitiveBox(piece);
  box.setOrigin(0, 0, 0);
  box.setRotation(0, 0, 0);
  box.setBounds(-0.5f, 0.f, -0.5f, 1, 1, 1);
  model.addPiece(piece);
  piece.addPrimitive(box);
  setSelectedPiece(piece);
  setSelectedPrimitive(box);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
 
  }

@Override
public int getXSize()
  {
  return 256;
  }

@Override
public int getYSize()
  {
  return 240;
  }

int lastClickXLeft;
int lastClickZLeft;
int lastClickXRight;
int lastClickZRight;
float yaw;
float pitch;
float viewDistance = 5.f;

@Override
public void handleMouseInput()
  {
  super.handleMouseInput();  
  int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
  int z = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
  int button = Mouse.getEventButton();
  boolean state = Mouse.getEventButtonState();
  int wheelDelta = Mouse.getEventDWheel();
  if(this.isMouseOverControl(x, z))
    {
    return;
    }

  if(button==-1 && wheelDelta!=0)
    {
    AWLog.logDebug("handling wheel input: "+x+","+z+","+" :: b: "+button+" s:"+state+" w: "+wheelDelta);
    if(wheelDelta<0)
      {
      viewDistance+=0.25f;
      }
    else
      {
      viewDistance-=0.25f;
      }
    viewPosX = viewTargetX + viewDistance * MathHelper.sin(yaw) * MathHelper.cos(pitch);
    viewPosZ = viewTargetZ + viewDistance * MathHelper.cos(yaw) * MathHelper.cos(pitch);
    viewPosY = viewTargetY + viewDistance * MathHelper.sin(pitch);
    }
  else if(button==0)
    {
    AWLog.logDebug("handling mouse input: "+x+","+z+","+" :: b: "+button+" s:"+state+" w: "+wheelDelta);
    if(state)
      {
      this.lastClickXLeft = x;
      this.lastClickZLeft = z;      
      }
    else
      {
      /**
       * TODO move viewTarget relative to current view yaw/pitch
       * (will need move both viewTarget and viewPos in order to have proper movement)
       * (need to move relative to current view facing -- will require a bit of trig)
       */
      }
    }
  else if(button==1 && state)
    {
    AWLog.logDebug("handling mouse input: "+x+","+z+","+" :: b: "+button+" s:"+state+" w: "+wheelDelta);
    this.lastClickXRight = x;
    this.lastClickZRight = z;
    } 
  else if(Mouse.isButtonDown(1))
    {
    float yawInput = x - lastClickXRight;
    float pitchInput = z - lastClickZRight;
      
    yaw -= yawInput*Trig.TORADIANS;
    pitch -= pitchInput*Trig.TORADIANS;
    
    viewPosX = viewTargetX + viewDistance * MathHelper.sin(yaw) * MathHelper.cos(pitch);
    viewPosZ = viewTargetZ + viewDistance * MathHelper.cos(yaw) * MathHelper.cos(pitch);
    viewPosY = viewTargetY + viewDistance * MathHelper.sin(pitch);
    
    this.lastClickXRight = x;
    this.lastClickZRight = z;
    /**
     * TODO move viewPosition using x/y as degrees of change input on yaw/pitch
     */
    }
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  setupModelView();
  renderGrid();
  enableModelLighting();
  if(model!=null)
    {
    TextureManager.bindTexture();
    model.renderModel();
    TextureManager.resetBoundTexture();
    }  
  resetModelView();
  }

@Override
public void drawExtraForeground(int mouseX, int mouseY, float partialTick)
  {

  }

private void setupModelView()
  {  
  /**
   * load projection matrix and clear it (set to identity)
   */
  GL11.glMatrixMode(GL11.GL_PROJECTION);
  GL11.glPushMatrix(); 
  GL11.glLoadIdentity();
  
  /**
   * set up the base projection transformation, as well as
   */
  float aspect = (float)this.mc.displayWidth/(float)this.mc.displayHeight;  
  GLU.gluPerspective(60.f, aspect, 0.1f, 100.f); 
  GLU.gluLookAt(viewPosX, viewPosY, viewPosZ, viewTargetX, viewTargetY, viewTargetZ, 0, 1, 0);   
    
  GL11.glMatrixMode(GL11.GL_MODELVIEW);
  GL11.glPushMatrix();  
  GL11.glLoadIdentity();
  
  GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
  }

private void resetModelView()
  {
  GL11.glMatrixMode(GL11.GL_PROJECTION);
  GL11.glPopMatrix();
  GL11.glMatrixMode(GL11.GL_MODELVIEW);
  GL11.glPopMatrix();
  }

private void enableModelLighting()
  {
  int bright = this.player.worldObj.getLightBrightnessForSkyBlocks((int)this.player.posX, (int)this.player.posY, (int)this.player.posZ, 0);

  int var11 = bright % 65536;
  int var12 = bright / 65536;
  OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var11 / 1.0F, (float)var12 / 1.0F);

  RenderHelper.enableStandardItemLighting();
  }

int gridDisplayList = -1;

private void renderGrid()
  {
  GL11.glDisable(GL11.GL_TEXTURE_2D);
  GL11.glDisable(GL11.GL_LIGHTING);
  GL11.glLineWidth(2.f);
  if(gridDisplayList>=0)
    {    
    GL11.glCallList(gridDisplayList);
    }
  else
    {
    gridDisplayList = GL11.glGenLists(1);
    GL11.glNewList(gridDisplayList, GL11.GL_COMPILE_AND_EXECUTE);
    GL11.glColor4f(0.f, 0.f, 1.f, 1.f);
    for(int x = -5; x<=5; x++)
      {
      GL11.glBegin(GL11.GL_LINE_LOOP);
      GL11.glVertex3f(x, 0.f, -5.f);
      GL11.glVertex3f(x, 0.f, 5.f);
      GL11.glEnd();    
      }  
    for(int z = -5; z<=5; z++)
      {
      GL11.glBegin(GL11.GL_LINE_LOOP);
      GL11.glVertex3f(-5.f, 0.f, z);
      GL11.glVertex3f(5.f, 0.f, z);
      GL11.glEnd();    
      }
    GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
    GL11.glEndList();
    }
  GL11.glEnable(GL11.GL_LIGHTING);
  GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

@Override
public void updateScreenContents()
  {  
  if(model==null)
    {
    initModel();
    }
  }

@Override
public void setupControls()
  {
  this.setup.setupControls();//all controls are maintained in the setup class
  }

@Override
public void updateControls()
  {
  setup.updateControls(guiLeft, guiTop, width, height);//all controls are maintained in the setup class    
  }

ModelLoader loader = new ModelLoader();

@Override
public void handleFileSelection(File file)
  {
  switch(selectionMode)
  {
  case SELECT_LOAD:
    {
    ModelBaseAW model = loader.loadModel(file);
    GuiModelEditor.model = model;
    setSelectedPiece(null);
    setSelectedPrimitive(null);
    this.setup.updateButtonValues();    
    this.refreshGui();    
    }
    break;
  case SELECT_SAVE:
    { 
    loader.saveModel(model, file);
    }
    break;
  case SELECT_IMPORT_PIECE:
    {
    
    }
    break;
  case SELECT_LOAD_TEXTURE:
    {
    try
      {
      TextureManager.updateTextureContents(ImageIO.read(file));
      } 
    catch (IOException e)
      {
      e.printStackTrace();
      }
    }
    break;
  case SELECT_SAVE_TEXTURE:
    {
    
    }
    break;
  case SELECT_EXPORT_UV_MAP:
    {
    
    }
    break;
  }
  this.refreshGui();
  }

public void copyPiece(){}

public void deletePiece(){}

public void deletePrimitive(){}

public void changeParent()
  {
  
  }

public void swapBox()
  {
  
  }

public void openUVMap()
  {
  
  }

Primitive getSelectedPrimitive()
  {
  return selectedPrimitive;
  }

void setSelectedPrimitive(Primitive selectedPrimitive)
  {
  this.selectedPrimitive = selectedPrimitive;
  this.refreshGui();
  }

ModelPiece getSelectedPiece()
  {
  return selectedPiece;
  }

void setSelectedPiece(ModelPiece selectedPiece)
  {
  this.selectedPiece = selectedPiece;
  this.setSelectedPrimitive(null);
  this.refreshGui();
  }
}
