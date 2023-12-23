package net.flandre923.genshinimpactmod.data;

import com.google.gson.JsonElement;
import net.flandre923.genshinimpactmod.GenshinImpactMod;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.flandre923.genshinimpactmod.common.lib.ResourceLocationHelper.prefix;

public class ItemModelProvider implements DataProvider {
    private static final TextureKey LAYER1 = TextureKey.of("layer1");
    private static final TextureKey LAYER2 = TextureKey.of("layer2");
    private static final TextureKey LAYER3 = TextureKey.of("layer3");
    private static final Model GENERATED_1 = new Model(Optional.of(new Identifier("item/generated")), Optional.empty(), TextureKey.LAYER0, LAYER1);
    private static final Model GENERATED_2 = new Model(Optional.of(new Identifier("item/generated")), Optional.empty(), TextureKey.LAYER0, LAYER1, LAYER2);
    private static final Model HANDHELD_1 = new Model(Optional.of(new Identifier("item/handheld")), Optional.empty(), TextureKey.LAYER0, LAYER1);
    private static final Model HANDHELD_3 = new Model(Optional.of(new Identifier("item/handheld")), Optional.empty(), TextureKey.LAYER0, LAYER1, LAYER2, LAYER3);
    private static final Model WALL_INVENTORY = new Model(Optional.of(prefix("block/shapes/wall_inventory")), Optional.empty(), TextureKey.TOP, TextureKey.BOTTOM, TextureKey.WALL);
    private static final Model WALL_INVENTORY_CHECKERED = new Model(Optional.of(prefix("block/shapes/wall_inventory_checkered")), Optional.empty(), TextureKey.NORTH, TextureKey.SIDE);
    private static final TextureKey OUTSIDE = TextureKey.of("outside");
    private static final TextureKey CORE = TextureKey.of("core");
    private static final Model SPREADER = new Model(Optional.of(prefix("block/shapes/spreader_item")), Optional.empty(), TextureKey.SIDE, TextureKey.BACK, TextureKey.INSIDE, OUTSIDE, CORE);
//    private static final ModelWithOverrides GENERATED_OVERRIDES = new ModelWithOverrides(new ResourceLocation("item/generated"), TextureSlot.LAYER0);
//    private static final ModelWithOverrides GENERATED_OVERRIDES_1 = new ModelWithOverrides(new ResourceLocation("item/generated"), TextureSlot.LAYER0, LAYER1);
//    private static final ModelWithOverrides HANDHELD_OVERRIDES = new ModelWithOverrides(new ResourceLocation("item/handheld"), TextureSlot.LAYER0);
//    private static final ModelWithOverrides HANDHELD_OVERRIDES_2 = new ModelWithOverrides(new ResourceLocation("item/handheld"), TextureSlot.LAYER0, LAYER1, LAYER2);

    private final DataOutput packOutput;

    public ItemModelProvider(DataOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Set<Item> items = Registries.ITEM.stream().filter(i -> GenshinImpactMod.MOD_ID.equals(Registries.ITEM.getDefaultId().getNamespace()))
                .collect(Collectors.toSet());
        Map<Identifier, Supplier<JsonElement>> map = new HashMap<>();

//        registerItemBlocks(takeAll(items, i -> i instanceof BlockItem).stream().map(i -> (BlockItem) i).collect(Collectors.toSet()), map::put);
//        registerItemOverrides(items, map::put);
        registerItems(items, map::put);

        DataOutput.PathResolver modelPathProvider = packOutput.getResolver(DataOutput.OutputType.RESOURCE_PACK, "models");

        List<CompletableFuture<?>> output = new ArrayList<>();
        for (Map.Entry<Identifier, Supplier<JsonElement>> e : map.entrySet()) {
            Identifier id = e.getKey();
            output.add(DataProvider.writeToPath(writer,e.getValue().get(), modelPathProvider.resolveJson(id)));
        }

        return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
    }

    private static void registerItems(Set<Item> items, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
        takeAll(items).forEach(i -> Models.HANDHELD.upload(ModelIds.getItemModelId(i), TextureMap.layer0(i), consumer));

    }
    public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
        List<T> ret = Arrays.asList(items);
        for (T item : items) {
            if (!src.contains(item)) {
                GenshinImpactMod.LOGGER.warn("Item {} not found in set", item);
            }
        }
        if (!src.removeAll(ret)) {
            GenshinImpactMod.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
        }
        return ret;
    }

    public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList<>();

        Iterator<T> iter = src.iterator();
        while (iter.hasNext()) {
            T item = iter.next();
            if (pred.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }

        if (ret.isEmpty()) {
            GenshinImpactMod.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
        }
        return ret;
    }

    @Override
    public String getName() {
        return  "Genshin Impact Mod - Item Model Provider";
    }
}
