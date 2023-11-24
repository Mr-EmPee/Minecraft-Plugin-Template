package ml.empee.templateplugin.registries.themes;

import lombok.RequiredArgsConstructor;
import mr.empee.lightwire.annotations.Singleton;

@Singleton
@RequiredArgsConstructor
public class ThemeRegistry {

  private final Theme defaultTheme;

  public Theme getTheme(Type type) {
    switch (type) {
      case DEFAULT:
        return defaultTheme;
    }

    throw new IllegalArgumentException("Invalid theme " + type);
  }

  public enum Type {
    DEFAULT
  }

}
