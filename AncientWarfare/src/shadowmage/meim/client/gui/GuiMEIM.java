package shadowmage.meim.client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IFileSelectCallback;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.model.ModelBaseAW;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.client.model.Primitive;
import shadowmage.ancient_framework.client.model.PrimitiveBox;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_framework.common.utils.Trig;
import shadowmage.meim.client.meim_model.MEIMModelBase;
import shadowmage.meim.client.model.ModelModel;
import shadowmage.meim.client.modelrenderer.MEIMModelBox;
import shadowmage.meim.client.modelrenderer.MEIMModelRenderer;
import shadowmage.meim.client.texture.TextureManager;
import shadowmage.meim.common.MEIM;
import shadowmage.meim.common.config.MEIMConfig;

public class GuiMEIM extends GuiContainerAdvanced implements IFileSelectCallback
{

/**
 * absolute view changes
 */
private float viewTargetX;
private float viewTargetY;
private float viewTargetZ;

private float viewPosX;
private float viewPosY;
private float viewPosZ;

private float viewScale = 10;

private float tx;
private float tz;


private ModelLoader loader = new ModelLoader();

public ModelModel gridModel = new ModelModel();


public ModelBaseAW model;

ModelPiece currentPart;
PrimitiveBox currentBox;

boolean checkHit = false;
int hitNum = -1;

int imgNum;
BufferedImage img;

int partCopyNum = 1;

ContainerBase container;
ResourceLocation gridTexture;

public GuiMEIM(ContainerBase container)
  {
  super(container);
  this.model = (ModelBaseAW)MEIM.proxy.getModel();
  if(model==null)
    {
    System.out.println("setting up debug model");
    ModelBaseAW model = new ModelBaseAW();
    MEIM.proxy.setModel(model);
    this.model = model;
    this.initDebugModel();
    }
  this.zLevel = -2000f;
  this.updateViewPos(0,0,0,0);
  this.forceUpdate = true;
  Keyboard.enableRepeatEvents(true);
  TextureManager.allocateTexture();
  this.gridTexture = new ResourceLocation("ancientwarfare", "meim/test.png");
  this.container = container;
  }

/**
 * TODO move this out to model class or something....
 */
public void initDebugModel()
  {
  model.setTextureSize(256, 256);
  ModelPiece piece = new ModelPiece(model, "part1", 0, 0, 0, 0, 0, 0, null);  
  PrimitiveBox box = new PrimitiveBox(piece, 0, 0, 0, 10, 10, 10, 0, 0, 0, 0, 0);
  piece.addPrimitive(box);
  model.addPiece(piece);
  }

/**
 * callback from guiFileSelect...
 * @param selectionType
 */
@Override
public void handleFileSelection(int selectionType)
  {
  try
    {
    File f;
    if(this.fileSelection.isEmpty())
      {
      return;
      }
    switch(selectionType)
    {
    case SELECT_TEXTURE_LOAD:
    f = new File(this.fileSelection.get(0));
    if(f.exists())
      {
      this.img = ImageIO.read(f);
      TextureManager.updateTextureContents(img);
      }
    break;
    
    case SELECT_MODEL_LOAD:
    ModelBaseAW model = loader.loadModel(this.fileSelection.get(0));
    if(model!=null)
      {
      this.model = model;
      this.currentPart = null;
      this.currentBox = null;
      //this.currentPart = this.model.baseParts.get(0);
      //this.currentPart.setCurrent();
      //this.currentBox = this.currentPart.getFirstBox();
      MEIM.proxy.setModel(this.model);
      }
    break;
    
    case SELECT_MODEL_SAVE:
    this.loader.saveModel(this.model, this.fileSelection.get(0));
    break;
    
    case SELECT_MODEL_EXPORT:
    break;
    
    case SELECT_TEXTURE_EXPORT:
    break;
    
    case SELECT_MODEL_IMPORT:
    ModelBaseAW model2 = loader.loadModel(this.fileSelection.get(0));
    if(model2!=null)
      {
      for(ModelPiece part : model2.getBasePieces())
        {        
        this.model.addPiece(part);
        }
      }
    break;
    
    default:
    break;
    }
    }
  catch(IOException e)
    {
    MEIMConfig.logError("A file system error has occured while attempting to handle selection operation for type: "+selectionType);
    }
  this.forceUpdate = true;
  }

public static final int SELECT_TEXTURE_LOAD = 0;
public static final int SELECT_MODEL_LOAD = 1;
public static final int SELECT_MODEL_SAVE = 2;
public static final int SELECT_MODEL_EXPORT = 3;
public static final int SELECT_TEXTURE_EXPORT = 4;
public static final int SELECT_MODEL_IMPORT = 5;

public void swapParts(ModelPiece newPart)
  {  
  this.currentPart = newPart;
  }

@Override
public void onGuiClosed()
  {
  Keyboard.enableRepeatEvents(false);
  super.onGuiClosed();
  }

@Override
protected void mouseClicked(int par1, int par2, int par3)
  {
  super.mouseClicked(par1, par2, par3);
  if(this.isMouseOverControl(par1, par2))
    {
    return;
    }
  if(par3==0)
    {
    this.lastClickXLeft = par1;
    this.lastClickZLeft = par2;
    rotate = true;
    }
  else if(par3==1)
    {
    move = true;
    this.lastClickXRight = par1;
    this.lastClickZRight = par2;
    }
  this.checkHit = true;
  }

public int doSelection(int posX, int posY, int buttonNum)
  {
  posX = Mouse.getX();
  posY = Mouse.getY();  

  GL11.glDisable(GL11.GL_TEXTURE_2D);
  GL11.glClearColor(1.f, 1.f, 1.f, 1.f);
  GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
  this.model.renderForSelection();   

  byte[] pixelColorsb = new byte[3];
  ByteBuffer pixelColors = ByteBuffer.allocateDirect(3);
  GL11.glReadPixels(posX, posY, 1, 1, GL11.GL_RGB, GL11.GL_BYTE, pixelColors);

  for(int i = 0; i < 3 ; i++)
    {
    pixelColorsb[i] = pixelColors.get(i);
    }
  
  int r = pixelColorsb[0];
  int g = pixelColorsb[1];
  int b = pixelColorsb[2];

  GL11.glEnable(GL11.GL_TEXTURE_2D);
  AWLog.logDebug("colors clicked on: "+r+","+g+","+b);
  int color = (r<<16) | (g<<8) | b;
  return color;
  }

protected void updateViewPos(int yawDelta, int pitchDelta, int xDelta, int zDelta)
  {
  this.yaw += yawDelta;
  this.pitch += pitchDelta; 

  float yTrans = -zDelta * Trig.cosDegrees(pitch);
  float zTrans = -zDelta * -Trig.sinDegrees(pitch) * Trig.cosDegrees(yaw);
  float xTrans = -zDelta * -Trig.sinDegrees(pitch) * Trig.sinDegrees(yaw);
  
  xTrans += xDelta * Trig.cosDegrees(-yaw);
  zTrans += xDelta * Trig.sinDegrees(-yaw);
  
  this.viewTargetY += yTrans*0.0625f;
  this.viewTargetZ += zTrans*0.0625f;  
  this.viewTargetX += xTrans*0.0625f;

  this.viewPosX = Trig.sinDegrees(yaw) * Trig.cosDegrees(pitch) * viewScale;
  this.viewPosZ = Trig.cosDegrees(yaw) * Trig.cosDegrees(pitch) * viewScale;
  this.viewPosY = Trig.sinDegrees(pitch) * viewScale;
  }

List<String> fileSelection = new ArrayList<String>();


boolean rotate;
boolean move;
int lastClickXLeft;
int lastClickZLeft;
int lastClickXRight;
int lastClickZRight;
int yaw;
int pitch = 35;

@Override
public void handleMouseInput()
  {
  super.handleMouseInput();
  int wheel = Mouse.getEventDWheel();
  wheel = wheel > 0 ? -1 : wheel < 0 ? 1 : 0;
  int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
  int z = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
  if(this.isMouseOverControl(x, z))
    {
    return;
    }
  if(wheel!=0)
    {
    if((wheel<0 && this.viewScale>1) || (wheel>0 && this.viewScale <35))
      {
      this.viewScale+=wheel;
      this.updateViewPos(0,0,0,0);
      }      
    }
  else
    {
    if(!Mouse.getEventButtonState())
      {
      int yawDelta = 0;
      int pitchDelta = 0;
      int xDelta = 0;
      int zDelta = 0;
      if(rotate)
        {
        yawDelta -= (x-lastClickXLeft);
        pitchDelta+= (z-lastClickZLeft);
        lastClickXLeft = x;
        lastClickZLeft = z;
        }
      if(move)
        {
        xDelta+= (x - lastClickXRight);
        zDelta += (z - lastClickZRight);
        lastClickXRight = x;
        lastClickZRight = z;
        }
      if(rotate || move)
        {
        this.updateViewPos(yawDelta, pitchDelta, xDelta, zDelta);
        }
      if(Mouse.getEventButton()!=-1)
        {        
        if(Mouse.getEventButton()==0)
          {
          rotate = false;
          }
        else if(Mouse.getEventButton()==1)
          {
          move = false;
          }
        }
      }
    }
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

@Override
public String getGuiBackGroundTexture()
  {
  return null;
  }

/**
 * Draws the screen and all the components in it.
 */
public void drawScreen(int par1, int par2, float par3)
  {
  this.renderExtras(par1, par2, par3);  
  super.drawScreen(par1, par2, par3);
  }

public void renderExtras(int a, int b, float c)
  {   
  /**
   * clear screen (dk grey)(handles clearing after a selection draw)
   */

  GL11.glMatrixMode(GL11.GL_PROJECTION);
  GL11.glLoadIdentity();
  float aspect = (float)this.mc.displayWidth/(float)this.mc.displayHeight;
  GLU.gluPerspective(60.0F, aspect, 0.8F, 50.0F);
  
  GL11.glMatrixMode(GL11.GL_MODELVIEW);
  GL11.glLoadIdentity();
  GLU.gluLookAt(viewPosX, viewPosY, viewPosZ, 0, 0, 0, 0, 1, 0);
  
  GL11.glTranslatef(viewTargetX, viewTargetY, viewTargetZ);

  if(this.checkHit)
    {
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f );

    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    this.checkHit = false;    
    /**
     * clear screen (white, set background color)
     */
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_TEXTURE_2D);    
    int hitNum = this.doSelection(a, b, (int) c);
    AWLog.logDebug("selection number: "+hitNum);
    GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glEnable(GL11.GL_LIGHT0);
    GL11.glEnable(GL11.GL_LIGHT1);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    Primitive primitive = model.getPrimitive(hitNum);
    if(primitive!=null)
      {
      this.swapParts(primitive.parent);
      this.forceUpdate = true;
      }
    }

