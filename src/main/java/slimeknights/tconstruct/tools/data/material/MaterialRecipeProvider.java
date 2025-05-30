package slimeknights.tconstruct.tools.data.material;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.DifferenceIngredient;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import net.minecraftforge.fluids.FluidType;
import slimeknights.mantle.datagen.MantleTags;
import slimeknights.mantle.recipe.condition.TagCombinationCondition;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.data.BaseRecipeProvider;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.function.Consumer;

import static slimeknights.mantle.Mantle.COMMON;

public class MaterialRecipeProvider extends BaseRecipeProvider implements IMaterialRecipeHelper {
  public MaterialRecipeProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  public String getName() {
    return "Tinkers' Construct Material Recipe";
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
    addMaterialItems(consumer);
    addMaterialSmeltery(consumer);
  }

  private void addMaterialItems(Consumer<FinishedRecipe> consumer) {
    String folder = "tools/materials/";
    // tier 1
    materialRecipe(consumer, MaterialIds.wood,   Ingredient.of(Tags.Items.RODS_WOODEN), 1, 2, folder + "wood/sticks");
    // planks
    materialRecipe(consumer, MaterialIds.oak,      Ingredient.of(Items.OAK_PLANKS),      1, 1, folder + "wood/planks/oak");
    materialRecipe(consumer, MaterialIds.spruce,   Ingredient.of(Items.SPRUCE_PLANKS),   1, 1, folder + "wood/planks/spruce");
    materialRecipe(consumer, MaterialIds.birch,    Ingredient.of(Items.BIRCH_PLANKS),    1, 1, folder + "wood/planks/birch");
    materialRecipe(consumer, MaterialIds.jungle,   Ingredient.of(Items.JUNGLE_PLANKS),   1, 1, folder + "wood/planks/jungle");
    materialRecipe(consumer, MaterialIds.darkOak,  Ingredient.of(Items.DARK_OAK_PLANKS), 1, 1, folder + "wood/planks/dark_oak");
    materialRecipe(consumer, MaterialIds.acacia,   Ingredient.of(Items.ACACIA_PLANKS),   1, 1, folder + "wood/planks/acacia");
    materialRecipe(consumer, MaterialIds.mangrove, Ingredient.of(Items.MANGROVE_PLANKS), 1, 1, folder + "wood/planks/mangrove");
    materialRecipe(consumer, MaterialIds.cherry,   Ingredient.of(Items.CHERRY_PLANKS),   1, 1, folder + "wood/planks/cherry");
    materialRecipe(consumer, MaterialIds.crimson,  Ingredient.of(Items.CRIMSON_PLANKS),  1, 1, folder + "wood/planks/crimson");
    materialRecipe(consumer, MaterialIds.warped,   Ingredient.of(Items.WARPED_PLANKS),   1, 1, folder + "wood/planks/warped");
    materialRecipe(withCondition(consumer, TagCombinationCondition.difference(ItemTags.PLANKS, TinkerTags.Items.VARIANT_PLANKS)), MaterialIds.wood,
                   DifferenceIngredient.of(Ingredient.of(ItemTags.PLANKS), Ingredient.of(TinkerTags.Items.VARIANT_PLANKS)), 1, 1, folder + "wood/planks/default");
    // logs
    materialRecipe(consumer, MaterialIds.oak,      Ingredient.of(ItemTags.OAK_LOGS),      4, 1, ItemOutput.fromItem(Blocks.OAK_PLANKS),      folder + "wood/logs/oak");
    materialRecipe(consumer, MaterialIds.spruce,   Ingredient.of(ItemTags.SPRUCE_LOGS),   4, 1, ItemOutput.fromItem(Blocks.SPRUCE_PLANKS),   folder + "wood/logs/spruce");
    materialRecipe(consumer, MaterialIds.birch,    Ingredient.of(ItemTags.BIRCH_LOGS),    4, 1, ItemOutput.fromItem(Blocks.BIRCH_PLANKS),    folder + "wood/logs/birch");
    materialRecipe(consumer, MaterialIds.jungle,   Ingredient.of(ItemTags.JUNGLE_LOGS),   4, 1, ItemOutput.fromItem(Blocks.JUNGLE_PLANKS),   folder + "wood/logs/jungle");
    materialRecipe(consumer, MaterialIds.darkOak,  Ingredient.of(ItemTags.DARK_OAK_LOGS), 4, 1, ItemOutput.fromItem(Blocks.DARK_OAK_PLANKS), folder + "wood/logs/dark_oak");
    materialRecipe(consumer, MaterialIds.acacia,   Ingredient.of(ItemTags.ACACIA_LOGS),   4, 1, ItemOutput.fromItem(Blocks.ACACIA_PLANKS),   folder + "wood/logs/acacia");
    materialRecipe(consumer, MaterialIds.mangrove, Ingredient.of(ItemTags.MANGROVE_LOGS), 4, 1, ItemOutput.fromItem(Blocks.MANGROVE_PLANKS), folder + "wood/logs/mangrove");
    materialRecipe(consumer, MaterialIds.cherry,   Ingredient.of(ItemTags.CHERRY_LOGS),   4, 1, ItemOutput.fromItem(Blocks.CHERRY_PLANKS),   folder + "wood/logs/cherry");
    materialRecipe(consumer, MaterialIds.crimson,  Ingredient.of(ItemTags.CRIMSON_STEMS), 4, 1, ItemOutput.fromItem(Blocks.CRIMSON_PLANKS),  folder + "wood/logs/crimson");
    materialRecipe(consumer, MaterialIds.warped,   Ingredient.of(ItemTags.WARPED_STEMS),  4, 1, ItemOutput.fromItem(Blocks.WARPED_PLANKS),   folder + "wood/logs/warped");
    materialRecipe(withCondition(consumer, TagCombinationCondition.difference(ItemTags.LOGS, TinkerTags.Items.VARIANT_LOGS)), MaterialIds.wood,
                   DifferenceIngredient.of(Ingredient.of(ItemTags.LOGS), Ingredient.of(TinkerTags.Items.VARIANT_LOGS)), 4, 1,
                   ItemOutput.fromItem(Items.STICK, 2), folder + "wood/logs/default");
    // bamboo
    materialRecipe(consumer, MaterialIds.bamboo, Ingredient.of(Items.BAMBOO),           1, 9, folder + "wood/bamboo/stick");
    materialRecipe(consumer, MaterialIds.bamboo, Ingredient.of(ItemTags.BAMBOO_BLOCKS), 1, 1, folder + "wood/bamboo/block");
    materialRecipe(consumer, MaterialIds.bamboo, Ingredient.of(Blocks.BAMBOO_PLANKS),   1, 2, folder + "wood/bamboo/planks");
    // stone
    materialRecipe(consumer, MaterialIds.stone,      Ingredient.of(TinkerTags.Items.STONE),      1, 1, folder + "rock/stone");
    materialRecipe(consumer, MaterialIds.andesite,   Ingredient.of(TinkerTags.Items.ANDESITE),   1, 1, folder + "rock/andesite");
    materialRecipe(consumer, MaterialIds.diorite,    Ingredient.of(TinkerTags.Items.DIORITE),    1, 1, folder + "rock/diorite");
    materialRecipe(consumer, MaterialIds.granite,    Ingredient.of(TinkerTags.Items.GRANITE),    1, 1, folder + "rock/granite");
    materialRecipe(consumer, MaterialIds.deepslate,  Ingredient.of(TinkerTags.Items.DEEPSLATE),  1, 1, folder + "rock/deepslate");
    materialRecipe(consumer, MaterialIds.blackstone, Ingredient.of(TinkerTags.Items.BLACKSTONE), 1, 1, folder + "rock/blackstone");
    materialRecipe(consumer, MaterialIds.flint,      Ingredient.of(Items.FLINT),                 1, 1, folder + "flint");
    materialRecipe(consumer, MaterialIds.basalt,     Ingredient.of(TinkerTags.Items.BASALT),     1, 1, folder + "flint_basalt");
    // other tier 1
    materialRecipe(consumer, MaterialIds.bone,         Ingredient.of(Tags.Items.BONES),              1, 1, folder + "bone");
    materialRecipe(consumer, MaterialIds.chorus,       Ingredient.of(Items.POPPED_CHORUS_FRUIT),     1, 4, folder + "chorus_popped");
    metalMaterialRecipe(consumer, MaterialIds.copper, folder, "copper", false);
    // tier 1 binding
    materialRecipe(consumer, MaterialIds.string,       Ingredient.of(Tags.Items.STRING),             1, 4, folder + "string");
    materialRecipe(consumer, MaterialIds.leather,      Ingredient.of(Tags.Items.LEATHER),            1, 1, folder + "leather");
    materialRecipe(consumer, MaterialIds.leather,      Ingredient.of(Items.RABBIT_HIDE),             1, 2, folder + "rabbit_hide");
    materialRecipe(consumer, MaterialIds.vine,         Ingredient.of(Items.VINE, Items.TWISTING_VINES, Items.WEEPING_VINES), 1, 1, folder + "vine");

    // tier 2
    metalMaterialRecipe(consumer, MaterialIds.iron, folder, "iron", false);
    materialRecipe(consumer, MaterialIds.searedStone,   Ingredient.of(TinkerSmeltery.searedBrick),       1, 1, folder + "seared_stone/brick");
    materialRecipe(consumer, MaterialIds.searedStone,   Ingredient.of(TinkerTags.Items.SEARED_BLOCKS),   4, 1, ItemOutput.fromItem(TinkerSmeltery.searedBrick), folder + "seared_stone/block");
    materialRecipe(consumer, MaterialIds.scorchedStone, Ingredient.of(TinkerSmeltery.scorchedBrick),     1, 1, folder + "scorched_stone/brick");
    materialRecipe(consumer, MaterialIds.scorchedStone, Ingredient.of(TinkerTags.Items.SCORCHED_BLOCKS), 4, 1, ItemOutput.fromItem(TinkerSmeltery.scorchedBrick), folder + "scorched_stone/block");
    materialRecipe(consumer, MaterialIds.venombone,     Ingredient.of(TinkerMaterials.venombone),        1, 1, folder + "venombone");
    metalMaterialRecipe(consumer, MaterialIds.roseGold, folder, "rose_gold", false);
    materialRecipe(consumer, MaterialIds.necroticBone, Ingredient.of(TinkerTags.Items.WITHER_BONES), 1, 1, folder + "necrotic_bone");
    materialRecipe(consumer, MaterialIds.endstone, Ingredient.of(Tags.Items.END_STONES), 1, 2, folder + "endstone");

    materialRecipe(consumer, MaterialIds.skyslimeVine, Ingredient.of(TinkerWorld.skySlimeVine), 1, 1, folder + "skyslime_vine");
    // slimewood
    materialRecipe(consumer, MaterialIds.greenheart,  Ingredient.of(TinkerWorld.greenheart),  1, 1, folder + "slimewood/greenheart_planks");
    materialRecipe(consumer, MaterialIds.skyroot,     Ingredient.of(TinkerWorld.skyroot),     1, 1, folder + "slimewood/skyroot_planks");
    materialRecipe(consumer, MaterialIds.bloodshroom, Ingredient.of(TinkerWorld.bloodshroom), 1, 1, folder + "slimewood/bloodshroom_planks");
    materialRecipe(consumer, MaterialIds.enderbark,   Ingredient.of(TinkerWorld.enderbark),   1, 1, folder + "slimewood/enderbark_planks");
    materialRecipe(consumer, MaterialIds.greenheart,  Ingredient.of(TinkerWorld.greenheart.getLogItemTag()),  4, 1, ItemOutput.fromItem(TinkerWorld.greenheart),  folder + "slimewood/greenheart_logs");
    materialRecipe(consumer, MaterialIds.skyroot,     Ingredient.of(TinkerWorld.skyroot.getLogItemTag()),     4, 1, ItemOutput.fromItem(TinkerWorld.skyroot),     folder + "slimewood/skyroot_logs");
    materialRecipe(consumer, MaterialIds.bloodshroom, Ingredient.of(TinkerWorld.bloodshroom.getLogItemTag()), 4, 1, ItemOutput.fromItem(TinkerWorld.bloodshroom), folder + "slimewood/bloodshroom_logs");
    materialRecipe(consumer, MaterialIds.enderbark,   Ingredient.of(TinkerWorld.enderbark.getLogItemTag()),   4, 1, ItemOutput.fromItem(TinkerWorld.enderbark),   folder + "slimewood/enderbark_logs");

    // tier 3
    metalMaterialRecipe(consumer, MaterialIds.slimesteel, folder, "slimesteel", false);
    materialRecipe(consumer, MaterialIds.nahuatl, Ingredient.of(TinkerMaterials.nahuatl), 1, 1, folder + "nahuatl");
    metalMaterialRecipe(consumer, MaterialIds.amethystBronze, folder, "amethyst_bronze", false);
    metalMaterialRecipe(consumer, MaterialIds.pigIron, folder, "pig_iron", false);
    materialRecipe(consumer, MaterialIds.obsidian, Ingredient.of(Items.OBSIDIAN), 1, 1, folder + "obsidian");

    // tier 2 (nether)
    // tier 3 (nether)
    metalMaterialRecipe(consumer, MaterialIds.cobalt, folder, "cobalt", false);
    metalMaterialRecipe(consumer, MaterialIds.steel,  folder, "steel",  false);
    // tier 4
    metalMaterialRecipe(consumer, MaterialIds.cinderslime, folder, "cinderslime", false);
    metalMaterialRecipe(consumer, MaterialIds.queensSlime, folder, "queens_slime", false);
    metalMaterialRecipe(consumer, MaterialIds.manyullyn, folder, "manyullyn", false);
    metalMaterialRecipe(consumer, MaterialIds.hepatizon, folder, "hepatizon", false);
    materialRecipe(consumer, MaterialIds.blazewood, Ingredient.of(TinkerMaterials.blazewood), 1, 1, folder + "blazewood");
    materialRecipe(consumer, MaterialIds.blazingBone, Ingredient.of(TinkerMaterials.blazingBone), 1, 1, folder + "blazing_bone");
    //registerMetalMaterial(consumer, MaterialIds.soulsteel,   "soulsteel",    false);
    // debris has no storage block, just ingots and nuggets
    materialRecipe(consumer, MaterialIds.ancient, Ingredient.of(TinkerTags.Items.INGOTS_NETHERITE_SCRAP), 1, 1, folder + "ancient/ingot");
    materialRecipe(consumer, MaterialIds.ancient, Ingredient.of(TinkerTags.Items.NUGGETS_NETHERITE_SCRAP), 1, 9, folder + "ancient/nugget");

    // tier 5
    materialRecipe(consumer, MaterialIds.enderslimeVine, Ingredient.of(TinkerWorld.enderSlimeVine), 1, 1, folder + "enderslime_vine");

    // tier 2 (mod compat)
    metalMaterialRecipe(consumer, MaterialIds.osmium, folder, "osmium", true);
    metalMaterialRecipe(consumer, MaterialIds.tungsten, folder, "tungsten", true);
    metalMaterialRecipe(consumer, MaterialIds.platinum, folder, "platinum", true);
    metalMaterialRecipe(consumer, MaterialIds.silver, folder, "silver", true);
    metalMaterialRecipe(consumer, MaterialIds.lead, folder, "lead", true);
    materialRecipe(withCondition(consumer, tagCondition("treated_wood")),  MaterialIds.treatedWood, Ingredient.of(getItemTag(COMMON, "treated_wood")), 1, 1, folder + "treated_wood");
    // no whitestone, use repair kits
    // tier 3 (mod integration)
    metalMaterialRecipe(consumer, MaterialIds.bronze, folder, "bronze", true);
    metalMaterialRecipe(consumer, MaterialIds.constantan, folder, "constantan", true);
    metalMaterialRecipe(consumer, MaterialIds.invar, folder, "invar", true);
    materialRecipe(withCondition(consumer, tagCondition("ingots/uranium")), MaterialIds.necronium, Ingredient.of(TinkerMaterials.necroniumBone), 1, 1, folder + "necronium");
    metalMaterialRecipe(consumer, MaterialIds.electrum, folder, "electrum", true);
    // no plated slimewood, use repair kits

    // slimeskull
    metalMaterialRecipe(consumer, MaterialIds.gold, folder, "gold", false);
    materialRecipe(consumer, MaterialIds.glass,       Ingredient.of(Tags.Items.GLASS),                        1, 1, folder + "glass");
    materialRecipe(consumer, MaterialIds.glass,       Ingredient.of(Tags.Items.GLASS_PANES),                  1, 4, folder + "glass_pane");
    materialRecipe(consumer, MaterialIds.enderPearl,  Ingredient.of(Tags.Items.ENDER_PEARLS),                 1, 1, folder + "ender_pearl");
    materialRecipe(consumer, MaterialIds.rottenFlesh, Ingredient.of(Items.ROTTEN_FLESH),                      1, 1, folder + "rotten_flesh");
    // slimesuit
    materialRecipe(consumer, MaterialIds.enderslime, Ingredient.of(TinkerCommons.slimeball.get(SlimeType.ENDER)),    1, 1, folder + "enderslime/ball");
    materialRecipe(consumer, MaterialIds.enderslime, Ingredient.of(TinkerWorld.congealedSlime.get(SlimeType.ENDER)), 4, 1, folder + "enderslime/congealed");
    materialRecipe(consumer, MaterialIds.enderslime, Ingredient.of(TinkerWorld.slime.get(SlimeType.ENDER)),          9, 1, folder + "enderslime/block");
    materialRecipe(consumer, MaterialIds.phantom,    Ingredient.of(Items.PHANTOM_MEMBRANE),    1, 1, folder + "phantom_membrane");
  }

