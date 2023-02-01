package ogrenciYonetimPlatformu;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?serverTimezone=UTC", "root", "1234");
    Statement st = con.createStatement();

    Scanner scan = new Scanner(System.in);
    static int numara =1000;

    ArrayList<Ogrenci> ogrList = new ArrayList<>();

    public Menu() throws SQLException {
    }

    void islemler() throws SQLException {
        System.out.println(" ============= İŞLEMLER =============\n"+
        "\t\t 1-EKLEME \n" +
        "\t\t 2-ARAMA \n" +
        "\t\t 3-LİSTELEME \n" +
        "\t\t 4-SİLME \n" +
        "\t\t Q-ÇIKIŞ \n" + "\n"+
        "\t SEÇİMİNİZ:");

        try {
            int secim = scan.nextInt();
            switch (secim){
                case 1 : {
                    ekleme();
                    break;
                }
                case 2 : {
                    arama();
                    break;
                }
                case 3 : {
                    listeleme();
                    break;
                }
                case 4 : {
                    silme();
                    break;
                } default:{
                    System.out.println("Hatalı deger girdiniz!!!");
                    islemler();
                }
            }

        }catch (InputMismatchException | ClassNotFoundException | SQLException e){

            String islem = scan.next();
            if (islem.equalsIgnoreCase("q")){
                System.out.println("******SİSTEMDEN ÇIKILIYOR******");
                st.close();
                con.close();

                System.exit(0);
            }else {
                System.out.println("Hatalı giriş yapıldı !!!");
                islemler();
            }
        }
    }

    private void silme() throws ClassNotFoundException, SQLException {

        System.out.println("SİLİNECEK TC NO'YU GİRİNİZ: ");
        String tcNo = scan.next();

        String sorgu = "delete from ogrenciler1 where tc_no = '"+tcNo+"'";

        int count = st.executeUpdate(sorgu);
        if (count!=0) {
            System.out.println("Basari ile silindi");
            listeleme();
            islemler();
        }else {
            System.out.println("Böyle biri yok");
            islemler();
        }
    }

    private void listeleme() throws ClassNotFoundException, SQLException {

        String liste = "select * from ogrenciler1";
        ResultSet set = st.executeQuery(liste);

        while (set.next()){
            System.out.println(set.getString(1)+ " " +
                    set.getString(2)+" "+
                    set.getInt(3)+" "+
                    set.getString(4));
        }
        islemler();
    }

    private void arama() throws ClassNotFoundException, SQLException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Aramak istediğiniz tc no girin: ");
        String giris = scan.nextLine();

        try {
            String sorgu = "select * from ogrenciler1 where tc_no = '"+giris+"'";
            ResultSet set = st.executeQuery(sorgu);
            while (set.next()){
                System.out.println(set.getString(1)+ " " +set.getString(2)+" "+set.getInt(3)+" "+
                        set.getString(4));
            }
            islemler();
        } catch (SQLException e) {
            System.out.println("Aranan tc ye göre kayıt bulunamadı!!!");
            islemler();
        }
    }

    private void ekleme() throws ClassNotFoundException, SQLException {

        scan.nextLine();
        System.out.println("ÖĞRENCİ ADI: ");
        String ad= scan.nextLine();
        System.out.println("ÖĞRENCİNİN SOYADI: ");
        String sAd = scan.nextLine();
        System.out.println("ÖĞRENCİ TC NO: ");
        String tcNo = tcNoKontrol(scan.next());
        System.out.println("ÖĞRENCİNİN YAŞI: ");
        int yas = scan.nextInt();
        System.out.println("ÖĞRENCİ SINIFI: ");
        String sinif = scan.next();
        Ogrenci ogrenci = new Ogrenci(ad,sAd,tcNo,yas,++numara,sinif);
        ogrList.add(ogrenci);

        String Sqlstr = "INSERT INTO ogrenciler1 VALUES ('" + ad + " " + sAd + "', '" + tcNo + "', " +
                yas + ",'"+sinif+"')";

        try {
            st.executeUpdate(Sqlstr);
            System.out.println("Tabloya eklendi");
        } catch (SQLException e) {
            System.out.println("Eklenemedi");
        }

        islemler();


    }

    private String tcNoKontrol(String tcNo) {
        String[] tcNoArr = tcNo.split("");
        boolean kontrol = true;
        for (String each: tcNoArr) {
            if (!Character.isDigit(each.charAt(0))){
                kontrol = false;
            }
        }
        if (!kontrol){
            System.out.println("TC NO HATALI!!!");
            tcNoKontrol(scan.next());
        }
        return tcNo;
    }
}
