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

package com.mcmoonlake.api.attribute

/**
 * ## AttributeAbstract (属性抽象)
 *
 * @see [Attribute]
 * @author lgou2w
 * @since 2.0
 * @constructor AttributeAbstract
 * @param type Attribute Type.
 * @param type 属性类型.
 */
abstract class AttributeAbstract(
        override val type: AttributeType
) : Attribute {

    override val defValue: Double
        get() = type.def
}
