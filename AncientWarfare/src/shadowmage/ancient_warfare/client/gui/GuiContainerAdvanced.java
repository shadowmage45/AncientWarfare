package shadowmage.ancient_warfare.client.gui;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiElement;
import shadowmage.ancient_warfare.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollBarSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElementCallback;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerBase;
import shadowmage.ancient_warfare.common.interfaces.IContainerGuiCallback;


public abstract class GuiContainerAdvanced extends GuiContainer implements IContainerGuiCallback, IGuiElementCallback
{

protected final EntityPlayer player;
public DecimalFormat formatter = new DecimalFormat("#");
public DecimalFormat formatterOneDec = new DecimalFormat("#.#");
public DecimalFormat formatterTwoDec = new DecimalFormat("#.##");
public DecimalFormat formatterThreeDec = new DecimalFormat("#.###");

protected boolean forceUpdate = false;
private boolean shouldInit = true;
protected int mouseX;
protected int mouseY;

protected boolean shouldCloseOnVanillaKeys = false;

protected int tooltipDelayTicks = 10;

/**
 * gui controls...these are substitutes for the vanilla controlList...and allow for total control
 * over buttons and functions, while only overridding a minimal amount of vanilla code (and still allowing
 * for full use of the vanilla slot rendering and interface)
 */
protected HashMap<Integer, GuiElement> guiElements = new HashMap<Integer, GuiElement>();

public GuiContainerAdvanced(Container container)
  {
  super(container);
  this.player = Minecraft.getMinecraft().thePlayer;
  this.xSize = this.getXSize();
  this.ySize = this.getYSize();
  guiLeft = (this.width - this.xSize) / 2;
  guiTop = (this.height - this.ySize) / 2;  
  if(container instanceof ContainerBase)
    {
    ((ContainerBase)container).setGui(this);
    }  
  }

public void drawStringGui(String string, int x, int y, int color)
  {
  this.drawString(fontRenderer, string, guiLeft+x, guiTop+y, color);
  }

protected GuiElement currentMouseElement;

public boolean isMouseOverControl(int mouseX, int mouseY)
  {  
  for(Integer i : this.guiElements.keySet())
    {
    GuiElement e = this.guiElements.get(i);
    if(e.isMouseOver(mouseX, mouseY))
      {         
      return true;
      }
    }  
  return false;
  }

@Override
public void onElementReleased(IGuiElement element)
  {  
  }

@Override
public void onElementDragged(IGuiElement element)
  {  
  }

@Override
public void onElementMouseWheel(IGuiElement element, int amt)
  {
  }

@Override
public void onElementKeyTyped(char ch, int keyNum)
  {
  }

@Override
protected void keyTyped(char par1, int par2)
  {  
  for(Integer i : this.guiElements.keySet())
    {
    GuiElement el = this.guiElements.get(i);
    el.onKeyTyped(par1, par2);
    }
  if(this.shouldCloseOnVanillaKeys)
    {
    super.keyTyped(par1, par2);
    }
  else
    {
    boolean callSuper = true;
    if(par2 == this.mc.gameSettings.keyBindInventory.keyCode || par2 == Keyboard.KEY_ESCAPE)
      {
      callSuper = false;
      }
    if(callSuper)
      {
      super.keyTyped(par1, par2);
      } 
    }
  }

@Override
public void handleDataFromContainer(NBTTagCompound tag)
  {
  //REGULARLY NO-OP, MUST OVERRIDE
  }

public void sendDataToServer(NBTTagCompound tag)
  {
  if(this.inventorySlots instanceof ContainerBase)
    {
    ((ContainerBase)this.inventorySlots).sendDataToServer(tag);
    }
  else
    {
    Config.logError("Attempt to send data to server container from improperly setup GUI/Container.");
    Exception e = new IllegalAccessException();
    e.printStackTrace();
    }
  }

/**
 * get the width of the GUI in pixels, max of 256
 * @return
 */
public abstract int getXSize();

/**
 * get the height of the GUI in pixels, max of 240 for visibility reasons
 * @return
 */
public abstract int getYSize();

/**
 * get the string filepath/name of the texture to be rendered as a background for this GUI
 * @return
 */
public abstract String getGuiBackGroundTexture();

/**
 * called to render any background layer effects (pretty much everything)
 * called every RENDER tick
 * @param mouseX
 * @param mouseY
 * @param partialTime
 */
public abstract void renderExtraBackGround(int mouseX, int mouseY, float partialTime);

/**
 * called every game-tick, to update screen contents if it has changed/can change
 */
public abstract void updateScreenContents();

/**
 * called on construction to add gui control elements
 */
public abstract void setupControls();

/**
 * called fron updateScreenContents if the boolean flag forceUpdate==true
 */
public abstract void updateControls();

public int getGuiLeft()
  {
  return this.guiLeft;
  }

public int getGuiTop()
  {
  return this.guiTop;
  }

/**
 * add a button with adjusted position relative to GUI topLeft corner
 * @param id
 * @param x
 * @param y
 * @param width
 * @param wid
 * @param name
 * @return
 */
public GuiButtonSimple addGuiButton(int id, int x, int y, int width, int high, String name)
  {
  GuiButtonSimple button = this.addGuiButton(id, width, high, name);
  button.updateRenderPos(x, y);
  return button;
  }

public GuiButtonSimple addGuiButton(int id, int width, int high, String name)
  {
  GuiButtonSimple button = new GuiButtonSimple(id, this, width, high, name);
  this.guiElements.put(id, button);
  return button;
  }

public GuiCheckBoxSimple addCheckBox(int id, int x, int y, int len, int high)
  {
  GuiCheckBoxSimple box = this.addCheckBox(id, len, high);
  box.updateRenderPos(x, y);
  return box;
  }

public GuiCheckBoxSimple addCheckBox(int id, int len, int high)
  {
  GuiCheckBoxSimple box = new GuiCheckBoxSimple(id, this, len, high);
  this.guiElements.put(id, box);
  return box;
  }

/**
 * add an advanced textField box, includes callback methods for when text changes...
 * @param id
 * @param x
 * @param y
 * @param width
 * @param height
 * @param maxChars
 * @param initText
 * @return
 */
public GuiTextInputLine addTextField(int id, int x, int y, int width, int height, int maxChars, String initText)
  {
  GuiTextInputLine box = this.addTextField(id, width, height, maxChars, initText);
  box.updateRenderPos(x, y);
  return box;
  }

public GuiTextInputLine addTextField(int id, int width, int height, int maxChars, String initText)
  {
  GuiTextInputLine box = new GuiTextInputLine(id, this, width, height, maxChars, initText);
  this.guiElements.put(id, box);
  return box;
  }

public GuiNumberInputLine addNumberField(int id, int width, int height, int maxChars, String initText)
  {
  GuiNumberInputLine box = new GuiNumberInputLine(id, this, width, height, maxChars, initText);
  this.guiElements.put(id, box);
  return box;
  }

public GuiScrollBarSimple addScrollBarSimple(int id, int width, int height, int setSize, int displayedElements)
  {
  GuiScrollBarSimple bar = new GuiScrollBarSimple(id, this, width, height, setSize, displayedElements);
  this.guiElements.put(id, bar);
  return bar;
  }

@Override
public void initGui()
  { 
  super.initGui();  
  if(this.shouldInit)
    {
    this.setupControls();
    this.shouldInit = false;
    }  
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).updateGuiPos(guiLeft, guiTop);
    }  
  this.updateControls();
  }

