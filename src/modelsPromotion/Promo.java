package modelsPromotion;

import java.util.Date;

public class Promo {
    private String id;
    private Date berlakuHingga;

    public Promo(String id, Date berlakuHingga) {
        this.id = id;
        this.berlakuHingga = berlakuHingga;
    }

    public int calculateDisc() {
        int sum = 0;

        // Iterasi setiap karakter di id
        for (char c : id.toCharArray()) {
            if (Character.isDigit(c)) {
                sum += Character.getNumericValue(c);
            }
        }

        // Kalau tidak ada angka sama sekali
        if (sum == 0) {
            return 5;
        }

        // Kalau hasil >= 100, harus dipecah lagi
        while (sum >= 100) {
            int temp = 0;
            for (char c : String.valueOf(sum).toCharArray()) {
                temp += Character.getNumericValue(c);
            }
            sum = temp;
        }

        return sum;
    }

    public String getId() {
        return id;
    }

    public Date getBerlakuHingga() {
        return berlakuHingga;
    }
}
