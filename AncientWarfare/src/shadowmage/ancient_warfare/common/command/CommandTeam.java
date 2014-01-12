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

import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.utils.StringTools;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandTeam extends CommandBase
{

public CommandTeam()
  {

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
public void processCommand(ICommandSender icommandsender, String[] astring)
  {
  if(astring.length<=0){throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);}
  String command = astring [0];
  if(command.equals("list"))
    {
    EntityPlayer player = getCommandSenderAsPlayer(icommandsender);
    listCurrentTeam(player);
    }
  else if(command.equals("listteam"))
    {
    if(astring.length<=1){throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);}
    EntityPlayer player = getCommandSenderAsPlayer(icommandsender);
    int teamNum = StringTools.safeParseInt(astring[1]);
    listTeam(player, teamNum);
    }
  else if(command.equals("setteam"))
    {
    if(astring.length<=2){throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);}
    String name = astring[1];
    int teamNum = StringTools.safeParseInt(astring[2]);
    setTeam(name, teamNum);
    if (icommandsender instanceof EntityPlayerMP)
      {
      //add chat message 
      }
    else
      {
      //notify admins
      }
    }
  else if(command.equals("setrank"))
    {
    if(astring.length<=2){throw new WrongUsageException(getCommandUsage(icommandsender), new Object[0]);}
    String name = astring[1];
    int teamNum = StringTools.safeParseInt(astring[2]);
    setRank(name, teamNum);
    if(icommandsender instanceof EntityPlayerMP)
      {
      //add chat message 
      }
    else
      {
      //notify admins
      }
    }    
  }

private void listCurrentTeam(EntityPlayer player)
  {
  //TODO
  }

private void listTeam(EntityPlayer player, int teamNum)
  {
  //TODO
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
