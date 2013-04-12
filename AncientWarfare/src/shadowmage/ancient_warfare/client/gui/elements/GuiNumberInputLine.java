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
package shadowmage.ancient_warfare.client.gui.elements;

import java.text.DecimalFormat;

import shadowmage.ancient_warfare.common.utils.StringTools;

public class GuiNumberInputLine extends GuiTextInputLine
{

public DecimalFormat formatterThreeDec = new DecimalFormat("#.###");
public DecimalFormat formatterOneDec = new DecimalFormat("#.#");
float floatVal;
/**
 * @param elementNum
 * @param parent
 * @param x
 * @param y
 * @param w
 * @param h
 * @param maxChars
 * @param defaultText
 */
public GuiNumberInputLine(int elementNum, IGuiElementCallback parent,  int w, int h, int maxChars, String defaultText)
  {
  super(elementNum, parent, w, h, maxChars, defaultText);
  }

@Override
protected boolean isValidChar(char ch)
  {
  if(this.isValidNumber(ch))
    {
    return true;
    } 
  return false;
  }

char[] validNums = new char []{'0','1','2','3','4','5','6','7','8','9','.','-'};

protected boolean isValidNumber(char ch)
  {
  for(char vch : this.validNums)
    {
    if(vch==ch)
      {
      return true;
      }
    }
  return false;
  }

@Override
public boolean handleMouseWheel(int x, int y, int wheel)
  {
  this.floatVal += wheel;
  this.text = String.valueOf(floatVal);
  this.parent.onElementActivated(this);
  return true;
  }

@Override
protected void handleCharAction(char ch)
  {
  super.handleCharAction(ch);
  this.floatVal = StringTools.safeParseFloat(text);
  }

public int getIntVal()
  {
  return (int)this.floatVal;
  }

public float getFloatVal()
  {
  return this.floatVal;
  }

@Override
public void setText(String text)
  {
  this.text = text;
  try 
    {
    this.floatVal = Float.parseFloat(text);
    }
  catch(Exception e)
    {
    this.text = "";
    this.floatVal = 0.f;
    }
  }

public void setValue(float val)
  {
  this.floatVal = val;
  this.text = this.formatterThreeDec.format(floatVal);      
  }

}
