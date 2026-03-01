package lab2.testutil;

import lab2.stub.CsvTableStubFunction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public final class StubLoader {
  private StubLoader() {}

  public static CsvTableStubFunction load(String fileName, int xKeyScale) {
    try {
      var url = StubLoader.class.getClassLoader().getResource("stubs/" + fileName);
      if (url == null) throw new IllegalStateException("Missing stub file: " + fileName);
      Path path = Path.of(url.toURI());
      return new CsvTableStubFunction(fileName, path, ";", xKeyScale);
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Failed to load stub: " + fileName, e);
    }
  }
}