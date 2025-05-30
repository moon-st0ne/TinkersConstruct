package slimeknights.tconstruct.library.recipe.material;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import slimeknights.mantle.recipe.helper.LoggingRecipeSerializer;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialValueIngredient;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tables.TinkerTables;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Shaped recipe with a number of {@link slimeknights.tconstruct.library.recipe.ingredient.MaterialValueIngredient} to set the material of the result
 */
public class ShapedMaterialRecipe extends ShapedRecipe {
  private MaterialValueIngredient material;
  public ShapedMaterialRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, boolean showNotification) {
    super(id, group, category, width, height, ingredients, result, showNotification);
  }

  public ShapedMaterialRecipe(ShapedRecipe recipe) {
    this(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getRecipeWidth(), recipe.getRecipeHeight(), recipe.getIngredients(), recipe.result, recipe.showNotification());
  }

  /** Gets the material to match */
  @Nullable
  public MaterialValueIngredient getMaterial() {
    if (material == null) {
      // assume all material ingredients match the same stat type
      for (Ingredient ingredient : getIngredients()) {
        // collect all ingredients that match
        if (ingredient instanceof MaterialValueIngredient materialValue) {
          if (material == null) {
            material = materialValue;
          } else {
            // ensure the stat type matches, and expand the range
            material = material.merge(materialValue);
          }
        }
      }
      // if we found no materials, that is also an issue
      if (material == null) {
        TConstruct.LOG.error("No material ingredient found for material shaped recipe {}, this indicates a broken recipe", getId());
      }
    }
    return material;
  }

  @Nullable
  private MaterialVariantId findMaterial(CraftingContainer inventory) {
    MaterialValueIngredient material = getMaterial();
    if (material == null) {
      return null;
    }
    // ensure same material in all slots
    MaterialVariantId firstMaterial = null;
    for (int i = 0; i < inventory.getContainerSize(); i++) {
      ItemStack stack = inventory.getItem(i);
      if (!stack.isEmpty()) {
        // ignore anything that does not meet our requirements
        MaterialVariantId matchedMaterial = material.getMaterial(stack);
        if (matchedMaterial != null) {
          // first match is set
          if (firstMaterial == null) {
            firstMaterial = matchedMaterial;
          } else if (!firstMaterial.matchesVariant(matchedMaterial)) {
            // if same material but different variants, just discard the variant
            if (firstMaterial.getId().equals(matchedMaterial.getId())) {
              firstMaterial = firstMaterial.getId();
            } else {
              // if different materials, no match
              return null;
            }
          }
        }
      }
    }
    return firstMaterial;
  }

  @Override
  public boolean matches(CraftingContainer inventory, Level level) {
    if (!super.matches(inventory, level)) {
      return false;
    }

    // must have a material to match, no mixing
    return findMaterial(inventory) != null;
  }

  /** Sets the material for the given stack */
  public static void setMaterial(ItemStack stack, MaterialVariantId material) {
    ToolStack.from(stack).setMaterials(new MaterialNBT(List.of(MaterialVariant.of(material))));
  }

  @Override
  public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryAccess) {
    ItemStack stack = super.assemble(inventory, registryAccess);
    MaterialVariantId material = findMaterial(inventory);
    if (material != null) {
      setMaterial(stack, material);
    }
    return stack;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return TinkerTables.shapedMaterialRecipeSerializer.get();
  }

  public static class Serializer implements LoggingRecipeSerializer<ShapedMaterialRecipe> {
    @Override
    public ShapedMaterialRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      ShapedMaterialRecipe recipe = new ShapedMaterialRecipe(SHAPED_RECIPE.fromJson(recipeId, json));
      // ensure the material is valid, since we have all the needed information to check
      // better now than at runtime
      if (recipe.getMaterial() == null) {
        throw new JsonSyntaxException("Invalid material ingredients for shaped material recipe " + recipeId);
      }
      return recipe;
    }

    @Override
    @Nullable
    public ShapedMaterialRecipe fromNetworkSafe(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      ShapedRecipe recipe = SHAPED_RECIPE.fromNetwork(recipeId, buffer);
      return recipe == null ? null : new ShapedMaterialRecipe(recipe);
    }

    @Override
    public void toNetworkSafe(FriendlyByteBuf buffer, ShapedMaterialRecipe recipe) {
      SHAPED_RECIPE.toNetwork(buffer, recipe);
    }
  }
}