@Override
public void updateScreen()
  {
  super.updateScreen();
  guiLeft = (this.width - this.xSize) / 2;
  guiTop = (this.height - this.ySize) / 2;
  
  if(this.forceUpdate)
    {
    this.updateControls();
    this.forceUpdate = false;
    }
  this.updateScreenContents();
  
  if(this.currentMouseElement!=null && this.tooltipDelayTicks>0)
    {
    this.tooltipDelayTicks--;
    } 
    
  GuiElement e;
  GuiElement foundE = null;
  for(Integer i : this.guiElements.keySet())
    {
    e = this.guiElements.get(i);
    if(e.wasMouseOver())
      {
      foundE = e; 
      break;         
      }
    }  
  if(foundE!=null)
    {
    if(foundE!=currentMouseElement)
      {
      this.tooltipDelayTicks = 10;        
      this.currentMouseElement = foundE;
      }
    }
  else
    {
    this.currentMouseElement = null;
    }  
  }

@Override
protected void drawGuiContainerBackgroundLayer(float var1, int mouseX, int mouseY)
  { 
  String tex = this.getGuiBackGroundTexture();
  if(tex!=null)
    {
    this.drawQuadedTexture(guiLeft, guiTop, this.xSize, this.ySize, 256, 240, tex, 0, 0);
    }
  if(this.inventorySlots.inventorySlots.size()>0)
    {
    tex = Config.texturePath+"gui/guiButtons.png";
    this.mc.renderEngine.bindTexture(tex);
    for(Object ob : this.inventorySlots.inventorySlots)    
      {      
      Slot slot = (Slot)ob;
      this.drawTexturedModalRect(slot.xDisplayPosition-1+guiLeft, slot.yDisplayPosition-1+guiTop, 152, 120, 18, 18);      
      }    
    }  
  GL11.glPushMatrix();
  this.renderExtraBackGround(mouseX, mouseY, var1);  
  GL11.glDisable(GL12.GL_RESCALE_NORMAL);
  RenderHelper.disableStandardItemLighting();
  GL11.glDisable(GL11.GL_LIGHTING);
  GL11.glDisable(GL11.GL_DEPTH_TEST);
  for(Integer i : this.guiElements.keySet())
    {
    this.guiElements.get(i).drawElement(mouseX, mouseY);
    } 
  if(this.tooltipDelayTicks<=0 && this.currentMouseElement!=null && this.currentMouseElement.renderTooltip)
    {
    Config.logDebug("rendering tooltip");
    this.renderTooltip(mouseX, mouseY, this.currentMouseElement.getTooltip());
    }
  GL11.glPopMatrix();
  }

