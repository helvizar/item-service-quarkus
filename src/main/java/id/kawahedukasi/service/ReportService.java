package id.kawahedukasi.service;


import id.kawahedukasi.model.Item;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ReportService {

    public Response exportJasper() throws JRException {
        File file = new File("src/main/resources/item-report.jrxml"); //cari file path
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath()); // ngecompile
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(getAllItem()); // masukin data dari database
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameter(), dataSource); //menampilkan data
        byte[] jasperResult = JasperExportManager.exportReportToPdf(jasperPrint); // export data ke pdf

        return Response.ok().type("application/pdf").entity(jasperResult).build();

    }

    public List<Item> getAllItem() {
        List<Item> list = Item.listAll();
        return list;
    }

    public Map<String, Object> jasperParameter() {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("createdBy", "Helvizar");
        return parameter;
    }
}
