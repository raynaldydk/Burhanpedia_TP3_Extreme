package modelsPromotion;

import java.util.ArrayList;

public class VoucherRepository implements DiskonRepository<Voucher>{
    private ArrayList<Voucher> voucherList;

    public VoucherRepository() {
        voucherList = new ArrayList<>();
    }

    @Override
    public Voucher getById(String id) {
        for (Voucher voucher : voucherList) {
            if(voucher.getId().equals(id)){
                return voucher;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Voucher> getAll() {
        return voucherList;
    }

    @Override
    public void generate(String tanggalBerlaku) {
        //TODO
    }
}
