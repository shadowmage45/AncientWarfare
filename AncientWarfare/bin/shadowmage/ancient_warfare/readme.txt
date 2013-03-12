AncientWarfare c2012-2013 John Cummens aka Shadowmage
Released under the General Public License v3.0 (or later)


Refs:

read: http://accidentalnoise.sourceforge.net/minecraftworlds.html
http://mrl.nyu.edu/~perlin/noise/
http://gafferongames.com/networking-for-game-programmers/what-every-programmer-needs-to-know-about-game-networking/
http://en.wikipedia.org/wiki/Trajectory_of_a_projectile
http://gamedev.stackexchange.com/questions/17467/calculating-velocity-needed-to-hit-target-in-parabolic-arc  (its wrong...)


Code snips:

/*****************************************************************************************/
  float vAspect = this.mc.displayWidth/this.mc.displayHeight;
  int yPixels = this.mc.displayHeight;
  int newYPixels = yPixels - (int)(50.f*vAspect);
  float scaleY = (float)yPixels/(float)newYPixels;
  
  int xPixels = this.mc.displayWidth;
  int newXPixels = xPixels - 50;
  float scaleX = (float)xPixels/(float)newXPixels;
  
  GL11.glViewport(0, (int)(50*vAspect), newXPixels, newYPixels);
  
  GL11.glScalef(scaleX, scaleY, 1);
  for(int i = 0; i < 50; i++)
    {
    this.drawString(this.fontRenderer, "testString....aba;lkja;lhasdfasdfqwerqweradfadfaqwerqwetasdfawerqwerqasdfawerqwerqwerqasdfasdfasdfaqewrqwreghjkghuityuitftyertyfbgsrtwertsdfgasdfawreqwe", 0, i*10, 0xffffffff);
    }  
  GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);

/*****************************************************************************************/
