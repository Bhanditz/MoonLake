/*
 *  ProtocolLib - Bukkit server library that allows access to the Minecraft protocol.
 *  Copyright (C) 2012 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program;
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307 USA
 */

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

package com.minecraft.moonlake.api.nbt

import com.google.common.collect.Iterables
import com.minecraft.moonlake.api.notNull
import com.minecraft.moonlake.api.util.ConvertedList

class NBTWrappedList<T>(handle: Any, name: String) : NBTWrapper<MutableList<NBTBase<T>>>, NBTList<T> {

    private val container: NBTWrappedElement<MutableList<Any>> = NBTWrappedElement(handle, name)
    private val savedList: ConvertedList<Any, NBTBase<T>> by lazy {
        object: ConvertedList<Any, NBTBase<T>>(container.value) {
            override fun toIn(outer: NBTBase<T>): Any
                    = NBTFactory.fromBase(outer).handle
            override fun toOut(inner: Any): NBTBase<T>
                    = NBTFactory.fromNMS(inner)
            override fun toString(): String
                    = this@NBTWrappedList.toString()
            override fun add(element: NBTBase<T>): Boolean {
                validateElement(element)
                return super.add(element)
            }
            override fun add(index: Int, element: NBTBase<T>) {
                validateElement(element)
                super.add(index, element)
            }
            override fun addAll(elements: Collection<NBTBase<T>>): Boolean {
                var result = false
                elements.forEach { add(it); result = true }
                return result
            }
            private fun validateElement(element: NBTBase<T>) {
                if(elementType != NBTType.TAG_END) {
                    if(element.type != elementType)
                        throw IllegalArgumentException("不能添加元素 $element 到此类型为 $elementType 的列表.")
                } else {
                    container.setElementType(element.type)
                }
            }
        }
    }
    private var _elementType = container.getElementType()

    override val handle: Any
        get() = container.handle

    override var name: String
        get() = container.name
        set(value) { container.name = value }

    override val type: NBTType
        get() = NBTType.TAG_LIST

    override var value: MutableList<NBTBase<T>>
        get() = savedList
        set(value) {
            var lastElement: NBTBase<T>? = null
            val list = container.value
            list.clear()
            value.forEach {
                list.add(NBTFactory.fromBase(it).handle)
                lastElement = it
            }
            if(lastElement != null)
                container.setElementType(lastElement.notNull().type)
        }

    override var elementType: NBTType
        get() = _elementType
        set(value) { _elementType = value }

    override fun add(element: NBTBase<T>)
            { this.value.add(element) }

    @Suppress("UNCHECKED_CAST")
    override fun addString(value: String)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    @Suppress("UNCHECKED_CAST")
    override fun addByte(value: Byte)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    override fun addByte(value: Int)
            = addByte(value.toByte())

    @Suppress("UNCHECKED_CAST")
    override fun addShort(value: Short)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    override fun addShort(value: Int)
            = addShort(value.toShort())

    @Suppress("UNCHECKED_CAST")
    override fun addInt(value: Int)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    @Suppress("UNCHECKED_CAST")
    override fun addLong(value: Long)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    @Suppress("UNCHECKED_CAST")
    override fun addFloat(value: Float)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    @Suppress("UNCHECKED_CAST")
    override fun addDouble(value: Double)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    @Suppress("UNCHECKED_CAST")
    override fun addByteArray(value: ByteArray)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    @Suppress("UNCHECKED_CAST")
    override fun addIntArray(value: IntArray)
            = add(NBTFactory.of("", value) as NBTBase<T>)

    override fun addBoolean(value: Boolean)
            = addByte((if(value) 1 else 0).toByte())

    @Suppress("UNCHECKED_CAST")
    override fun addCompound(value: NBTCompound)
            = add(NBTFactory.ofWrapper(NBTType.TAG_COMPOUND, "", value.value) as NBTBase<T>)

    override fun remove(value: Any)
            { this.value.remove(value) }

    override fun getValue(index: Int): T
            = value[index].value

    override fun size(): Int
            = value.size

    override fun clear()
            = value.clear()

    override fun isEmpty(): Boolean
            = size() <= 0

    override fun isNotEmpty(): Boolean
            = !isEmpty()

    override fun iterator(): Iterator<T>
            = Iterables.transform(value, { input -> input?.value }).iterator()

    /** significant */

    override fun equals(other: Any?): Boolean {
        if(other === this)
            return true
        if(other is NBTWrappedList<*>)
            return other.container == container
        return false
    }

    override fun hashCode(): Int {
        return container.hashCode()
    }

    override fun toString(): String {
        return buildString {
            append("[")
            if(size() > 0) {
                if(elementType == NBTType.TAG_STRING)
                    append("\"${joinToString("\",\"")}\"")
                else
                    append(joinToString(", "))
            }
            append("]")
        }
    }
}
