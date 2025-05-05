package modelsPromotion;

import java.util.Date;

public class Voucher {
    private String id;
    private int sisaPemakaian;
    private Date berlakuHingga;

    public Voucher(String id, int sisaPemakaian, Date berlakuHingga) {
        this.id = id;
        this.sisaPemakaian = sisaPemakaian;
        this.berlakuHingga = berlakuHingga;
    }

    public int calculateDisc(String voucher) {
        int length = voucher.length();
        int mid = length / 2;
        int sum = 0;

        // Mengalikan angka dari ujung ke tengah
        for (int i = 0; i < mid; i++) {
            int left = Character.getNumericValue(voucher.charAt(i));
            int right = Character.getNumericValue(voucher.charAt(length - 1 - i));
            sum += left * right;
        }

        // Case untuk sum yang panjangnya ganjil
        if (length % 2 != 0) {
            sum += Character.getNumericValue(voucher.charAt(mid));
        }

        // Base case, jika sum <= 100, return sum
        if (sum <= 100) {
            return sum;
        }

        // Case untuk sum > 100, lakukan ulang rekursi
        return calculateDisc(String.valueOf(sum));
    }

    public String getId() {
        return id;
    }

    public Date getBerlakuHingga() {
        return berlakuHingga;
    }

    public int getSisaPemakaian() {
        return sisaPemakaian;
    }

    public void pakaiVoucher(){
        this.sisaPemakaian -= 1;
    }
}
