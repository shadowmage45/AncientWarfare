package codechicken.nei.recipe;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.src.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;

public abstract class TemplateRecipeHandler implements ICraftingHandler, IUsageHandler {
	public abstract class CachedRecipe {
	public boolean contains(Collection<PositionedStack> ingredients, ItemStack ingredient)
    {return false;}
	public void setIngredientPermutation(Collection<PositionedStack> ingredients, ItemStack ingredient)
    {}


	}
	public ArrayList<CachedRecipe> arecipes = new ArrayList<CachedRecipe>();
	public void loadUsageRecipes(ItemStack ingredient) {}
	public void loadCraftingRecipes(String outputId, Object... results) {}
	public void loadCraftingRecipes(ItemStack result){}
	public int recipiesPerPage(){return 2;}
	public void drawBackground(GuiContainerManager gui, int recipe)
	  {
	 
	  }
	public String getGuiTexture(){return "";}
}
