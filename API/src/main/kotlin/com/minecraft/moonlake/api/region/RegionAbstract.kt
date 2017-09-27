/*
 * Copyright (C) 2016-2017 The MoonLake (mcmoonlake@hotmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.minecraft.moonlake.api.region

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity

abstract class RegionAbstract(private val _world: World) : Region {

    override val world: World
        get() = _world

    override val center: RegionVector
        get() = minimumPoint + maximumPoint / 2

    override val area: Int
        get() {
            val min = minimumPoint
            val max = maximumPoint
            return ((max.x - min.x + 1.0) * (max.y - min.y + 1.0) * (max.z - min.z + 1.0)).toInt()
        }

    override val width: Int
        get() {
            val min = minimumPoint
            val max = maximumPoint
            return (max.x - min.x + 1.0).toInt()
        }

    override val height: Int
        get() {
            val min = minimumPoint
            val max = maximumPoint
            return (max.y - min.y + 1.0).toInt()
        }

    override val length: Int
        get() {
            val min = minimumPoint
            val max = maximumPoint
            return (max.z - min.z + 1.0).toInt()
        }

    override fun contains(location: Location): Boolean
            = if(location.world != world) false else contains(RegionVector(location.x, location.y, location.z))

    override fun contains(entity: Entity): Boolean
            = contains(entity.location)

    override fun contains(block: Block): Boolean
            = contains(block.location)

    override fun iterator(): MutableIterator<RegionVectorBlock>
            = RegionIterator(this)

    /** significant */

    override fun equals(other: Any?): Boolean {
        if(other === this)
            return true
        if(other is RegionAbstract)
            return world == other.world
        return false
    }

    override fun hashCode(): Int {
        return world.hashCode()
    }

    override fun toString(): String {
        return "RegionAbstract(world=${world.name})"
    }

    /** abstract */

    override abstract fun serialize(): MutableMap<String, Any>
}
