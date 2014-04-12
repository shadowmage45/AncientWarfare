package shadowmage.ancient_structures.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.tile.SpawnerSettings;
import shadowmage.ancient_structures.common.tile.SpawnerSettings.EntitySpawnGroup;
import shadowmage.ancient_structures.common.tile.SpawnerSettings.EntitySpawnSettings;
import shadowmage.ancient_structures.common.tile.TileAdvancedSpawner;
import shadowmage.ancient_warfare.common.network.GUIHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockAdvancedSpawner extends ItemBlock implements IItemKeyInterface
{

public ItemBlockAdvancedSpawner(int p_i45328_1_)
  {
  super(p_i45328_1_);
  }

@Override
public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
  {
  if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("spawnerSettings"))
    {
    AWLog.logDebug("no tag exists for spawner item... adding default tag");
    SpawnerSettings settings = SpawnerSettings.getDefaultSettings();
    NBTTagCompound defaultTag = new NBTTagCompound();
    settings.writeToNBT(defaultTag);
    stack.setTagInfo("spawnerSettings", defaultTag);
    }
  boolean val = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
  if(!world.isRemote && val)
    {
    TileEntity te = world.getBlockTileEntity(x, y, z);
    if(te instanceof TileAdvancedSpawner)
      {
      TileAdvancedSpawner tile = (TileAdvancedSpawner)te;
      SpawnerSettings settings = new SpawnerSettings();
      settings.readFromNBT(stack.getTagCompound().getCompoundTag("spawnerSettings"));      
      tile.setSettings(settings);
      }    
    }
  return val;  
  }

@Override
public void onKeyAction(EntityPlayer player, ItemStack stack)
  {
  if(!player.worldObj.isRemote)
    {
    if(player.isSneaking())
      {
      GUIHandler.instance().openGUI(GUIHandler.SPAWNER_ITEM_INVENTORY, player, player.worldObj, 0, 0, 0);   
      }    
    else
      {
      GUIHandler.instance().openGUI(GUIHandler.SPAWNER_ITEM, player, player.worldObj, 0, 0, 0);   
      }
    }
  }

SpawnerSettings tooltipSettings = new SpawnerSettings();
@Override
@SideOnly(Side.CLIENT)
public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
  List<String> list = (List<String>)par3List;
  if(!par1ItemStack.hasTagCompound() || !par1ItemStack.getTagCompound().hasKey("spawnerSettings"))
    {
    list.add(StatCollector.translateToLocal("guistrings.corrupt_item"));
    return;
    }
  tooltipSettings.readFromNBT(par1ItemStack.getTagCompound().getCompoundTag("spawnerSettings"));
  List<EntitySpawnGroup> groups = tooltipSettings.getSpawnGroups();
  list.add(StatCollector.translateToLocal("guistrings.spawner.group_count")+": "+groups.size());
  EntitySpawnGroup group;
  for(int i = 0; i < groups.size(); i++)
    {
    group = groups.get(i);
    list.add(StatCollector.translateToLocal("guistrings.spawner.group_number")+": "+(i+1) + " "+StatCollector.translateToLocal("guistrings.spawner.group_weight")+": "+group.getWeight());
    for(EntitySpawnSettings set : group.getEntitiesToSpawn())
      {
      list.add("  "+StatCollector.translateToLocal("guistrings.spawner.entity_type")+": "+set.getEntityId() +" "+set.getSpawnMin()+" to "+set.getSpawnMax()+" ("+ (set.getSpawnTotal()<0 ? "infinite" :set.getSpawnTotal()) +" total)");      
      }
    }  
  }

@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List stackList)
  {
  ItemStack stack = new ItemStack(Block.blocksList[this.getBlockID()]);
  SpawnerSettings settings = SpawnerSettings.getDefaultSettings();
  NBTTagCompound defaultTag = new NBTTagCompound();
  settings.writeToNBT(defaultTag);
  stack.setTagInfo("spawnerSettings", defaultTag);
  stackList.add(stack);
  }

}
