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
 
 
package com.minecraft.moonlake.api.packet.wrapper;

import com.minecraft.moonlake.api.packet.exception.PacketInitializeException;
import com.minecraft.moonlake.property.*;
import com.minecraft.moonlake.validate.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.minecraft.moonlake.reflect.Reflect.*;

/**
 * <h1>PacketPlayOutExplosion</h1>
 * 数据包输出爆炸（详细doc待补充...）
 *
 * @version 2.0
 * @author Month_Light
 */
public class PacketPlayOutExplosion extends PacketPlayOutBukkitAbstract {

    private final static Class<?> CLASS_PACKETPLAYOUTEXPLOSION;
    private final static Class<?> CLASS_VEC3D;

    static {

        try {

            CLASS_PACKETPLAYOUTEXPLOSION = PackageType.MINECRAFT_SERVER.getClass("PacketPlayOutExplosion");
            CLASS_VEC3D = PackageType.MINECRAFT_SERVER.getClass("Vec3D");
        }
        catch (Exception e) {

            throw new PacketInitializeException("The net.minecraft.server packet play out explosion reflect raw initialize exception.", e);
        }
    }

    private DoubleProperty x;
    private DoubleProperty y;
    private DoubleProperty z;
    private FloatProperty radius;
    private List<BlockPosition> records;
    private ObjectProperty<Vector> vector;

    public PacketPlayOutExplosion() {

        this(0d, 0d, 0d);
    }

    public PacketPlayOutExplosion(double x, double y, double z) {

        this(x, y, z, 0f, null, null);
    }

    public PacketPlayOutExplosion(Location location, float radius) {

        this(location.getX(), location.getY(), location.getZ(), radius, null, null);
    }

    public PacketPlayOutExplosion(Location location, float radius, List<BlockPosition> records, Vector vector) {

        this(location.getX(), location.getY(), location.getZ(), radius, records, vector);
    }

    public PacketPlayOutExplosion(double x, double y, double z, float radius) {

        this(x, y, z, radius, null, null);
    }

    public PacketPlayOutExplosion(double x, double y, double z, float radius, List<BlockPosition> records, Vector vector) {

        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.z = new SimpleDoubleProperty(z);
        this.radius = new SimpleFloatProperty(radius);
        this.records = records == null ? new ArrayList<>() : records;
        this.vector = new SimpleObjectProperty<>(vector == null ? new Vector() : vector);
    }

    public DoubleProperty xProperty() {

        return x;
    }

    public DoubleProperty yProperty() {

        return y;
    }

    public DoubleProperty zProperty() {

        return z;
    }

    public FloatProperty radiusProperty() {

        return radius;
    }

    public List<BlockPosition> recordsProperty() {

        return records;
    }

    public ObjectProperty<Vector> vectorProperty() {

        return vector;
    }

    @Override
    protected boolean sendPacket(Player... players) throws Exception {

        Vector vector = vectorProperty().get();
        Validate.notNull(vector, "The vector object is null.");

        try {
            // 先用调用 NMS 的 PacketPlayOutExplosion 构造函数, 参数 double, double, double, float, List, Vec3D
            // 进行反射实例发送
            List<Object> nmsBlockPositionList = new ArrayList<>();
            if(records != null && !records.isEmpty())
                for(BlockPosition blockPosition : records)
                    nmsBlockPositionList.add(blockPosition.asNMS());
            // 实例化其他参数值
            Object nmsVec3D = instantiateObject(CLASS_VEC3D, vector.getX(), vector.getY(), vector.getZ());
            Object packet = instantiateObject(CLASS_PACKETPLAYOUTEXPLOSION, x.get(), y.get(), z.get(), radius.get(), nmsBlockPositionList, nmsVec3D);
            sendPacket(players, packet);
            return true;

        } catch (Exception e) {
            // 如果异常了说明 NMS 的 PacketPlayOutExplosion 构造函数不存在这个参数类型
            // 那么用反射直接设置字段值方式来发送
            try {
                // 判断字段数量等于 8 个的话就是有此方式
                // 这八个字段分别对应 double, double, double, float, List, double, double, double 的 8 个属性
                List<Object> nmsBlockPositionList = new ArrayList<>();
                if(records != null && !records.isEmpty())
                    for(BlockPosition blockPosition : records)
                        nmsBlockPositionList.add(blockPosition.asNMS());

                Object packet = instantiateObject(CLASS_PACKETPLAYOUTEXPLOSION);
                Object[] values = { x.get(), y.get(), z.get(), radius.get(), nmsBlockPositionList, vector.getX(), vector.getY(), vector.getZ()};
                setFieldAccessibleAndValueSend(players, 8, CLASS_PACKETPLAYOUTEXPLOSION, packet, values);
                return true;

            } catch (Exception e1) {
            }
        }
        // 否则前面的方式均不支持则返回 false 并抛出不支持运算异常
        return false;
    }
}
