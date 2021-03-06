/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.multiblock.interact

import net.minecraftforge.fluids.{Fluid, FluidStack}

trait CIFluidOutput extends CIOutputFaces with CITankInfo {
  def outputFluid(resource: FluidStack, doDrain: Boolean): FluidStack
  def outputFluid(amount: Int, doDrain: Boolean): FluidStack
  def canOutputFluid(fluid: Fluid): Boolean
}
