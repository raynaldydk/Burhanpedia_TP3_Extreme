package modelsPromotion;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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
        // Generate kode voucher random
        Random random = new Random();
        StringBuilder randomGeneratedCode = new StringBuilder();

        // Randomly get 10 digit code from A-Z
        for (int i = 0; i < 10; i++) {
            char randomGeneratedChar = (char) (random.nextInt(26) + 'A');
            randomGeneratedCode.append(randomGeneratedChar);
        }
        String generatedCode = randomGeneratedCode.toString();

        // Convert random code menjadi numerik
        StringBuilder voucherNumericCode = new StringBuilder();
        for (int i = 0; i < generatedCode.length(); i++) {
            char randomChar = generatedCode.charAt(i);
            int randomDigit = randomChar - 'A' + 10;
            int finalDigit = (randomDigit * i) % 10;
            voucherNumericCode.append(finalDigit);
        }
        String numericCode = voucherNumericCode.toString();

        // Tentukan sisa pemakaian voucher
        int sisaPemakaian = Character.getNumericValue(numericCode.charAt(random.nextInt(10)));

        // Set sisa pemakaian ke 1, jika angka random yang didapat dari index ke-X = 0
        if(sisaPemakaian == 0){
            sisaPemakaian = 1;
        }

        // Parse tanggal
        LocalDate tanggal = LocalDate.parse(tanggalBerlaku);
        Date tanggalBerlakuDate = Date.from(tanggal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Buat instance baru voucher
        Voucher voucherBaru = new Voucher(numericCode, sisaPemakaian, tanggalBerlakuDate);

        // Simpan voucher baru ke repo
        voucherList.add(voucherBaru);

        // Print success msg
        System.out.printf("Voucher berhasil dibuat: %s\n", voucherBaru.getId());

    }
}
