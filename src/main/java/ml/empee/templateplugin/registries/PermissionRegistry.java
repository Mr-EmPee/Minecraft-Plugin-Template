package ml.empee.templateplugin.registries;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mr.empee.lightwire.annotations.Instance;
import mr.empee.lightwire.annotations.Singleton;

@Singleton
@RequiredArgsConstructor
public class PermissionRegistry {

  @Getter
  @Instance
  private static PermissionRegistry permissionRegistry;

  private static final String PREFIX = "DEMO.";
  public static final String ADMIN = PREFIX + "admin";

}
