package shadowmage.ancient_warfare.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldAdvanced extends GuiTextField
{

public int textBoxNumber = 0;
GuiContainerAdvanced parent;

private int x;
private int y;
private int xSize;
private int ySize;
private boolean isNumberValue;

public GuiTextFieldAdvanced(int id, GuiContainerAdvanced parent, int xPos,  int yPos, int xSize, int ySize)
  {
  super(parent.getFontRenderer(), xPos, yPos, xSize, ySize);
  this.parent = parent;
  this.textBoxNumber = id;
  this.x = xPos;
  this.y = yPos;
  this.xSize = xSize;
  this.ySize = ySize;
  }

public GuiTextField setAsNumberBox()
  {
  this.isNumberValue = true;
  return this;
  }

@Override
public boolean textboxKeyTyped(char par1, int par2)
  {
  if(this.isFocused())
    {
    if(par2==28)//enter
      {
      this.parent.onTextBoxActivated(this, 0);
      }
    if(par2==200)//up arrow
      {
      this.parent.onTextBoxActivated(this, 1);
      }
    if(par2==208)//down arrow
      {
      this.parent.onTextBoxActivated(this, -1);
      }
    }
  if(this.isNumberValue)
    {
    if(!this.numberBoxKeyTyped(par1, par2))
      {
      return false;
      }    
    }  
  return super.textboxKeyTyped(par1, par2);
  }

/**
 * validates that the input is either : valid control input, or valid number intput....
 * @param charVal
 * @param charNum
 * @return
 */
public boolean numberBoxKeyTyped(char charVal, int charNum)
  {
  for(int i : this.validCharNums)
    {
    if(charVal==i)
      {
      return true;
      }
    }
  for(int i : this.validControlNums)
    {
    if(charNum==i)
      {
      return true;
      }
    }
  for(char ch: this.validNumbers)
    {
    if(charVal == ch)
      {
      return true;
      }
    }
  return false;
  }

char[] validNumbers = new char[]{'0','1','2','3','4','5','6','7','8','9','.'};
int[] validCharNums = new int[]{1,3,22,24};
int[] validControlNums = new int[]{14,199,203,205,207,211};

public boolean onMouseWheel(int change)
  {
  this.parent.onTextBoxActivated(this, change);
  return true;
  }

public boolean isMouseOver(int rawX, int rawY)
  {
  return rawX>=x && rawX<x+xSize && rawY >=y && rawY < y+ySize;
  }

public int getIntValue()
  {
  try
    {
    return Integer.parseInt(this.getText());
    }
  catch(NumberFormatException e)
    {
    return 0;
    }
  }

public float getFloatValue()
  {
  try
    {
    return Float.parseFloat(this.getText());
    }
  catch(NumberFormatException e)
    {
    return 0;
    }
  }

}