  /**
   * debug light enable...
   * 
   * 
   */

  int bright = this.player.worldObj.getLightBrightnessForSkyBlocks((int)this.player.posX, (int)this.player.posY, (int)this.player.posZ, 0);


  int var11 = bright % 65536;
  int var12 = bright / 65536;
  OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var11 / 1.0F, (float)var12 / 1.0F);
  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

  /**
   * end debug light enable..
   */

  GL11.glClearColor(0.2f, 0.2f, 0.2f, 1.0f );
  GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
  RenderHelper.enableStandardItemLighting();
  
  /**
   * render grid....
   */
  
  this.renderGrid();
  TextureManager.bindTexture();
   
  if(this.model!=null)
    {
    this.model.renderModel();
    } 
  mc.entityRenderer.setupOverlayRendering();
  RenderHelper.disableStandardItemLighting();
  TextureManager.resetBoundTexture();
  }

protected void renderGrid()
  {
  this.mc.renderEngine.bindTexture(gridTexture);
  this.gridModel.render();
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  int size = 12;
  int buffer = 2;
  int col1 = -guiLeft + 4;
  int col2 = col1+size+buffer;
  int col3 = col2+size+buffer;
  int col4 = col3+size+buffer;
  int col5 = col4+size+buffer;
  int col6 = col5+size+buffer;
  int col7 = col6+size+buffer;
  int row1 = -guiTop + 4;
  int row2 = row1+size+buffer;
  int row3 = row2+size+buffer;
  int row4 = row3+size+buffer;
  int row5 = row4+size+buffer;
  int row6 = row5+size+buffer;
  int row7 = row6+size+buffer;
  int row8 = row7+size+buffer;
  int row9 = row8+size+buffer;
  int row10 = row9+size+buffer;
  int row11 = row10+size+buffer;
  int row12 = row11+size+buffer;
  int row13 = row12+size+buffer;
  int row14 = row13+size+buffer;
  int row15 = row14+size+buffer;
  int row16 = row15+size+buffer;
  int row17 = row16+size+buffer;
  
  this.drawStringGui( "pX", col1, row6, 0xffffffff);
  this.drawStringGui( "pY", col1, row7, 0xffffffff);
  this.drawStringGui( "pZ", col1, row8, 0xffffffff);
  this.drawStringGui( "rX", col1, row9, 0xffffffff);
  this.drawStringGui( "rY", col1, row10, 0xffffffff);
  this.drawStringGui( "rZ", col1, row11, 0xffffffff);
  this.drawStringGui( "bW", col1, row12, 0xffffffff);
  this.drawStringGui( "bH", col1, row13, 0xffffffff);
  this.drawStringGui( "bL", col1, row14, 0xffffffff);
  this.drawStringGui( "bX", col1, row15, 0xffffffff);
  this.drawStringGui( "bY", col1, row16, 0xffffffff);
  this.drawStringGui( "bZ", col1, row17, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  //Config.logDebug("updating screen contents");
  
  }

public void swapCurrentPartParent()
  {
  if(this.currentPart!=null)
    {
    this.currentPart = null;
    this.currentBox = null;
    this.forceUpdate = true;
    mc.displayGuiScreen(new GuiSwapParents(this.container,this, this.currentPart));
    }
  }

public void clearCurrentPartParent()
  {
  if(this.currentPart!=null && this.currentPart.getParent()!=null)
    {
    this.currentPart.getParent().removeChild(currentPart);
    }
  }

public void clearCurrentSelection()
  {
  this.currentPart = null;
  this.currentBox = null;
  this.forceUpdate = true;
  }

public void deleteCurrentSelection()
  {
  if(this.currentPart!=null)
    {
    this.model.removePiece(this.currentPart.getName());
    this.currentPart = null;
    this.forceUpdate = true;
    }
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  this.forceUpdate = true;
  switch(element.getElementNumber())
  {
  case 0://done
  this.clearCurrentSelection();
  player.closeScreen();
  break;

  case 1://piece-name box interaction (enter pressed)
  if(this.currentPart!=null)
    {
    this.model.removePiece(currentPart);
    this.currentPart.setName(this.partNameField.getText());
    this.model.addPiece(currentPart);
    }
  break;

  case 2:
  break;

  case 3:
  break;

  case 4:
  break;

  case 5://swap parent (open gui)
  this.swapCurrentPartParent();  
  break;

  case 6://delete current selection, set current part to null
  this.deleteCurrentSelection();
  break;

  case 7://add Child to piece  
  mc.displayGuiScreen(new GuiAddPart(this, currentPart)); 
  break;

  case 8://add box to piece
  break;

  case 9://selection x++
  if(currentPart!=null)
    {
    currentPart.setPosition(currentPart.x()+1, currentPart.y(), currentPart.z());
    }
  break;  
  case 10://selection x--
  if(currentPart!=null)
    {
    currentPart.setPosition(currentPart.x()-1, currentPart.y(), currentPart.z());
    }
  break;  
  case 11://selection y++
  if(currentPart!=null)
    {
    currentPart.setPosition(currentPart.x(), currentPart.y()+1, currentPart.z());
    }
  break;  
  case 12://selection y--
  if(currentPart!=null)
    {
    currentPart.setPosition(currentPart.x(), currentPart.y()-1, currentPart.z());
    }
  break;  
  case 13://selection z++
  if(currentPart!=null)
    {
    currentPart.setPosition(currentPart.x(), currentPart.y(), currentPart.z()+1);
    }
  break;  
  case 14://selection z--
  if(currentPart!=null)
    {
    currentPart.setPosition(currentPart.x(), currentPart.y(), currentPart.z()-1);
    }
  break;

  case 15://selection rx++
  if(currentPart!=null)
    {
    currentPart.setRotation(currentPart.rx()+1, currentPart.ry(), currentPart.rz());
    }
  break;
  case 16://selection rx--
  if(currentPart!=null)
    {
    currentPart.setRotation(currentPart.rx()-1, currentPart.ry(), currentPart.rz());
    }
  break;
  case 17://selection ry++
  if(currentPart!=null)
    {
    currentPart.setRotation(currentPart.rx(), currentPart.ry()+1, currentPart.rz());
    }
  break;
  case 18://selection ry--
  if(currentPart!=null)
    {
    currentPart.setRotation(currentPart.rx(), currentPart.ry()-1, currentPart.rz());
    }
  break;
  case 19://selection rz++
  if(currentPart!=null)
    {
    currentPart.setRotation(currentPart.rx(), currentPart.ry(), currentPart.rz()+1);
    }
  break;
  case 20://selection rz--
  if(currentPart!=null)
    {
    currentPart.setRotation(currentPart.rx(), currentPart.ry(), currentPart.rz()-1);
    }
  break;

  /**
   * boxes...
   */
  case 21: //w++
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.x2++;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }  
  break;
  case 22: //w--
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.x2--;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;
  case 23: //h++
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.y2++;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;
  case 24: //h--
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.y2--;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;
  case 25://l++
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.z2++;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;
  case 26://l--
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.z2--;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;

  case 27://x++
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.x1++;
    box.x2++;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;
  case 28://x--
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.x1--;
    box.x2--;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;
  case 29://y++
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.y1++;
    box.y2++;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;
  case 30://y--
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.y1--;
    box.y2--;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;    
  case 31://z++
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.z1++;
    box.z2++;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;    
  case 32://z--
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.z1--;
    box.z2--;
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;  

  case 33://clear selection
  this.clearCurrentSelection();
  break;  

  case 34://clear current selections parent part...
  this.clearCurrentPartParent();
  break;

  /**
   * text field handling, through to 46
   */
  case 35:
  if(this.currentPart!=null)
    {
    this.currentPart.setPosition(piecePosFields[0].getFloatVal(), currentPart.y(), currentPart.z());     
    }
  break;  
  case 36:
  if(this.currentPart!=null)
    {
    this.currentPart.setPosition(currentPart.x(), piecePosFields[1].getFloatVal(), currentPart.z());
    }  
  break; 
  case 37:
  if(this.currentPart!=null)
    {
    this.currentPart.setPosition(currentPart.x(), currentPart.y(), piecePosFields[2].getFloatVal());
    }  
  break;  
  case 38:
  if(this.currentPart!=null)
    {
    this.currentPart.setRotation(pieceRotFields[0].getFloatVal(), currentPart.ry(), currentPart.rz());
    }
  break;
  case 39:
  if(this.currentPart!=null)
    {
    this.currentPart.setRotation(currentPart.rx(), pieceRotFields[1].getFloatVal(), currentPart.rz());
    }
  break;
  case 40:
  if(this.currentPart!=null)
    {
    this.currentPart.setRotation(currentPart.rx(), currentPart.ry(), pieceRotFields[2].getFloatVal());
    }
  break;
  
  case 41:  
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.x2 = box.x1 + this.currentBox.x1+boxSizeFields[0].getIntVal();
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;  
    }  
  break;
  case 42:
  if(this.currentPart!=null && this.currentBox!=null)
    {    
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.y2 = box.y1 + this.currentBox.x1+boxSizeFields[1].getIntVal();
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    } 
  break;
  case 43:
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.z2 = box.z1 + this.currentBox.x1+boxSizeFields[2].getIntVal();
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;
    }
  break;
  case 44:
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.x1 = boxPosFields[0].getFloatVal();
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;   
    }
  break;
  case 45:
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.y1 = boxPosFields[1].getFloatVal();
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;  
    }
  break;
  case 46:
  if(this.currentPart!=null && this.currentBox!=null)
    {
    PrimitiveBox box = new PrimitiveBox(currentPart, this.currentBox.x1, this.currentBox.y1, this.currentBox.z1, this.currentBox.x2, this.currentBox.y2, this.currentBox.z2, this.currentBox.rx, this.currentBox.ry, this.currentBox.rz, this.currentBox.tx, this.currentBox.ty);
    box.z1 = boxPosFields[2].getFloatVal();
    this.currentPart.removePrimitive(currentBox);
    this.currentPart.addPrimitive(box);
    this.currentBox = box;  
    }
  break;

  case 47:
  break;
  case 48:
  break;


  case 99://EXPORT
  mc.displayGuiScreen(new GuiExportModel(this));
  break;

  case 100://edit texture...
  mc.displayGuiScreen(new GuiTextureMapEditor(this));
  break;

  case 101://load model
  mc.displayGuiScreen(new GuiFileSelect(this, this, fileSelection, MEIMConfig.getModelSaveDir(), this.SELECT_MODEL_LOAD, false));
  break;
  
  case 102://save model
  mc.displayGuiScreen(new GuiFileSelect(this ,this, fileSelection, MEIMConfig.getModelSaveDir(), this.SELECT_MODEL_SAVE, true));  
  break;

  case 103://load texture
  mc.displayGuiScreen(new GuiFileSelect(this ,this, fileSelection, MEIMConfig.getTexLoadDir(), this.SELECT_TEXTURE_LOAD, false));
  break;
  
  case 104://import pieces from saved model
  mc.displayGuiScreen(new GuiFileSelect(this ,this, fileSelection, MEIMConfig.getTexLoadDir(), this.SELECT_MODEL_IMPORT, false));
  break;
  
  case 105://copy part
  this.copyCurrentPart();
  break;

  default:
  break;
  }
  this.forceUpdate = true;
  }

