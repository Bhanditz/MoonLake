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

package com.minecraft.moonlake.impl.player

import com.minecraft.moonlake.api.player.IllegalOfflinePlayerException
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

open class MoonLakePlayerImpl_v1_10_R1 : MoonLakePlayerImpl_v1_9_R2 {

    /** constructor */

    @Throws(IllegalOfflinePlayerException::class)
    constructor(uuid: UUID) : super(uuid)

    @Throws(IllegalOfflinePlayerException::class)
    constructor(player: Player) : super(player)

    override fun stopSound(sound: Sound)
            = bukkitPlayer.stopSound(sound)

    override fun stopSound(sound: String)
            = bukkitPlayer.stopSound(sound)
}
