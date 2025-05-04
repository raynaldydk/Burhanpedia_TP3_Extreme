package modelsPromotion;

import java.util.ArrayList;

public interface DiskonRepository<T> {
    T getById(String id);
    ArrayList<T> getAll();
    void generate(String tanggalBerlaku);
}
