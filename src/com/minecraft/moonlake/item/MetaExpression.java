package com.minecraft.moonlake.item;

import com.minecraft.moonlake.api.item.ItemLibraryFactorys;
import com.minecraft.moonlake.api.item.meta.MetaLibrary;
import com.minecraft.moonlake.property.ReadOnlyBooleanProperty;
import com.minecraft.moonlake.property.ReadOnlyIntegerProperty;
import com.minecraft.moonlake.property.SimpleBooleanProperty;
import com.minecraft.moonlake.property.SimpleIntegerProperty;
import com.minecraft.moonlake.util.StringUtil;
import com.minecraft.moonlake.validate.Validate;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by MoonLake on 2016/9/12.
 */
public class MetaExpression extends AttributeExpression implements MetaLibrary {

    public MetaExpression() {

    }

    @Override
    public ItemStack setDisplayName(ItemStack itemStack, String displayName) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(displayName, "The itemstack displayName object is null.");

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;

        if(itemMeta != null) {

            itemMeta.setDisplayName(StringUtil.toColor(displayName).get());
            itemStack.setItemMeta(itemMeta);
            itemStack.setDurability((short)100);
        }
        return itemStack;
    }

    @Override
    public ItemStack setAmount(ItemStack itemStack, int amount) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        itemStack.setAmount(amount);

        return itemStack;
    }

    @Override
    public ItemStack setDurability(ItemStack itemStack, int durability) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        itemStack.setDurability((short) durability);

        return itemStack;
    }

    @Override
    public ReadOnlyIntegerProperty getDurability(ItemStack itemStack) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        return new SimpleIntegerProperty(itemStack.getDurability());
    }

    @Override
    public ItemStack resetDurability(ItemStack itemStack) {

        return setDurability(itemStack, 0); // set durability to 0 be recipe itemstack
    }

    @Override
    public ItemStack addDurability(ItemStack itemStack, int durability) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        int nowDurability = itemStack.getDurability();

        return setDurability(itemStack, nowDurability - durability); // set durability subtract be add durability
    }

    @Override
    public ItemStack takeDurability(ItemStack itemStack, int durability) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        int nowDurability = itemStack.getDurability();

        return setDurability(itemStack, nowDurability + durability); // set durability add be add durability
    }

    @Override
    public Set<String> getLores(ItemStack itemStack) {

        return getLores(itemStack, false);
    }

    @Override
    public Set<String> getLores(ItemStack itemStack, boolean ignoreColor) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;

        if(itemMeta == null || !itemMeta.hasLore()) {

            return null;
        }
        if(!ignoreColor) {

            return new HashSet<>(itemMeta.getLore());
        }
        return new HashSet<>(StringUtil.stripColor(itemMeta.getLore()).get());
    }

    @Override
    public ItemStack setLore(ItemStack itemStack, String... lore) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(lore, "The itemstack lore object is null.");

        return setLore(itemStack, Arrays.asList(lore));
    }

    @Override
    public ItemStack setLore(ItemStack itemStack, Collection<? extends String> lore) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(lore, "The itemstack lore object is null.");

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;

        if(itemMeta != null) {

            List<String> loreList = lore.stream().map(str -> StringUtil.toColor(str).get()).collect(Collectors.toList());

            if(loreList != null) {

                itemMeta.setLore(loreList);
                itemStack.setItemMeta(itemMeta);
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack addLore(ItemStack itemStack, String... lore) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(lore, "The itemstack lore object is null.");

        if(lore.length <= 0) {

            return itemStack;
        }
        Set<String> sourceLore = getLores(itemStack);

        if(sourceLore == null) {

            sourceLore = new HashSet<>();
        }
        sourceLore.addAll(Arrays.asList(lore));

        return setLore(itemStack, sourceLore);
    }

    @Override
    public ItemStack addLore(ItemStack itemStack, Collection<? extends String> lore) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(lore, "The itemstack lore object is null.");

        if(lore.size() <= 0) {

            return itemStack;
        }
        Set<String> sourceLore = getLores(itemStack);

        if(sourceLore == null) {

            sourceLore = new HashSet<>();
        }
        sourceLore.addAll(lore);

        return setLore(itemStack, sourceLore);
    }

    @Override
    public ItemStack clearLore(ItemStack itemStack) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        return setLore(itemStack, new ArrayList<>());
    }

    @Override
    public ReadOnlyBooleanProperty hasLore(ItemStack itemStack) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        Set<String> lore = getLores(itemStack);

        return new SimpleBooleanProperty(lore != null && lore.size() > 0);
    }

    @Override
    public ReadOnlyBooleanProperty containLore(ItemStack itemStack, String... lore) {

        Validate.notNull(lore, "The itemstack lore object is null.");

        return containLore(itemStack, Arrays.asList(lore));
    }

    @Override
    public ReadOnlyBooleanProperty containLore(ItemStack itemStack, Collection<? extends String> lore) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(lore, "The itemstack lore object is null.");

        Set<String> sourceLore = getLores(itemStack);

        if(sourceLore == null) {

            return new SimpleBooleanProperty(false);
        }
        boolean state = false;

        for(final String str : lore) {

            state = sourceLore.contains(str);

            if(!state) {

                break;
            }
        }
        return new SimpleBooleanProperty(state);
    }

    @Override
    public ReadOnlyBooleanProperty containLoreIgnoreColor(ItemStack itemStack, String... lore) {

        Validate.notNull(lore, "The itemstack lore object is null.");

        return containLoreIgnoreColor(itemStack, Arrays.asList(lore));
    }

    @Override
    public ReadOnlyBooleanProperty containLoreIgnoreColor(ItemStack itemStack, Collection<? extends String> lore) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(lore, "The itemstack lore object is null.");

        Set<String> sourceLore = getLores(itemStack, true);

        if(sourceLore == null) {

            return new SimpleBooleanProperty(false);
        }
        boolean state = false;

        for(final String str : lore) {

            state = sourceLore.contains(str);

            if(!state) {

                break;
            }
        }
        return new SimpleBooleanProperty(state);
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments(ItemStack itemStack) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        Map<Enchantment, Integer> enchantmentMap = itemStack.getEnchantments();

        return enchantmentMap != null && enchantmentMap.size() > 0 ? enchantmentMap : null;
    }

    @Override
    public ItemStack addEnchantment(ItemStack itemStack, Enchantment enchantment, int level) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(enchantment, "The itemstack enchantment object is null.");

        itemStack.addUnsafeEnchantment(enchantment, level);

        return itemStack;
    }

    @Override
    public ItemStack addEnchantment(ItemStack itemStack, Map<Enchantment, Integer> enchantmentMap) {

        Validate.notNull(enchantmentMap, "The itemstack enchantment map object is null.");

        if(!enchantmentMap.isEmpty()) {

            for(Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {

                addEnchantment(itemStack, entry.getKey(), entry.getValue());
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack addSafeEnchantment(ItemStack itemStack, Enchantment enchantment, int level) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(enchantment, "The itemstack enchantment object is null.");
        Validate.isTrue(level >= enchantment.getStartLevel() && level <= enchantment.getMaxLevel(), "The itemstack enchantment level not safe.");

        itemStack.addEnchantment(enchantment, level);

        return itemStack;
    }

    @Override
    public ItemStack addSafeEnchantment(ItemStack itemStack, Map<Enchantment, Integer> enchantmentMap) {

        Validate.notNull(enchantmentMap, "The itemstack enchantment map object is null.");

        if(enchantmentMap.size() > 0) {

            for(Map.Entry<Enchantment, Integer> entry : enchantmentMap.entrySet()) {

                addSafeEnchantment(itemStack, entry.getKey(), entry.getValue());
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack removeEnchantment(ItemStack itemStack, Enchantment enchantment) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(enchantment, "The itemstack enchantment object is null.");

        itemStack.removeEnchantment(enchantment);

        return itemStack;
    }

    @Override
    public ItemStack removeEnchantment(ItemStack itemStack, Collection<? extends Enchantment> enchantments) {

        Validate.notNull(enchantments, "The itemstack enchantment collection object is null.");

        if(enchantments.size() > 0) {

            for(final Enchantment enchantment : enchantments) {

                removeEnchantment(itemStack, enchantment);
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack clearEnchantment(ItemStack itemStack) {

        Validate.notNull(itemStack, "The itemstack enchantment collection object is null.");

        Map<Enchantment, Integer> enchantmentMap = getEnchantments(itemStack);

        if(enchantmentMap != null && enchantmentMap.size() > 0) {

            enchantmentMap.keySet().forEach(itemStack::removeEnchantment);
        }
        return itemStack;
    }

    @Override
    public ReadOnlyBooleanProperty hasEnchantment(ItemStack itemStack, Enchantment enchantment) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(enchantment, "The itemstack enchantment object is null.");

        return new SimpleBooleanProperty(itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchant(enchantment));
    }

    @Override
    public Set<ItemFlag> getFlags(ItemStack itemStack) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;

        if(itemMeta == null) {

            return null;
        }
        return itemMeta.getItemFlags();
    }

    @Override
    public ItemStack addFlags(ItemStack itemStack, ItemFlag... flags) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(flags, "The itemstack flag object is null.");

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;

        if(itemMeta == null) {

            return itemStack;
        }
        itemMeta.addItemFlags(flags);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public ItemStack addFlags(ItemStack itemStack, Collection<? extends ItemFlag> flags) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(flags, "The itemstack flag object is null.");

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;

        if(itemMeta == null) {

            return itemStack;
        }
        itemMeta.addItemFlags(flags.toArray(new ItemFlag[flags.size()]));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public ItemStack removeFlags(ItemStack itemStack, ItemFlag... flags) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(flags, "The itemstack flag object is null.");

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;

        if(itemMeta == null) {

            return itemStack;
        }
        itemMeta.removeItemFlags(flags);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public ItemStack removeFlags(ItemStack itemStack, Collection<? extends ItemFlag> flags) {

        Validate.notNull(itemStack, "The itemstack object is null.");
        Validate.notNull(flags, "The itemstack flag object is null.");

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : null;

        if(itemMeta == null) {

            return itemStack;
        }
        itemMeta.removeItemFlags(flags.toArray(new ItemFlag[flags.size()]));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public ReadOnlyBooleanProperty hasFlags(ItemStack itemStack, ItemFlag... flags) {

        Validate.notNull(flags, "The itemstack flag object is null.");

        Set<ItemFlag> flagSet = getFlags(itemStack);

        if(flagSet == null) {

            return new SimpleBooleanProperty(false);
        }
        boolean state = false;

        if(flags.length > 0) {

            for(final ItemFlag flag : flags) {

                state = flagSet.contains(flag);

                if(!state) {

                    break;
                }
            }
        }
        return new SimpleBooleanProperty(state);
    }

    @Override
    public ItemStack clearFlags(ItemStack itemStack) {

        Set<ItemFlag> flagSet = getFlags(itemStack);

        if(flagSet == null || flagSet.size() <= 0) {

            return itemStack;
        }
        return removeFlags(itemStack, flagSet);
    }

    @Override
    public ItemStack setLeatherColor(ItemStack itemStack, Color color) {

        Validate.notNull(itemStack, "The itemstack flag object is null.");
        Validate.isTrue(ItemLibraryFactorys.item().isLeatherArmor(itemStack), "The itemstack type not leather.");
        Validate.notNull(color, "The itemstack leather color object is null.");

        if(!itemStack.hasItemMeta()) {

            return itemStack;
        }
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        leatherArmorMeta.setColor(color);
        itemStack.setItemMeta(leatherArmorMeta);

        return itemStack;
    }

    @Override
    public ItemStack setLeatherColor(ItemStack itemStack, int red, int green, int blue) {

        return setLeatherColor(itemStack, Color.fromRGB(red, green, blue));
    }
}