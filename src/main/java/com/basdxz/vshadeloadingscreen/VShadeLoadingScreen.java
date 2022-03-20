package com.basdxz.vshadeloadingscreen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import lombok.*;

@Mod(modid = Tags.MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.7.10]")
public class VShadeLoadingScreen {

    @Mod.EventHandler
    @SneakyThrows
    public void postInit(FMLPostInitializationEvent event) {
       // Thread.sleep(100000);
    }
}