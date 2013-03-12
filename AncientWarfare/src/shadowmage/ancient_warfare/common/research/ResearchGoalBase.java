/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
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
package shadowmage.ancient_warfare.common.research;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;

public class ResearchGoalBase implements IResearchGoal
{
private static ResearchGoalBase[] researchGoals = new ResearchGoalBase[4096];

protected int researchGoalNumber = 0;
protected String displayName = "";
protected String displayTooltip = "";
protected List<IResearchGoal> dependencies = new ArrayList<IResearchGoal>();
protected List<String> detailedDescription = new ArrayList<String>();

public ResearchGoalBase(int num)
  {
  if(num<0 || num >= researchGoals.length)
    {
    Config.logError("Research goal number out of range: "+num);
    return;
    }
  if(researchGoals[num]!=null)
    {
    Config.logError("Research goal being overwritten");
    }  
  this.researchGoals[num] = this;
  }

@Override
public int getGlobalResearchNum()
  {
  return researchGoalNumber;
  }

@Override
public String getDisplayName()
  {
  return displayName;
  }

@Override
public String getDisplayTooltip()
  {  
  return displayTooltip;
  }

@Override
public List<IResearchGoal> getDependencies()
  {
  return this.dependencies;
  }

@Override
public List<String> getDetailedDescription()
  {
  return this.detailedDescription;
  }

@Override
public IResearchGoal getGoalByNumber(int num)
  {
  if(num>=0 && num < this.researchGoals.length)
    {
    return this.researchGoals[num];
    }
  return null;
  }

@Override
public void addDependencies(IResearchGoal... deps)
  {
  for(int i = 0; i < deps.length; i++)
    {
    if(deps[i]!=null)
      {
      this.dependencies.add(deps[i]);
      }
    }
  }

}
