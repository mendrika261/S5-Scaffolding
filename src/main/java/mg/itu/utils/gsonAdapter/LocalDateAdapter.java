package mg.itu.utils.gsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Override
  public void write(JsonWriter jsonWriter, LocalDate localDate)
      throws IOException {
    if (localDate == null) {
      jsonWriter.nullValue();
    } else {
      jsonWriter.value(localDate.format(FORMATTER));
    }
  }

  @Override
  public LocalDate read(JsonReader jsonReader) throws IOException {
    return LocalDate.parse(jsonReader.nextString(), FORMATTER);
  }
}
