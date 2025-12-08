package care.cuddliness.stacy.command;

import care.cuddliness.stacy.command.data.info.StacyCommandInfo;
import com.google.gson.*;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StacyCommandFileHandler {

    private final Path DATA_PATH = Path.of("data/commands.json");
    private static final Logger LOGGER = LoggerFactory.getLogger(StacyCommandFileHandler.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public @Nullable List<StacyCommandInfo> loadCommands() {
        FileReader reader;
        try {
            reader = new FileReader(DATA_PATH.toFile());
        } catch (FileNotFoundException ex) {
            return null;
        }
        JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

        List<StacyCommandInfo> commands = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            commands.add(new StacyCommandInfo(jsonObject.get("name").getAsString(), jsonObject.get("id").getAsLong()));
        }
        return commands;
    }

    public void saveCommands(@NotNull List<StacyCommandInfo> commands) {
        if (Files.notExists(DATA_PATH)) {
            try {
                Path parent = DATA_PATH.getParent();
                if (Files.notExists(parent)) Files.createDirectories(parent);
                Files.createFile(DATA_PATH);
            } catch (IOException ex) {
                LOGGER.error("Error creating slash command file", ex);
            }
        }

        try (Writer writer = Files.newBufferedWriter(DATA_PATH)) {
            JsonArray jsonArray = new JsonArray();

            for (StacyCommandInfo command : commands)
                jsonArray.add(this.createCommandObject(command));

            GSON.toJson(jsonArray, writer);
        } catch (IOException ex) {
            LOGGER.error("Error writing slash commands", ex);
        }
    }

    private @NotNull JsonObject createCommandObject(@NotNull StacyCommandInfo command) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", command.getName());
        jsonObject.addProperty("id", command.getId());
        return jsonObject;
    }
}
