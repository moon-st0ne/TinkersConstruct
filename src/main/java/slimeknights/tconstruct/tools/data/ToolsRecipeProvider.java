package slimeknights.tconstruct.tools.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import slimeknights.mantle.recipe.data.ConsumerWrapperBuilder;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.data.BaseRecipeProvider;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.json.predicate.material.MaterialPredicate;
import slimeknights.tconstruct.library.json.predicate.material.MaterialStatTypePredicate;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.PartSwapCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialValueIngredient;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipeBuilder;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;
import slimeknights.tconstruct.world.TinkerHeadType;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

public class ToolsRecipeProvider extends BaseRecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {
  public ToolsRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  public String getName() {
    return "Tinkers' Construct Tool Recipes";
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
    this.addToolBuildingRecipes(consumer);
    this.addPartRecipes(consumer);
  }

  private void addToolBuildingRecipes(Consumer<FinishedRecipe> consumer) {
    String folder = "tools/building/";
    String armorFolder = "tools/armor/";
    // stone
    toolBuilding(consumer, TinkerTools.pickaxe, folder);
    toolBuilding(consumer, TinkerTools.sledgeHammer, folder);
    toolBuilding(consumer, TinkerTools.veinHammer, folder);
    // dirt
    toolBuilding(consumer, TinkerTools.mattock, folder);
    toolBuilding(consumer, TinkerTools.pickadze, folder);
    toolBuilding(consumer, TinkerTools.excavator, folder);
    // wood
    toolBuilding(consumer, TinkerTools.handAxe, folder);
    toolBuilding(consumer, TinkerTools.broadAxe, folder);
    // plants
    toolBuilding(consumer, TinkerTools.kama, folder);
    toolBuilding(consumer, TinkerTools.scythe, folder);
    // sword
    ToolBuildingRecipeBuilder.toolBuildingRecipe(TinkerTools.dagger.get())
                             .outputSize(2)
                             .save(consumer, prefix(TinkerTools.dagger, folder));
    toolBuilding(consumer, TinkerTools.sword, folder);
    toolBuilding(consumer, TinkerTools.cleaver, folder);
    // bow
    toolBuilding(consumer, TinkerTools.crossbow, folder);
    toolBuilding(consumer, TinkerTools.longbow, folder);

    // specialized
    ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, TinkerTools.flintAndBrick)
                          .requires(Items.FLINT)
                          .requires(Ingredient.of(TinkerSmeltery.searedBrick, TinkerSmeltery.scorchedBrick))
                          .unlockedBy("has_seared", has(TinkerSmeltery.searedBrick))
                          .unlockedBy("has_scorched", has(TinkerSmeltery.scorchedBrick))
                          .save(consumer, prefix(TinkerTools.flintAndBrick, folder));

