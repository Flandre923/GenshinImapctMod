package net.flandre923.genshinimpactmod.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public interface CustomCreativeTabContents {
    void addToCreativeTab(Item me, ItemGroup.Entries output);
}
