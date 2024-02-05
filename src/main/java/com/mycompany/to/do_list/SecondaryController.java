package com.mycompany.to.do_list;

import static com.mycompany.to.do_list.PrimaryController.etkinlikListesi;
import static com.mycompany.to.do_list.PrimaryController.secilenTarih;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SecondaryController {
    @FXML
    Button yeni_etkinlik,etkinlik_sil,etkinlik_duzenle,ana_sayfa;
    @FXML
    ListView liste;
    @FXML
    TextField saat_baslangic,saat_bitis;
    @FXML
    TextArea etkinlik_icerik;
    
    
    @FXML
    private void initialize() {
        etkinlikleriYukle();
    }
    
    void etkinlikleriYukle() {
        // String dosyaAdi = "etkinlikler.txt";

        // Sadece seçilen tarihe ait etkinlikleri filtrele
        try {
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
    private void etkinlikSec() {
        try {
            int selectedIndex = liste.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                String secilenEtkinlik = (String) liste.getItems().get(selectedIndex);
                String[] etkinlikBilgileri = secilenEtkinlik.split(" / ");

                // Etkinlik bilgilerini TextField ve TextArea'ya yerleştir
                if (etkinlikBilgileri.length >= 4) {
                    saat_baslangic.setText(etkinlikBilgileri[1]);
                    saat_bitis.setText(etkinlikBilgileri[2]);
                    etkinlik_icerik.setText(etkinlikBilgileri[3]);
                } 
                else {
                    showAlert("Uyarı", "Etkinlik bilgileri eksik.");
                }
            } 
            else {
                showAlert("Uyarı", "Lütfen bir etkinlik seçin.");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void etkinlikDuzenle() {
        try {
            int selectedIndex = liste.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                String secilenEtkinlik = (String) liste.getItems().get(selectedIndex);
                String[] etkinlikBilgileri = secilenEtkinlik.split(" / ");

                // Etkinlik bilgilerini TextField ve TextArea'ya yerleştir
                if (etkinlikBilgileri.length >= 4) {
                    // Değişiklikleri uygula
                    String yeniSaatBaslangic = saat_baslangic.getText();
                    String yeniSaatBitis = saat_bitis.getText();
                    String yeniIcerik = etkinlik_icerik.getText();

                    // Yeni bilgileri içeren etkinliği oluştur
                    String guncellenenEtkinlik = etkinlikBilgileri[0] + " / " + yeniSaatBaslangic + " / " + yeniSaatBitis + " / " + yeniIcerik;

                    // Etkinliği güncelle
                    liste.getItems().set(selectedIndex, guncellenenEtkinlik);
                    etkinlikListesi.remove(selectedIndex);
                    etkinlikListesi.add(guncellenenEtkinlik);

                    // Dosyaya tekrar yaz
                    dosyayaYaz();
                    saat_baslangic.setText("");
                    saat_bitis.setText("");
                    etkinlik_icerik.setText("");
                } 
                else {
                    showAlert("Uyarı", "Etkinlik bilgileri eksik.");
                }
            } 
            else {
                showAlert("Uyarı", "Lütfen bir etkinlik seçin.");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    @FXML
    private void etkinlikSil() {
        try {
            int selectedIndex = liste.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                // Seçilen etkinliği sil
                etkinlikListesi.remove(selectedIndex);

                // ListView'i güncelle
                liste.getItems().setAll(etkinlikListesi);

                // Dosyaya tekrar yaz
                dosyayaYaz();
                saat_baslangic.setText("");
                saat_bitis.setText("");
                etkinlik_icerik.setText("");
            } 
            else {
                showAlert("Uyarı", "Lütfen bir etkinlik seçin.");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean isValidTimeFormat(String time) {
        // Saat formatı kontrolü için bir regex kullanabilirsiniz
        // Bu örnekte, iki haneli sayılar arasında iki nokta işareti ile ayrılmış format kabul ediliyor
        String timeRegex = "^\\d{2}:\\d{2}$";
        return time.matches(timeRegex);
    }

    @FXML
    private void yeniEtkinlik() {
        try {
            if (secilenTarih != null) {
                String baslangicSaat = saat_baslangic.getText();
                String bitisSaat = saat_bitis.getText();
                String icerik = etkinlik_icerik.getText();

                // Gerekli kontrol işlemleri
                if (baslangicSaat.isEmpty() || bitisSaat.isEmpty() || icerik.isEmpty()) {
                    showAlert("Uyarı", "Tüm alanları doldurun.");
                    return;
                }

                // Saat format kontrolü
                if (!isValidTimeFormat(baslangicSaat) || !isValidTimeFormat(bitisSaat)) {
                    showAlert("Uyarı", "Hatalı saat formatı");
                    return;
                }

                // Yeni etkinliği listeye ekle
                String yeniEtkinlik = secilenTarih + " / " + baslangicSaat + " / " + bitisSaat + " / " + icerik;
                etkinlikListesi.add(yeniEtkinlik);

                // ListView'i güncelle
                etkinlikleriYukle();

                // Dosyaya tekrar yaz
                dosyayaYaz();
                saat_baslangic.setText("");
                saat_bitis.setText("");
                etkinlik_icerik.setText("");
            } 
            else {
                showAlert("Uyarı", "Lütfen bir tarih seçin.");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static void dosyayaYaz() {
        if (secilenTarih != null) {
            // Dosyayı temizle ve tekrar yaz
            String dosyaAdi = "etkinlikler.txt";

            try (PrintWriter writer = new PrintWriter(new FileWriter(dosyaAdi))) {
                for (String etkinlik : etkinlikListesi) {
                    writer.println(etkinlik);
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void switchToPrimary() throws IOException { // primary ekrana geçiş yap
        App.setRoot("primary");
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

}