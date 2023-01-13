package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import id.kawahedukasi.model.dto.UploadItemRequest;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ApplicationScoped
public class ExcelService {

    public Response upload(UploadItemRequest request) throws IOException {
        List<Item> itemList = new ArrayList<>();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.file); //membuka file pake input stream class
        XSSFWorkbook workbook = new XSSFWorkbook(byteArrayInputStream); // mengambil workbook dari file yang diupload
        XSSFSheet sheet = workbook.getSheet("Sheet1"); // mengambil sheet berdasarkan nama dari workbook

        //hapus header
        sheet.removeRow(sheet.getRow(0)); //hapus row pertama
        // sheet.removeRow(sheet.getRow(1));  //hapus row selanjutnya (jika diperlukan)

        //looping menggunakan Interator class
        Iterator<Row> rowIterator = sheet.rowIterator();
        while(rowIterator.hasNext()) {
            Item item = new Item();

            Row row = rowIterator.next();
            String name = row.getCell(0).getStringCellValue(); //mengambil data pada cell excel berdasarkan tipe datanya, yaitu String
            Double price = row.getCell(1).getNumericCellValue(); //mengambil data pada cell excel berdasarkan tipe datanya, yaitu Double
            Long count = Double.valueOf(row.getCell(2).getNumericCellValue()).longValue(); //mengambil data pada cell excel berdasarkan tipe datanya, yaitu Long
            String type = row.getCell(3).getStringCellValue(); //mengambil data pada cell excel berdasarkan tipe datanya, yaitu string
            String description = row.getCell(4).getStringCellValue(); //mengambil data pada cell excel berdasarkan tipe datanya, yaitu string

            // mendeklarasikan value menggunakan setter
            item.setName(name);
            item.setPrice(price);
            item.setCount(count);
            item.setType(type);
            item.setDescription(description);

            itemList.add(item);
        }
        persistListItem(itemList);

        return Response.ok().build();

    }

    public Response download() throws IOException {
        List<Item> itemList = Item.listAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("list-item");

        //set header
        int rowNumber = 0;
        Row row = sheet.createRow(rowNumber++);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("name");
        row.createCell(2).setCellValue("price");
        row.createCell(3).setCellValue("count");
        row.createCell(4).setCellValue("type");
        row.createCell(5).setCellValue("description");
        row.createCell(6).setCellValue("created_at");
        row.createCell(7).setCellValue("updated_at");

        for(Item item : itemList) {
            row = sheet.createRow(rowNumber++);
            row.createCell(0).setCellValue(item.getId());
            row.createCell(1).setCellValue(item.getName());
            row.createCell(2).setCellValue(item.getPrice());
            row.createCell(3).setCellValue(item.getCount());
            row.createCell(4).setCellValue(item.getType());
            row.createCell(5).setCellValue(item.getDescription());
            row.createCell(6).setCellValue(item.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
            row.createCell(7).setCellValue(item.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        return Response.ok()
                .type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Disposition", "attachment; filename=\"peserta_list_excel.xlsx\"")
                .entity(outputStream.toByteArray()).build();
    }

    @Transactional
    @TransactionConfiguration(timeout = 30)
    public void persistListItem(List<Item> itemList) {
        Item.persist(itemList);
    }
}
