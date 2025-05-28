/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package dev.kanca.labelbgst;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.PrintServiceExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

/**
 *
 * @author rizki
 */
public class LabelBgst {

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                // Mendekode URL
                String url = URLDecoder.decode(args[0], StandardCharsets.UTF_8.name());
                System.out.println("Received URL: " + url);
                System.out.println("----------------------------------------------------------------");
                System.out.println("  ____  ____ ___  __        __                          _");
                System.out.println(" |  _ \\/ ___|_ _| \\ \\      / /__  _ __   ___  ___  ___ | |__   ___");
                System.out.println(" | |_) \\___ \\| |   \\ \\ /\\ / / _ \\| '_ \\ / _ \\/ __|/ _ \\| '_ \\ / _ \\");
                System.out.println(" |  _ < ___) | |    \\ V  V / (_) | | | | (_) \\__ \\ (_) | |_) | (_) |");
                System.out.println(" |_| \\_\\____/___|    \\_/\\_/ \\___/|_| |_|\\___/|___/\\___/|_.__/ \\___/");
                System.out.println("----------------------------------------------------------------");

                // Menghapus bagian "labelprint://"
                String data = url.substring("labelprint://".length());

                // Memisahkan parameter
                String[] params = data.split("/");

                List<String> namaPasien = new ArrayList<>();
                List<String> tglLahir = new ArrayList<>();
                List<String> rm = new ArrayList<>();
                List<String> ruang = new ArrayList<>();
                
//                System.out.println(params.length);
//                System.out.println(params[0]);

                switch (params[0]) {
                    case "lb":
                        namaPasien = new ArrayList<>(List.of(params[1].split("!")));
                        tglLahir = new ArrayList<>(List.of(params[2].split("!")));
                        rm = new ArrayList<>(List.of(params[3].split("!")));
                        ruang = new ArrayList<>(List.of(params[4].split("!")));
                        break;
                    default:
                        System.out.println("Tidak ada parameter yang sesuai");
                        break;
                }

                if (params.length > 0 && params[0].equals("lb")) {
                    for (int i = 0; i < namaPasien.size(); i++) {
                        String namaPasienPrint = URLDecoder.decode(namaPasien.get(i), StandardCharsets.UTF_8);
                        String tglLahirPrint = URLDecoder.decode(tglLahir.get(i), StandardCharsets.UTF_8);
                        String rmPrint = URLDecoder.decode(rm.get(i), StandardCharsets.UTF_8);
                        String ruangPrint = URLDecoder.decode(ruang.get(i), StandardCharsets.UTF_8);

                        // Lakukan sesuatu dengan parameter
                        System.out.println("Nama: " + namaPasienPrint);
                        System.out.println("Tgl. Lahir: " + tglLahirPrint);
                        System.out.println("RM: " + rmPrint);
                        System.out.println("Ruang: " + ruangPrint);

                        cetakTiket(namaPasienPrint, tglLahirPrint, rmPrint, ruangPrint);
                    }
                } else {
                    System.out.println("Invalid URL format");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No argument received.");
        }
    }

    private static void cetakTiket(String namaPasien, String tglLahir, String rm, String ruang) throws JRException {
        try {
            Helper hlp = new Helper();
            Map<String, String> settings;
            settings = hlp.getSettings("cfg.config");

            String printerName = settings.get("PrinterLabel");
            String reportName = settings.get("ReportLabel");

            List<Map<String, Object>> dataSource = new ArrayList<>();
            Map<String, Object> data = new HashMap<>();

//            data.put("cobadiisidata", "goblok");
            data.put("nama", namaPasien);
            data.put("tglLahir", tglLahir);
            data.put("noRm", rm);
            data.put("ruang", ruang);

            dataSource.add(data);

            JasperPrint jasperPrint = JasperFillManager.fillReport("./rpt/" + reportName, null, new JRBeanCollectionDataSource(dataSource));

            PrintService ps = null;
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            for (PrintService printService : printServices) {
//                    System.out.println(printService.getName());
                if (printService.getName().equals(printerName)) {
                    System.out.println("Printer: " + printService.getName());
                    ps = printService;

                    // Do something with the desired printer (e.g., use it for printing)
                    // Konfigurasi printer
                    PrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
                    ((SimplePrintServiceExporterConfiguration) configuration).setPrintService(ps);
                    ((SimplePrintServiceExporterConfiguration) configuration).setPrintRequestAttributeSet(new HashPrintRequestAttributeSet(new Copies(1)));

                    // Mencetak laporan
                    JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setConfiguration(configuration);
                    System.out.println("Mencetak ...");
                    exporter.exportReport();
                    System.out.println("Selesai");
                    break; // Exit loop once the desired printer is found
                }
            }

//                JasperExportManager.exportReportToPdfFile(jasperPrint, "./rpt/bgst.pdf");
//            System.out.println("Laporan berhasil dicetak!");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
