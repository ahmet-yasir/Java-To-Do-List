package com.mycompany.to.do_list;

import static com.mycompany.to.do_list.SecondaryController.dosyayaYaz;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

public class PrimaryController {
    @FXML
    Button etkinlik_duzenle, etkinlik_tamamla, cikis;  // FXML'de tanımlanan butonlar
    @FXML
    ListView liste;  // FXML'de tanımlanan ListView
    @FXML
    DatePicker tarih_secme;  // FXML'de tanımlanan DatePicker

    static List<String> etkinlikListesi = new ArrayList<>();  // Etkinlik listesini tutan static bir liste
    static String secilenTarih;  // Seçilen tarihi tutan static bir değişken

    @FXML
    private void initialize() {
        // Uygulama başladığında yapılacak işlemleri buraya ekleyebilirsiniz.
        etkinlikListesiniYukle();  // Etkinlik listesini dosyadan yükle
        LocalDate gun = LocalDate.now();
        tarih_secme.setValue(gun);
        etkinlikleriYukle();  // ListView'e etkinlikleri yükle
    }

    @FXML
    private void switchToSecondary() throws IOException {
        if (secilenTarih != null) {
            App.setRoot("secondary");  // Secondary ekranına geçiş yap
        } else {
            showAlert("Uyarı", "Lütfen bir tarih seçin.");  // Tarih seçilmemişse uyarı göster
        }
    }

    @FXML
    void etkinlikleriYukle() {
        // Sadece seçilen tarihe ait etkinlikleri filtrele
        try {
            secilenTarih = tarih_secme.getValue().toString();
            List<String> filtrelenmisListe = etkinlikListesi.stream()
                    .filter(etkinlik -> etkinlik.startsWith(secilenTarih))
                    .collect(Collectors.toList());
            // ListView'i güncelle
            liste.getItems().setAll(filtrelenmisListe);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void etkinlikTamamla() {
        int selectedIndex = liste.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Seçilen etkinliği sil
            etkinlikListesi.remove(selectedIndex);

            // ListView'i güncelle
            liste.getItems().setAll(etkinlikListesi);

            // Dosyaya tekrar yaz
            dosyayaYaz();

            showInfoDialog("Etkinlik Tamamlandı ve Silindi!");
        } else {
            // Kullanıcı bir etkinlik seçmemişse uyarı göster
            showAlert("Uyarı", "Lütfen bir etkinlik seçin.");
        }
    }

    @FXML
    private void exit() {
        // Uygulamadan çıkış işlemleri
        System.exit(0);
    }
    /**
     * Dosyadan etkinlik listesini yükler.
     * "etkinlikler.txt" adlı dosyadan okuma yapar ve etkinlikListesi'ni günceller.
     */
    private void etkinlikListesiniYukle() {
        etkinlikListesi.clear(); // Etkinlik listesini temizle

        String dosyaAdi = "etkinlikler.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(dosyaAdi))) {
            String line;
            while ((line = br.readLine()) != null) {
                etkinlikListesi.add(line); // Dosyadan okunan her satırı etkinlik listesine ekle
            }
        } catch (IOException e) {
            e.printStackTrace(); // Dosya okuma hatası durumunda hata izini yazdır
        }
    }

    /**
     * Uyarı mesajı gösteren bir iletişim kutusu görüntüler.
     * @param title Uyarı iletişim kutusu başlığı
     * @param message Uyarı içeriği
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Bilgi mesajı gösteren bir iletişim kutusu görüntüler.
     * @param message Bilgi içeriği
     */
    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
