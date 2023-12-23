package net.flandre923.genshinimpactmod.common.lib;

import net.flandre923.genshinimpactmod.GenshinImpactMod;
import net.minecraft.util.Identifier;

public class ResourceLocationHelper {
    public static Identifier prefix(String path) {
        return new Identifier(GenshinImpactMod.MOD_ID, path);
    }
}
