package com.minecraft.moonlake.api.player;

import com.minecraft.moonlake.exception.PlayerNotOnlineException;
import com.minecraft.moonlake.validate.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * <h1>PlayerExpression</h1>
 * 玩家支持库抽象实现类
 *
 * @version 1.0
 * @author Month_Light
 */
abstract class PlayerExpression implements PlayerLibrary {

    /**
     * 玩家支持库抽象实现类构造函数
     */
    public PlayerExpression() {

    }

    @Override
    public Player fromName(String name) {

        Validate.notNull(name, "The player name object is null.");

        return Bukkit.getServer().getPlayer(name);
    }

    @Override
    public Player fromUUID(UUID uuid) {

        Validate.notNull(uuid, "The player uuid object is null.");

        return Bukkit.getServer().getPlayer(uuid);
    }

    @Override
    @SuppressWarnings("deprecation")
    public Player fromUUID(String uuid) {

        Validate.notNull(uuid, "The player uuid object is null.");

        return fromUUID(UUID.fromString(uuid));
    }

    @Override
    public MoonLakePlayer fromNames(String name) {

        Player target = fromName(name);

        if(target == null || !target.isOnline()) {

            throw new PlayerNotOnlineException(name);
        }
        return new SimpleMoonLakePlayer(target);
    }

    @Override
    public MoonLakePlayer fromUUIDs(UUID uuid) {

        Player target = fromUUID(uuid);

        if(target == null || !target.isOnline()) {

            throw new PlayerNotOnlineException(uuid.toString());
        }
        return new SimpleMoonLakePlayer(target);
    }

    @Override
    @SuppressWarnings("deprecation")
    public MoonLakePlayer fromUUIDs(String uuid) {

        Validate.notNull(uuid, "The player uuid object is null.");

        return fromUUIDs(UUID.fromString(uuid));
    }
}
