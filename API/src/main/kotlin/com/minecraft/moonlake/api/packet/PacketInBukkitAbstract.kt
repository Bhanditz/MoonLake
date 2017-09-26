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

package com.minecraft.moonlake.api.packet

import com.minecraft.moonlake.api.player.MoonLakePlayer
import com.minecraft.moonlake.api.runTaskAsync
import com.minecraft.moonlake.api.utility.MinecraftReflection
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

abstract class PacketInBukkitAbstract(clazzName: String) : PacketBukkitAbstract(MinecraftReflection.getMinecraftClass(clazzName)), PacketInBukkit {

    override fun receive(sender: MoonLakePlayer)
            = receive(sender.getBukkitPlayer())

    override fun receive(sender: Player) = try {
        // TODO
    } catch(e: Exception) {
        throw PacketException(e)
    }

    override fun receiveAsync(sender: MoonLakePlayer, plugin: Plugin)
            = receiveAsync(sender.getBukkitPlayer(), plugin)

    override fun receiveAsync(sender: Player, plugin: Plugin)
            { plugin.runTaskAsync { receive(sender) } }
}
