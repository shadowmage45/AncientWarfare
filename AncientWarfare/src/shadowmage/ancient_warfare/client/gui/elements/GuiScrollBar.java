package shadowmage.ancient_warfare.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;


public class GuiScrollBar extends Gui
{

int drawPosX;
int drawPosY;
int drawWidth;
int drawHeight;

final int buffer = 5;
/**
 * height of the handle-button/barthingie, in pixels...
 */
int handleHeight;

/**
 * current position of the scroll bar
 */
int handleTop;

/**
 * used to calc delta of movement
 */
int mouseDownY;
int mouseUpY;
int buttonNum;
boolean wasMousePressed = false;

private Minecraft mc;


public GuiScrollBar(int xPos, int yPos, int width, int height, int setSize, int displaySize)
  {
  this.drawPosX = xPos;
  this.drawPosY = yPos;
  this.drawWidth = width;
  this.drawHeight = height;
  this.mc = Minecraft.getMinecraft();  
  this.updateHandleHeight(setSize, displaySize);
  this.updateHandleDisplayPos(0);
  }

/**
 * used on-screen init to reset the renderPos of this guiElement
 * @param newX
 * @param newY
 */
public void updatePosition(int newX, int newY)
  {
  this.drawPosX = newX;
  this.drawPosY = newY;
  }

public void drawScrollBar()
  {  
  int halfHeight = this.drawHeight/2;
  int halfWidth = this.drawWidth/2;
     
  /**
   * the topLeft coords of the texture.. (goes below normal buttons, to the right of checkboxes)
   */
  int texULX = 80;
  int texULY = 120;
  
  int texUsedWidth = 40;
  int texUsedHeight = 128;
     
  int texRight = texULX + texUsedWidth;
  int texBottom = texULY + texUsedHeight;
  
  int texUMX = texRight - halfWidth;
  int texUMY = texULY;
  int texMLX = texULX;
  int texMLY = texBottom - halfHeight;
  int texMMX = texUMX;
  int texMMY = texMLY;
  
  /**
   * used to determine the active texture (highlighted or not)...
   */
  
  GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/shadowmage/ancient_warfare/resources/gui/guiButtons.png"));
  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   
  //this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, w, h);
  
  /**
   * draws the scroll bar vertical bar
   */
  this.drawTexturedModalRect(this.drawPosX, this.drawPosY, texULX, texULY, halfWidth, halfHeight);
  this.drawTexturedModalRect(this.drawPosX + halfWidth, this.drawPosY, texUMX, texUMY, halfWidth, halfHeight);
  this.drawTexturedModalRect(this.drawPosX, this.drawPosY + halfHeight, texMLX, texMLY, halfWidth, halfHeight);
  this.drawTexturedModalRect(this.drawPosX + halfWidth, this.drawPosY + halfHeight, texMMX, texMMY, halfWidth, halfHeight);
    
  
  halfWidth = (this.drawWidth-10)/2;
  halfHeight = (this.handleHeight)/2;
  texULX = 120;
  texULY = 120;
  
  texUsedWidth = 30;
  texUsedHeight = 128;
     
  texRight = texULX + texUsedWidth;
  texBottom = texULY + texUsedHeight;
  
  texUMX = texRight - halfWidth;
  texUMY = texULY;
  texMLX = texULX;
  texMLY = texBottom - halfHeight;
  texMMX = texUMX;
  texMMY = texMLY;
  
  int handleT = this.drawPosY + this.handleTop + 5;
  int handleM = this.drawPosY + this.handleTop + halfHeight + 5;
  
  this.drawTexturedModalRect(this.drawPosX + 5, handleT, texULX, texULY, halfWidth, halfHeight);
  this.drawTexturedModalRect(this.drawPosX + 5 + halfWidth, handleT, texUMX, texUMY, halfWidth, halfHeight);
  this.drawTexturedModalRect(this.drawPosX + 5, handleM, texMLX, texMLY, halfWidth, halfHeight);
  this.drawTexturedModalRect(this.drawPosX + 5 + halfWidth, handleM, texMMX, texMMY, halfWidth, halfHeight);   
  }

public boolean isMouseOver(int x, int y)
  {
  return x>= drawPosX+buffer && x< drawPosX+drawWidth+buffer && y>=drawPosY+this.handleTop+this.buffer && y < drawPosY+handleTop+handleHeight+buffer;
  }

/**
 * called from parent GUI onMouseClicked, only if
 * @param x
 * @param y
 * @param num
 */
public void onMousePressed(int x, int y, int num)
  {
  if(this.isMouseOver(x, y))
    {
    this.buttonNum = num;    
    this.mouseDownY = y;
    }
  }

public void onMouseMoved(int x, int y, int num)
  {
  if(this.buttonNum>=0)
    {    
    this.mouseUpY = y;
    int delta =  mouseUpY - mouseDownY;
    this.updateHandleDisplayPos(delta);
    this.mouseDownY = this.mouseUpY;
    }
  }

public void onMouseReleased(int x, int y, int num)
  {
  if(this.buttonNum>=0 && num == this.buttonNum)
    {    
    this.buttonNum = -1;
    this.mouseUpY = y;
    int delta =  mouseUpY - mouseDownY;
    this.updateHandleDisplayPos(delta);
    }
  }

/**
 * update the top (and bottom) displayPos of the the handle...
 */
private void updateHandleDisplayPos(int yDelta)
  {  
  this.handleTop += yDelta;
  if(this.handleTop<0)
    {
    this.handleTop = 0;
    }
  int lowestTopPosition = this.drawHeight-this.buffer*2-this.handleHeight;
  if(this.handleTop>lowestTopPosition)
    {
    this.handleTop = lowestTopPosition;
    }
  }

/**
 *  update the size of the handle, relative to the size of the underlying elementSet
 *  should be called BEFORE updateHandlePos, and before getTopIndex...
 */
private void updateHandleHeight(int setSize, int displayElements)
  {
  int availBarHeight = this.drawHeight - this.buffer*2 - 20;//20 is the minimum handle height...
  float elementPercent = (float)displayElements/(float)setSize;
  if(elementPercent>1)
    {
    elementPercent = 1;
    }
  float bar = (float)availBarHeight * (float)elementPercent;
  int barHeight = (int) (bar + 20);
  this.handleHeight = barHeight;
  }

/**
 * 
 * @param setSize total number of elements in the underlying set
 * @param displayElements how many elements are displayed on the screen at any one time
 * @param elementHeight the height in pixels of a single element
 * @return
 */
public int getTopIndexForSet(int setSize, int displayElements)
  {
  int validBarPixels = this.drawHeight - this.buffer*2 - this.handleHeight;
  int extraSetElements = setSize - displayElements;
  if(extraSetElements<=0)//the set is smaller than the display area
    {
    return 0;//element 0 is the first element viewed.
    }
  float pxPerElement = (float)validBarPixels / (float)extraSetElements;
  int element = (int) ((float)handleTop / (float)pxPerElement);
  return element;
  }

}
