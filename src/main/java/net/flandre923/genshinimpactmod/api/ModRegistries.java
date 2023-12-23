package net.flandre923.genshinimpactmod.api;

import net.flandre923.genshinimpactmod.GenshinImpactMod;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModRegistries {
    public static final RegistryKey<ItemGroup> BOTANIA_TAB_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP,
            new Identifier(GenshinImpactMod.MOD_ID, "genshenimpactmod"));
}
