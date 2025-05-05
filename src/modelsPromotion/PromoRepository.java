package modelsPromotion;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class PromoRepository implements DiskonRepository<Promo> {
    private ArrayList<Promo> promoList;

    public PromoRepository() {
        promoList = new ArrayList<>();
    }

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
        // Generate 8 karakter random dari variabel ALPHANUMERIC
        int panjangString = 8;
        StringBuilder sb = new StringBuilder(panjangString);
        String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < panjangString; i++) {
            int index = secureRandom.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(index));
        }

        String karakterRandom = sb.toString();

        // Get timestamp dalam format unix untuk waktu saat ini, encode ke Base64, dan ambil 8 karakter pertama
        // Ambil timestamp unix saat ini
        long timestamp = Instant.now().getEpochSecond();

        // Convert timestamp ke string dan encode base64
        String timestampStr = Long.toString(timestamp);
        String encoded = Base64.getEncoder().encodeToString(timestampStr.getBytes());

        String randomUnix = encoded.substring(0, Math.min(8, encoded.length()));

        // Combine 2 string tersebut
        String result = karakterRandom + randomUnix;

        // Ubah tanggalBerlaku ke Date dengan format yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(tanggalBerlaku, formatter);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Tambakan ke list promo
        Promo promoBaru = new Promo(result, date);
        promoList.add(promoBaru);

        // Success msg
        System.out.printf("Promo berhasil dibuat : %s\n", promoBaru.getId());
    }


}
