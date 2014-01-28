package shadowmage.meim.client.gui;

import java.awt.image.BufferedImage;

import shadowmage.meim.client.modelrenderer.MEIMModelBox;
import shadowmage.meim.common.config.MEIMConfig;

public class TextureExportMap
{

public class Map3DColorArray
{
byte[][] colors = new byte [256][256];

public byte get(int x, int z)
  {
  if(x<0 || x>=256 || z<0 || z>=256)
    {
    return 0;
    }
  return this.colors[x][z];
  }

public void set(int x, int z, byte val)
  {
  if(x<0 || x>=256 || z<0 || z>=256)
    {
    return;
    }
  this.colors[x][z]=val;
  }
}

public Map3DColorArray mapColors = new Map3DColorArray();


final byte FRONTFACE = 100;
final byte LEFTFACE = 1;
final byte BACKFACE = 2;
final byte RIGHTFACE =3;
final byte TOPFACE = 4;
final byte BOTTOMFACE = 5;
final byte EMPTY = 0;

public void printTriplet(int a, int b, int c)
  {
  System.out.println(a+","+b+","+c);
  }

public int getColorFor(int x, int z)
  { 
  return this.getColorForIndex(this.mapColors.get(x, z));
  }

public void set(int x, int y, byte color)
  {
  this.mapColors.set(x, y, color);
  }

public int getColorForIndex(byte index)
  {  
  switch(index)
    {
    case FRONTFACE://air  
    return 0xffff7f7f;
    case LEFTFACE:
    return 0xff7fff7f;
    case BACKFACE:
    return 0xffff0000;
    case RIGHTFACE:
    return 0xFF00ff00;
    case TOPFACE:
    return 0xFF7f7fff;
    case BOTTOMFACE:
    return 0xFF0000ff;
    case EMPTY:
    return 0xff7f7f7f;
    default:
    return 0xffffffff;
    }
  }

public void createBitMapForTexture()
  {
  BufferedImage img = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
    
  }

public void addBoxToMapAt(MEIMModelBox box, int x, int y)
  {
  MEIMConfig.logDebug("adding box to texmap at :"+x+","+y);
  int w = box.width*2 + box.length*2;
  int h = box.height + box.length;
  
  byte[][] colors = createTextureMapForBox(box);
  
  for(int u = x, tx=0 ; tx < w && u <256 ; u++, tx++)
    {
    for(int v = y, ty=0; ty < h && v < 256 ; v++, ty++)
      {
      if(this.mapColors.get(u, v)==EMPTY)
        {
        this.mapColors.set(u, v, colors[tx][ty]);        
        }
      }
    }
  }

public byte[][] createTextureMapForBox(MEIMModelBox box)
  {
  if(box==null)
    {
    return null;
    }  
  int w = box.width*2 + box.length*2;
  int h = box.height + box.length;
  
  
  byte[][] byteMap = new byte[w][h];
  for(int x = 0; x < w; x++)
    {
    for(int y = 0; y < h ; y++)
      {
      if( y < box.length)
        {
        if(x<box.length)//UL corner...unmapped
          {
          byteMap[x][y] = EMPTY;
          }
        else if(x >=box.length && x <box.length+box.width)//Top face
          {
          byteMap[x][y] = TOPFACE;
          }
        else if(x >= box.length+box.width && x < box.length+box.width*2)//bottom face
          {
          byteMap[x][y] = BOTTOMFACE;
          }
        else if(x >= box.length + box.width*2 && x < w)
          {
          byteMap[x][y] = EMPTY;
          }
        }
      else if(y>=box.length)
        {
        if(x<box.length)
          {
          byteMap[x][y] = LEFTFACE;
          }
        else if(x>=box.length && x< box.length+box.width)
          {
          byteMap[x][y] = BACKFACE;
          }
        else if(x>=box.length+box.width && x < box.length*2 + box.width)
          {
          byteMap[x][y] = RIGHTFACE;
          }
        else if(x>=box.length*2 + box.width && x < box.length*2 + box.width*2)
          {
          byteMap[x][y] = FRONTFACE;
          }
        }
      }
    }
  return byteMap;
  }

}
