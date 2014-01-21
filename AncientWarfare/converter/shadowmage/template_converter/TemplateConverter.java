/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.template_converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;

public class TemplateConverter
{

HashMap<Integer, String> blockIDToName = new HashMap<Integer, String>();

File in;
File out;

static
{
add17NameMaping(1, "stone");
add17NameMaping(2, "grass");
add17NameMaping(3, "dirt");
add17NameMaping(4, "cobblestone");
add17NameMaping(5, "planks");
add17NameMaping(6, "sapling");
add17NameMaping(7, "bedrock");
add17NameMaping(8, "flowing_water");
add17NameMaping(9, "water");
add17NameMaping(10, "flowing_lava");
add17NameMaping(11, "lava");
add17NameMaping(12, "sand");
add17NameMaping(13, "gravel");
add17NameMaping(14, "gold_ore");
add17NameMaping(15, "iron_ore");
add17NameMaping(16, "coal_ore");
add17NameMaping(17, "log");
add17NameMaping(18, "leaves");
add17NameMaping(19, "sponge");
add17NameMaping(20, "glass");
add17NameMaping(21, "lapis_ore");
add17NameMaping(22, "lapis_block");
add17NameMaping(23, "dispenser");
add17NameMaping(24, "sandstone");
add17NameMaping(25, "noteblock");
add17NameMaping(26, "bed");
add17NameMaping(27, "golden_rail");
add17NameMaping(28, "detector_rail");
add17NameMaping(29, "sticky_piston");
add17NameMaping(30, "web");
add17NameMaping(31, "tallgrass");
add17NameMaping(32, "deadbush");
add17NameMaping(33, "piston");
add17NameMaping(34, "piston_head");
add17NameMaping(35, "wool");
add17NameMaping(36, "piston_extension");
add17NameMaping(37, "yellow_flower");
add17NameMaping(38, "red_flower");
add17NameMaping(39, "brown_mushroom");
add17NameMaping(40, "red_mushroom");
add17NameMaping(41, "gold_block");
add17NameMaping(42, "iron_block");
add17NameMaping(43, "double_stone_slab");
add17NameMaping(44, "stone_slab");
add17NameMaping(45, "brick");
add17NameMaping(46, "tnt");
add17NameMaping(47, "bookshelf");
add17NameMaping(48, "mossy_cobblestone");
add17NameMaping(49, "obsidian");
add17NameMaping(50, "torch");
add17NameMaping(51, "fire");
add17NameMaping(52, "mob_spawner");
add17NameMaping(53, "oak_stairs");
add17NameMaping(53, "chest");
add17NameMaping(55, "redstone_wire");
add17NameMaping(56, "diamond_ore");
add17NameMaping(57, "diamond_block");
add17NameMaping(58, "crafting_table");
add17NameMaping(59, "wheat");
add17NameMaping(60, "farmland");
add17NameMaping(61, "furnace");
add17NameMaping(62, "lit_furnace");
add17NameMaping(63, "standing_sign");
add17NameMaping(64, "wooden_door");
add17NameMaping(65, "ladder");
add17NameMaping(66, "rail");
add17NameMaping(67, "stone_stairs");
add17NameMaping(68, "wall_sign");
add17NameMaping(69, "lever");
add17NameMaping(70, "stone_pressure_plate");
add17NameMaping(71, "iron_door");
add17NameMaping(72, "wooden_pressure_plate");
add17NameMaping(73, "redstone_ore");
add17NameMaping(74, "lit_redstone_ore");
add17NameMaping(75, "unlit_redstone_torch");
add17NameMaping(76, "redstone_torch");
add17NameMaping(77, "stone_button");
add17NameMaping(78, "snow_layer");
add17NameMaping(79, "ice");
add17NameMaping(80, "snow");
add17NameMaping(81, "cactus");
add17NameMaping(82, "clay");
add17NameMaping(83, "reeds");
add17NameMaping(84, "jukebox");
add17NameMaping(85, "fence");
add17NameMaping(86, "pumpkin");
add17NameMaping(87, "netherrack");
add17NameMaping(88, "soul_sand");
add17NameMaping(89, "glowstone");
add17NameMaping(90, "portal");
add17NameMaping(91, "lit_pumpkin");
add17NameMaping(92, "cake");
add17NameMaping(93, "unpowered_repeater");
add17NameMaping(94, "powered_repeater");
add17NameMaping(95, "stained_glass");
add17NameMaping(96, "trapdoor");
add17NameMaping(97, "monster_egg");
add17NameMaping(98, "stonebrick");
add17NameMaping(99, "brown_mushroom_block");
add17NameMaping(100, "red_mushroom_block");
add17NameMaping(101, "iron_bars");
add17NameMaping(102, "glass_pane");
add17NameMaping(103, "melon_block");
add17NameMaping(104, "pumpkin_stem");
add17NameMaping(105, "melon_stem");
add17NameMaping(106, "vine");
add17NameMaping(107, "fence_gate");
add17NameMaping(108, "brick_stairs");
add17NameMaping(109, "stone_brick_stairs");
add17NameMaping(110, "mycelium");
add17NameMaping(111, "waterlily");
add17NameMaping(112, "nether_brick");
add17NameMaping(113, "nether_brick_fence");
add17NameMaping(114, "nether_brick_stairs");
add17NameMaping(115, "nether_wart");
add17NameMaping(116, "enchanting_table");
add17NameMaping(117, "brewing_stand");
add17NameMaping(117, "cauldron");
add17NameMaping(119, "end_portal");
add17NameMaping(120, "end_portal_frame");
add17NameMaping(121, "end_stone");
add17NameMaping(122, "dragon_egg");
add17NameMaping(123, "redstone_lamp");
add17NameMaping(124, "lit_redstone_lamp");
add17NameMaping(125, "double_wooden_slab");
add17NameMaping(126, "wooden_slab");
add17NameMaping(127, "cocoa");
add17NameMaping(128, "sandstone_stairs");
add17NameMaping(129, "emerald_ore");
add17NameMaping(130, "ender_chest");
add17NameMaping(131, "tripwire_hook");
add17NameMaping(132, "tripwire");
add17NameMaping(133, "emerald_block");
add17NameMaping(134, "spruce_stairs");
add17NameMaping(135, "birch_stairs");
add17NameMaping(136, "jungle_stairs");
add17NameMaping(137, "command_block");
add17NameMaping(138, "beacon");
add17NameMaping(139, "cobblestone_wall");
add17NameMaping(140, "flower_pot");
add17NameMaping(141, "carrots");
add17NameMaping(142, "potatoes");
add17NameMaping(143, "wooden_button");
add17NameMaping(144, "skull");
add17NameMaping(145, "anvil");
add17NameMaping(146, "trapped_chest");
add17NameMaping(147, "light_weighted_pressure_plate");
add17NameMaping(148, "heavy_weighted_pressure_plate");
add17NameMaping(149, "unpowered_comparator");
add17NameMaping(150, "powered_comparator");
add17NameMaping(151, "daylight_detector");
add17NameMaping(152, "redstone_block");
add17NameMaping(153, "quartz_ore");
add17NameMaping(154, "hopper");
add17NameMaping(155, "quartz_block");
add17NameMaping(156, "quartz_stairs");
add17NameMaping(157, "activator_rail");
add17NameMaping(158, "dropper");
add17NameMaping(159, "stained_hardened_clay");
add17NameMaping(160, "stained_glass_pane");
add17NameMaping(161, "leaves2");
add17NameMaping(162, "log2");
add17NameMaping(163, "acacia_stairs");
add17NameMaping(164, "dark_oak_stairs");
add17NameMaping(170, "hay_block");
add17NameMaping(171, "carpet");
add17NameMaping(172, "hardened_clay");
add17NameMaping(173, "coal_block");
add17NameMaping(174, "packed_ice");
add17NameMaping(175, "double_plant");

}

private static void add17NameMaping(int id, String name){}

public TemplateConverter(File in, File out)
  {
  this.in = in;
  this.out = out;
  }

public void doConversion() throws IOException
  {
  if(!out.exists()){out.createNewFile();}
  List<String> lines = Files.readAllLines(in.toPath(), Charset.forName("UTF-8"));
  for(String line : lines)
    {
    System.out.println(line);
    }  
  }


}
