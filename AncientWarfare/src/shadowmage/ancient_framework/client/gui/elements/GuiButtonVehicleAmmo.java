/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_framework.client.gui.elements;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_warfare.client.render.AWTextureManager;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;

public class GuiButtonVehicleAmmo extends GuiButtonSimple
{

IAmmoType ammo;
VehicleBase vehicle;
protected static RenderItem itemRenderer = new RenderItem();

/**
 * @param elementNum
 * @param parent
 * @param w
 * @param h
 * @param name
 */
public GuiButtonVehicleAmmo(int elementNum, IGuiElementCallback parent, int x, int y, int w, IAmmoType ammo, VehicleBase vehicle)
  {
  super(elementNum, parent, w, 20, "");
  this.ammo = ammo;
  this.vehicle = vehicle;
  if(ammo!=null)
    {
    this.displayString = StatCollector.translateToLocal(ammo.getDisplayName());
    }
  this.updateRenderPos(x, y);
  this.displayString =  this.displayString.length() > 30 ? this.displayString.substring(0, 30) : this.displayString;
  }
  
@Override
public void drawElement(int mouseX, int mouseY)
  {
  if(!this.hidden)
    {
    int texOffset = this.getHoverState();
    int vOffset = texOffset * 40;//will return 0, 40, or 80..for inactive, active, hover, apply to Y offset in UV rendering
    int guiLeftOffset = this.renderWithGuiOffset ? this.guiLeft : 0;
    int guiTopOffset = this.renderWithGuiOffset ? this.guiTop : 0;    
    String tex = Config.texturePath+"gui/guiButtons.png";
    this.drawQuadedTexture(guiLeftOffset+renderPosX, guiTopOffset+renderPosY, width, height, 256, 40, tex, 0, vOffset);
    
    //draw ammo icon on left
    //draw ammo name to the right of that
    //on the far right, ammo qty
    int qty = 0;
    if(this.ammo!=null)
      {
      AWTextureManager.bindTexture("/gui/items.png");
      qty = vehicle.ammoHelper.getCountOf(ammo);
      this.renderItemIcon(guiLeftOffset+2+this.renderPosX, guiTopOffset+2+this.renderPosY, ammo.getDisplayIcon(), 16, 16);
      }
    String qtyString = String.valueOf(qty);
    int fontColor = 14737632;
    if(!this.enabled)
      {
      fontColor = -6250336;
      }
    else if(this.isMouseOver)
      {
      fontColor = 16777120;
      }
    int stringX = guiLeftOffset + this.renderPosX + 22;
    int stringY = guiTopOffset + this.renderPosY + 6; 
    int string2X = guiLeftOffset + this.renderPosX + this.width - 10 - mc.fontRenderer.getStringWidth(qtyString);
    this.drawString(mc.fontRenderer, this.displayString, stringX, stringY, fontColor);
    this.drawString(mc.fontRenderer, qtyString, string2X, stringY, fontColor);
    }  
  }

public void renderItemIcon(int x, int y, Icon icon, int xSize, int ySize)
  {
  itemRenderer.renderIcon(x, y, icon, 16, 16);//x,y,icon,xSize,ySize  
  }  

}
