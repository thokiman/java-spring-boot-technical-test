package com.springboot.demo.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.springboot.demo.map.GithubUser;
import com.springboot.demo.map.GithubUsers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

public class PdfUtil {
    private static final Logger logger = LoggerFactory.getLogger(PdfUtil.class);
    public static boolean writePdf (String filePath, GithubUsers githubUsers) {
        Boolean hasSuccess = false;
        try {
            // Create a new Document
            Document document = new Document();
            List nestedList = new List(List.ORDERED);

            // Create a new PDFWriter instance to write the document to a file
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Open the document
            document.open();

            document.add(new Paragraph("List of Github User : "));
            for (GithubUser githubUser : githubUsers.getItems()) {

                // Add content to the document
                nestedList.add(new ListItem("User : " + githubUser.getLogin()));
                List sublist = new List(false, false, 40);
                sublist.setListSymbol(new Chunk("", FontFactory.getFont(FontFactory.HELVETICA, 10)));
                sublist.add("Id : " + githubUser.getId());
                sublist.add("Repo Url : " + githubUser.getReposUrl());
                nestedList.add(sublist);

            }
            document.add(nestedList);

            // Close the document
            document.close();
            pdfWriter.close();

            hasSuccess=true;
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            hasSuccess =false;
        }
        return hasSuccess;
    }

    public static byte[] readPdf(String filePath) {
        byte[] pdfBytes = null;

        try {
            // Load the PDF document
            PDDocument document = PDDocument.load(new File(filePath));

            // Convert the PDF document to a byte array
            pdfBytes = toByteArray(document);

            // Close the document
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfBytes;
    }
    private static byte[] toByteArray(PDDocument document) throws IOException {
        // Create a new ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Save the document to the ByteArrayOutputStream
        document.save(outputStream);

        // Convert the ByteArrayOutputStream to a byte array
        byte[] byteArray = outputStream.toByteArray();

        // Close the ByteArrayOutputStream
        outputStream.close();

        return byteArray;
    }

    public static byte[] convertBlobToPDF(Blob blob, String outputPath) throws IOException, SQLException {
        byte[] buffer = new byte[4096];
        try (InputStream inputStream = blob.getBinaryStream();
             FileOutputStream outputStream = new FileOutputStream(outputPath)) {

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();

        }

        return buffer;
    }

    public static Blob convertToBlob(byte[] byteArray) throws SQLException {
        return new SerialBlob(byteArray);
    }



}
