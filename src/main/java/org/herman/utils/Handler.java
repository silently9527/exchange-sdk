package org.herman.utils;

@FunctionalInterface
public interface Handler<T> {

  void handle(T t);
}
