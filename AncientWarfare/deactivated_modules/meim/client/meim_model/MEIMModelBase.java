package shadowmage.meim.client.meim_model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBase;
import shadowmage.meim.client.modelrenderer.MEIMModelRenderer;

/**
 * base class representing an entire model
 * @author Shadowmage
 *
 */
public class MEIMModelBase extends ModelBase
{


public static final float PIXELSIZE = 0.0625f;
public List<MEIMModelRenderer> baseParts = new ArrayList<MEIMModelRenderer>();
String texName;

public void render()
  {
  for(MEIMModelRenderer rend : this.baseParts)
    {
    rend.render(PIXELSIZE);
    }
  }

public void renderSelectionMode()
  {
  for(MEIMModelRenderer rend : this.baseParts)
    {
    rend.renderForPicking();
    }
  }

public void clearCurrentPartStatus()
  {
  //TODO
  }

private MEIMModelRenderer searchResult = null;

public MEIMModelRenderer getRenderForNumber(int partNum)
  {
  searchResult = null;
  for(MEIMModelRenderer renderer : baseParts)
    {
    if(renderer.pieceName==partNum)
      {
      return renderer;
      }   
    recurseNumberSearch(renderer, partNum);
    if(searchResult!=null)
      {
      return searchResult;
      }
    }
  return searchResult;
  }

private void recurseNumberSearch(MEIMModelRenderer rend, int num)
  {
  if(rend.pieceName==num)
    {
    searchResult = rend;
    return;
    }
  if(rend.childModels!=null)
    {
    for(MEIMModelRenderer render : rend.childModels)
      {
      recurseNumberSearch(render, num);
      }    
    }
  }

public MEIMModelRenderer getRenderForName(String name)
  {
  searchResult = null;
  for(MEIMModelRenderer renderer : baseParts)
    {
    if(renderer.boxName.equals(name))
      {
      return renderer;
      }
    recurseNameSearch(name, renderer);
    if(searchResult!=null)
      {
      return searchResult;
      }
    }
  return searchResult;
  }

public void recurseNameSearch(String name, MEIMModelRenderer renderer)
  {
  if(renderer.boxName.equals(name))
    {
    searchResult = renderer;
    return;
    }
  if(renderer.childModels!=null)
    {
    for(MEIMModelRenderer rend : renderer.childModels)
      {
      recurseNameSearch(name, rend);
      }    
    }
  }

public void removePart(MEIMModelRenderer rend)
  {
  if(rend.parent==null)//is a base part..
    {
    this.baseParts.remove(rend);
    }
  else
    {
    rend.parent.childModels.remove(rend);
    }
  }

public void swapPartParent(MEIMModelRenderer part, MEIMModelRenderer newParent)
  {
  if(part==newParent || part==null)
    {
    return;
    }
  if(part.parent!=null)
    {
    //remove current part from current parents list
    part.parent.childModels.remove(part);
    part.parent = null;
    }
  else if(this.baseParts.contains(part))
    {
    this.baseParts.remove(part);
    }
  part.parent = newParent;
  if(part.parent!=null)
    {    
    part.parent.addChild(part);
    }  
  else
    {
    this.baseParts.add(part);
    }
  }

}
