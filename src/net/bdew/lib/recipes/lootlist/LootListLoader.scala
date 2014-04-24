/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * https://raw.github.com/bdew/bdlib/master/MMPL-1.0.txt
 */

package net.bdew.lib.recipes.lootlist

import net.bdew.lib.recipes.gencfg.GenericConfigLoader
import net.minecraftforge.oredict.OreDictionary
import net.bdew.lib.BdLib

/**
 * Loader mixin for loot lists
 */
trait LootListLoader extends GenericConfigLoader {
  def resolveLootList(entry: EntryLootList) =
    (for ((chance, ref) <- entry.list) yield {
      try {
        val itemStack = getConcreteStack(ref)
        if (itemStack == null) {
          BdLib.logWarn("Unable to resolve %s: null returned", ref)
          None
        } else {
          if (itemStack.getItemDamage == OreDictionary.WILDCARD_VALUE) {
            BdLib.logInfo("meta/damage is unset in %s, defaulting to 0", ref)
            itemStack.setItemDamage(0)
          }
          Some((chance, itemStack))
        }
      } catch {
        case e: Throwable =>
          BdLib.logWarn("Unable to resolve %s: %s", ref, e.getMessage)
          None
      }
    }).flatten
}