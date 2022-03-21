package com.basdxz.vshadeloadingscreen.common;

import lombok.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceHelper {
    public static String readResourceAsString(String path) {
        val is = readResourceAsInputStream(path);
        if (is == null) return null;
        val br = new BufferedReader(new InputStreamReader(is));
        val source = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                source.append(line).append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source.toString();
    }

    @SneakyThrows
    public static InputStream assetInputStream(String assetName) {
        return Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation(assetName));
    }

    public static InputStream readResourceAsInputStream(String filename) {
        return ResourceHelper.class.getResourceAsStream(filename);
    }
}
