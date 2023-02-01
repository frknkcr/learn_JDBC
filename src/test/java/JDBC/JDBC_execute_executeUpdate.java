package JDBC;

import java.sql.*;

public class JDBC_execute_executeUpdate {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        /*
    A) CREATE TABLE, DROP TABLE, ALTER TABLE gibi DDL ifadeleri icin sonuc kümesi (ResultSet)
       dondurmeyen metotlar kullanilmalidir. Bunun icin JDBC'de 2 alternatif bulunmaktadir.
        1) execute() metodu
        2) executeUpdate() metodu.
    B) - execute() metodu her tur SQL ifadesiyle kullanibilen genel bir komuttur.
       - execute(), Boolean bir deger dondurur. DDL islemlerinde false dondururken,
         DML islemlerinde true deger dondurur.
       - Ozellikle, hangi tip SQL ifadesinine hangi method'un uygun oldugunun bilinemedigi, belli olmadigi
         durumlarda tercih edilmektedir.
    C) - executeUpdate() metodu ise INSERT, Update gibi DML islemlerinde yaygin kullanilir.
       - bu islemlerde islemden etkilenen satir sayisini dondurur.
       - Ayrıca, DDL islemlerinde de kullanilabilir ve bu islemlerde 0 dondurur.
       */

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?serverTimezone=UTC", "root", "1234");
        Statement st = con.createStatement();

        /*=======================================================================
       	 ORNEK1 : Ogrenciler tablosunu siliniz.
      	========================================================================*/

        String dropQuery = "DROP TABLE isciler";
        // System.out.println(st.execute(dropQuery));

        try {
            st.execute(dropQuery);
            System.out.println("Silindi");
        } catch (SQLException e) {
            System.out.println("HATA");
        }

        /*=======================================================================
       	 ORNEK1 : isciler adinda bir tablo olusturunuz id int, birin varchar(10), maas int
      	========================================================================*/

        String createTable = "CREATE TABLE isciler" +
                             "(id INT, " +
                             "birim VARCHAR(10), " +
                             "maas INT)";

        try {
            st.execute(createTable);
            System.out.println("Tablo olusturuldu");
        }catch (SQLSyntaxErrorException e){
            System.out.println("Tablo olusturulamadı");
        }

        /*=======================================================================
		  ORNEK3: isciler tablosuna yeni bir kayit (80, 'ARGE', 4000)
		  ekleyelim.
		========================================================================*/

        String insertData = "insert into isciler values (80, 'ARGE', 4000)";
        System.out.println("Eklenen kayit : "+st.executeUpdate(insertData));

        /*=======================================================================
          ORNEK4: isciler tablosuna birden fazla yeni kayıt ekleyelim.
            INSERT INTO isciler VALUES(70, 'HR', 5000)
            INSERT INTO isciler VALUES(60, 'LAB', 3000)
            INSERT INTO isciler VALUES(50, 'ARGE', 4000)
         ========================================================================*/

        /*
        String insertData1 = "insert into isciler values (70, 'HR', 5000),(60, 'LAB', 3000),(50, 'ARGE', 4000)";
        System.out.println("Eklenen kayit : "+st.executeUpdate(insertData1));
         */
        System.out.println("=============== 1. Yontem ==============");

        String [] queries = {"insert into isciler values(70, 'HR', 5000)",
                             "insert into isciler values (60, 'LAB', 3000)",
                             "insert into isciler values (50, 'ARGE', 4000)"};
        int count = 0;
        for (String each:queries) {
            count += st.executeUpdate(each);
        }
        System.out.println(count+" Satir eklendi");


        // Ayri ayri sorgular ile veritabanina tekrar tekrar ulasmak islemlerin
        // yavas yapilmasina yol acar. 10000 tane veri kaydi yapildigi dusunuldugunde
        // bu kotu bir yaklasimdir.

        System.out.println("=============== 2. Yontem ==============");
        // 2.YONTEM (addBatch ve executeBatch() metotlari ile)
        // ----------------------------------------------------
        // addBatch metodu ile SQL ifadeleri gruplandirilabilir ve executeBatch()
        // metodu ile veritabanina bir kere gonderilebilir.
        // executeBatch() metodu bir int [] dizi dondurur. Bu dizi her bir ifade sonucunda
        // etkilenen satir sayisini gosterir.

        String [] queries1 = {"insert into isciler values(10, 'TEKNIK', 3000)",
                "insert into isciler values (20, 'KANTIN', 2000)",
                "insert into isciler values (30, 'ARGE', 5000)"};

        for (String each:queries1) {
            st.addBatch(each);
        }
        st.executeBatch();
        System.out.println("Satirlar eklendi");



        /*=======================================================================
          ORNEK5: isciler tablosunu görüntüleyin.
         ========================================================================*/

        String selectQuery = "select * from isciler";
        ResultSet iscilerTablo = st.executeQuery(selectQuery);
        System.out.println("=========ISCILER TABLO==========");
        while (iscilerTablo.next()){
            System.out.println(iscilerTablo.getInt(1)+" "+
                               iscilerTablo.getString(2)+" "+
                               iscilerTablo.getInt(3));
        }

        /*=======================================================================
        ORNEK6: isciler tablosundaki maasi 5000'den az olan iscilerin maasina
        %10 zam yapiniz
        ========================================================================*/

        String updateMaas = "update isciler set maas = maas*1.1 where maas< 5000";
        System.out.println("\n"+st.executeUpdate(updateMaas)+" satir guncellendi");

        /*=======================================================================
	      ORNEK7: isciler tablosunun son halini goruntuleyin.
	     ========================================================================*/

        ResultSet iscilerTablo1 = st.executeQuery(selectQuery);
        System.out.println("\n=========ISCILER ZAMLI TABLO==========");
        while (iscilerTablo1.next()){
            System.out.println(iscilerTablo1.getInt(1)+" "+
                    iscilerTablo1.getString(2)+" "+
                    iscilerTablo1.getInt(3));
        }

        /*=======================================================================
	      ORNEK8: Birimi arge olan iscileri tablodan silelim.
	     ========================================================================*/

        String deleteArge = "delete from isciler where birim='Arge'";
        System.out.println(st.executeUpdate(deleteArge)+" satir silindi");

        /*=======================================================================
	      ORNEK9: isciler tablosunun son halini goruntuleyin.
	     ========================================================================*/

        ResultSet iscilerTablo2 = st.executeQuery(selectQuery);
        System.out.println("\n=========ISCILER GUNCEL TABLO==========");
        while (iscilerTablo2.next()){
            System.out.println(iscilerTablo2.getInt(1)+" "+
                    iscilerTablo2.getString(2)+" "+
                    iscilerTablo2.getInt(3));
        }

        con.close();
        st.close();
        iscilerTablo.close();
        iscilerTablo1.close();
        iscilerTablo2.close();

    }

}
