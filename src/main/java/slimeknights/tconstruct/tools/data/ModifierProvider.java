package slimeknights.tconstruct.tools.data;

import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidType;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.block.BlockPredicate;
import slimeknights.mantle.data.predicate.block.BlockPropertiesPredicate;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.mantle.data.predicate.damage.DamageTypePredicate;
import slimeknights.mantle.data.predicate.damage.SourceAttackerPredicate;
import slimeknights.mantle.data.predicate.entity.HasEnchantmentEntityPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.data.predicate.entity.MobTypePredicate;
import slimeknights.mantle.data.predicate.item.ItemPredicate;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.json.LevelingInt;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.json.RandomLevelingValue;
import slimeknights.tconstruct.library.json.predicate.HasMobEffectPredicate;
import slimeknights.tconstruct.library.json.predicate.TinkerPredicate;
import slimeknights.tconstruct.library.json.predicate.tool.HasModifierPredicate;
import slimeknights.tconstruct.library.json.predicate.tool.ToolContextPredicate;
import slimeknights.tconstruct.library.json.variable.block.BlockVariable;
import slimeknights.tconstruct.library.json.variable.entity.AttributeEntityVariable;
import slimeknights.tconstruct.library.json.variable.entity.ConditionalEntityVariable;
import slimeknights.tconstruct.library.json.variable.entity.EntityEffectLevelVariable;
import slimeknights.tconstruct.library.json.variable.entity.EntityVariable;
import slimeknights.tconstruct.library.json.variable.melee.EntityMeleeVariable;
import slimeknights.tconstruct.library.json.variable.melee.EntityMeleeVariable.WhichEntity;
import slimeknights.tconstruct.library.json.variable.mining.BlockLightVariable;
import slimeknights.tconstruct.library.json.variable.mining.BlockMiningSpeedVariable;
import slimeknights.tconstruct.library.json.variable.stat.EntityConditionalStatVariable;
import slimeknights.tconstruct.library.json.variable.tool.ToolStatVariable;
import slimeknights.tconstruct.library.json.variable.tool.ToolVariable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier.TooltipDisplay;
import slimeknights.tconstruct.library.modifiers.modules.armor.BlockDamageSourceModule;
import slimeknights.tconstruct.library.modifiers.modules.armor.CoverGroundWalkerModule;
import slimeknights.tconstruct.library.modifiers.modules.armor.EffectImmunityModule;
import slimeknights.tconstruct.library.modifiers.modules.armor.MaxArmorAttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.armor.MobDisguiseModule;
import slimeknights.tconstruct.library.modifiers.modules.armor.ProtectionModule;
import slimeknights.tconstruct.library.modifiers.modules.armor.ReplaceBlockWalkerModule;
import slimeknights.tconstruct.library.modifiers.modules.armor.ToolActionWalkerTransformModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule.TooltipStyle;
import slimeknights.tconstruct.library.modifiers.modules.behavior.ConditionalStatModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.MaterialRepairModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.ReduceToolDamageModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.RepairModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.ShowOffhandModule;
import slimeknights.tconstruct.library.modifiers.modules.behavior.ToolActionTransformModule;
import slimeknights.tconstruct.library.modifiers.modules.build.EnchantmentModule;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierRequirementsModule;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierSlotModule;
import slimeknights.tconstruct.library.modifiers.modules.build.RarityModule;
import slimeknights.tconstruct.library.modifiers.modules.build.SetStatModule;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.modifiers.modules.build.StatCopyModule;
import slimeknights.tconstruct.library.modifiers.modules.build.SwappableSlotModule;
import slimeknights.tconstruct.library.modifiers.modules.build.SwappableToolTraitsModule;
import slimeknights.tconstruct.library.modifiers.modules.build.VolatileFlagModule;
import slimeknights.tconstruct.library.modifiers.modules.combat.ConditionalMeleeDamageModule;
import slimeknights.tconstruct.library.modifiers.modules.combat.KnockbackModule;
import slimeknights.tconstruct.library.modifiers.modules.combat.LootingModule;
import slimeknights.tconstruct.library.modifiers.modules.combat.MeleeAttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.combat.MobEffectModule;
import slimeknights.tconstruct.library.modifiers.modules.display.DurabilityBarColorModule;
import slimeknights.tconstruct.library.modifiers.modules.display.ModifierVariantNameModule;
import slimeknights.tconstruct.library.modifiers.modules.mining.ConditionalMiningSpeedModule;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay.UniqueForLevels;
import slimeknights.tconstruct.library.recipe.modifiers.adding.SwappableModifierRecipe.VariantFormatter;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.capability.TinkerDataKeys;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.capability.inventory.InventoryMenuModule;
import slimeknights.tconstruct.library.tools.capability.inventory.InventoryModule;
import slimeknights.tconstruct.library.tools.capability.inventory.InventorySlotMenuModule;
import slimeknights.tconstruct.library.tools.capability.inventory.ToolInventoryCapability;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.shared.TinkerAttributes;
import slimeknights.tconstruct.shared.TinkerEffects;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.logic.ModifierEvents;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;
import slimeknights.tconstruct.tools.modules.HeadlightModule;
import slimeknights.tconstruct.tools.modules.MeltingModule;
import slimeknights.tconstruct.tools.modules.OverburnModule;
import slimeknights.tconstruct.tools.modules.OvergrowthModule;
import slimeknights.tconstruct.tools.modules.SmeltingModule;
import slimeknights.tconstruct.tools.modules.TheOneProbeModule;
import slimeknights.tconstruct.tools.modules.armor.DepthProtectionModule;
import slimeknights.tconstruct.tools.modules.armor.EnderclearanceModule;
import slimeknights.tconstruct.tools.modules.armor.FlameBarrierModule;
import slimeknights.tconstruct.tools.modules.armor.GlowWalkerModule;
import slimeknights.tconstruct.tools.modules.armor.KineticModule;
import slimeknights.tconstruct.tools.modules.armor.LightspeedAttributeModule;
import slimeknights.tconstruct.tools.modules.armor.RecurrentProtectionModule;
import slimeknights.tconstruct.tools.modules.armor.ShieldStrapModule;
import slimeknights.tconstruct.tools.modules.armor.ToolBeltModule;
import slimeknights.tconstruct.tools.modules.interaction.ExtinguishCampfireModule;
import slimeknights.tconstruct.tools.modules.interaction.PlaceGlowModule;
import slimeknights.tconstruct.tools.modules.ranged.BulkQuiverModule;
import slimeknights.tconstruct.tools.modules.ranged.RestrictAngleModule;
import slimeknights.tconstruct.tools.modules.ranged.TrickQuiverModule;

import static slimeknights.tconstruct.common.TinkerTags.Items.ARMOR;
import static slimeknights.tconstruct.common.TinkerTags.Items.HARVEST;
import static slimeknights.tconstruct.common.TinkerTags.Items.MELEE;
import static slimeknights.tconstruct.common.TinkerTags.Items.WORN_ARMOR;
import static slimeknights.tconstruct.library.json.math.ModifierFormula.LEVEL;
import static slimeknights.tconstruct.library.json.math.ModifierFormula.MULTIPLIER;
import static slimeknights.tconstruct.library.json.math.ModifierFormula.VALUE;
import static slimeknights.tconstruct.library.modifiers.modules.behavior.RepairModule.FACTOR;
import static slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial.ARMOR_SLOTS;

