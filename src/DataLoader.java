import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class DataLoader {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static List<Character> loadCharacters(String path) {
        try (FileReader reader = new FileReader(path)) {
            Type listType = new TypeToken<List<Character>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<Event> loadEvents(String path) {
        try (FileReader reader = new FileReader(path)) {
            Type listType = new TypeToken<List<Event>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