/**
 * render an itemStack with tooltip and opt. qty/dmg overlay, at the specified x,y
 * @param x
 * @param y
 */
public void renderItemStack(ItemStack stack, int x, int y, int mouseX, int mouseY, boolean renderOverlay)
  {
  GL11.glPushMatrix();
  RenderHelper.enableGUIStandardItemLighting();
  GL11.glDisable(GL11.GL_LIGHTING);
  GL11.glEnable(GL12.GL_RESCALE_NORMAL);
  GL11.glEnable(GL11.GL_COLOR_MATERIAL);
  GL11.glEnable(GL11.GL_LIGHTING);  
  itemRenderer.zLevel = 100.0F; 
  this.itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, stack, x, y);//render item
  if(renderOverlay)
    {
    this.itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, stack, x, y);
    }
  itemRenderer.zLevel = 0.0F;
  GL11.glDisable(GL11.GL_LIGHTING);
  if(this.isMouseInRawArea(x, y, 16, 16, mouseX, mouseY))
    {
    this.drawItemStackTooltip(stack, x, y);
    }   
  GL11.glPopMatrix();

  GL11.glEnable(GL11.GL_DEPTH_TEST);
  }

/**
 * render the given list of strings as a tooltip at the given mouse x,y coordinates
 * @param x
 * @param y
 * @param info
 */
protected void renderTooltip(int x, int y, List<String> info)
  {
  if(info==null || info.isEmpty())
    {
    return;
    }
  GL11.glDisable(GL12.GL_RESCALE_NORMAL);
  RenderHelper.disableStandardItemLighting();
  GL11.glDisable(GL11.GL_LIGHTING);
  GL11.glDisable(GL11.GL_DEPTH_TEST);
  Iterator<String> it = info.iterator();
  String line;
  if(!it.hasNext())
    {
    return;
    }  
  int widestLength = 0;
  int testLength;
  while(it.hasNext())
    {
    int lineLen;
    line = it.next();
    if (this.fontRenderer.getStringWidth(line) > widestLength)
      {
      widestLength = this.fontRenderer.getStringWidth(line);
      }
    }  
  int renderX = x + 12;
  int renderY = y - 12;
  int borderSize = 8;
  if (info.size() > 1)
    {
    borderSize += 2 + (info.size() - 1) * 10;
    }
  if (this.guiTop + renderY + borderSize + 6 > this.height)
    {
    renderY = this.height - borderSize - this.guiTop - 6;
    }
  this.zLevel = 300.0F;
  itemRenderer.zLevel = 300.0F;
  int renderColor = -267386864;
  this.drawGradientRect(renderX - 3, renderY - 4, renderX + widestLength + 3, renderY - 3, renderColor, renderColor);
  this.drawGradientRect(renderX - 3, renderY + borderSize + 3, renderX + widestLength + 3, renderY + borderSize + 4, renderColor, renderColor);
  this.drawGradientRect(renderX - 3, renderY - 3, renderX + widestLength + 3, renderY + borderSize + 3, renderColor, renderColor);
  this.drawGradientRect(renderX - 4, renderY - 3, renderX - 3, renderY + borderSize + 3, renderColor, renderColor);
  this.drawGradientRect(renderX + widestLength + 3, renderY - 3, renderX + widestLength + 4, renderY + borderSize + 3, renderColor, renderColor);
  int var11 = 1347420415;
  int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
  this.drawGradientRect(renderX - 3, renderY - 3 + 1, renderX - 3 + 1, renderY + borderSize + 3 - 1, var11, var12);
  this.drawGradientRect(renderX + widestLength + 2, renderY - 3 + 1, renderX + widestLength + 3, renderY + borderSize + 3 - 1, var11, var12);
  this.drawGradientRect(renderX - 3, renderY - 3, renderX + widestLength + 3, renderY - 3 + 1, var11, var11);
  this.drawGradientRect(renderX - 3, renderY + borderSize + 2, renderX + widestLength + 3, renderY + borderSize + 3, var12, var12);
  /**
   * re-grab a fresh iterator
   */
  it = info.iterator();
  boolean firstPass = true;
  while(it.hasNext())
    {
    line = it.next();
    line = "\u00a77" + line;
    this.fontRenderer.drawStringWithShadow(line, renderX, renderY, -1);
    if (firstPass)
      {
      renderY += 2;
      firstPass = false;
      }
    renderY += 10;       
    }
  this.zLevel = 0.0F;
  itemRenderer.zLevel = 0.0F;
  }

