package org.example.app.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.app.domain.User;
import org.example.framework.attribute.RequestAttributes;
import org.example.framework.security.Authentication;
import java.util.Collection;
import java.util.stream.Stream;

public class UserHelper {
  private UserHelper() {
  }

  public static User getUser(HttpServletRequest req) {
    return Stream.of(req.getAttribute(RequestAttributes.AUTH_ATTR))
            .map(o -> ((Authentication)o).getPrincipal())
            .map(o -> ((User)o))
            .findFirst().orElseThrow(RuntimeException::new);
  }

  public static Collection<String> getRoles(HttpServletRequest req) {
    return Stream.of(req.getAttribute(RequestAttributes.AUTH_ATTR))
            .map(o -> ((Authentication)o).getAuthorities())
            .findAny().orElseThrow(RuntimeException::new);
  }

}
