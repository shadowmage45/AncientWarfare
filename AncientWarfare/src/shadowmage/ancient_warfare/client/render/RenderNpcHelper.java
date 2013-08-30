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
package shadowmage.ancient_warfare.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.StringTranslate;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.config.Settings;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;

public class RenderNpcHelper extends RenderBiped
{

/**
 * @param par1ModelBiped
 * @param par2
 */
public RenderNpcHelper(ModelBiped par1ModelBiped, float par2)
  {
  super(par1ModelBiped, par2);
  }

@Override
public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
  {
  super.doRenderLiving(par1EntityLiving, par2, par4, par6, par8, par9);
  if(Settings.getRenderNpcNameplates())
    {
    NpcBase npc = (NpcBase) par1EntityLiving;
    String displayLabel = StringTranslate.getInstance().translateKey(npc.npcType.getLevelName(npc.rank))+" "+npc.getHealth()+"/"+npc.getMaxHealth();
    this.renderLivingLabel(par1EntityLiving, displayLabel, par2, par4, par6, 64);
    }
  }

/**
 * shamelessly copied to enable color-differentiation per-entity
 */
@Override
protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double renderX, double renderY, double renderZ, int renderDistance)
  {
  double var10 = par1EntityLiving.getDistanceSqToEntity(this.renderManager.livingPlayer);  
  if (var10 <= (double)(renderDistance * renderDistance))
    {
    NpcBase npc = (NpcBase)par1EntityLiving;
    boolean hostile = TeamTracker.instance().isHostileTowards(npc.worldObj, npc.teamNum, TeamTracker.instance().getTeamForPlayer(Minecraft.getMinecraft().thePlayer));
    if(hostile && !Config.renderHostileNames)
      {
      return;
      }
    FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
    float var13 = 1.6F;
    float var14 = 0.016666668F * var13;
    GL11.glPushMatrix();
    GL11.glTranslatef((float)renderX, (float)renderY + 2.3F, (float)renderZ);
    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    GL11.glScalef(-var14, -var14, var14);
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDepthMask(false);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    Tessellator tessellator = Tessellator.instance;
    byte yOffset = 0;// Y offset
   
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    tessellator.startDrawingQuads();
    int xOffset = fontRenderer.getStringWidth(par2Str) / 2;// 
    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
    tessellator.addVertex((double)(-xOffset - 1), (double)(-1 + yOffset), 0.0D);
    tessellator.addVertex((double)(-xOffset - 1), (double)(8 + yOffset), 0.0D);
    tessellator.addVertex((double)(xOffset + 1), (double)(8 + yOffset), 0.0D);
    tessellator.addVertex((double)(xOffset + 1), (double)(-1 + yOffset), 0.0D);
    tessellator.draw();
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    int color = 553648127;
    int color2 = -1;
    if(hostile)
      {
      color = 0xffff0000;
      color2 = 0xffff0000;
      }
    fontRenderer.drawString(par2Str, -fontRenderer.getStringWidth(par2Str) / 2, yOffset, color);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDepthMask(true);
    fontRenderer.drawString(par2Str, -fontRenderer.getStringWidth(par2Str) / 2, yOffset, color2);//was-1
    if(!hostile && Settings.getRenderNpcObjectives())
      {
      renderObjectiveIcon(npc.getAIObjectiveID());
      renderTaskIcon(npc.getAITaskID());
//      renderErrorIcon(npc.getAIErrorID());
      }
    GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_BLEND);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPopMatrix();
    }
  }

public void renderObjectiveIcon(byte obj)
  {
  if(obj>=0)
    {
    RenderTools.renderIcon(getObjectiveTexture(obj), 16, 16, -10, -16);
    }  
  }

public void renderTaskIcon(byte task)
  {
  if(task>=0)
    {
    RenderTools.renderIcon(getTaskTexture(task), 16, 16, 10,-16);
    }
  }

public void renderErrorIcon(byte error)
  {
  if(error>=0)
    {
    RenderTools.renderIcon(Config.texturePath+"items/testIcon1.png", 16, 16,20,-16);
    }
  }

public String getObjectiveTexture(byte objective)
  {
  String base = Config.texturePath+"custom_icons/npc_status/";
  switch(objective)
  {
  case 0:
  return base + "objective_attack.png";//
  case 1:
  return base + "objective_attack_ranged.png";//
  case 2:
  return base + "objective_deposit.png";//
  case 3:
  return base + "objective_dismount.png";//
  case 4:
  return base + "objective_follow_player.png";//
  case 5:
  return base + "objective_work.png";//
  case 6:
  return base + "objective_mount.png";//
  case 7:
  return base + "objective_upkeep.png";//
  case 8:
  return base + "objective_patrol.png";//
  case 9:
  return base + "objective_shelter.png";//
  case 10:
  return base + "objective_home.png";//
  case 11:
  return base + "objective_wander.png";//
  case 12:
  return base + "objective_follow_commander.png";//
  case 13:
  return base + "objective_follow_commander.png";//
  case 14:
  return base + "objective_heal.png";//
  case 15:
  return base + "objective_repair_vehicle.png";//
  case 16:
  return base + "objective_courier.png";//
  case 17:
  return base + "objective_guard.png";//
  case 18:
  default:
  return base + "objective_home.png";
  }  
  }


public String getTaskTexture(byte task)
  {
  String base = Config.texturePath+"custom_icons/npc_status/";
  switch(task)
  {
  case 0:
  return base + "task_attack.png";//
  case 1:
  return base + "task_attack_ranged.png";//
  case 2:
  return base + "task_dismount.png";//
  case 3:
  return base + "task_work.png";//
  case 4:
  return base + "task_patrol.png";//
  case 5:
  return base + "task_mount.png";//
  case 6:
  return base + "task_move_to.png";//
  case 7:
  return base + "task_heal.png";//
  case 8:
  return base + "task_repair_vehicle.png";//
  case 9:
  return base + "task_courier.png";//
  case 10:
  default:
  return base + "objective_home.png";
  }  
  }
}