public class ModifierProvider extends AbstractModifierProvider implements IConditionBuilder {
  public ModifierProvider(PackOutput packOutput) {
    super(packOutput);
  }

  @Override
  protected void addModifiers() {
    EquipmentSlot[] handSlots = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};
    EquipmentSlot[] armorSlots = ARMOR_SLOTS;
    EquipmentSlot[] armorMainHand = {EquipmentSlot.MAINHAND, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};

    // extra modifier slots
    ModifierSlotModule UPGRADE = ModifierSlotModule.slot(SlotType.UPGRADE).eachLevel(1);
    buildModifier(ModifierIds.writable)   .tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(UPGRADE);
    buildModifier(ModifierIds.recapitated).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(UPGRADE);
    buildModifier(ModifierIds.harmonious) .tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(UPGRADE);
    buildModifier(ModifierIds.resurrected).tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(UPGRADE);
    buildModifier(ModifierIds.gilded)     .tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(ModifierSlotModule.slot(SlotType.UPGRADE).eachLevel(2));
    buildModifier(ModifierIds.draconic)   .tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(ModifierSlotModule.slot(SlotType.ABILITY).eachLevel(1));
    IJsonPredicate<IToolContext> ancientTool = ToolContextPredicate.tag(TinkerTags.Items.ANCIENT_TOOLS);
    buildModifier(ModifierIds.rebalanced)
      .tooltipDisplay(TooltipDisplay.TINKER_STATION).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(new SwappableSlotModule(1))
      .addModule(new SwappableSlotModule(null, 1, ModifierCondition.ANY_CONTEXT.with(ancientTool)), ModifierHooks.VOLATILE_DATA)
      .addModule(new SwappableSlotModule.BonusSlot(null, SlotType.ABILITY, SlotType.UPGRADE, -1, ModifierCondition.ANY_CONTEXT.with(ancientTool.inverted())))
      .addModule(new SwappableSlotModule.BonusSlot(null, SlotType.ABILITY, SlotType.ABILITY, -1, ModifierCondition.ANY_CONTEXT.with(ancientTool)))
      .addModule(new SwappableToolTraitsModule(null, "traits", ToolHooks.REBALANCED_TRAIT));

