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

/**
 * map of research numbers, to work around order-of-initialization issues with cyclic dependencies
 * involving vehicles/materials/recipes -- and should solve many others in the future
 * @author Shadowmage
 *
 */
public class ResearchGoalNumbers
{
public static int mobility1 = 0;
public static int mobility2 = 1;
public static int mobility3 = 2;
public static int mobility4 = 3;
public static int mobility5 = 4;

public static int turrets1 = 5;
public static int turrets2 = 6;
public static int turrets3 = 7;
public static int turrets4 = 8;
public static int turrets5 = 9;

public static int torsion1 = 10;
public static int torsion2 = 11;
public static int torsion3 = 12;
public static int torsion4 = 13;
public static int torsion5 = 14;

public static int counterweights1 = 15;
public static int counterweights2 = 16;
public static int counterweights3 = 17;
public static int counterweights4 = 18;
public static int counterweights5 = 19;

public static int gunpowder1 = 20;
public static int gunpowder2 = 21;
public static int gunpowder3 = 22;
public static int gunpowder4 = 23;
public static int gunpowder5 = 24;


public static int explosives1 = 50;
public static int explosives2 = 51;
public static int explosives3 = 52;
public static int rockets1 = 53;
public static int rockets2 = 54;
public static int rockets3 = 55;
public static int flammables1 = 56;
public static int flammables2 = 57;
public static int flammables3 = 58;

public static int wood1 = 200;
public static int wood2 = 201;
public static int wood3 = 202;
public static int wood4 = 203;
public static int wood5 = 204;

public static int iron1 = 205;
public static int iron2 = 206;
public static int iron3 = 207;
public static int iron4 = 208;
public static int iron5 = 209;

}
