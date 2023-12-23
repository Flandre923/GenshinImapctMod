package net.flandre923.genshinimpactmod.common.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.flandre923.genshinimpactmod.common.item.characters.KleeA;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static net.flandre923.genshinimpactmod.common.lib.ResourceLocationHelper.prefix;

public class ModItems {
    private static final Map<Identifier, Item> ALL = new LinkedHashMap<>(); // Preserve insertion order

    public static final Item KLEE_A = make(prefix("klee_a"), new KleeA(unstackable()));

    private static <T extends Item> T make(Identifier id, T item) {
        var old = ALL.put(id, item);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        }
        return item;
    }

    public static void registerItems(BiConsumer<Item, Identifier> r) {
        for (var e : ALL.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }

    public static Item.Settings defaultBuilder() {
        return new FabricItemSettings();
    }

    private static Item.Settings unstackable() {
        return defaultBuilder().maxCount(1);
    }



}