  private void addMaterialSmeltery(Consumer<FinishedRecipe> consumer) {
    String folder = "tools/materials/";

    // melting and casting
    // tier 2
    materialMeltingCasting(consumer, MaterialIds.iron,          TinkerFluids.moltenIron,    folder);
    materialMeltingCasting(consumer, MaterialIds.copper,        TinkerFluids.moltenCopper,  folder);
    materialMeltingCasting(consumer, MaterialIds.searedStone,   TinkerFluids.searedStone,   FluidValues.BRICK, folder);
    materialMeltingCasting(consumer, MaterialIds.scorchedStone, TinkerFluids.scorchedStone, FluidValues.BRICK, folder);
    // half a clay is 1 seared brick per grout amounts
    materialComposite(consumer, MaterialIds.rock, MaterialIds.searedStone,        TinkerFluids.moltenClay, FluidValues.BRICK / 2, folder);
    materialComposite(consumer, MaterialIds.wood, MaterialIds.slimewoodComposite, TinkerFluids.earthSlime, FluidValues.SLIMEBALL, folder);
    materialComposite(consumer, MaterialIds.flint, MaterialIds.scorchedStone,     TinkerFluids.magma,      FluidValues.SLIMEBALL / 2, folder);
    materialComposite(consumer, MaterialIds.bone, MaterialIds.venombone,          TinkerFluids.venom,      FluidValues.SLIMEBALL, folder);
    // oxidize copper and iron via water, it does not rust iron because magic
    MaterialFluidRecipeBuilder.material(MaterialIds.oxidizedIron)
                              .setInputId(MaterialIds.iron)
                              .setFluid(MantleTags.Fluids.WATER, FluidValues.BOTTLE)
                              .setTemperature(1)
                              .save(consumer, location(folder + "composite/iron_oxidized"));
    MaterialFluidRecipeBuilder.material(MaterialIds.oxidizedCopper)
                              .setInputId(MaterialIds.copper)
                              .setFluid(MantleTags.Fluids.WATER, FluidValues.BOTTLE)
                              .setTemperature(1)
                              .save(consumer, location(folder + "composite/copper_oxidized"));

    // tier 3
    materialMeltingCasting(consumer, MaterialIds.slimesteel,     TinkerFluids.moltenSlimesteel, folder);
    materialMeltingCasting(consumer, MaterialIds.amethystBronze, TinkerFluids.moltenAmethystBronze, folder);
    materialMeltingCasting(consumer, MaterialIds.roseGold,       TinkerFluids.moltenRoseGold, folder);
    materialMeltingCasting(consumer, MaterialIds.pigIron,        TinkerFluids.moltenPigIron, folder);
    materialMeltingCasting(consumer, MaterialIds.cobalt,         TinkerFluids.moltenCobalt, folder);
    materialMeltingCasting(consumer, MaterialIds.steel,          TinkerFluids.moltenSteel, folder);
    materialMeltingCasting(consumer, MaterialIds.obsidian,       TinkerFluids.moltenObsidian, FluidValues.GLASS_BLOCK, folder);
    // allow rose gold as a bowstring by string composite, means we also get a redundant binding recipe, but thats fine
    materialComposite(consumer,        MaterialIds.string, MaterialIds.roseGold,   TinkerFluids.moltenRoseGold, FluidValues.INGOT, folder);
    materialMeltingComposite(consumer, MaterialIds.wood,   MaterialIds.nahuatl,    TinkerFluids.moltenObsidian, FluidValues.GLASS_BLOCK, folder);
    materialMeltingComposite(consumer, MaterialIds.string, MaterialIds.darkthread, TinkerFluids.moltenObsidian, FluidValues.GLASS_PANE, folder);

    // tier 4
    materialMeltingCasting(consumer, MaterialIds.cinderslime, TinkerFluids.moltenCinderslime, folder);
    materialMeltingCasting(consumer, MaterialIds.queensSlime, TinkerFluids.moltenQueensSlime, folder);
    materialMeltingCasting(consumer, MaterialIds.hepatizon,   TinkerFluids.moltenHepatizon,   folder);
    materialMeltingCasting(consumer, MaterialIds.manyullyn,   TinkerFluids.moltenManyullyn,   folder);
    materialComposite(consumer, MaterialIds.bloodshroom,  MaterialIds.blazewood,   TinkerFluids.blazingBlood, FluidType.BUCKET_VOLUME / 5, folder);
    materialComposite(consumer, MaterialIds.necroticBone, MaterialIds.blazingBone, TinkerFluids.blazingBlood, FluidType.BUCKET_VOLUME / 5, folder);
    materialMeltingComposite(consumer, MaterialIds.leather, MaterialIds.ancientHide, TinkerFluids.moltenDebris, FluidValues.INGOT, folder);
    // no casting ancient, only melting it. Smeltery Recipe Provider adds in a repair kit casting
    materialMelting(consumer, MaterialIds.ancient, TinkerFluids.moltenDebris, FluidValues.INGOT, folder);

    // tier 2 compat
    compatMeltingCasting(consumer, MaterialIds.osmium,   TinkerFluids.moltenOsmium,   folder);
    compatMeltingCasting(consumer, MaterialIds.tungsten, TinkerFluids.moltenTungsten, folder);
    compatMeltingCasting(consumer, MaterialIds.platinum, TinkerFluids.moltenPlatinum, folder);
    compatMeltingCasting(consumer, MaterialIds.silver,   TinkerFluids.moltenSilver,   folder);
    compatMeltingCasting(consumer, MaterialIds.lead,     TinkerFluids.moltenLead,     folder);
    compatMeltingCasting(consumer, MaterialIds.aluminum, TinkerFluids.moltenAluminum, folder);
    materialComposite(withCondition(consumer, tagCondition("ingots/aluminum")), MaterialIds.rock, MaterialIds.whitestoneAluminum, TinkerFluids.moltenAluminum, FluidValues.INGOT, folder, "whitestone_from_aluminum");
    materialComposite(withCondition(consumer, tagCondition("ingots/tin")),      MaterialIds.rock, MaterialIds.whitestoneTin,      TinkerFluids.moltenTin,      FluidValues.INGOT, folder, "whitestone_from_tin");
    materialComposite(withCondition(consumer, tagCondition("ingots/zinc")),     MaterialIds.rock, MaterialIds.whitestoneZinc,     TinkerFluids.moltenZinc,     FluidValues.INGOT, folder, "whitestone_from_zinc");
    TagKey<Fluid> creosote = getFluidTag(COMMON, "creosote");
    MaterialFluidRecipeBuilder.material(MaterialIds.treatedWood)
      .setInputId(MaterialIds.wood)
      .setFluid(FluidIngredient.of(creosote, 125))
      .setTemperature(600)
      .save(withCondition(consumer, new TagFilledCondition<>(creosote)), location(folder + "composite/treated_wood"));
    // tier 3 compat
    compatMeltingCasting(consumer, MaterialIds.constantan,     TinkerFluids.moltenConstantan, "nickel", folder);
    compatMeltingCasting(consumer, MaterialIds.invar,          TinkerFluids.moltenInvar,      "nickel", folder);
    compatMeltingCasting(consumer, MaterialIds.electrum,       TinkerFluids.moltenElectrum,   "silver", folder);
    compatMeltingCasting(consumer, MaterialIds.bronze,         TinkerFluids.moltenBronze,     "tin", folder);
    materialMeltingComposite(withCondition(consumer, tagCondition("ingots/uranium")), MaterialIds.necroticBone, MaterialIds.necronium, TinkerFluids.moltenUranium, FluidValues.INGOT, folder);
    materialMeltingComposite(withCondition(consumer, new OrCondition(tagCondition("ingots/brass"), tagCondition("ingots/zinc"))),
                             MaterialIds.slimewood, MaterialIds.platedSlimewood, TinkerFluids.moltenBrass, FluidValues.INGOT, folder);

    // slimesuit
    materialMeltingCasting(consumer, MaterialIds.gold, TinkerFluids.moltenGold, folder);
    materialMeltingCasting(consumer, MaterialIds.enderPearl, TinkerFluids.moltenEnder, FluidValues.SLIMEBALL, folder);
    materialMeltingCasting(consumer, MaterialIds.glass, TinkerFluids.moltenGlass, FluidValues.GLASS_BLOCK, folder);
    materialMeltingCasting(consumer, MaterialIds.enderslime, TinkerFluids.enderSlime, FluidValues.SLIMEBALL, folder);
  }
}
