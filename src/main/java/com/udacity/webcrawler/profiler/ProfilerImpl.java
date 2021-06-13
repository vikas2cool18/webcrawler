package com.udacity.webcrawler.profiler;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

/**
 * Concrete implementation of the {@link Profiler}.
 */
final class ProfilerImpl implements Profiler {

  private final Clock clock;
  private final ProfilingState state = new ProfilingState();
  private final ZonedDateTime startTime;

  @Inject
  ProfilerImpl(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.startTime = ZonedDateTime.now(clock);
  }

  @Override
  public <T> T wrap(Class<T> klass, T delegate) {
    Objects.requireNonNull(klass);

    // TODO: Use a dynamic proxy (java.lang.reflect.Proxy) to "wrap" the delegate in a
    //       ProfilingMethodInterceptor and return a dynamic proxy from this method.
    //       See https://docs.oracle.com/javase/10/docs/api/java/lang/reflect/Proxy.html.
    boolean isValid = checkProfilerHasAnnotation(klass);
    if (!isValid) {
      throw new IllegalArgumentException("Profiler.wrap() should throw an IllegalArgumentException if the wrapped interface does not contain a @Profiled method.");
    }
    Objects.requireNonNull(klass);
    InvocationHandler invocationHandler = new ProfilingMethodInterceptor(clock, delegate, state);
    T instance = (T) Proxy.newProxyInstance(klass.getClassLoader(), new Class[]{klass}, invocationHandler);
    return instance;
  }

  @Override
  public void writeData(Path path) throws IOException {
    // TODO: Write the ProfilingState data to the given file path. If a file already exists at that
    //       path, the new data should be appended to the existing file.
    Objects.requireNonNull(path);
    Writer writer = null;
    if (Files.notExists(path)) {
      Files.createFile(path);
    }
    writer = Files.newBufferedWriter(path);
    writeData(writer);
    writer.flush();
  }

  @Override
  public void writeData(Writer writer) throws IOException {
    writer.write("Run at " + RFC_1123_DATE_TIME.format(startTime));
    writer.write(System.lineSeparator());
    state.write(writer);
    writer.write(System.lineSeparator());
  }

  private boolean checkProfilerHasAnnotation(Class<?> klass) {
    Method methodArr[] = klass.getDeclaredMethods();
    if (methodArr == null || methodArr.length == 0) {
      return false;
    }
    for (Method method : methodArr) {
      if (method.getAnnotation(Profiled.class) != null) {
        return true;
      }
    }
    return false;
  }

}
