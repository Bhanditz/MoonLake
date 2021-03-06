/*
 * Copyright (C) 2016-Present The MoonLake (mcmoonlake@hotmail.com)
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

package com.mcmoonlake.api.packet

import com.mcmoonlake.api.block.BlockData
import com.mcmoonlake.api.block.BlockPosition
import java.util.*

data class PacketOutMultiBlockChange(
        var chunkX: Int,
        var chunkZ: Int,
        var blockData: MutableList<Data>
) : PacketOutBukkitAbstract("PacketPlayOutMultiBlockChange") {

    @Deprecated("")
    constructor() : this(0, 0, ArrayList())

    override fun read(data: PacketBuffer) {
        chunkX = data.readInt()
        chunkZ = data.readInt()
        blockData = (0 until data.readVarInt()).map {
            Data(data.readShort().toInt(), BlockData.fromId(data.readVarInt()))
        }.toMutableList()
    }

    override fun write(data: PacketBuffer) {
        data.writeInt(chunkX)
        data.writeInt(chunkZ)
        data.writeVarInt(blockData.size)
        blockData.forEach {
            data.writeShort(it.offset);
            data.writeVarInt(it.blockData.toId())
        }
    }

    data class Data(val offset: Int, var blockData: BlockData) {

        val blockPosition: BlockPosition
            get() = BlockPosition(offset.shr(12).and(15), offset and 0xFF, offset.shr(8).and(15))
    }
}
