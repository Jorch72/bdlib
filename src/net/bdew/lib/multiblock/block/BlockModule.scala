/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.block

import net.bdew.lib.Misc
import net.bdew.lib.PimpVanilla._
import net.bdew.lib.block.{BaseBlock, BlockTooltip, HasTE}
import net.bdew.lib.config.MachineManagerMultiblock
import net.bdew.lib.multiblock.tile.TileModule
import net.bdew.lib.multiblock.{ResourceProvider, Tools}
import net.bdew.lib.render.connected.ConnectedTextureBlock
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.{BlockPos, ChatComponentTranslation, EnumChatFormatting, EnumFacing}
import net.minecraft.world.{IBlockAccess, World}

abstract class BlockModule[T <: TileModule](name: String, val kind: String, material: Material, val TEClass: Class[T], val machines: MachineManagerMultiblock)
  extends BaseBlock(name, material) with HasTE[T] with ConnectedTextureBlock with BlockTooltip {

  def resources: ResourceProvider

  override def canPlaceBlockAt(world: World, pos: BlockPos) =
    Tools.findConnections(world, pos, kind).size <= 1

  override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) = {
    getTE(world, pos).tryConnect()
  }

  override def onNeighborBlockChange(world: World, pos: BlockPos, state: IBlockState, neighborBlock: Block) = {
    getTE(world, pos).tryConnect()
  }

  override def breakBlock(world: World, pos: BlockPos, state: IBlockState) = {
    getTE(world, pos).onBreak()
    super.breakBlock(world, pos, state)
  }

  override def canConnect(world: IBlockAccess, origin: BlockPos, target: BlockPos) = {
    val me = getTE(world, origin)
    me.connected.contains(target) || (world.getTileSafe[TileModule](target) exists { other =>
      me.getCore.isDefined && other.getCore == me.getCore
    })
  }

  override def onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (player.isSneaking) return false
    if (world.isRemote) return true
    val te = getTE(world, pos)
    if (te.connected.isDefined) {
      val p = te.connected.get
      val bs = world.getBlockState(p)
      bs.getBlock.onBlockActivated(world, p, bs, player, side, 0, 0, 0)
    } else {
      player.addChatMessage(new ChatComponentTranslation("bdlib.multiblock.notconnected"))
    }
    true
  }

  override def getTooltip(stack: ItemStack, player: EntityPlayer, advanced: Boolean): List[String] = {
    List(Misc.toLocal("bdlib.multiblock.tip.module")) ++ (
      for ((machine, (min, max)) <- machines.getMachinesForBlock(this)) yield {
        val name = machine.getController.getLocalizedName
        if (min > 0) {
          Misc.toLocalF("bdlib.multiblock.tip.module.range",
            EnumChatFormatting.YELLOW + name + EnumChatFormatting.RESET,
            EnumChatFormatting.YELLOW + min.toString + EnumChatFormatting.RESET,
            EnumChatFormatting.YELLOW + max.toString + EnumChatFormatting.RESET)
        } else {
          Misc.toLocalF("bdlib.multiblock.tip.module.max",
            EnumChatFormatting.YELLOW + name + EnumChatFormatting.RESET,
            EnumChatFormatting.YELLOW + max.toString + EnumChatFormatting.RESET)
        }
      })
  }
}
