package net.flandre923.genshinimpactmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.flandre923.genshinimpactmod.api.ModRegistries;
import net.flandre923.genshinimpactmod.common.item.CustomCreativeTabContents;
import net.flandre923.genshinimpactmod.common.item.ModItems;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class GenshinImpactMod implements ModInitializer {
    public static final String MOD_ID = "genshinimpactmod";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private final Set<Item> itemsToAddToCreativeTab = new LinkedHashSet<>();

    private final BiConsumer<Item, Identifier> boundForItem =
            (t, id) -> {
                this.itemsToAddToCreativeTab.add(t);
                Registry.register(Registries.ITEM, id, t);
            };


    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        //注册物品
        ModItems.registerItems(boundForItem);

        //添加创造物品栏
        Registry.register(
                Registries.ITEM_GROUP,
                ModRegistries.BOTANIA_TAB_KEY,
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.botania.botania").styled((style -> style.withColor(Formatting.WHITE))))
                        .icon(() -> new ItemStack(ModItems.KLEE_A))
//                        .backgroundSuffix()
                        .build()
        );


        //添加物品到创造物品栏
        ItemGroupEvents.modifyEntriesEvent(ModRegistries.BOTANIA_TAB_KEY)
                .register(entries -> {
                    for (Item item : this.itemsToAddToCreativeTab) {
                        if (item instanceof CustomCreativeTabContents cc) {
                            cc.addToCreativeTab(item, entries);
                        } else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
                            cc.addToCreativeTab(item, entries);
                        } else {
                            entries.add(item);
                        }
                    }
                });

    }
}
