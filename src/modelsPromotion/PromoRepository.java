package modelsPromotion;

import java.util.ArrayList;

public class PromoRepository implements DiskonRepository<Promo> {
    private ArrayList<Promo> promoList;

    @Override
    public Promo getById(String id) {
        for (Promo promo : promoList) {
            if(promo.getId().equals(id)){
                return promo;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Promo> getAll() {
        return promoList;
    }

    @Override
    public void generate(String tanggalBerlaku) {
        // TODO
    }
}
