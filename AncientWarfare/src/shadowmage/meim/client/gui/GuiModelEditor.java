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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

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

ModelPiece selectedPiece;
Primitive selectedPrimitive;

float viewPosX, viewPosY, viewPosZ, viewTargetX, viewTargetY, viewTargetZ;
private GuiModelEditorSetup setup;

public GuiModelEditor(ContainerBase container)
  {
  super(container);
  if(model==null)
    {
    initModel();
    }
  this.shouldCloseOnVanillaKeys = true;
  this.setup = new GuiModelEditorSetup(this);
  viewPosX = 5;
  viewPosZ = 5;
  viewPosY = 5;
  }

public void initModel()
  {
  model = new ModelBaseAW();
  ModelPiece piece = new ModelPiece(model, "part1", 0, 0, 0, 0, 0, 0, null);
  PrimitiveBox box = new PrimitiveBox(piece);
  box.setOrigin(0, 0, 0);
  box.setRotation(0, 0, 0);
  box.setBounds(-0.5f, 0.f, -0.5f, 1, 1, 1);
  model.addPiece(piece);
  piece.addPrimitive(box);
  selectedPiece = piece;
  selectedPrimitive = box;
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  //element actions handled by anonymous element classes implementation in GuiModelEditorSetup
  //this.refreshGui();
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

private ResourceLocation testTex = new ResourceLocation("ancientwarfare", "test.png");

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
  if(this.selectedPiece!=null && this.selectedPrimitive!=null)
    {
//    AWLog.logDebug("selected piece: "+selectedPiece);
//    AWLog.logDebug("selected prim: "+selectedPrimitive);
//    this.selectedPiece.setRotation(selectedPiece.rx(), selectedPiece.ry()+0.1f, selectedPiece.rz());
//    this.setup.updateButtonValues();
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
    selectedPiece = null;
    selectedPrimitive = null;
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

public void clearSelection()
  {
  selectedPiece = null;
  selectedPrimitive = null;
  }

public void changeParent()
  {
  
  }

public void swapBox()
  {
  
  }

public void openUVMap()
  {
  
  }
}
