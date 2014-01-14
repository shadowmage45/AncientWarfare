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
package shadowmage.ancient_warfare.common.tracker.entry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;

/**
 * dataStruct representing all data for a single team
 * @author Shadowmage
 *
 */
public class TeamEntry implements INBTTaggable
{

public int teamNum;
public List<TeamMemberEntry> members = new ArrayList<TeamMemberEntry>();
public Set<String> applicants = new HashSet<String>();

public Set<Integer> nonHostileTeams = new HashSet<Integer>();

public void addNewPlayer(String name, byte rank)
  {
  for(TeamMemberEntry entry : this.members)
    {
    if(entry.memberName.equals(name))
      {
      return;
      }
    }
  this.members.add(new TeamMemberEntry(name, rank));
  }

public TeamMemberEntry getEntryFor(String name)
  {
  for(TeamMemberEntry entry : this.members)
    {
    if(entry.getMemberName().equals(name))
      {
      return entry;
      }
    }
  return null;
  }

public void removePlayer(String name)
  {
  Iterator<TeamMemberEntry> it = members.iterator();
  TeamMemberEntry entry;
  while(it.hasNext())
    {
    entry = it.next();
    if(entry.memberName.equals(name))
      {
      it.remove();
      break;
      }
    }
  }

public byte getPlayerRank(String name)
  {
  for(TeamMemberEntry entry : members)
    {
    if(entry.getMemberName().equals(name))
      {
      return entry.getMemberRank();
      }
    }
  return -1;
  }

public boolean containsPlayer(String name)
  {
  return this.getPlayerRank(name)>=0;
  }

public void addApplicant(String name)
  {  
  if(!this.applicants.contains(name))
    {
    this.applicants.add(name);
    }
  }

public void removeApplicant(String name)
  {
  this.applicants.remove(name);
  }

public Set<String> getApplicantList()
  {
  return this.applicants;
  }

public void approveApplicant(String name)
  {
  this.removeApplicant(name);
  this.addNewPlayer(name, (byte) 0);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("num", this.teamNum);
  NBTTagList namesList = new NBTTagList();
  TeamMemberEntry entry;
  for(int i = 0; i < this.members.size(); i++)
    {    
    entry = this.members.get(i);
    NBTTagCompound memberTag = new NBTTagCompound();
    memberTag.setString("name", entry.getMemberName());
    memberTag.setByte("rank", entry.getMemberRank());
    namesList.appendTag(memberTag);
    }  
  tag.setTag("teamMembers", namesList);
  
  int[] nonHost = new int[this.nonHostileTeams.size()];
  int index = 0;
  for(Integer i : this.nonHostileTeams)
    {
    nonHost[index]=i;
    index++;
    }  
  tag.setIntArray("nonHost", nonHost);  
  
  NBTTagList applicantsList = new NBTTagList();
  for(String name : applicants)
    {
    applicantsList.appendTag(new NBTTagString("name", name));
    }
  tag.setTag("applicants", applicantsList);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.members.clear();
  this.teamNum = tag.getInteger("num");
  NBTTagList namesList = tag.getTagList("teamMembers");
  for(int i = 0; i < namesList.tagCount(); i++)
    {
    NBTTagCompound memberTag = (NBTTagCompound) namesList.tagAt(i);
    String name = memberTag.getString("name");
    byte rank = memberTag.getByte("rank");
    this.members.add(new TeamMemberEntry(name, rank));
    }  
  int[] nonHost = tag.getIntArray("nonHost");
  this.nonHostileTeams.clear();
  for(int i = 0; i < nonHost.length; i++)
    {
    this.nonHostileTeams.add(nonHost[i]);
    }  
  if(tag.hasKey("applicants"))
    {
    namesList = tag.getTagList("applicants");
    NBTTagString applicant;
    for(int i = 0; i < namesList.tagCount(); i++)
      {
      applicant = (NBTTagString) namesList.tagAt(i);
      this.applicants.add(applicant.data);
      }
    }
  }

public boolean isHostileTowards(int num)
  {
  return num !=this.teamNum && !this.nonHostileTeams.contains(num);
  }

/**
 * 
 * @param team being updated
 * @param status if hostile
 */
public void handleHostileStatusChange(byte team, boolean status)
  {
  if(team!=(byte)this.teamNum)
    {
    if(status)//adding to hostile list/removing from non-hostile list
      {
      if(this.nonHostileTeams.contains(Integer.valueOf(team)))
        {
        this.nonHostileTeams.remove(Integer.valueOf(team));
        }
      }
    else
      {
      if(!this.nonHostileTeams.contains(Integer.valueOf(team)))
        {
        this.nonHostileTeams.add(Integer.valueOf(team));
        }
      }
    }
  }

public class TeamMemberEntry implements INBTTaggable
{
String memberName = "";
byte memberRank = 0;

private TeamMemberEntry(){}

private TeamMemberEntry(String name, byte rank)
  {
  this.memberName = name;
  this.memberRank = rank;
  }

public String getMemberName()
  {
  return this.memberName;  
  }

public byte getMemberRank()
  {
  return this.memberRank;
  }

public void setMemberRank(byte rank)
  {
  if(rank>=0 && rank<=10)
    {
    this.memberRank = rank;
    }
  }


@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("rank", memberRank);
  tag.setString("name", memberName);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.memberName = tag.getString("name");
  this.memberRank = tag.getByte("rank");
  }

}

}
