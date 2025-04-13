package wiam.wiamspammer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.slf4j.LoggerFactory;
import wiam.wiamspammer.automessage.AutoMessage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AutoMessageConfig {
    public AutoMessageConfig(boolean canAutoMessage, List<Entry> entryList) {
        this.canAutoMessage = canAutoMessage;
        this.entryList = entryList;
    }

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("wiamspammer.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static AutoMessageConfig INSTANCE;

    public static AutoMessageConfig getDefault() {
        List<Entry> list = new ArrayList<>();
        Entry entry1 = new Entry(true, true, false, true, new ArrayList<>(), "^(\\d{4})-(\\d{2})-(\\d{2})$", false, "Y:${0}, D:${1}, H:${2}", 0);
        list.add(entry1);
        Entry entry2 = new Entry(true, true, false, true, new ArrayList<>(), "v?(\\d+)\\.(\\d+)\\.(\\d+)(?:-([\\w.]+))?  ", false, "abbabbabba", 0);
        list.add(entry2);
        return new AutoMessageConfig(true, list);
    }

    public boolean canAutoMessage;

    public List<Entry> entryList;

    public static class Entry {
        public Entry(boolean isThisOn, boolean isPositiveMatch, boolean isMatchSystemMessage, boolean isMatchChatMessage, List<String> matchPlayers, String pattern, boolean isMatchDmMessage, String message, float sendDelay) {
            this.isThisOn = isThisOn;
            this.isPositiveMatch = isPositiveMatch;
            this.isMatchSystemMessage = isMatchSystemMessage;
            this.isMatchChatMessage = isMatchChatMessage;
            this.matchPlayers = matchPlayers;
            this.pattern = pattern;
            this.isMatchDmMessage = isMatchDmMessage;
            this.message = message;
            this.sendDelay = sendDelay;
        }

        public boolean isThisOn;

        public boolean isPositiveMatch;

        public boolean isMatchSystemMessage;

        public boolean isMatchChatMessage;

        public boolean isMatchDmMessage;

        public String pattern;

        public List<String> matchPlayers;

        public String message;

        public float sendDelay;

        public AutoMessage toAutoMessage(ClientPlayNetworkHandler networkHandler) {
            return new AutoMessage(isThisOn, isPositiveMatch, isMatchSystemMessage, isMatchChatMessage, isMatchDmMessage, pattern, matchPlayers, message, sendDelay, networkHandler);
        }

        public static List<String> toPlayerList(String string) {
            List<String> result = new ArrayList<>();
            if (string == null || string.trim().isEmpty()) {
                return result;
            }
            String[] parts = string.split("\\s*,\\s*");
            for (String part : parts) {
                String trimmed = part.trim();
                result.add(trimmed);
            }
            return result;
        }

        public static String toString(List<String> list) {
            if (list == null || list.isEmpty()) {
                return "";
            }
            return String.join(", ", list);
        }
    }

    public static void toAutoMessage(ClientPlayNetworkHandler networkHandler) {
        if (INSTANCE == null) load();
        if (INSTANCE.canAutoMessage) {
            AutoMessage.turnOn();
        } else {
            AutoMessage.turnOff();
        }
        AutoMessage.setAutoMessages(INSTANCE.entryList.stream().map(entry -> entry.toAutoMessage(networkHandler)).toList());
    }

    public static AutoMessageConfig load() {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(CONFIG_PATH.toFile()), StandardCharsets.UTF_8)) {
            LoggerFactory.getLogger("wiamspammer").info("Loading config file: " + CONFIG_PATH.toFile().getAbsolutePath());
            INSTANCE = GSON.fromJson(reader, AutoMessageConfig.class);
        } catch (IOException e) {
            // 文件不存在时创建默认配置
            return createDefaultConfig(CONFIG_PATH, GSON);
        }
        toAutoMessage(MinecraftClient.getInstance().getNetworkHandler());
        return INSTANCE;
    }

    public static AutoMessageConfig createDefaultConfig(Path path, Gson gson) {
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            INSTANCE = getDefault();
            save(path, gson); // 尝试保存默认配置
            return INSTANCE;
        } catch (IOException ex) {
            LoggerFactory.getLogger(AutoMessageConfig.class).error("Failed to create default config", ex);
            // 返回默认配置，即使保存失败
            return getDefault();
        }
    }

    public static void save(){
        save(CONFIG_PATH, GSON);
    }


    public static void save(Path path, Gson gson) {
        try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8)) {
            String json = gson.toJson(INSTANCE);
            writer.write(json);
            toAutoMessage(MinecraftClient.getInstance().getNetworkHandler());
        } catch (FileNotFoundException e) {
            LoggerFactory.getLogger(AutoMessageConfig.class).error("File not found", e);
        } catch (IOException e) {
            LoggerFactory.getLogger(AutoMessageConfig.class).error("I/O error", e);
        }
    }

    public static void init(){
        INSTANCE = load();
    }
}
