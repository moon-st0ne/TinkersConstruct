package slimeknights.tconstruct.library.modifiers.modules.armor;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus.Internal;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition.ConditionalModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModuleBuilder;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

/**
 * Module to block damage of the passed sources
 * @param source  Predicate of sources to block
 */
public record BlockDamageSourceModule(IJsonPredicate<DamageSource> source, ModifierCondition<IToolStackView> condition) implements DamageBlockModifierHook, ModifierModule, ConditionalModule<IToolStackView> {
  private static final List<ModuleHook<?>> DEFAULT_HOOKS = List.of(ModifierHooks.DAMAGE_BLOCK);
  public static final RecordLoadable<BlockDamageSourceModule> LOADER = RecordLoadable.create(
    DamageSourcePredicate.LOADER.defaultField("damage_source", BlockDamageSourceModule::source),
    ModifierCondition.TOOL_FIELD,
    BlockDamageSourceModule::new);

  /** @apiNote Internal constructor, use {@link #source(IJsonPredicate)} */
  @Internal
  public BlockDamageSourceModule {}

  @Override
  public List<ModuleHook<?>> getDefaultHooks() {
    return DEFAULT_HOOKS;
  }

  @Override
  public boolean isDamageBlocked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount) {
    return condition.matches(tool, modifier) && this.source.matches(source);
  }

  @Override
  public RecordLoadable<BlockDamageSourceModule> getLoader() {
    return LOADER;
  }
  

  /* Builder */

  public static Builder source(IJsonPredicate<DamageSource> source) {
    return new Builder(source);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Builder extends ModuleBuilder.Stack<Builder> {
    private final IJsonPredicate<DamageSource> source;

    public BlockDamageSourceModule build() {
      return new BlockDamageSourceModule(source, condition);
    }
  }
}
