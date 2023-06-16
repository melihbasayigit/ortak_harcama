package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.Main.isimler;

public class Main {

    static ArrayList<String> isimler = new ArrayList<>();
    static ArrayList<Harcama> harcamaListesi = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("İsim Gir:");
            String temp = scanner.nextLine();
            if (temp.isEmpty()) {
                break;
            }
            isimler.add(temp);
        }
        for (int i = 0; i < isimler.size(); i++) {
            System.out.println((i + 1) + ". " + isimler.get(i));
        }

        while (true) {
            System.out.println("Harcama yapan kişinin nosunu girin ve harcama tutarını girin: " +
                    "(BİTİRMEK İÇİN BOŞLUK GİRİN)");
            String input1 = scanner.nextLine();
            if (input1.isEmpty())
                break;
            System.out.println("Harcama ile bağlantılı kişilerin nosunu girin");
            String input2 = scanner.nextLine();
            String[] x = input1.split(" ");
            String[] y = input2.split(" ");
            Harcama harcama = new Harcama();
            harcama.no = Integer.parseInt(x[0]) - 1;
            harcama.harcama_tutari = Float.parseFloat(x[1]);
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = 0; i < y.length; i++) {
                arrayList.add(Integer.parseInt(y[i]) - 1);
            }
            harcama.iliskilendirilmis_kisiler = arrayList;
            harcamaListesi.add(harcama);
        }

        ArrayList<Borc> borclar = hesapla();

        for(Borc borc : borclar){
            System.out.println(borc);
        }
    }

    static ArrayList<Harcama> ilgili_harcamalar(int harcamaSahibi, int borcSahibi) {
        List<Harcama> no1Harcamalar = harcamaListesi.stream()
                .filter(harcama -> harcama.no == harcamaSahibi)
                .filter(harcama -> harcama.iliskilendirilmis_kisiler.contains(borcSahibi))
                .toList();

        return new ArrayList<>(no1Harcamalar);
    }

    static Borc harcama_yap(int one, int two) {
        ArrayList<Harcama> oneHarcama = ilgili_harcamalar(one, two);
        ArrayList<Harcama> twoHarcama = ilgili_harcamalar(two, one);

        float oneToplamHarcama = 0;
        float twoToplamHarcama = 0;

        for (Harcama singleOne : oneHarcama) {
            oneToplamHarcama += singleOne.kisi_basi_ort();
        }
        for (Harcama singleTwo : twoHarcama) {
            twoToplamHarcama += singleTwo.kisi_basi_ort();
        }

        Borc borc;

        if (oneToplamHarcama > twoToplamHarcama) {
            borc = new Borc(one, two, oneToplamHarcama - twoToplamHarcama);
        } else {
            borc = new Borc(two, one, twoToplamHarcama - oneToplamHarcama);
        }

        return borc;
    }

    static ArrayList<Borc> hesapla(){
        ArrayList<Borc> borclistesi = new ArrayList<>();
        for(int a = 0; a < isimler.size() -1 ; a++){
            for(int e = a+1; e < isimler.size() ; e++){
                Borc borc = harcama_yap(a,e);
                borclistesi.add(borc);
            }
            int finalA = a;
            harcamaListesi = new ArrayList<>(harcamaListesi.stream().filter(harcama ->
                harcama.no != finalA
            ).toList());
        }

        return borclistesi;
    }
}

class Harcama {
    int no;
    float harcama_tutari;

    ArrayList<Integer> iliskilendirilmis_kisiler;

    Harcama() {
    }

    float kisi_basi_ort() {
        return harcama_tutari / ((float) iliskilendirilmis_kisiler.size());
    }
}

class Borc {
    int parayiAlan;
    int parayiVeren;
    float tutar;

    public Borc(int parayiAlan, int parayiVeren, float tutar) {
        this.parayiAlan = parayiAlan;
        this.parayiVeren = parayiVeren;
        this.tutar = tutar;
    }

    @Override
    public String toString() {
        return "Borc{" +
                "parayiAlan=" + isimler.get(parayiAlan)+
                ", parayiVeren=" + isimler.get(parayiVeren) +
                ", tutar=" + tutar +
                '}';
    }
}