public void copyCurrentPart()
  {
  if(this.currentPart!=null)
    {    
    String partName = this.currentPart.getName()+"CP"+this.partCopyNum;
    ModelPiece newPart = this.currentPart.copy();
    newPart.setName(partName);
    model.addPiece(newPart);
    this.swapParts(newPart);
    this.partCopyNum++;
    }
  }

GuiTextInputLine partNameField;

@Override
public void setupControls()
  {
  /**
   * topright side controls (file controls mostly)
   */
  this.addGuiButton(0,  35, 16, "Done");
  this.addGuiButton(99,  35, 16, "Export");
  this.addGuiButton(100,  35, 16, "UVMap");  
  this.addGuiButton(101, 35, 16, "Load");
  this.addGuiButton(102,  35, 16, "Save");
  this.addGuiButton(103, 35, 16, "LoadTx");
  this.addGuiButton(104, 35, 16, "Im.Pc.");

  /**
   * right-side scroll-bar for piece-names....
   */
  
  int size = 12;
  int buffer = 2;
  int col1 = -guiLeft + 4;
  int col2 = col1+size+buffer;
  int col3 = col2+size+buffer;
  int col4 = col3+size+buffer;
  int col5 = col4+size+buffer;
  int col6 = col5+size+buffer;
  int col7 = col6+size+buffer;
  int row1 = -guiTop + 4;
  int row2 = row1+size+buffer;
  int row3 = row2+size+buffer;
  int row4 = row3+size+buffer;
  int row5 = row4+size+buffer;
  int row6 = row5+size+buffer;
  int row7 = row6+size+buffer;
  int row8 = row7+size+buffer;
  int row9 = row8+size+buffer;
  int row10 = row9+size+buffer;
  int row11 = row10+size+buffer;
  int row12 = row11+size+buffer;
  int row13 = row12+size+buffer;
  int row14 = row13+size+buffer;
  int row15 = row14+size+buffer;
  int row16 = row15+size+buffer;
  int row17 = row16+size+buffer;

  /**
   * these 4 need to go, be replaced with something useful
   */
  this.partNameField = this.addTextField(1, 80, 12, 20, "");  
  
  this.addGuiButton(5, size*3+buffer*2, size, "SwpPar");//swap parent
  this.addGuiButton(34,size*3+buffer*2, size, "ClrPar");//clear parent
  this.addGuiButton(33,  size*3+buffer*2, size, "ClrSel");//clear selection
  this.addGuiButton(105, size*3+buffer*2, size, "Copy");
  this.addGuiButton(6, size*6+buffer*5, size, "Delete");//nextBoxInPiece
  this.addGuiButton(7, size*6+buffer*5, size, "Add Part/Child");//addChildToPiece

  /**
   * piece offset  
   */
  this.addGuiButton(9,  size, size, "+");
  this.addGuiButton(10, size, size, "-");
  this.addGuiButton(11, size, size, "+");
  this.addGuiButton(12, size, size, "-");
  this.addGuiButton(13, size, size, "+");
  this.addGuiButton(14, size, size, "-");
  this.piecePosFields[0] = this.addNumberField(35, size*3+buffer*2, size, 6, "");
  this.piecePosFields[1] = this.addNumberField(36, size*3+buffer*2, size, 6, "");
  this.piecePosFields[2] = this.addNumberField(37, size*3+buffer*2, size, 6, "");

  /**
   * piece rotation
   */  
  this.addGuiButton(15, size, size, "+");
  this.addGuiButton(16, size, size, "-");
  this.addGuiButton(17, size, size, "+");
  this.addGuiButton(18, size, size, "-");
  this.addGuiButton(19, size, size, "+");
  this.addGuiButton(20, size, size, "-");
  this.pieceRotFields[0] = this.addNumberField(38, size*3+buffer*2, size, 6, "");
  this.pieceRotFields[1] = this.addNumberField(39, size*3+buffer*2, size, 6, "");
  this.pieceRotFields[2] = this.addNumberField(40, size*3+buffer*2, size, 6, "");

  /**
   * box size
   */
  this.addGuiButton(21, size, size, "+");
  this.addGuiButton(22, size, size, "-");
  this.addGuiButton(23, size, size, "+");
  this.addGuiButton(24, size, size, "-");
  this.addGuiButton(25, size, size, "+");
  this.addGuiButton(26, size, size, "-");
  this.boxSizeFields[0] = this.addNumberField(41, size*3+buffer*2, size, 6, "");
  this.boxSizeFields[1] = this.addNumberField(42, size*3+buffer*2, size, 6, "");
  this.boxSizeFields[2] = this.addNumberField(43, size*3+buffer*2, size, 6, "");

  /**
   * box offset
   */
  this.addGuiButton(27, size, size, "+");
  this.addGuiButton(28, size, size, "-");
  this.addGuiButton(29, size, size, "+");
  this.addGuiButton(30, size, size, "-");
  this.addGuiButton(31, size, size, "+");
  this.addGuiButton(32, size, size, "-");
  this.boxPosFields[0] = this.addNumberField(44, size*3+buffer*2, size, 6, "");
  this.boxPosFields[1] = this.addNumberField(45, size*3+buffer*2, size, 6, "");
  this.boxPosFields[2] = this.addNumberField(46, size*3+buffer*2, size, 6, "");
  
//  this.scaleField = this.addNumberField(106, size*3+buffer*2, size, 6, "1.0");
//  this.scaleField.setValue(1);
  }

