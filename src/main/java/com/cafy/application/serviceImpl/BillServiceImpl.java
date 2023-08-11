package com.cafy.application.serviceImpl;

import com.cafy.application.constents.CofeConstants;
import com.cafy.application.dao.BillDao;
import com.cafy.application.entity.Bill;
import com.cafy.application.jwt.JwtFilter;
import com.cafy.application.service.BillService;
import com.cafy.application.utils.CofeUtils;
import com.google.gson.JsonArray;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

     @Autowired
     JwtFilter jwtFilter;

     @Autowired
     BillDao billDao;
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generate report");
        try {
            String fileName;
            if (validateRequestMap(requestMap)){
                if (requestMap.containsKey("isGenerate") && !(Boolean)requestMap.get("isGenerate")){
                    fileName = (String) requestMap.get("uuid");
                }else {
                    fileName = CofeUtils.getUUID();
                    requestMap.put("uuid",fileName);
                    insertBill(requestMap);
                }
                String data = "Name :"+ requestMap.get("name") + "\n"+"Contact Number:"+ requestMap.get("contactNumber") +
                        "\n"+"Email :"+ requestMap.get("email") + "\n"+"Payment Method:"+ requestMap.get("paymentMethod");
                Document document = new Document();
                PdfWriter.getInstance(document,new FileOutputStream(CofeConstants.STORE_LOCATION+"/"+fileName+".pdf"));
                document.open();
                setRectangleInPdf(document);

                Paragraph paragraph = new Paragraph("Management System ",getFont("Header"));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                Paragraph paragraph2 = new Paragraph(data+"\n \n",getFont("Data"));
                document.add(paragraph2);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = CofeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
              //  JSONArray jsonArray = new JSONArray((String) requestMap.get("productDetails"));

                for(int i = 0 ; i < jsonArray.length();i++){
                    addRows(table,CofeUtils.getMapFromJson(String.valueOf(jsonArray.getJSONObject(i))));
                 //   JSONObject explrObject = jsonArray.getJSONObject(i);
               }
                document.add(table);
                Paragraph footer = new Paragraph("Total :"+ requestMap.get("totalAmount")+ "\n"
                        + "Thank you For Visiting. Please Visit Again ",getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\""+ fileName+ "\"}",HttpStatus.OK);
            }else {
                return CofeUtils.getResponseEntity("Required Data Not Found",HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

     private void addRows(PdfPTable table, Map<String, Object> data) {

        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name","Category","Quantity","Price","Sub Total").forEach(columnTitle ->{
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.YELLOW);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            header.setBorderColor(BaseColor.BLUE);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type){
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_BOLD,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside Rectangle");
        Rectangle rectangle = new Rectangle(577,825,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);

    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal((Integer) requestMap.get("totalAmount"));
            bill.setProductDetail((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("paymentMethod")
                && requestMap.containsKey("productDetails") && requestMap.containsKey("totalAmount");
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> bills = new ArrayList<>();
        if (jwtFilter.isAdmin()){
            bills = billDao.getAllBills();

        }else {
            bills = billDao.getBillByUserName(jwtFilter.getCurrentUser());

        }
        return new ResponseEntity<>(bills,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("requestMap {}",requestMap);
        try {
            byte[] byteArray = new byte[0];
            if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap))
                return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
            String  filePath = CofeConstants.STORE_LOCATION+"/"+requestMap.get("uuid")+".pdf";
            if (CofeUtils.isFileExist(filePath)){
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }else {
                requestMap.put("isGenerate",false);
                generateReport(requestMap);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional optional = billDao.findById(id);
            if (!optional.isEmpty()){
                billDao.deleteById(id);
                return CofeUtils.getResponseEntity("Bill Deleted Successfully",HttpStatus.OK);
            }else {
                return CofeUtils.getResponseEntity("Bill id does not exist",HttpStatus.OK);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream inputStream = new FileInputStream(initialFile);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return bytes;
    }
}
