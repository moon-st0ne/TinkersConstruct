package slimeknights.tconstruct.world;

import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingVisibilityEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent.FinalizeSpawn;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.world.logic.AncientToolItemListing;

import java.util.Collections;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = TConstruct.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldEvents {
  /* Heads */

  @SubscribeEvent
  static void livingVisibility(LivingVisibilityEvent event) {
    Entity lookingEntity = event.getLookingEntity();
    if (lookingEntity == null) {
      return;
    }
    LivingEntity entity = event.getEntity();
    ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
    Item item = helmet.getItem();
    if (item != Items.AIR && TinkerWorld.headItems.contains(item)) {
      if (lookingEntity.getType() == ((TinkerHeadType)((SkullBlock)((BlockItem)item).getBlock()).getType()).getType()) {
        event.modifyVisibility(0.5f);
      }
    }
  }

  @SubscribeEvent
  static void creeperKill(LivingDropsEvent event) {
    DamageSource source = event.getSource();
    if (source != null) {
      Entity entity = source.getEntity();
      if (entity instanceof Creeper creeper) {
        if (creeper.canDropMobsSkull()) {
          LivingEntity dying = event.getEntity();
          TinkerHeadType headType = TinkerHeadType.fromEntityType(dying.getType());
          if (headType != null && Config.COMMON.headDrops.get(headType).get()) {
            creeper.increaseDroppedSkulls();
            event.getDrops().add(dying.spawnAtLocation(TinkerWorld.heads.get(headType)));
          }
        }
      }
    }
  }

  // ancient tool equipment
  @SuppressWarnings({"deprecation", "OverrideOnly"})  // in that event, I can't call the event method or I'll get a stack overflow
  @SubscribeEvent
  static void livingSpawn(FinalizeSpawn event) {
    // TODO: this feels like it should be JSON controlled
    // do I want a more generalized system that works for our slime types too?
    Mob mob = event.getEntity();
    EntityType<?> type = mob.getType();
    // 5% chance for a zombie piglin to spawn with a battle sign, doesn't mean they drop it though
    ServerLevelAccessor level = event.getLevel();
    if ((type == EntityType.ZOMBIFIED_PIGLIN || type == EntityType.PIGLIN || type == EntityType.PIGLIN_BRUTE || type == EntityType.HUSK || type == EntityType.ZOMBIE_VILLAGER || type == EntityType.DROWNED)
        && level.getRandom().nextFloat() < 0.05f) {
      // forge event runs before finalize spawn so we can't just set our item now or it may get overwritten
      // instead, we cancel the event (which blocks vanilla finalize), then finalize ourself, then can set our item after
      event.setCanceled(true);
      mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), event.getSpawnType(), event.getSpawnData(), event.getSpawnTag());

      Item item = mob.getMainHandItem().getItem();
      RandomSource random = level.getRandom();
      // zombie villagers/husks just always get it if the chance is m  et
      if (type == EntityType.HUSK) {
        mob.setItemInHand(InteractionHand.MAIN_HAND, ToolBuildHandler.buildItemRandomMaterials(TinkerTools.meltingPan.get(), random));
      } else if (type == EntityType.ZOMBIE_VILLAGER) {
        mob.setItemInHand(InteractionHand.MAIN_HAND, ToolBuildHandler.buildItemRandomMaterials(TinkerTools.warPick.get(), random));
      } else if (type == EntityType.DROWNED) {
        // only update drowned if they are holding nothing, keep their trident or fishing rod
        if (item == Items.AIR) {
          ToolStack swasher = ToolBuildHandler.buildToolRandomMaterials(TinkerTools.swasher.get(), random);
          // add random amount of lava between 0 and 2000mb
          ToolTankHelper.TANK_HELPER.setFluid(swasher, new FluidStack(Fluids.LAVA, random.nextInt(FluidType.BUCKET_VOLUME * 2 + 1)));
          mob.setItemInHand(InteractionHand.MAIN_HAND, swasher.createStack());
        }
        // only replace golden sword or golden axes with our item, if they are holding nothing or a crossbow do nothing
      } else if (item == Items.GOLDEN_SWORD || item == Items.GOLDEN_AXE) {
        mob.setItemInHand(InteractionHand.MAIN_HAND, ToolBuildHandler.buildItemRandomMaterials(TinkerTools.battlesign.get(), random));
      }
    }
  }

  @SubscribeEvent
  static void wanderingTrades(WandererTradesEvent event) {
    // add ancient tools to the wandering trader table
    int weight = Config.COMMON.wandererAncientToolWeight.get();
    if (weight > 0) {
      event.getRareTrades().addAll(Collections.nCopies(weight, AncientToolItemListing.INSTANCE));
    }
  }
}
