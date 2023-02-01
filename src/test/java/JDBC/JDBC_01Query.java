package JDBC;

import java.sql.*;

public class JDBC_01Query {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // 1- Ilgili driver'i yuklemeliyiz. MySQL kullandigimizi bildiriyoruz.
        // Driver'i bulamama ihtimaline karsi forName metodu icin ClassNotFoundException
        // method signature'imiza axception olarak firlatmamizi istiyor.

        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2- Baglantiyi olusturmak icin username ve password girmeliyiz
        // Burada da username ve password'un yanlis olma ihtimaline karsi
        // SQLException firlatmamizi istiyor

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?serverTimezone=UTC", "root", "1234");

        // 3 - SQL query'leri icin bir Statement objesi olusturup , javada SQL
        // sorgularimiz icin bir alan acacagiz.

        Statement st = con.createStatement();

        // 4- SQL query'lerimizi yapip calistirabiliriz

        ResultSet veri = st.executeQuery("SELECT * FROM personel");

        // 5- Sonuclari görmek icin Iteration ile Set icerisindeki elemanlari
        // while dongusi ile yazdiriyoruz.

        while (veri.next()) {
            System.out.println(veri.getInt(1)+" "+veri.getString(2)+" "+veri.getString(3)+
                    " "+veri.getInt(4)+" "+veri.getString(5));
        }

        // 6- Olusturulan baglantilari kapatiyoruz

        con.close();
        st.close();
        veri.close();

    }
}
