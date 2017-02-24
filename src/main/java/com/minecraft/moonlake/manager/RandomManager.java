/*
 * Copyright (C) 2016 The MoonLake Authors
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
 
 
package com.minecraft.moonlake.manager;

import com.minecraft.moonlake.validate.Validate;
import org.bukkit.Color;

import java.util.Random;
import java.util.UUID;

/**
 * <h1>RandomManager</h1>
 * 随机管理实现类
 *
 * @version 1.0
 * @author Month_Light
 */
public class RandomManager extends MoonLakeManager {

    private final static Random RANDOM;
    private final static char[] STRING_CHAR;

    static {

        RANDOM = new Random(System.nanoTime());
        STRING_CHAR = new char[] {

                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
        };
    }

    /**
     * 随机管理实现类构造函数
     */
    private RandomManager() {

    }

    /**
     * 获取随机类实例对象
     *
     * @return 随机类实例对象
     */
    public static Random getRandom() {

        return RANDOM;
    }

    /**
     * 获取指定范围的整数型随机数
     *
     * @param min 最低范围
     * @param max 最大范围
     * @return 范围内的随机数
     * @throws IllegalArgumentException 如果最低范围大于最大范围则抛出异常
     */
    public static int nextInt(final int min, final int max) {

        Validate.isTrue(min < max, "The random int value (min > max) exception.");

        if(min == max) {

            return min;
        }
        return Math.abs(getRandom().nextInt()) % (max - min + 1) + min;
    }

    /**
     * 获取指定范围的长整数型随机数
     *
     * @param min 最低范围
     * @param max 最大范围
     * @return 范围内的随机数
     * @throws IllegalArgumentException 如果最低范围大于最大范围则抛出异常
     */
    public static long nextLong(final long min, final long max) {

        Validate.isTrue(min < max, "The random int value (min > max) exception.");

        if(min == max) {

            return min;
        }
        return Math.abs(getRandom().nextLong()) % (max - min + 1) + min;
    }

    /**
     * 获取指定范围的单精度浮点型随机数
     *
     * @param min 最低范围
     * @param max 最大范围
     * @return 范围内的随机数
     * @throws IllegalArgumentException 如果最低范围大于最大范围则抛出异常
     */
    public static double nextFloat(final float min, final float max) {

        Validate.isTrue(min < max, "The random double value (min > max) exception.");

        if(min == max) {

            return min;
        }
        return Math.abs(getRandom().nextFloat()) * (max - min) + min;
    }

    /**
     * 获取指定范围的双精度浮点型随机数
     *
     * @param min 最低范围
     * @param max 最大范围
     * @return 范围内的随机数
     * @throws IllegalArgumentException 如果最低范围大于最大范围则抛出异常
     */
    public static double nextDouble(final double min, final double max) {

        Validate.isTrue(min < max, "The random double value (min > max) exception.");

        if(min == max) {

            return min;
        }
        return Math.abs(getRandom().nextDouble()) * (max - min) + min;
    }

    /**
     * 获取随机的 UUID 对象
     *
     * @return UUID
     */
    public static UUID nextUUID() {

        return UUID.randomUUID();
    }

    /**
     * 获取随机长度的字符串
     *
     * @param length 长度
     * @return 随机字符串
     */
    public static String nextString(int length) {

        return nextString(length, STRING_CHAR);
    }

    /**
     * 获取随机长度的字符串
     *
     * @param length 长度
     * @return 随机字符串
     * @throws IllegalArgumentException 如果随机字符数组对象为 {@code null} 则抛出异常
     */
    public static String nextString(int length, char[] randomChar) {

        Validate.notNull(randomChar, "The random char[] object is null.");

        char[] valueChar = new char[length];

        for(int i = 0; i < valueChar.length; i++) {

            valueChar[i] = randomChar[getRandom().nextInt(randomChar.length)];
        }
        return new String(valueChar);
    }

    /**
     * 获取随机的 RGB 颜色 (0 - 255)
     *
     * @return 随机颜色
     */
    public static Color nextColor() {

        int red = getRandom().nextInt(256);
        int green = getRandom().nextInt(256);
        int blue = getRandom().nextInt(256);

        return Color.fromRGB(red, green, blue);
    }
}