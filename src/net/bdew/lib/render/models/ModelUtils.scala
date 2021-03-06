/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/bdlib
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.lib.render.models

import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.resources.model.IBakedModel
import net.minecraftforge.client.model.{IFlexibleBakedModel, IPerspectiveAwareModel}

object ModelUtils {

  /**
    * Ensures a model implements IPerspectiveAwareModel
    * Most models already implement that interface, even if it isn't represented in their compile-time type, so this will just be a cast 99% of the time
    *
    * @return either model itself casted to the right type or it wrapped in am implementation based on getItemCameraTransforms
    */
  def makePerspectiveAware(model: IBakedModel): IPerspectiveAwareModel =
    if (model.isInstanceOf[IPerspectiveAwareModel])
      model.asInstanceOf[IPerspectiveAwareModel]
    else
      new IPerspectiveAwareModel.MapWrapper(makeFlexible(model), IPerspectiveAwareModel.MapWrapper.getTransforms(model.getItemCameraTransforms))

  /**
    * Ensures a model implements IFlexibleBakedModel
    * Most models already implement that interface, even if it isn't represented in their compile-time type, so this will just be a cast 99% of the time
    *
    * @return either model itself casted to the right type or it wrapped in a default implementation
    */
  def makeFlexible(model: IBakedModel): IFlexibleBakedModel =
    if (model.isInstanceOf[IFlexibleBakedModel])
      model.asInstanceOf[IFlexibleBakedModel]
    else
      new IFlexibleBakedModel.Wrapper(model, DefaultVertexFormats.BLOCK)
}