/**
 * is mouse over slot (or designated area)?
 */
protected boolean isMouseInRawArea(int slotX, int slotY, int slotWidth, int slotHeight, int mouseX, int mouseY)
  {
  return mouseX >= slotX - 1 && mouseX < slotX + slotWidth + 1 && mouseY >= slotY - 1 && mouseY < slotY + slotHeight + 1;
  }

/**
 * is mouse over slot (or designated area)?
 */
protected boolean isMouseInAdjustedArea(int slotX, int slotY, int slotWidth, int slotHeight, int mouseX, int mouseY)
  {
  mouseX-=this.guiLeft;
  mouseY-=this.guiTop;
  return mouseX >= slotX - 1 && mouseX < slotX + slotWidth + 1 && mouseY >= slotY - 1 && mouseY < slotY + slotHeight + 1;
  }

/**
 * render a 50px X 10px status bar, at the X,Y location relative to the topleft of the actual GUI
 * @param x
 * @param y
 * @param length
 */
public void render50pxStatusBar(int x, int y, int length)
  { 
  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  this.mc.renderEngine.bindTexture("/Catapult_Mod/gui/statusBar.png");
  this.drawTexturedModalRect(guiLeft+x, guiTop+y, 0, 0, length, 10);
  }

/**
 * method to render an entity onto screen, must be called as part of the background, or some crappy lighting will interfere with it from vanilla itemrendering code
 * 
 * @param mc
 * @param xTrans
 * @param yTrans
 * @param scaleFactor
 * @param cursorPosX
 * @param cursorPosY
 * @param entity
 */
public void renderEntityIntoInventory(Minecraft mc, int xTrans, int yTrans, int scaleFactor, Entity entity, float rotationFactor)
  {  
  GL11.glEnable(GL11.GL_COLOR_MATERIAL);
  GL11.glPushMatrix();  
  GL11.glTranslatef((float)xTrans, (float)yTrans, 50.0F);
  GL11.glScalef((float)(-scaleFactor), (float)scaleFactor, (float)scaleFactor);
  GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
  GL11.glRotatef(-rotationFactor+entity.rotationYaw, 0.0F, 1.0F, 0.0F);
  GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
  GL11.glTranslatef(0.0F, mc.thePlayer.yOffset, 0.0F);
  RenderManager.instance.playerViewY = 180.0F;
  RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);  
  GL11.glPopMatrix();  
  RenderHelper.disableStandardItemLighting();
  GL11.glDisable(GL12.GL_RESCALE_NORMAL);
  OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
  GL11.glDisable(GL11.GL_TEXTURE_2D);
  OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }

/**
 * render a living entity into a container GUI, must be called as part of the background, or some crappy lighting will interfere with it from vanilla itemrendering code
 * @param par0Minecraft
 * @param living
 * @param xTrans
 * @param yTrans
 * @param scaleFactor
 * @param cursorPosX
 * @param cursorPosY
 */
