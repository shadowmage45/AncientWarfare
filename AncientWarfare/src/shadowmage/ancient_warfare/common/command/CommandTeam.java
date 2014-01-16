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
package shadowmage.ancient_warfare.common.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry.TeamMemberEntry;
import shadowmage.ancient_warfare.common.utils.StringTools;

public class CommandTeam extends CommandBase
{

public CommandTeam()
  {

  }

@Override
public int getRequiredPermissionLevel()
  {
  return 2;
  }

@Override
public String getCommandName()
  {
  return "awteam";
  }

@Override
public String getCommandUsage(ICommandSender icommandsender)
  {
  return "commands.awteam.usage";
  }

@Override
public boolean isUsernameIndex(String[] str, int par2)
  {
  if(str[0].equals("list")){return false;}
  if(str[0].equals("set")){return par2==2;}
  if(str[0].equals("setrank")){return par2==1;}
  return false;
  }

@Override
public List addTabCompletionOptions(ICommandSender cmd, String[] str)
  {
  if(str.length==1)
    {
    return getListOfStringsMatchingLastWord(str, new String[] {"set", "setrank", "list"});
    }
  else if(str.length==2)
    {
    if(str[0].equals("set"))
      {
      return null;//would match vs team numbers
      }
    else if(str[0].equals("setrank"))
      {
      return getListOfStringsMatchingLastWord(str, this.getListOfPlayerUsernames());
      }
    else if(str[0].equals("list"))
      {
      return null;//would match vs team numbers
      }    
    }
  else if(str.length==3)
    {
    if(str[0].equals("set"))
      {
      return getListOfStringsMatchingLastWord(str, this.getListOfPlayerUsernames());
      }
    else
      {
      return null;//setrank, would match vs team numbers
      }
    }
  return null;
  }

protected String[] getListOfPlayerUsernames()
  {
  return MinecraftServer.getServer().getAllUsernames();
  }

@Override
public void processCommand(ICommandSender icommandsender, String[] astring)
  {
  Config.logDebug("processing team command..."+icommandsender);
  if(astring.length<=0){throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);}
  String command = astring [0];
  if(command.equals("list"))
    {
    if (icommandsender instanceof EntityPlayerMP)
      {
      EntityPlayer player = getCommandSenderAsPlayer(icommandsender);
      if(astring.length>=2)
        {
        int teamNum = StringTools.safeParseInt(astring[1]);
        listTeam(player, teamNum);
        }
      else
        {        
        listCurrentTeam(player);
        }
      } 
    }
  else if(command.equals("set"))
    {
    if(astring.length<=2){throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);}    
    int teamNum = StringTools.safeParseInt(astring[1]);
    String name = astring[2];
    setTeam(name, teamNum);
    if (icommandsender instanceof EntityPlayerMP)
      {
      EntityPlayer player = (EntityPlayer)icommandsender;
      player.addChatMessage("Set: "+name+ " to team: "+teamNum + " at rank 0");
      }
    else
      {
      notifyAdmins("Set: "+name+ " to team: "+teamNum + " at rank 0" + " via remote command");
      }
    }
  else if(command.equals("setrank"))
    {
    if(astring.length<=2){throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);}
    String name = astring[1];
    int newRank = StringTools.safeParseInt(astring[2]);    
    setRank(name, newRank);
    if(icommandsender instanceof EntityPlayerMP)
      {
      EntityPlayer player = (EntityPlayer)icommandsender;
      player.addChatMessage("Set: "+name+ " to rank: "+newRank);
      }
    else
      {
      notifyAdmins("Set: "+name+ " to rank: "+newRank + " via remote command");
      }
    }    
  }

private void notifyAdmins(String message)
  {
  EntityPlayer player;
  for(Object playerObj : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
    {
    player = (EntityPlayer)playerObj;
    if(AWCore.instance.proxy.isPlayerOp(player))
      {
      player.addChatMessage(message);
      }
    }
  }

private void listCurrentTeam(EntityPlayer player)
  {
  TeamEntry entry = TeamTracker.instance().getTeamEntryFor(player);
  player.addChatMessage("Team: "+entry.teamNum+" has "+entry.members.size()+" members: ");
  for(TeamMemberEntry member : entry.members)
    {
    player.addChatMessage(member.getMemberName());
    }
  }

private void listTeam(EntityPlayer player, int teamNum)
  {
  TeamEntry entry = TeamTracker.instance().getTeamEntryFor(player.worldObj, teamNum);
  player.addChatMessage("Team: "+entry.teamNum+" has "+entry.members.size()+" members: ");
  for(TeamMemberEntry member : entry.members)
    {
    player.addChatMessage(member.getMemberName());
    }
  }

private void setTeam(String playerName, int teamNum)
  {
  TeamTracker.instance().setPlayerTeam(playerName, teamNum);
  }

private void setRank(String playerName, int newRank)
  {
  TeamTracker.instance().setPlayerRank(playerName, newRank);
  }

}