    // staff
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TinkerTools.skyStaff)
                       .pattern("CWC")
                       .pattern(" I ")
                       .pattern(" W ")
                       .define('C', TinkerWorld.skyGeode)
                       .define('W', TinkerWorld.skyroot.getLogItemTag())
                       .define('I', TinkerMaterials.roseGold.getIngotTag())
                       .unlockedBy("has_wood", has(TinkerWorld.skyroot.getLogItemTag()))
                       .save(consumer, prefix(TinkerTools.skyStaff, folder));
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TinkerTools.earthStaff)
                       .pattern("CWC")
                       .pattern(" I ")
                       .pattern(" W ")
                       .define('C', TinkerWorld.earthGeode)
                       .define('W', TinkerWorld.greenheart.getLogItemTag())
                       .define('I', TinkerMaterials.cobalt.getIngotTag())
                       .unlockedBy("has_wood", has(TinkerWorld.greenheart.getLogItemTag()))
                       .save(consumer, prefix(TinkerTools.earthStaff, folder));
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TinkerTools.ichorStaff)
                       .pattern("CWC")
                       .pattern(" I ")
                       .pattern(" W ")
                       .define('C', TinkerWorld.ichorGeode)
                       .define('W', TinkerWorld.bloodshroom.getLogItemTag())
                       .define('I', TinkerMaterials.queensSlime.getIngotTag())
                       .unlockedBy("has_wood", has(TinkerWorld.bloodshroom.getLogItemTag()))
                       .save(consumer, prefix(TinkerTools.ichorStaff, folder));
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TinkerTools.enderStaff)
                       .pattern("CWC")
                       .pattern(" I ")
                       .pattern(" W ")
                       .define('C', TinkerWorld.enderGeode)
                       .define('W', TinkerWorld.enderbark.getLogItemTag())
                       .define('I', Tags.Items.INGOTS_NETHERITE)
                       .unlockedBy("has_wood", has(TinkerWorld.enderbark.getLogItemTag()))
                       .save(consumer, prefix(TinkerTools.enderStaff, folder));

    // travelers gear
    Consumer<FinishedRecipe> shapedMaterial = ConsumerWrapperBuilder.wrap(TinkerTables.shapedMaterialRecipeSerializer.get()).build(consumer);
    Function<MaterialStatsId,Ingredient> materialsCosting = type -> MaterialValueIngredient.of(MaterialPredicate.and(MaterialPredicate.CASTABLE, new MaterialStatTypePredicate(type)), 1);
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersGear.get(ArmorItem.Type.HELMET))
      .pattern("l l")
      .pattern("glg")
      .pattern("c c")
      .define('c', materialsCosting.apply(PlatingMaterialStats.HELMET.getId()))
      .define('l', Tags.Items.LEATHER)
      .define('g', Tags.Items.GLASS_PANES_COLORLESS)
      .unlockedBy("has_item", has(Tags.Items.LEATHER))
      .save(shapedMaterial, location(armorFolder + "travelers_goggles"));
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersGear.get(ArmorItem.Type.CHESTPLATE))
      .pattern("l l")
      .pattern("lcl")
      .pattern("lcl")
      .define('c', materialsCosting.apply(PlatingMaterialStats.CHESTPLATE.getId()))
      .define('l', Tags.Items.LEATHER)
      .unlockedBy("has_item", has(Tags.Items.LEATHER))
      .save(shapedMaterial, location(armorFolder + "travelers_chestplate"));
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersGear.get(ArmorItem.Type.LEGGINGS))
      .pattern("lll")
      .pattern("c c")
      .pattern("l l")
      .define('c', materialsCosting.apply(PlatingMaterialStats.LEGGINGS.getId()))
      .define('l', Tags.Items.LEATHER)
      .unlockedBy("has_item", has(Tags.Items.LEATHER))
      .save(shapedMaterial, location(armorFolder + "travelers_pants"));
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersGear.get(ArmorItem.Type.BOOTS))
      .pattern("c c")
      .pattern("l l")
      .define('c', materialsCosting.apply(PlatingMaterialStats.BOOTS.getId()))
      .define('l', Tags.Items.LEATHER)
      .unlockedBy("has_item", has(Tags.Items.LEATHER))
      .save(shapedMaterial, location(armorFolder + "travelers_boots"));
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersShield)
                       .pattern(" w ")
                       .pattern("wlw")
                       .pattern(" w ")
                       .define('l', Tags.Items.LEATHER)
                       .define('w', TinkerTables.pattern)
                       .unlockedBy("has_item", has(Tags.Items.LEATHER))
                       .save(consumer, location(armorFolder + "travelers_shield"));
    PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(TinkerTools.travelersGear.values().toArray(new Item[0])), 2)
      .save(consumer, location(armorFolder + "travelers_swapping"));

    // plate armor
    TinkerTools.plateArmor.forEach(item -> toolBuilding(consumer, item, armorFolder, TConstruct.getResource("plate_armor")));
    MaterialCastingRecipeBuilder.tableRecipe(TinkerTools.plateShield.get())
                                .setCast(MaterialIngredient.of(TinkerToolParts.shieldCore), true)
                                .setItemCost(3)
                                .save(consumer, location(armorFolder + "plate_shield"));
    PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(TinkerTools.plateArmor.get(ArmorItem.Type.HELMET)), 3)
      .save(consumer, location(armorFolder + "plate_helmet_swapping"));
    PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(TinkerTools.plateArmor.get(ArmorItem.Type.CHESTPLATE)), 6)
      .save(consumer, location(armorFolder + "plate_chestplate_swapping"));
    PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(TinkerTools.plateArmor.get(ArmorItem.Type.LEGGINGS)), 5)
      .save(consumer, location(armorFolder + "plate_leggings_swapping"));
    PartSwapCastingRecipeBuilder.tableRecipe(Ingredient.of(TinkerTools.plateArmor.get(ArmorItem.Type.BOOTS)), 2)
      .save(consumer, location(armorFolder + "plate_boots_swapping"));

    // slimeskull
    slimeskullCasting(consumer, MaterialIds.glass,        Items.CREEPER_HEAD,          armorFolder);
    slimeskullCasting(consumer, MaterialIds.bone,         Items.SKELETON_SKULL,        armorFolder);
    slimeskullCasting(consumer, MaterialIds.necroticBone, Items.WITHER_SKELETON_SKULL, armorFolder);
    slimeskullCasting(consumer, MaterialIds.rottenFlesh,  Items.ZOMBIE_HEAD,           armorFolder);
    slimeskullCasting(consumer, MaterialIds.gold,         Items.PIGLIN_HEAD,           armorFolder);
    slimeskullCasting(consumer, MaterialIds.enderPearl,  TinkerWorld.heads.get(TinkerHeadType.ENDERMAN),         armorFolder);
    // TODO 1.20: switch this to bogged, perhaps use a new bone type for stray
    slimeskullCasting(consumer, MaterialIds.venombone,   TinkerWorld.heads.get(TinkerHeadType.STRAY),            armorFolder);
    slimeskullCasting(consumer, MaterialIds.string,      TinkerWorld.heads.get(TinkerHeadType.SPIDER),           armorFolder);
    slimeskullCasting(consumer, MaterialIds.darkthread,  TinkerWorld.heads.get(TinkerHeadType.CAVE_SPIDER),      armorFolder);
    slimeskullCasting(consumer, MaterialIds.iron,        TinkerWorld.heads.get(TinkerHeadType.HUSK),             armorFolder);
    slimeskullCasting(consumer, MaterialIds.copper,      TinkerWorld.heads.get(TinkerHeadType.DROWNED),          armorFolder);
    slimeskullCasting(consumer, MaterialIds.blazingBone, TinkerWorld.heads.get(TinkerHeadType.BLAZE),            armorFolder);
    slimeskullCasting(consumer, MaterialIds.roseGold,    TinkerWorld.heads.get(TinkerHeadType.PIGLIN_BRUTE),     armorFolder);
    slimeskullCasting(consumer, MaterialIds.pigIron,     TinkerWorld.heads.get(TinkerHeadType.ZOMBIFIED_PIGLIN), armorFolder);

    // slimelytra
    ItemCastingRecipeBuilder.basinRecipe(TinkerTools.slimesuit.get(ArmorItem.Type.CHESTPLATE))
                            .setCast(Items.ELYTRA, true)
                            .setFluidAndTime(TinkerFluids.enderSlime, FluidValues.SLIME_CONGEALED * 8)
                            .save(consumer, location(armorFolder + "slimelytra"));

    // slimeshell
    ItemCastingRecipeBuilder.basinRecipe(TinkerTools.slimesuit.get(ArmorItem.Type.LEGGINGS))
                            .setCast(Items.SHULKER_SHELL, true)
                            .setFluidAndTime(TinkerFluids.enderSlime, FluidValues.SLIME_CONGEALED * 7)
                            .save(consumer, location(armorFolder + "slimeshell"));

    // boots
    ItemCastingRecipeBuilder.basinRecipe(TinkerTools.slimesuit.get(ArmorItem.Type.BOOTS))
                            .setCast(Items.RABBIT_FOOT, true)
                            .setFluidAndTime(TinkerFluids.enderSlime, FluidValues.SLIME_CONGEALED * 4)
                            .save(consumer, location(armorFolder + "slime_boots"));
  }

  private void addPartRecipes(Consumer<FinishedRecipe> consumer) {
    String partFolder = "tools/parts/";
    String castFolder = "smeltery/casts/";
    partRecipes(consumer, TinkerToolParts.repairKit, TinkerSmeltery.repairKitCast, 2, partFolder, castFolder);
    // head
    partRecipes(consumer, TinkerToolParts.pickHead,     TinkerSmeltery.pickHeadCast,     2, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.hammerHead,   TinkerSmeltery.hammerHeadCast,   8, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.smallAxeHead, TinkerSmeltery.smallAxeHeadCast, 2, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.broadAxeHead, TinkerSmeltery.broadAxeHeadCast, 8, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.smallBlade,   TinkerSmeltery.smallBladeCast,   2, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.broadBlade,   TinkerSmeltery.broadBladeCast,   8, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.bowLimb,      TinkerSmeltery.bowLimbCast,      2, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.bowGrip,      TinkerSmeltery.bowGripCast,      2, partFolder, castFolder);
    // other parts
    partRecipes(consumer, TinkerToolParts.toolBinding,  TinkerSmeltery.toolBindingCast,  1, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.toughBinding, TinkerSmeltery.toughBindingCast, 3, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.adzeHead,     TinkerSmeltery.adzeHeadCast,     2, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.largePlate,   TinkerSmeltery.largePlateCast,   4, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.toolHandle,   TinkerSmeltery.toolHandleCast,   1, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.toughHandle,  TinkerSmeltery.toughHandleCast,  3, partFolder, castFolder);
    // armor
    partWithDummy(consumer, TinkerToolParts.plating.get(ArmorItem.Type.HELMET),     TinkerSmeltery.dummyPlating.get(ArmorItem.Type.HELMET),     TinkerSmeltery.helmetPlatingCast,     3, partFolder, castFolder);
    partWithDummy(consumer, TinkerToolParts.plating.get(ArmorItem.Type.CHESTPLATE), TinkerSmeltery.dummyPlating.get(ArmorItem.Type.CHESTPLATE), TinkerSmeltery.chestplatePlatingCast, 6, partFolder, castFolder);
    partWithDummy(consumer, TinkerToolParts.plating.get(ArmorItem.Type.LEGGINGS),   TinkerSmeltery.dummyPlating.get(ArmorItem.Type.LEGGINGS),   TinkerSmeltery.leggingsPlatingCast,   5, partFolder, castFolder);
    partWithDummy(consumer, TinkerToolParts.plating.get(ArmorItem.Type.BOOTS),      TinkerSmeltery.dummyPlating.get(ArmorItem.Type.BOOTS),      TinkerSmeltery.bootsPlatingCast,      2, partFolder, castFolder);
    partRecipes(consumer, TinkerToolParts.maille, TinkerSmeltery.mailleCast, 2, partFolder, castFolder);

    // bowstrings and shield cores are part builder exclusive. Shield core additionally disallows anything that conflicts with casting shield plating (obsidian/nahuatl conflict)
    uncastablePart(consumer, TinkerToolParts.bowstring.get(), 1, null, partFolder);
    uncastablePart(consumer, TinkerToolParts.shieldCore.get(), 4, PlatingMaterialStats.SHIELD.getId(), partFolder);
  }

  /** Helper to create a casting recipe for a slimeskull variant */
  private void slimeskullCasting(Consumer<FinishedRecipe> consumer, MaterialId material, ItemLike skull, String folder) {
    MaterialIdNBT nbt = new MaterialIdNBT(Collections.singletonList(material));
    ItemCastingRecipeBuilder.basinRecipe(ItemOutput.fromStack(nbt.updateStack(new ItemStack(TinkerTools.slimesuit.get(ArmorItem.Type.HELMET)))))
                            .setCast(skull, true)
                            .setFluidAndTime(TinkerFluids.enderSlime, FluidValues.SLIME_CONGEALED * 5)
                            .save(consumer, location(folder + "slime_skull/" + material.getPath()));
  }
}
