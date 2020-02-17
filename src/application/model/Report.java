package application.model;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import javax.swing.*;

import application.database.DBConnection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

public class Report extends JFrame {
    public void showReport(String reportName, LocalDate startDate, LocalDate endDate){
        try {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("startDate", Date.valueOf(startDate));
            parameters.put("endDate", Date.valueOf(endDate));
            Connection conn = DBConnection.getConnection();
            System.out.println(reportName);
            System.out.println(getClass().getResourceAsStream("../view/"+reportName+".jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("../view/"+reportName+".jrxml"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(1920, 1080);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void eBarimt(){
        try {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("order_id", Order.getBillId());
            Connection conn = DBConnection.getConnection();
            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("../view/ebarimt.jrxml"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(600, 850);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}