    // tier upgrades
    // emerald
    buildModifier(ModifierIds.emerald)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(new RarityModule(Rarity.UNCOMMON))
      .addModule(StatBoostModule.multiplyBase(ToolStats.DURABILITY).flat(0.5f))
      .addModule(RepairModule.builder().flat(0.5f))
      // armor
      .addModule(StatBoostModule.add(ToolStats.ARMOR_TOUGHNESS).flat(1))
      // melee harvest
      .addModule(StatBoostModule.multiplyConditional(ToolStats.ATTACK_DAMAGE).flat(0.25f))
      .addModule(StatBoostModule.multiplyConditional(ToolStats.MINING_SPEED).flat(0.25f))
      .addModule(SetStatModule.set(ToolStats.HARVEST_TIER).value(Tiers.IRON))
      // ranged
      .addModule(StatBoostModule.add(ToolStats.ACCURACY).flat(0.1f));
    // diamond
    buildModifier(ModifierIds.diamond)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(new RarityModule(Rarity.UNCOMMON))
      .addModule(StatBoostModule.add(ToolStats.DURABILITY).flat(500))
      // armor grants less durability boost
      .addModule(StatBoostModule.add(ToolStats.DURABILITY).toolItem(ItemPredicate.tag(ARMOR)).flat(-250))
      .addModule(StatBoostModule.add(ToolStats.ARMOR).flat(1))
      // melee harvest
      .addModule(StatBoostModule.add(ToolStats.ATTACK_DAMAGE).flat(0.5f))
      .addModule(StatBoostModule.add(ToolStats.MINING_SPEED).flat(2))
      .addModule(SetStatModule.set(ToolStats.HARVEST_TIER).value(Tiers.DIAMOND))
      // ranged
      .addModule(StatBoostModule.add(ToolStats.PROJECTILE_DAMAGE).flat(0.5f));
    // netherite
    buildModifier(ModifierIds.netherite)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(new RarityModule(Rarity.RARE))
      .addModule(new VolatileFlagModule(IModifiable.INDESTRUCTIBLE_ENTITY))
      .addModule(StatBoostModule.multiplyBase(ToolStats.DURABILITY).flat(0.2f))
      // armor
      .addModule(StatBoostModule.add(ToolStats.ARMOR_TOUGHNESS).flat(1))
      .addModule(StatBoostModule.add(ToolStats.KNOCKBACK_RESISTANCE).flat(0.05f))
      // melee harvest
      .addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_DAMAGE).flat(0.2f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.MINING_SPEED).flat(0.25f))
      .addModule(SetStatModule.set(ToolStats.HARVEST_TIER).value(Tiers.NETHERITE))
      // ranged
      .addModule(StatBoostModule.multiplyBase(ToolStats.VELOCITY).flat(0.1f));

    // general
    buildModifier(ModifierIds.worldbound).addModule(new VolatileFlagModule(IModifiable.INDESTRUCTIBLE_ENTITY)).addModule(new RarityModule(Rarity.UNCOMMON)).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
    buildModifier(ModifierIds.shiny).addModule(new VolatileFlagModule(IModifiable.SHINY)).addModule(new RarityModule(Rarity.EPIC)).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
    buildModifier(ModifierIds.offhanded)
      .addModule(new VolatileFlagModule(IModifiable.DEFER_OFFHAND))
      .addModule(new VolatileFlagModule(IModifiable.NO_INTERACTION, new ModifierCondition<>(ToolContextPredicate.ANY, ModifierEntry.VALID_LEVEL.min(2))))
      .levelDisplay(new UniqueForLevels(2));
    // general abilities
    buildModifier(ModifierIds.reach)
      .addModule(AttributeModule.builder(ForgeMod.BLOCK_REACH.get(), Operation.ADDITION).eachLevel(1))
      .addModule(AttributeModule.builder(ForgeMod.ENTITY_REACH.get(), Operation.ADDITION).eachLevel(1));
    buildModifier(ModifierIds.glowing)
      .levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL)
      .addModule(ShowOffhandModule.DISALLOW_BROKEN)
      .addModule(new PlaceGlowModule(5))
      .addModule(new GlowWalkerModule(new LevelingValue(2, 1), 3, 5));
    buildModifier(TinkerModifiers.melting)
      .levelDisplay(ModifierLevelDisplay.PLUSES)
      .addModule(ToolTankHelper.TANK_HANDLER)
      .addModule(StatBoostModule.add(ToolTankHelper.CAPACITY_STAT).eachLevel(FluidType.BUCKET_VOLUME))
      // give a bonus 500 degrees and a bonus 3 nuggets and 50% of a gem at level 2
      .addModule(MeltingModule.builder().temperature(new LevelingInt(500, 500)).nuggetsPerMetal(new LevelingInt(9, 3)).shardsPerGem(new LevelingInt(6, 2)).build());
    IJsonPredicate<IToolContext> noUnbreakable = HasModifierPredicate.hasModifier(TinkerModifiers.unbreakable.getId(), 1).inverted();
    buildModifier(ModifierIds.reinforced)
      // level 0 to 5: 0.025 * LEVEL * (11 - LEVEL)
      .addModule(ReduceToolDamageModule.builder().toolContext(noUnbreakable).maxLevel(5).formula()
                                       .constant(0.025f).variable(LEVEL).multiply() // 0.025 * level
                                       .constant(11).variable(LEVEL).subtract()     // 11 - level
                                       .multiply().build())
      // level 6+: 0.5 + level * 0.05
      .addModule(ReduceToolDamageModule.builder().toolContext(noUnbreakable).minLevel(6).amount(0.5f, 0.05f));
    // unbreakable priority is after overslime but before standard modifiers like dense
    buildModifier(TinkerModifiers.unbreakable)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS).priority(125)
      .addModule(ModifierRequirementsModule.builder().requireModifier(ModifierIds.netherite, 1).requireModifier(ModifierIds.reinforced, 5).modifierKey(TinkerModifiers.unbreakable).build())
      .addModule(new DurabilityBarColorModule(0xffffff))
      .addModule(ReduceToolDamageModule.builder().flat(1.0f));
    buildModifier(ModifierIds.tank).addModules(StatBoostModule.add(ToolTankHelper.CAPACITY_STAT).eachLevel(FluidType.BUCKET_VOLUME), ToolTankHelper.TANK_HANDLER);
    buildModifier(ModifierIds.overforced).addModule(StatBoostModule.add(OverslimeModifier.OVERSLIME_STAT).eachLevel(75));
    buildModifier(ModifierIds.soulbound).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(new VolatileFlagModule(ModifierEvents.SOULBOUND));
    // compat
    buildModifier(ModifierIds.theOneProbe, modLoaded("theoneprobe")).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(TheOneProbeModule.INSTANCE);
    buildModifier(ModifierIds.headlight, modLoaded("headlight"))
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(new ModifierVariantNameModule(VariantFormatter.PARAMETER))
      .addModule(new HeadlightModule(10));

    // harvest
    buildModifier(ModifierIds.haste)
      .levelDisplay(new UniqueForLevels(5))
      .addModule(StatBoostModule.add(ToolStats.MINING_SPEED).eachLevel(4))
      .addModule(AttributeModule.builder(TinkerAttributes.MINING_SPEED_MULTIPLIER, Operation.MULTIPLY_TOTAL).toolItem(ItemPredicate.tag(ARMOR)).eachLevel(0.1f));
    buildModifier(ModifierIds.blasting).addModule(
      ConditionalMiningSpeedModule.builder()
        .customVariable("resistance", new BlockMiningSpeedVariable(BlockVariable.BLAST_RESISTANCE, 3))
        .formula()
        .constant(3)
        .constant(6).customVariable("resistance").subtract() // (6 - resistance)
        .constant(1.5f)
        .divide() // above / 1.5
        .power() // 3^above
        .constant(10).min() // min(above, 10)
        .variable(LEVEL).multiply() // above * level
        .variable(MULTIPLIER).multiply() // above * multiplier
        .variable(VALUE).add() // above + newSpeed
        .build());
    buildModifier(ModifierIds.hydraulic).addModule(
      ConditionalMiningSpeedModule.builder()
        .customVariable("bonus", new EntityConditionalStatVariable(new ConditionalEntityVariable(
          LivingEntityPredicate.EYES_IN_WATER,
          new ConditionalEntityVariable(new HasEnchantmentEntityPredicate(Enchantments.AQUA_AFFINITY), 8, 40),
          new ConditionalEntityVariable(LivingEntityPredicate.RAINING, 4, 0)
        ), 8)).formula()
        .variable(MULTIPLIER).customVariable("bonus").multiply()
        .variable(LEVEL).multiply()
        .variable(VALUE).add()
        .build());
    buildModifier(ModifierIds.lightspeed).addModule(
      ConditionalMiningSpeedModule.builder()
        .customVariable("light", new BlockLightVariable(LightLayer.BLOCK, 15))
        .formula()
        .constant(3)
        .customVariable("light").constant(5).subtract()
        .constant(5).divide()
        .power()
        .variable(LEVEL).multiply()
        .variable(MULTIPLIER).multiply()
        .variable(VALUE).add().build())
      .addModule(new LightspeedAttributeModule("", Attributes.MOVEMENT_SPEED, Operation.ADDITION, LightLayer.BLOCK, 5, 0.0009f, 0.005f));



    // loot
    // constant enchants are harvest exclusive as we want to avoid non-harvest acting oddly with armor variant
    IJsonPredicate<Item> harvest = ItemPredicate.tag(HARVEST);
    IJsonPredicate<Item> armor = ItemPredicate.tag(WORN_ARMOR);
    buildModifier(TinkerModifiers.silky).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                                        .addModule(EnchantmentModule.builder(Enchantments.SILK_TOUCH).toolItem(harvest).constant())
                                        .addModule(EnchantmentModule.builder(Enchantments.SILK_TOUCH).toolItem(armor).armorHarvest(ARMOR_SLOTS));
    EnchantmentModule CONSTANT_FORTUNE = EnchantmentModule.builder(Enchantments.BLOCK_FORTUNE).toolItem(harvest).constant();
    EnchantmentModule ARMOR_FORTUNE = EnchantmentModule.builder(Enchantments.BLOCK_FORTUNE).toolItem(armor).armorHarvest(ARMOR_SLOTS);
    // note chestplates will have both modules, but will get ignored due to setting the looting slot
    // the air check on weapon looting is for projectiles which use an item of air in their tool context
    LootingModule WEAPON_LOOTING = LootingModule.builder().toolItem(ItemPredicate.or(ItemPredicate.set(Items.AIR), ItemPredicate.tag(MELEE))).weapon();
    LootingModule ARMOR_LOOTING = LootingModule.builder().toolItem(armor).armor(ARMOR_SLOTS);
    buildModifier(ModifierIds.luck).levelDisplay(new UniqueForLevels(3)).addModules(CONSTANT_FORTUNE, ARMOR_FORTUNE, WEAPON_LOOTING, ARMOR_LOOTING);
    buildModifier(ModifierIds.fortune).addModules(CONSTANT_FORTUNE, ARMOR_FORTUNE);
    buildModifier(ModifierIds.looting).addModules(WEAPON_LOOTING, ARMOR_LOOTING);
    // note that the held tool bonus is hardcoded to 50% based on this modifier ID
    buildModifier(ModifierIds.experienced).addModule(AttributeModule.builder(TinkerAttributes.EXPERIENCE_MULTIPLIER, Operation.MULTIPLY_BASE).toolItem(ItemPredicate.tag(ARMOR)).eachLevel(0.25f));


    /// attack
    buildModifier(TinkerModifiers.knockback)
      // do not boost chestplate attacks twice, thats a bit too much knockback for the cost
      .addModule(KnockbackModule.builder().toolItem(ItemPredicate.tag(TinkerTags.Items.CHESTPLATES).inverted()).eachLevel(0.5f))
      .addModule(AttributeModule.builder(Attributes.ATTACK_KNOCKBACK, Operation.ADDITION).slots(armorSlots).eachLevel(1));
    buildModifier(TinkerModifiers.padded)
      .priority(75) // run after knockback
      .addModule(KnockbackModule.builder().formula()
        .variable(VALUE)
        .constant(2).variable(LEVEL).power() // 2^LEVEL
        .divide().build()); // KNOCKBACK / 2^LEVEL
    buildModifier(ModifierIds.sticky)
      .addModule(MobEffectModule.builder(MobEffects.MOVEMENT_SLOWDOWN).level(RandomLevelingValue.perLevel(0, 0.5f)).time(RandomLevelingValue.random(20, 10)).build());

    // damage boost
    // vanilla give +1, 1.5, 2, 2.5, 3, but that is low
    // we instead do +0.75, +1.5, +2.25, +3, +3.75
    buildModifier(ModifierIds.sharpness).addModule(StatBoostModule.add(ToolStats.ATTACK_DAMAGE).eachLevel(0.75f)).levelDisplay(new UniqueForLevels(5, true));
    buildModifier(ModifierIds.swiftstrike).addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_SPEED).eachLevel(0.05f)).levelDisplay(new UniqueForLevels(5));
    buildModifier(ModifierIds.smite).addModule(ConditionalMeleeDamageModule.builder().target(new MobTypePredicate(MobType.UNDEAD)).eachLevel(2.0f));
    buildModifier(ModifierIds.antiaquatic).addModule(ConditionalMeleeDamageModule.builder().target(new MobTypePredicate(MobType.WATER)).eachLevel(2.0f));
    buildModifier(ModifierIds.cooling).addModule(ConditionalMeleeDamageModule.builder().target(LivingEntityPredicate.FIRE_IMMUNE).eachLevel(1.6f));
    IJsonPredicate<LivingEntity> baneSssssPredicate = LivingEntityPredicate.or(new MobTypePredicate(MobType.ARTHROPOD), LivingEntityPredicate.tag(TinkerTags.EntityTypes.CREEPERS));
    buildModifier(ModifierIds.baneOfSssss)
      .addModule(ConditionalMeleeDamageModule.builder().target(baneSssssPredicate).eachLevel(2.0f))
      .addModule(MobEffectModule.builder(MobEffects.MOVEMENT_SLOWDOWN).level(RandomLevelingValue.flat(4)).time(RandomLevelingValue.random(20, 10)).target(baneSssssPredicate).build(), ModifierHooks.MELEE_HIT);
    buildModifier(ModifierIds.killager).addModule(ConditionalMeleeDamageModule.builder().target(LivingEntityPredicate.or(
      new MobTypePredicate(MobType.ILLAGER),
      LivingEntityPredicate.LOADER.tag(TinkerTags.EntityTypes.VILLAGERS))).eachLevel(2.0f));
    buildModifier(ModifierIds.pierce)
      // less than sharpness, but pierces 1 armor
      .addModule(StatBoostModule.add(ToolStats.ATTACK_DAMAGE).eachLevel(0.5f))
      .addModule(MeleeAttributeModule.builder(Attributes.ARMOR, Operation.ADDITION).eachLevel(-1))
      // use a mob effect to make this work on ranged, to ensure it automatically cancels
      .addModule(MobEffectModule.builder(TinkerEffects.pierce.get()).level(RandomLevelingValue.perLevel(0, 1)).time(RandomLevelingValue.flat(2)).build(), ModifierHooks.PROJECTILE_HIT);

    // ranged
    buildModifier(ModifierIds.power).addModule(StatBoostModule.add(ToolStats.PROJECTILE_DAMAGE).amount(0.5f, 0.5f));
    buildModifier(ModifierIds.quickCharge).addModule(StatBoostModule.multiplyBase(ToolStats.DRAW_SPEED).eachLevel(0.25f));
    buildModifier(ModifierIds.trueshot).addModule(StatBoostModule.add(ToolStats.ACCURACY).eachLevel(0.1f));
    buildModifier(ModifierIds.blindshot).addModule(StatBoostModule.add(ToolStats.ACCURACY).eachLevel(-0.1f));
    buildModifier(ModifierIds.trickQuiver).priority(70) // before bulk quiver
      .addModule(InventoryModule.builder().pattern(pattern("tipped_arrow"))
                                .toolItem(ItemPredicate.tag(TinkerTags.Items.CROSSBOWS).inverted())
                                .filter(TinkerPredicate.ARROW)
                                .limitPerLevel(32).flatSlots(3))
      .addModule(InventoryModule.builder().pattern(pattern("tipped_arrow"))
                                .toolItem(ItemPredicate.tag(TinkerTags.Items.CROSSBOWS))
                                .filter(ItemPredicate.or(TinkerPredicate.ARROW, ItemPredicate.set(Items.FIREWORK_ROCKET)))
                                .limitPerLevel(32).flatSlots(3))
      .addModule(TrickQuiverModule.INSTANCE)
      .addModule(InventoryMenuModule.ANY);
    buildModifier(ModifierIds.bulkQuiver).priority(50) // after crystalshot as bulk prioritizes "inventory ammo"
      .addModule(InventoryModule.builder().pattern(pattern("arrow"))
                                .toolItem(ItemPredicate.tag(TinkerTags.Items.CROSSBOWS).inverted())
                                .filter(TinkerPredicate.ARROW)
                                .slotsPerLevel(2))
      .addModule(InventoryModule.builder().pattern(pattern("arrow"))
                                .toolItem(ItemPredicate.tag(TinkerTags.Items.CROSSBOWS))
                                .filter(ItemPredicate.or(TinkerPredicate.ARROW, ItemPredicate.set(Items.FIREWORK_ROCKET)))
                                .slotsPerLevel(2))
      .addModule(BulkQuiverModule.INSTANCE)
      .addModule(InventoryMenuModule.ANY);

    // armor
    buildModifier(TinkerModifiers.golden).addModule(new VolatileFlagModule(ModifiableArmorItem.PIGLIN_NEUTRAL)).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
    buildModifier(ModifierIds.wings).addModule(new VolatileFlagModule(ModifiableArmorItem.ELYTRA)).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
    buildModifier(ModifierIds.knockbackResistance).addModule(StatBoostModule.add(ToolStats.KNOCKBACK_RESISTANCE).eachLevel(0.1f));
    buildModifier(ModifierIds.ricochet).addModule(AttributeModule.builder(TinkerAttributes.KNOCKBACK_MULTIPLIER, Operation.MULTIPLY_BASE).eachLevel(0.2f));

    // defense
    buildModifier(ModifierIds.revitalizing).addModule(AttributeModule.builder(Attributes.MAX_HEALTH, Operation.ADDITION).slots(armorSlots).eachLevel(2));
    // protection
    buildModifier(ModifierIds.protection).addModule(ProtectionModule.builder().eachLevel(1.25f));
    buildModifier(ModifierIds.meleeProtection)
      .addModule(MaxArmorAttributeModule.builder(TinkerAttributes.USE_ITEM_SPEED, Operation.ADDITION).heldTag(TinkerTags.Items.HELD).tooltipStyle(TooltipStyle.PERCENT).eachLevel(0.05f))
      // disallow indirect damage to guard against misuse of the melee damage types
      .addModule(ProtectionModule.builder().sources(DamageSourcePredicate.CAN_PROTECT, DamageSourcePredicate.tag(TinkerTags.DamageTypes.MELEE_PROTECTION), DamageSourcePredicate.IS_INDIRECT.inverted()).eachLevel(2f));
    buildModifier(ModifierIds.projectileProtection)
      .addModule(MaxArmorAttributeModule.builder(Attributes.KNOCKBACK_RESISTANCE, Operation.ADDITION).heldTag(TinkerTags.Items.HELD).eachLevel(0.05f))
      .addModule(ProtectionModule.builder().sources(DamageSourcePredicate.CAN_PROTECT, DamageSourcePredicate.tag(TinkerTags.DamageTypes.PROJECTILE_PROTECTION)).eachLevel(2f));
    buildModifier(ModifierIds.fireProtection)
      .addModule(EnchantmentModule.builder(Enchantments.FIRE_PROTECTION).protection())
      .addModule(ProtectionModule.builder().sources(DamageSourcePredicate.CAN_PROTECT, DamageSourcePredicate.tag(TinkerTags.DamageTypes.FIRE_PROTECTION)).eachLevel(2.5f));
    buildModifier(ModifierIds.blastProtection)
      .addModule(EnchantmentModule.builder(Enchantments.BLAST_PROTECTION).protection())
      .addModule(ProtectionModule.builder().sources(DamageSourcePredicate.CAN_PROTECT, DamageSourcePredicate.tag(TinkerTags.DamageTypes.PROJECTILE_PROTECTION)).eachLevel(2.5f));
    buildModifier(ModifierIds.magicProtection)
      .addModule(MaxArmorAttributeModule.builder(TinkerAttributes.BAD_EFFECT_DURATION, Operation.MULTIPLY_BASE).heldTag(TinkerTags.Items.HELD).eachLevel(-0.05f))
      .addModule(ProtectionModule.builder().sources(DamageSourcePredicate.CAN_PROTECT, DamageSourcePredicate.tag(TinkerTags.DamageTypes.MAGIC_PROTECTION)).eachLevel(2.5f));
    buildModifier(ModifierIds.turtleShell)
      .addModule(AttributeModule.builder(ForgeMod.SWIM_SPEED.get(), Operation.MULTIPLY_TOTAL).slots(armorSlots).eachLevel(0.05f))
      .addModule(ProtectionModule.builder()
                                 .toolItem(ItemPredicate.or(ItemPredicate.tag(TinkerTags.Items.HELMETS), ItemPredicate.tag(TinkerTags.Items.CHESTPLATES)))
                                 .entity(LivingEntityPredicate.EYES_IN_WATER).eachLevel(2.5f))
      .addModule(ProtectionModule.builder()
                                 .toolItem(ItemPredicate.or(ItemPredicate.tag(TinkerTags.Items.LEGGINGS), ItemPredicate.tag(TinkerTags.Items.BOOTS)))
                                 .entity(LivingEntityPredicate.FEET_IN_WATER).eachLevel(2.5f));
    buildModifier(ModifierIds.shulking)
      .addModule(MaxArmorAttributeModule.builder(TinkerAttributes.CROUCH_DAMAGE_MULTIPLIER, Operation.MULTIPLY_BASE).heldTag(TinkerTags.Items.HELD).eachLevel(-0.1f))
      .addModule(ProtectionModule.builder().eachLevel(2.5f));
    buildModifier(ModifierIds.dragonborn)
      .addModule(MaxArmorAttributeModule.builder(TinkerAttributes.CRITICAL_DAMAGE, Operation.ADDITION).heldTag(TinkerTags.Items.HELD).tooltipStyle(TooltipStyle.PERCENT).eachLevel(0.05f))
      .addModule(ProtectionModule.builder().entity(TinkerPredicate.AIRBORNE).eachLevel(2.5f));
    // helmet
    buildModifier(ModifierIds.respiration).addModule(EnchantmentModule.builder(Enchantments.RESPIRATION).constant());
    buildModifier(ModifierIds.aquaAffinity).addModule(EnchantmentModule.builder(Enchantments.AQUA_AFFINITY).constant()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
    buildModifier(TinkerModifiers.itemFrame).addModule(InventoryModule.builder().pattern(pattern("item_frame")).flatLimit(1).slotsPerLevel(1));
    // chestplate
    buildModifier(ModifierIds.strength).addModule(AttributeModule.builder(Attributes.ATTACK_DAMAGE, Operation.MULTIPLY_TOTAL).slots(armorSlots).eachLevel(0.1f));
    // leggings
    buildModifier(ModifierIds.pockets)
      .addModule(InventoryModule.builder().slotsPerLevel(18))
      .addModule(InventoryMenuModule.ANY);
    buildModifier(ModifierIds.toolBelt).priority(85)
      .levelDisplay(ModifierLevelDisplay.PLUSES)
      .addModule(InventoryModule.builder().pattern(pattern("tool_belt")).slots(3, 1))
      .addModule(new ToolBeltModule(TooltipKey.NORMAL, TooltipKey.CONTROL))
      .addModule(InventoryMenuModule.SHIFT);
    buildModifier(TinkerModifiers.shieldStrap).priority(95)
      .addModule(InventoryModule.builder().pattern(pattern("shield_plus")).slotsPerLevel(1))
      .addModule(new ShieldStrapModule(TooltipKey.NORMAL))
      .addModule(InventoryMenuModule.SHIFT)
      .addModule(new VolatileFlagModule(ToolInventoryCapability.INCLUDE_OFFHAND));
    buildModifier(ModifierIds.stepUp).addModule(AttributeModule.builder(ForgeMod.STEP_HEIGHT_ADDITION.get(), Operation.ADDITION).slots(armorSlots).eachLevel(0.5f));
    buildModifier(ModifierIds.speedy).addModule(AttributeModule.builder(Attributes.MOVEMENT_SPEED, Operation.MULTIPLY_TOTAL).slots(armorMainHand).eachLevel(0.1f));
    buildModifier(ModifierIds.leaping).addModule(AttributeModule.builder(TinkerAttributes.JUMP_BOOST, Operation.ADDITION).eachLevel(1));
    buildModifier(ModifierIds.swiftSneak).addModule(EnchantmentModule.builder(Enchantments.SWIFT_SNEAK).constant());
    // TODO: consider higher levels keeping more of the inventory
    buildModifier(ModifierIds.soulBelt).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(new ArmorLevelModule(TinkerDataKeys.SOUL_BELT, true, null)).addModule(ModifierRequirementsModule.builder().modifierKey(ModifierIds.soulBelt).requireModifier(ModifierIds.soulbound, 1).build());
    buildModifier(ModifierIds.workbench)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(InventoryMenuModule.ANY)
      .addModule(InventorySlotMenuModule.INSTANCE)
      .addModule(new VolatileFlagModule(ToolInventoryCapability.INVENTORY_CRAFTING));
    buildModifier(ModifierIds.craftingTable)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(InventoryMenuModule.ANY)
      .addModule(InventorySlotMenuModule.INSTANCE)
      .addModule(new VolatileFlagModule(ToolInventoryCapability.CRAFTING_TABLE));
    // boots
    buildModifier(ModifierIds.depthStrider).addModule(EnchantmentModule.builder(Enchantments.DEPTH_STRIDER).constant());
    buildModifier(ModifierIds.featherFalling).addModule(ProtectionModule.builder().source(DamageSourcePredicate.tag(TinkerTags.DamageTypes.FALL_PROTECTION)).eachLevel(3.75f));
    buildModifier(ModifierIds.longFall)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(ModifierRequirementsModule.builder().requireModifier(ModifierIds.featherFalling, 4).modifierKey(ModifierIds.longFall).build())
      .addModule(BlockDamageSourceModule.source(DamageSourcePredicate.tag(TinkerTags.DamageTypes.FALL_PROTECTION)).build());
    buildModifier(ModifierIds.frostWalker)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(BlockDamageSourceModule.source(new DamageTypePredicate(DamageTypes.HOT_FLOOR)).build())
      .addModule(ReplaceBlockWalkerModule.builder().replaceAlways(BlockPropertiesPredicate.block(Blocks.WATER).matches(LiquidBlock.LEVEL, 0).build(), Blocks.FROSTED_ICE.defaultBlockState()).amount(2, 1));
    buildModifier(ModifierIds.snowdrift).priority(90).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(CoverGroundWalkerModule.block(Blocks.SNOW).amount(0.5f, 1));
    buildModifier(ModifierIds.bouncy).levelDisplay(ModifierLevelDisplay.NO_LEVELS).addModule(AttributeModule.builder(TinkerAttributes.BOUNCY.get(), Operation.ADDITION).tooltipStyle(TooltipStyle.NONE).flat(1));
    buildModifier(ModifierIds.doubleJump).levelDisplay(new UniqueForLevels(4, false)).addModule(AttributeModule.builder(TinkerAttributes.JUMP_COUNT.get(), Operation.ADDITION).slots(ARMOR_SLOTS).tooltipStyle(TooltipStyle.NONE).flat(1));
    // shield
    buildModifier(ModifierIds.boundless)
      .levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL)
      .addModule(AttributeModule.builder(TinkerAttributes.PROTECTION_CAP, Operation.ADDITION).tooltipStyle(TooltipStyle.PERCENT).toolItem(ItemPredicate.tag(ARMOR)).eachLevel(0.1f));

    // interaction
    buildModifier(ModifierIds.pathing)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(ShowOffhandModule.DISALLOW_BROKEN)
      .addModule(ExtinguishCampfireModule.INSTANCE)
      .addModule(ToolActionTransformModule.builder(ToolActions.SHOVEL_FLATTEN, SoundEvents.SHOVEL_FLATTEN).requireGround().build())
      .addModule(ToolActionWalkerTransformModule.builder(ToolActions.SHOVEL_FLATTEN, SoundEvents.SHOVEL_FLATTEN).amount(0.5f, 1));
    buildModifier(ModifierIds.stripping)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(ShowOffhandModule.DISALLOW_BROKEN)
      .addModule(ToolActionTransformModule.builder(ToolActions.AXE_STRIP, SoundEvents.AXE_STRIP).build())
      .addModule(ToolActionTransformModule.builder(ToolActions.AXE_SCRAPE, SoundEvents.AXE_SCRAPE).eventId(LevelEvent.PARTICLES_SCRAPE).build())
      .addModule(ToolActionTransformModule.builder(ToolActions.AXE_WAX_OFF, SoundEvents.AXE_WAX_OFF).eventId(LevelEvent.PARTICLES_WAX_OFF).build());
    buildModifier(ModifierIds.tilling)
      .addModule(ShowOffhandModule.DISALLOW_BROKEN)
      .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
      .addModule(ToolActionTransformModule.builder(ToolActions.HOE_TILL, SoundEvents.HOE_TILL).build())
      .addModule(ToolActionWalkerTransformModule.builder(ToolActions.HOE_TILL, SoundEvents.HOE_TILL).amount(0.5f, 1));

    // traits
    buildModifier(ModifierIds.smelting)
      .priority(110) // want to be higher than bonking and alike
      .levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL)
      .addModule(InventoryMenuModule.SHIFT)
      .addModule(new SmeltingModule(RecipeType.SMELTING, 10, InventoryModule.builder().pattern(pattern("fire")).flatLimit(1).slotsPerLevel(1)));

    // internal
    buildModifier(ModifierIds.overslimeFriend).tooltipDisplay(TooltipDisplay.NEVER);
    buildModifier(ModifierIds.snowBoots).addModule(new VolatileFlagModule(ModifiableArmorItem.SNOW_BOOTS)).tooltipDisplay(TooltipDisplay.NEVER);

    // traits - tier 1
    buildModifier(ModifierIds.cultivated).addModule(RepairModule.builder().eachLevel(0.5f));
    buildModifier(ModifierIds.stringy).addModule(MaterialRepairModule.material(MaterialIds.string).constant(140));
    buildModifier(ModifierIds.unburdened)
      .addModule(StatBoostModule.add(ToolStats.USE_ITEM_SPEED).eachLevel(0.1f))
      .addModule(AttributeModule.builder(TinkerAttributes.USE_ITEM_SPEED, Operation.ADDITION).slots(ARMOR_SLOTS).tooltipStyle(TooltipStyle.PERCENT).toolItem(ItemPredicate.tag(WORN_ARMOR)).eachLevel(0.05f));
    buildModifier(ModifierIds.depthProtection).addModule(DepthProtectionModule.builder().baselineHeight(64).neutralRange(32).eachLevel(1.25f));
    buildModifier(ModifierIds.enderclearance).addModule(EnderclearanceModule.INSTANCE).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL);
    // traits - tier 2
    buildModifier(ModifierIds.overgrowth).addModule(new OvergrowthModule(LevelingValue.eachLevel(0.05f)));
    buildModifier(ModifierIds.searing).addModule(ConditionalMiningSpeedModule.builder().blocks(TinkerPredicate.CAN_MELT_BLOCK).eachLevel(6f));
    buildModifier(ModifierIds.scorching).addModule(ConditionalMeleeDamageModule.builder().target(LivingEntityPredicate.ON_FIRE).eachLevel(2f));
    buildModifier(ModifierIds.airborne)
      // 400% boost means 5x mining speed
      .addModule(ConditionalMiningSpeedModule.builder().holder(LivingEntityPredicate.ON_GROUND.inverted()).percent().allowIneffective().flat(4), ModifierHooks.BREAK_SPEED)
      // accuracy gets a 0.5 boost under the stricter version of in air (no boost just for being on a ladder)
      .addModule(ConditionalStatModule.stat(ToolStats.ACCURACY).holder(TinkerPredicate.AIRBORNE).flat(0.5f));
    buildModifier(ModifierIds.antitoxin)
      .addModule(ConditionalMeleeDamageModule.builder()
        .attacker(new HasMobEffectPredicate(MobEffects.POISON))
        .customVariable("poison", new EntityMeleeVariable(new EntityEffectLevelVariable(MobEffects.POISON), WhichEntity.ATTACKER, 0))
        .formula()
         // gives 1.5 bonus per level at poison 1, 2.5 at poison 2
        .customVariable("poison").constant(0.5f).add().variable(LEVEL).multiply().variable(MULTIPLIER).multiply()
        // finally, add in base damage
        .variable(VALUE).add().build())
      .addModule(ConditionalStatModule.stat(ToolStats.DRAW_SPEED)
        .holder(new HasMobEffectPredicate(MobEffects.POISON))
        .customVariable("poison", new EntityConditionalStatVariable(new EntityEffectLevelVariable(MobEffects.POISON), 0))
        .formula()
        // gives 0.15 bonus per level at poison 1, .25 at poison 2
        .customVariable("poison").constant(0.5f).add().constant(0.1f).multiply().variable(LEVEL).multiply().variable(MULTIPLIER).multiply()
        // finally, add in base damage
        .variable(VALUE).add().build());
    buildModifier(ModifierIds.raging)
      .addModule(ConditionalMeleeDamageModule.builder()
        .customVariable("health", new EntityMeleeVariable(EntityVariable.HEALTH, WhichEntity.ATTACKER, 0))
        .customVariable("max_health", new EntityMeleeVariable(new AttributeEntityVariable(Attributes.MAX_HEALTH), WhichEntity.ATTACKER, 20))
        .formula()
        .customVariable("health")
        // add (10 - max_health) to health, at minimum 0, to account for low max health
        .constant(10).customVariable("max_health").subtract().nonNegative().add()
        // linear bonus from 2 to 8, max bonus below 2, no bonus above 8
        .constant(10).subtractFlipped().constant(8).divide().percentClamp()
        // get 3 bonus per level, bring in standard multiplier
        .variable(LEVEL).multiply().constant(3).multiply().variable(MULTIPLIER).multiply()
        // finally, add in base damage
        .variable(VALUE).add().build())
      .addModule(ConditionalStatModule.stat(ToolStats.DRAW_SPEED)
        .customVariable("health", new EntityConditionalStatVariable(EntityVariable.HEALTH, 0))
        .customVariable("max", new EntityConditionalStatVariable(new AttributeEntityVariable(Attributes.MAX_HEALTH), 20))
        .formula()
        .customVariable("health")
        // add (10 - max_health) to health, at minimum 0, to account for low max health
        .constant(10).customVariable("max").subtract().nonNegative().add()
        // linear bonus from 2 to 8, max bonus below 2, no bonus above 8
        .constant(10).subtractFlipped().constant(8).divide().percentClamp()
        // get 0.25 bonus per level, bring in standard multiplier
        .variable(LEVEL).multiply().constant(0.25f).multiply().variable(MULTIPLIER).multiply()
        // finally, add in base damage
        .variable(VALUE).add().build());

    // triats - tier 2
    buildModifier(ModifierIds.scorchProtection)
      .addModule(EnchantmentModule.builder(Enchantments.FIRE_PROTECTION).protection())
      .addModule(ProtectionModule.builder().sources(DamageSourcePredicate.CAN_PROTECT, SourceAttackerPredicate.causing(LivingEntityPredicate.FIRE_IMMUNE)).eachLevel(1.25f));

    // traits - tier 2 compat
    buildModifier(ModifierIds.lustrous);
    buildModifier(ModifierIds.sharpweight)
      .addModule(StatBoostModule.multiplyBase(ToolStats.MINING_SPEED).eachLevel(0.15f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.DRAW_SPEED).eachLevel(0.1f))
      .addModule(StatBoostModule.add(ToolStats.ARMOR_TOUGHNESS).eachLevel(2))
      .addModule(AttributeModule.builder(Attributes.MOVEMENT_SPEED, Operation.MULTIPLY_BASE).eachLevel(-0.1f));
    buildModifier(ModifierIds.heavy)
      .addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_DAMAGE).eachLevel(0.15f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.PROJECTILE_DAMAGE).eachLevel(0.10f))
      .addModule(StatBoostModule.add(ToolStats.KNOCKBACK_RESISTANCE).eachLevel(0.15f))
      .addModule(AttributeModule.builder(Attributes.MOVEMENT_SPEED, Operation.MULTIPLY_BASE).eachLevel(-0.1f));
    buildModifier(ModifierIds.featherweight)
      .addModule(StatBoostModule.multiplyBase(ToolStats.DRAW_SPEED).eachLevel(0.07f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.ACCURACY).eachLevel(0.07f))
      .addModule(ProtectionModule.builder().eachLevel(-1.25f))
      .addModule(AttributeModule.builder(TinkerAttributes.USE_ITEM_SPEED, Operation.ADDITION).tooltipStyle(TooltipStyle.PERCENT).toolItem(ItemPredicate.tag(ARMOR)).eachLevel(0.1f));
    buildModifier(ModifierIds.dense)
      // from 0 to 5, repair formula is FACTOR * (1 - 0.025 * LEVEL * (11 - LEVEL))
      .addModule(RepairModule.builder().maxLevel(5).formula()
          .variable(FACTOR)
            .constant(1)
              .constant(0.025f).variable(LEVEL).multiply()
              .constant(11).variable(LEVEL).subtract()
            .multiply()
          .subtract()
        .multiply().build())
      // at 6+, repair formula is FACTOR * (0.5 - LEVEL * 0.05), simple formula supports below
      .addModule(RepairModule.builder().minLevel(6).amount(-0.5f, -0.05f))
      // durability formula is 1 - 1/(1.5^LEVEL)
      .addModule(ReduceToolDamageModule.builder().formula()
          .constant(1)
            .constant(1)
            .constant(1.5f).variable(LEVEL).power()
          .divide()
        .subtract().build());
    buildModifier(ModifierIds.consecrated).addModule(ProtectionModule.builder().attacker(new MobTypePredicate(MobType.UNDEAD)).eachLevel(1.25f));
    buildModifier(ModifierIds.preserved).addModules(StatBoostModule.multiplyBase(ToolStats.DURABILITY).eachLevel(0.15f), RepairModule.builder().eachLevel(0.15f));

    // traits - tier 3
    buildModifier(ModifierIds.overcast)
      .addModule(StatBoostModule.add(OverslimeModifier.OVERSLIME_STAT).eachLevel(25))
      .addModule(StatBoostModule.multiplyBase(OverslimeModifier.OVERSLIME_STAT).eachLevel(0.5f));
    buildModifier(ModifierIds.crumbling).addModule(ConditionalMiningSpeedModule.builder().blocks(BlockPredicate.REQUIRES_TOOL.inverted()).allowIneffective().eachLevel(0.5f));
    buildModifier(ModifierIds.enhanced).priority(60).addModule(UPGRADE);
    buildModifier(ModifierIds.crystalbound).addModule(RestrictAngleModule.INSTANCE).addModule(StatBoostModule.add(ToolStats.VELOCITY).eachLevel(0.1f));
    buildModifier(ModifierIds.crystalstrike)
      .addModule(AttributeModule.builder(Attributes.ATTACK_SPEED, Operation.MULTIPLY_TOTAL).eachLevel(0.025f))
      .addModule(new ArmorLevelModule(TinkerDataKeys.CRYSTALSTRIKE, false, TinkerTags.Items.HELD_ARMOR));
    // traits - tier 3 nether
    buildModifier(ModifierIds.lightweight)
      .addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_SPEED).eachLevel(0.07f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.MINING_SPEED).eachLevel(0.07f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.DRAW_SPEED).eachLevel(0.03f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.VELOCITY).eachLevel(0.03f));
    buildModifier(ModifierIds.ductile)
      .addModule(StatBoostModule.multiplyBase(ToolStats.DURABILITY).eachLevel(0.1f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_DAMAGE).eachLevel(0.1f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.PROJECTILE_DAMAGE).eachLevel(0.05f))
      .addModule(StatBoostModule.add(ToolStats.ARMOR_TOUGHNESS).eachLevel(1));
    // traits - tier 3 compat
    buildModifier(ModifierIds.maintained)
      .addModule(ConditionalMiningSpeedModule.builder()
        .customVariable("durability", ToolVariable.CURRENT_DURABILITY)
        .customVariable("max_durability", new ToolStatVariable(ToolStats.DURABILITY))
        .formula()
        .customVariable("max_durability").constant(0.5f).multiply().duplicate()
        .customVariable("durability").subtractFlipped()
        .nonNegative().divideFlipped()
        .variable(LEVEL).multiply()
        .constant(6).multiply()
        .variable(MULTIPLIER).multiply()
        .variable(VALUE).add().build())
      .addModule(ConditionalStatModule.stat(ToolStats.VELOCITY)
        .customVariable("durability", ToolVariable.CURRENT_DURABILITY)
        .customVariable("max_durability", new ToolStatVariable(ToolStats.DURABILITY))
        .formula()
        .customVariable("max_durability").constant(0.5f).multiply().duplicate()
        .customVariable("durability").subtractFlipped()
        .nonNegative().divideFlipped()
        .variable(LEVEL).multiply()
        .constant(0.05f).multiply()
        .variable(MULTIPLIER).multiply()
        .variable(VALUE).add().build())
      .addModule(AttributeModule.builder(Attributes.ARMOR_TOUGHNESS, Operation.ADDITION)
        .toolItem(ItemPredicate.tag(TinkerTags.Items.ARMOR))
        .customVariable("durability", ToolVariable.CURRENT_DURABILITY)
        .customVariable("max_durability", new ToolStatVariable(ToolStats.DURABILITY))
        .formula()
        .customVariable("max_durability").constant(0.5f).multiply().duplicate()
        .customVariable("durability").subtractFlipped()
        .nonNegative().divideFlipped()
        .variable(LEVEL).multiply()
        .constant(2).multiply().build());
    // traits - tier 4
    buildModifier(ModifierIds.overburn).addModules(OverburnModule.INSTANCE, StatBoostModule.add(ToolTankHelper.CAPACITY_STAT).flat(FluidType.BUCKET_VOLUME), ToolTankHelper.TANK_HANDLER);
    buildModifier(ModifierIds.overlord)
      .addModule(StatCopyModule.builder(OverslimeModifier.OVERSLIME_STAT, ToolStats.DURABILITY).eachLevel(0.1f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.DURABILITY).levelRange(1, 6).eachLevel(-0.15f))
      .addModule(StatBoostModule.multiplyBase(ToolStats.DURABILITY).minLevel(7).flat(-0.99999f)); // once the level gets too high, just reduce it to almost nothing, should land at 1
    buildModifier(ModifierIds.fortified).priority(60).addModule(ModifierSlotModule.slot(SlotType.DEFENSE).eachLevel(1));
    buildModifier(ModifierIds.kinetic).addModule(KineticModule.INSTANCE);
    buildModifier(ModifierIds.recurrentProtection).addModule(new RecurrentProtectionModule(LevelingValue.flat(0.5f), LevelingInt.eachLevel(5 * 20)));
    buildModifier(ModifierIds.flameBarrier).addModule(new FlameBarrierModule(LevelingValue.eachLevel(1.875f)));
    buildModifier(ModifierIds.vintage)
      .addModule(ModifierSlotModule.slot(SlotType.ABILITY).eachLevel(1))
      .addModule(StatBoostModule.multiplyAll(ToolStats.MINING_SPEED).eachLevel(-0.1f))
      .addModule(StatBoostModule.multiplyAll(ToolStats.ATTACK_SPEED).eachLevel(-0.1f))
      .addModule(StatBoostModule.multiplyAll(ToolStats.DRAW_SPEED).eachLevel(-0.1f))
      // for defensive builds, offhand debuffs main hand attack speed
      .addModule(AttributeModule.builder(Attributes.ATTACK_SPEED, Operation.MULTIPLY_TOTAL).slots(EquipmentSlot.OFFHAND).toolItem(ItemPredicate.tag(TinkerTags.Items.HELD_ARMOR)).eachLevel(-0.1f))
      .addModule(AttributeModule.builder(Attributes.MOVEMENT_SPEED, Operation.MULTIPLY_TOTAL).slots(ARMOR_SLOTS).eachLevel(-0.1f));

    // traits - slimeskull
    buildModifier(ModifierIds.mithridatism).addModule(new EffectImmunityModule(MobEffects.POISON)).levelDisplay(ModifierLevelDisplay.NO_LEVELS);

    // mob disguise
    buildModifier(ModifierIds.creeperDisguise        ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.CREEPER));
    buildModifier(ModifierIds.endermanDisguise       ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.ENDERMAN));
    buildModifier(ModifierIds.skeletonDisguise       ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.SKELETON));
    buildModifier(ModifierIds.strayDisguise          ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.STRAY));
    buildModifier(ModifierIds.witherSkeletonDisguise ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.WITHER_SKELETON));
    buildModifier(ModifierIds.spiderDisguise         ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.SPIDER));
    buildModifier(ModifierIds.caveSpiderDisguise     ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.CAVE_SPIDER));
    buildModifier(ModifierIds.zombieDisguise         ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.ZOMBIE));
    buildModifier(ModifierIds.huskDisguise           ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.HUSK));
    buildModifier(ModifierIds.drownedDisguise        ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.DROWNED));
    buildModifier(ModifierIds.blazeDisguise          ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.BLAZE));
    buildModifier(ModifierIds.piglinDisguise         ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.PIGLIN));
    buildModifier(ModifierIds.piglinBruteDisguise    ).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.PIGLIN_BRUTE));
    buildModifier(ModifierIds.zombifiedPiglinDisguise).levelDisplay(ModifierLevelDisplay.SINGLE_LEVEL).addModule(new MobDisguiseModule(EntityType.ZOMBIFIED_PIGLIN));

    // iron now gives magnetic. Steel is also just has better than irons old trait
    addRedirect(id("sturdy"), redirect(ModifierIds.ductile));
    // merged armor modifiers into standard ones
    addRedirect(id("path_maker"), redirect(ModifierIds.pathing));
    addRedirect(id("plowing"), redirect(ModifierIds.tilling));
    addRedirect(id("lightspeed_armor"), redirect(ModifierIds.lightspeed));
  }

  @Override
  public String getName() {
    return "Tinkers' Construct Modifiers";
  }

  /** Short helper to get a modifier ID */
  private static ModifierId id(String name) {
    return new ModifierId(TConstruct.MOD_ID, name);
  }

  /** Short helper to get a modifier ID */
  private static Pattern pattern(String name) {
    return new Pattern(TConstruct.MOD_ID, name);
  }
}
