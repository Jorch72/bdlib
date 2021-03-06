/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.tile.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

trait BaseInventory extends IInventory {
  var inv: Array[ItemStack] = new Array(getSizeInventory)

  def getInventoryStackLimit = 64

  override def hasCustomName = false
  override def getDisplayName = null
  override def getName = ""

  override def isUseableByPlayer(player: EntityPlayer) = true
  override def closeInventory(player: EntityPlayer) = {}
  override def openInventory(player: EntityPlayer) = {}

  override def getStackInSlot(i: Int): ItemStack = inv(i)

  override def isItemValidForSlot(slot: Int, stack: ItemStack) = true

  override def setInventorySlotContents(slot: Int, stack: ItemStack) = {
    inv(slot) = stack
    markDirty()
  }

  override def removeStackFromSlot(slot: Int) = {
    val st = inv(slot)
    inv(slot) = null
    markDirty()
    st
  }

  override def decrStackSize(slot: Int, n: Int): ItemStack = {
    val item = inv(slot)
    if (item != null) {
      if (item.stackSize <= n) {
        inv(slot) = null
        markDirty()
        return item
      } else {
        val newStack = item.splitStack(n)
        if (item.stackSize == 0) {
          inv(slot) = null
        }
        markDirty()
        return newStack
      }
    } else {
      return null
    }
  }

  override def clear() = {
    inv = new Array(getSizeInventory)
    markDirty()
  }

  override def getFieldCount = 0
  override def getField(id: Int) = 0
  override def setField(id: Int, value: Int) = {}
}
