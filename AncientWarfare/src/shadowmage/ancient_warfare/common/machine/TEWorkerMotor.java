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
package shadowmage.ancient_warfare.common.machine;

import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.interfaces.ITEWorkSite;
import shadowmage.ancient_warfare.common.interfaces.IWorker;

public class TEWorkerMotor extends TEMachine implements ITEWorkSite
{

protected IWorker worker;

/**
 * 
 */
public TEWorkerMotor()
  {
  this.canPointVertical = true;
  this.canUpdate = true;
  }

@Override
public void doWork(IWorker worker)
  {
  if(worker==this.worker && worker!=null)
    {
    
    }
  }

@Override
public boolean hasWork()
  {
  return false;
  }

@Override
public boolean canHaveMoreWorkers(IWorker worker)
  {
  return this.worker==null || worker==this.worker;
  }

@Override
public void addWorker(IWorker worker)
  {
  if(this.worker==null)
    {
    this.worker = worker;
    }
  }

@Override
public void removeWorker(IWorker worker)
  {
  if(worker==this.worker)
    {
    this.worker = null;
    }
  }

@Override
public void broadcastWork(int maxRange)
  {
    
  }

@Override
public CivicWorkType getWorkType()
  {
  return CivicWorkType.MINE;
  }

@Override
public boolean isWorkSite()
  {
  return true;
  }

}
