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
package shadowmage.ancient_warfare.client.gui.structure;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;


public class GuiTextBox extends Gui
{

/**
 * basic params
 */
int xSize;
int ySize;
final int displayLines;
final int lineLength;
int textColor;
int backGroundColor;
boolean activated = false;

/**
 * the actual lines driving the char array to be displayed
 */
List<String> lines;

/**
 * cursor position on screen
 */
int cursorPosX;
int cursorPosY;

/**
 * leftMost char drawn
 * and topMost drawn char
 */
int viewX;
int viewY;

/**
 * used to trigger updates in viewedChars array
 */
int prevViewX;
int prevViewY;

/**
 * the data that is drawn onto the screen
 */
char[][] screenChars;

public GuiTextBox(int xSize, int ySize, List<String> lines)
  {
  this(xSize, ySize, 10, 32, 0xffffffff, 0x00000000, lines);  
  }

public GuiTextBox(int xSize, int ySize, int displayLines, int lineLength, int textColorHex, int backColorHex, List<String> lines)
  {
  this.xSize = xSize;
  this.ySize = ySize;
  this.displayLines = displayLines;
  this.lineLength = lineLength;
  this.textColor = textColorHex;
  this.backGroundColor = backColorHex;
  this.lines = lines;
  this.screenChars = new char[displayLines][lineLength];
  if(this.lines!=null)
    {
    this.updateScreenChars();
    }
  }

public boolean onKeyTyped(char charValue, int keyCode)
  {
  if(!this.activated)
    {
    return false;
    }  
  if(keyCode==200)//up arrow
    {
    this.moveCursor(0, -1);
    }
  else if(keyCode==208)//down arrow
    {
    this.moveCursor(0, 1);
    }
  else if(keyCode==203)
    {
    this.moveCursor(-1, 0);    
    }
  else if(keyCode==205)
    {
    this.moveCursor(1, 0);
    }
  else if(keyCode==211)//delete
    {
    this.handleDeleteAction();
    }
  else if(keyCode==14)//backspace
    {
    this.handleBackspaceAction();
    }
  else if(keyCode==28)
    {
    this.handleEnterAction();
    }
  else
    {
    this.handleCharAction(charValue);
    }
  
  /**
   * TODO
   * shift+arrow keys--highlight selection
   * copy/cut/paste -- copy/cut/paste
   */
  
  /**
   * delete--
   * if at end of line, bring next line up if available and add onto end of this line
   * else if at a char, remove char at cursor
   */
  
  /**
   * backspace--
   * if at beginning of line, bring current line up if possible and add onto end of previous line 
   * else remove character preceding cursor
   * move cursor back
   */
  
  /**
   * return--
   * cut current line at pos, from pos onwards move down onto next line.
   */
   
  /**
   * chars--
   * insert char into string, set string, move cursor
   */
  return true;
  }

private void handleCharAction(char ch)
  {
  String line = getCurrentLine();
  int lineNum = viewY+cursorPosY;
  int charNum = viewX+cursorPosX;
  String newLine = "";
  for(int i = 0; i < line.length(); i ++)
    {
    if(i==charNum)
      {
      newLine = newLine + String.valueOf(ch);
      }
    newLine = newLine + line.charAt(i);
    }
  this.moveCursor(1, 0);
  }

private void handleEnterAction()
  {
  String line = getCurrentLine();
  int lineNum = viewY+cursorPosY;
  int charNum = viewX+cursorPosX;
  }

private void handleDeleteAction()
  {
  String line = getCurrentLine();
  int lineNum = viewY+cursorPosY;
  int charNum = viewX+cursorPosX;
  if(charNum==line.length())//pointer is at end of the line, bring next line up onto the end of this one
    {
    line = joinLines(line, this.lines.get(viewY+cursorPosY+1));
    this.setCurrentLine(line);
    this.deleteLine(lineNum+1);
    }
  else if(line.length()>0)
    {    
    deleteCharInString(line, viewX+cursorPosX);      
    }  
  this.updateScreenChars();
  }

private void handleBackspaceAction()
  {
  String line = getCurrentLine();
  int lineNum = viewY+cursorPosY;
  int charNum = viewX+cursorPosX;
  if(charNum==0)//cursor is at beginning of line, bring this line up onto the end of the previous
    {
    if(lineNum>0)
      {
      moveCursor(0,-1);//move cursor up to previous line;
      setCursorToEndOfCurrentLine();
      line = getCurrentLine();
      line = joinLines(line, lines.get(viewY+cursorPosY));
      this.setCurrentLine(line);
      this.deleteLine(viewY+cursorPosY+1);
      }
    }
  else
    {
    this.deleteCharInString(line, charNum-1);
    this.setCurrentLine(line);
    }
  this.updateScreenChars();
  }

private void setCursorToEndOfCurrentLine()
  {
  int lineLen = this.currentStringLength();
  if(lineLen < lineLength)
    {
    this.cursorPosX = lineLen;
    }
  else
    {
    this.cursorPosX = lineLen-viewX;
    }
  }

private String getCurrentLine()
  {
  if(viewY+cursorPosY < 0 || viewY+cursorPosY >= this.lines.size())
    {
    return "";
    }
  return this.lines.get(viewY+cursorPosY);
  }

private void deleteCurrentLine()
  {
  deleteLine(this.viewY+cursorPosY);
  }

private void deleteLine(int lineNum)
  {
  //TODO needs checking
  this.lines.remove(lineNum);
  }

private void setCurrentLine(String line)
  {
  //TODO needs checking
  this.lines.set(viewY+cursorPosY, line);
  }

private int getCharIndex()
  {
  //TODO needs checking
  return this.viewX+cursorPosX;
  }

private int currentStringLength()
  {
  //TODO needs checking
  return this.lines.get(viewY+cursorPosY).length();
  }

private String joinLines(String first, String second)
  {
  //TODO needs checking
  return first+second;
  }

private String deleteCharInString(String line, int charIndex)
  {
  if(charIndex >= line.length() || charIndex <= 0)
    {
    return line;
    }
  int len = line.length();
  String ret = new String("");
  for(int i = 0; i < line.length(); i++)
    {
    if(i!=charIndex)
      {
      ret = ret + line.charAt(i);
      }
    }
  return ret;
  }

private void moveCursor(int xMove, int yMove)
  {
  if(this.lines==null)
    {
    this.cursorPosX = 0;
    this.cursorPosY = 0;
    return;
    }

  cursorPosY += yMove;
  cursorPosX += xMove;
  
  if(cursorPosY < 0)
    {
    this.cursorPosY = 0;
    if(this.viewY>0)
      {
      this.viewY--;
      }
    }
  else if(this.cursorPosY >= this.displayLines)
    {
    this.cursorPosY = this.displayLines-1;//reset to bottom line, and check if can scroll entire view down    
    if(viewY + displayLines < lines.size())
      {
      Config.logDebug("scrolling down");
      viewY++;
      }
    }
  
  if(cursorPosX<0)
    {
    cursorPosX = 0;
    if(this.viewX>0)
      {
      this.viewX--;
      }
    }
  else if(cursorPosX >= this.lineLength)
    {
    cursorPosX = this.lineLength-1;
    int lastCharIndex = this.viewX+this.lineLength - 1;
    if(lastCharIndex +1 < lines.get(viewY+cursorPosY).length())
      {
      viewX++;
      }
    }
  
  if(this.hasViewChanged())
    {    
    Config.logDebug("updating screen chars");
    this.updateScreenChars();
    }
  }


private boolean hasViewChanged()
  {
  return this.viewX != this.prevViewX || this.viewY != this.prevViewY;
  }

public boolean isMouseOver(int x, int y)
  {
  return false;
  }

public boolean onMouseClicked(int buttonNum, int x, int y)
  {
  return false;
  }

public void drawTextBox(FontRenderer fontRenderer, int xPos, int yPos)
  {
  
  int border = 4;
  int charWidth = 7;
  int charHeight = 10;

  drawRect(xPos - 1, yPos - 1, xPos + this.xSize + 1, yPos + this.ySize + 1, -6250336);
  drawRect(xPos, yPos, xPos + this.xSize, yPos + this.ySize, -16777216);
  if(this.lines==null)
    {
    return;
    }
  if(this.hasViewChanged())
    {
    this.updateScreenChars();
    }  
  for(int y = 0; y < this.screenChars.length; y++)
    {
    for(int x = 0; x < this.screenChars[y].length; x++)
      {
      this.renderCharAt(fontRenderer, xPos + border + charWidth*x, yPos + border + charHeight*y, this.screenChars[y][x]);
      }
    }
  this.renderCursor(fontRenderer, xPos + border + charWidth * cursorPosX, yPos + border + charHeight*cursorPosY + 1);
  }

private void renderCursor(FontRenderer fontRenderer, int posX, int posY)
  {
  fontRenderer.drawString("_", posX, posY, 0xffff7f7f, false);
  }

private void updateScreenChars()
  {
  this.prevViewX = this.viewX;
  this.prevViewY = this.viewY;
  for(int y = 0; y < this.displayLines; y++)
    {
    boolean blankLine = this.viewY+y >= this.lines.size();    
    for(int x = 0; x < this.lineLength; x++)
      {       
      if(blankLine)
        {
        this.screenChars[y][x] = ' ';
        }
      else
        {
        boolean blankChar = x >= this.lines.get(y+viewY).length();
        if(blankChar)
          {
          this.screenChars[y][x] = ' ';
          }
        else
          {
          this.screenChars[y][x] = this.lines.get(y+viewY).charAt(x+viewX);
          }
        }
      }
    }
  }

private void renderCharAt(FontRenderer fontRenderer, int x, int y, char ch)
  {  
  //TODO not make this so freaking ghetto--- e.g. write a proper char renderer instead of 
  //using an entire string renderer to render a single char
  fontRenderer.drawString(String.valueOf(ch), x, y, textColor, false);
  }


}
