package account.buiseness;

public interface Mapper<T, U> {
    U mapToEntity(T dto);

    T mapToDTO(U entity);
}