public void renderEntityLivingIntoInventory(Minecraft par0Minecraft, EntityLiving living, int xTrans, int yTrans, int scaleFactor, float cursorPosX, float cursorPosY)
  {
  GL11.glEnable(GL11.GL_COLOR_MATERIAL);
  GL11.glPushMatrix();
  GL11.glTranslatef((float)xTrans, (float)yTrans, 50.0F);
  GL11.glScalef((float)(-scaleFactor), (float)scaleFactor, (float)scaleFactor);
  GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
  float var6 = living.renderYawOffset;
  float var7 = living.rotationYaw;
  float var8 = living.rotationPitch;
  GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
  RenderHelper.enableStandardItemLighting();
  GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
  GL11.glRotatef(-((float)Math.atan((double)(cursorPosY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
  living.renderYawOffset = (float)Math.atan((double)(cursorPosX / 40.0F)) * 20.0F;
  living.rotationYaw = (float)Math.atan((double)(cursorPosX / 40.0F)) * 40.0F;
  living.rotationPitch = -((float)Math.atan((double)(cursorPosY / 40.0F))) * 20.0F;
  living.rotationYawHead = living.rotationYaw;
  GL11.glTranslatef(0.0F, par0Minecraft.thePlayer.yOffset, 0.0F);
  RenderManager.instance.playerViewY = 180.0F;
  RenderManager.instance.renderEntityWithPosYaw(living, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
  living.renderYawOffset = var6;
  living.rotationYaw = var7;
  living.rotationPitch = var8;
  GL11.glPopMatrix();
  RenderHelper.disableStandardItemLighting();
  GL11.glDisable(GL12.GL_RESCALE_NORMAL);
  OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
  GL11.glDisable(GL11.GL_TEXTURE_2D);
  OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }

public void closeGUI()
  {
  this.player.closeScreen();
  }

@Override
public void handleMouseInput()
  {  
  int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
  int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
  this.mouseX = mouseX;
  this.mouseY = mouseY;
  int wheel = Mouse.getEventDWheel();
  wheel = wheel > 0 ? 1 : wheel < 0 ? -1 : 0;  
  int buttonNum = Mouse.getEventButton();
  boolean buttonActivated = buttonNum != -1;
  boolean buttonPressed = Mouse.getEventButtonState();
  boolean wheelActivated = wheel!= 0;
  
  if(buttonActivated && buttonPressed)
    {
    Config.logDebug("button pressed");
    }
  GuiElement el;  
  for(Integer i : this.guiElements.keySet())
    {
    el = this.guiElements.get(i);
    if(buttonActivated && buttonPressed)
      {      
      el.onMousePressed(mouseX, mouseY, buttonNum);
      }
    else if(buttonActivated)
      {
      el.onMouseReleased(mouseX, mouseY, buttonNum);
      }
    else if(wheelActivated)
      {
      el.onMouseWheel(mouseX, mouseY, wheel);
      }
    else
      {
      el.onMouseMoved(mouseX, mouseY, buttonNum);
      }
    
    } 
  super.handleMouseInput();
  }

/**
 * @return
 */
public FontRenderer getFontRenderer()
  {
  return this.fontRenderer;
  }

public GuiElement getElementByNumber(int num)
  {
  return this.guiElements.get(num);
  }

/**
 * renders the four corners of a texture, from the corner inward (e.g. for size-adaptable elements)
 * @param x renderPosX
 * @param y renderPosY
 * @param w renderWidth
 * @param h renderHeight
 * @param tw textureUsedWidth
 * @param th textureUsedHeight
 * @param tex theTexture
 * @param u textureStartX
 * @param v textureStartY
 */
protected void drawQuadedTexture(int x, int y, int w, int h, int tw, int th, String tex, int u, int v)
  {  
  int halfW = w/2;
  int halfH = h/2;  
  int u1 = u + tw - halfW;
  int v1 = v + th - halfH;
  Minecraft.getMinecraft().renderEngine.bindTexture(tex);
//  GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(tex));
  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  this.drawTexturedModalRect(x, y, u, v, halfW, halfH);
  this.drawTexturedModalRect(x + halfW, y, u1, v, halfW, halfH);
  this.drawTexturedModalRect(x, y + halfH, u, v1, halfW, halfH);
  this.drawTexturedModalRect(x + halfW, y + halfH, u1, v1, halfW, halfH);
  }

}