private GuiNumberInputLine[] piecePosFields = new GuiNumberInputLine[3];
private GuiNumberInputLine[] pieceRotFields = new GuiNumberInputLine[3];
private GuiNumberInputLine[] boxSizeFields = new GuiNumberInputLine[3];
private GuiNumberInputLine[] boxPosFields = new GuiNumberInputLine[3];

private GuiNumberInputLine scaleField;

@Override
public void updateControls()
  {  
  this.getElementByNumber(0).updateRenderPos(  -guiLeft + width - 40, -guiTop+4);
  this.getElementByNumber(99).updateRenderPos( -guiLeft + width - 40, -guiTop+4+16*1+2*1);
  this.getElementByNumber(100).updateRenderPos(-guiLeft + width - 40, -guiTop+4+16*2+2*2);
  this.getElementByNumber(101).updateRenderPos(-guiLeft + width - 40, -guiTop+4+16*3+2*3);
  this.getElementByNumber(102).updateRenderPos(-guiLeft + width - 40, -guiTop+4+16*4+2*4);
  this.getElementByNumber(103).updateRenderPos(-guiLeft + width - 40, -guiTop+4+16*5+2*5);
  this.getElementByNumber(104).updateRenderPos(-guiLeft + width - 40, -guiTop+4+16*6+2*6);
//  this.scaleField.updateRenderPos(-guiLeft + width - 40, -guiTop+4+16*7+2*7);  
  
  int size = 12;
  int buffer = 2;
  int col1 = -guiLeft + 4;
  int col2 = col1+size+buffer;
  int col3 = col2+size+buffer;
  int col4 = col3+size+buffer;
  int col5 = col4+size+buffer;
  int col6 = col5+size+buffer;
  int col7 = col6+size+buffer;
  int row1 = -guiTop + 4;
  int row2 = row1+size+buffer;
  int row3 = row2+size+buffer;
  int row4 = row3+size+buffer;
  int row5 = row4+size+buffer;
  int row6 = row5+size+buffer;
  int row7 = row6+size+buffer;
  int row8 = row7+size+buffer;
  int row9 = row8+size+buffer;
  int row10 = row9+size+buffer;
  int row11 = row10+size+buffer;
  int row12 = row11+size+buffer;
  int row13 = row12+size+buffer;
  int row14 = row13+size+buffer;
  int row15 = row14+size+buffer;
  int row16 = row15+size+buffer;
  int row17 = row16+size+buffer;
  
 
   
  this.getElementByNumber(1).updateRenderPos(col1, row1);
  this.getElementByNumber(5).updateRenderPos(col1, row3);
  this.getElementByNumber(6).updateRenderPos(col1, row5);
  this.getElementByNumber(7).updateRenderPos(col1, row2);
  //this.getElementByNumber(8).updateRenderPos(col4, row4);
  this.getElementByNumber(9).updateRenderPos(col2, row6);
  this.getElementByNumber(10).updateRenderPos(col6, row6);
  this.getElementByNumber(11).updateRenderPos(col2, row7);
  this.getElementByNumber(12).updateRenderPos(col6, row7);
  this.getElementByNumber(13).updateRenderPos(col2, row8);
  this.getElementByNumber(14).updateRenderPos(col6, row8); 
   
  this.getElementByNumber(15).updateRenderPos( col2, row9);
  this.getElementByNumber(16).updateRenderPos(col6, row9);
  this.getElementByNumber(17).updateRenderPos(col2, row10);
  this.getElementByNumber(18).updateRenderPos(col6, row10);
  this.getElementByNumber(19).updateRenderPos(col2, row11);
  this.getElementByNumber(20).updateRenderPos(col6, row11);
  
  
  this.getElementByNumber(21).updateRenderPos(col2, row12);
  this.getElementByNumber(22).updateRenderPos(col6, row12);
  this.getElementByNumber(23).updateRenderPos(col2, row13);
  this.getElementByNumber(24).updateRenderPos(col6, row13);
  this.getElementByNumber(25).updateRenderPos(col2, row14);
  this.getElementByNumber(26).updateRenderPos(col6, row14);  
 
  this.getElementByNumber(27).updateRenderPos(col2, row15);
  this.getElementByNumber(28).updateRenderPos(col6, row15);
  this.getElementByNumber(29).updateRenderPos(col2, row16);
  this.getElementByNumber(30).updateRenderPos(col6, row16);
  this.getElementByNumber(31).updateRenderPos(col2, row17);
  this.getElementByNumber(32).updateRenderPos(col6, row17);
  this.getElementByNumber(33).updateRenderPos(col1, row4);
  this.getElementByNumber(105).updateRenderPos(col4, row4);
  this.getElementByNumber(34).updateRenderPos(col4, row3);
  this.getElementByNumber(35).updateRenderPos(col3, row6);
  this.getElementByNumber(36).updateRenderPos(col3, row7);
  this.getElementByNumber(37).updateRenderPos(col3, row8);
  this.getElementByNumber(38).updateRenderPos(col3, row9);
  this.getElementByNumber(39).updateRenderPos(col3, row10);
  this.getElementByNumber(40).updateRenderPos(col3, row11);
  this.getElementByNumber(41).updateRenderPos(col3, row12);
  this.getElementByNumber(42).updateRenderPos(col3, row13);
  this.getElementByNumber(43).updateRenderPos(col3, row14);
  this.getElementByNumber(44).updateRenderPos(col3, row15);
  this.getElementByNumber(45).updateRenderPos(col3, row16);
  this.getElementByNumber(46).updateRenderPos(col3, row17);
      
  if(this.currentPart!=null)
    {
    this.piecePosFields[0].setValue(currentPart.x());
    this.piecePosFields[1].setValue(currentPart.y());
    this.piecePosFields[2].setValue(currentPart.z());
    this.pieceRotFields[0].setValue(Trig.toDegrees(this.currentPart.rx()));
    this.pieceRotFields[1].setValue(Trig.toDegrees(this.currentPart.ry()));
    this.pieceRotFields[2].setValue(Trig.toDegrees(this.currentPart.rz()));    
    }
  else
    {
    this.piecePosFields[0].setValue(0);
    this.piecePosFields[1].setValue(0);
    this.piecePosFields[2].setValue(0);
    this.pieceRotFields[0].setValue(0);
    this.pieceRotFields[1].setValue(0);
    this.pieceRotFields[2].setValue(0);
    }
  if(this.currentBox!=null)
    {
    this.boxPosFields[0].setValue(this.currentBox.x1);
    this.boxPosFields[1].setValue(this.currentBox.y1);
    this.boxPosFields[2].setValue(this.currentBox.z1);
    this.boxSizeFields[0].setValue(this.currentBox.x2 - this.currentBox.x1);
    this.boxSizeFields[1].setValue(this.currentBox.y2 - this.currentBox.y1);
    this.boxSizeFields[2].setValue(this.currentBox.z2 - this.currentBox.z1);
    }
  else
    {
    this.boxPosFields[0].setValue(0);
    this.boxPosFields[1].setValue(0);
    this.boxPosFields[2].setValue(0);
    this.boxSizeFields[0].setValue(0);
    this.boxSizeFields[1].setValue(0);
    this.boxSizeFields[2].setValue(0);
    }  
  if(this.currentPart!=null)
    {
    this.partNameField.setText(this.currentPart.getName());
    }
  else
    {
    this.partNameField.setText("");
    }
  }

}
