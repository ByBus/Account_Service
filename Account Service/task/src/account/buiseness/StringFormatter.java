package account.buiseness;

public interface StringFormatter<T> {
    T format(String string);
    String formatToString(T obj);
}
