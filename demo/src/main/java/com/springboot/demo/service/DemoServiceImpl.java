package com.springboot.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.demo.constants.DemoCommon;
import com.springboot.demo.constants.DemoHttp;
import com.springboot.demo.dao.DemoDAO;
import com.springboot.demo.entity.History;
import com.springboot.demo.exception.DemoNotFoundException;
import com.springboot.demo.map.GithubUsers;
import com.springboot.demo.dto.NewDocumentDTO;
import com.springboot.demo.dto.OldDocumentDTO;
import com.springboot.demo.response.DocumentResponse;
import com.springboot.demo.response.HistoryResponse;
import com.springboot.demo.util.HttpUtil;
import com.springboot.demo.util.PdfUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DemoServiceImpl  implements  DemoService{
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
    private DemoDAO demoDAO;

    @Autowired
    public DemoServiceImpl(DemoDAO demoDAO) {
        this.demoDAO = demoDAO;
    }

    @Override
    public List<HistoryResponse> findHistories() {
        List<HistoryResponse> lsHistoryResponse = new ArrayList<>();

        List<History> lsHistory = demoDAO.findAll();
        for (History history : lsHistory) {
            HistoryResponse historyResponse = new HistoryResponse();
            historyResponse.setAuthor(history.getDocAuthor());
            historyResponse.setDocument(history.getDocName());
            Instant iTime = history.getCreatedAt().toInstant();
            String sTime = DateTimeFormatter.ofPattern(DemoCommon.CREATES_AT_PATTERN_FORMAT).withZone(ZoneId.of(DemoCommon.ZONE_ID))
                    .format(iTime);

            historyResponse.setCreated_at(sTime);

            lsHistoryResponse.add(historyResponse);
        }

        return lsHistoryResponse;
    }

    @Transactional
    @Override
    public DocumentResponse saveNewPDF(NewDocumentDTO newDocumentDTO) throws JsonProcessingException, SQLException {
        DocumentResponse documentResponse = new DocumentResponse();
        String fileDir = null;
        String author = null;
        StringBuilder sbUrl = new StringBuilder(DemoHttp.BASE_URL + "?");

        sbUrl.append(DemoHttp.QS_QUERY);
        if (newDocumentDTO.getQuery() == null) {
            throw new DemoNotFoundException("missing query parameter with value as string, but required for Github API");
        }
        if (newDocumentDTO.getQuery() != null) {
            sbUrl.append(newDocumentDTO.getQuery());
        }

        sbUrl.append("&");
        sbUrl.append(DemoHttp.QS_SORT);
        if (newDocumentDTO.getSort() == null) {
            sbUrl.append(DemoHttp.DEFAULT_SORT);
        }
        if (newDocumentDTO.getSort() != null) {
            String[] validArr = new String[] {DemoHttp.OPTIONAL_SORT_FOLLOWERS, DemoHttp.OPTIONAL_SORT_REPOSITORIES, DemoHttp.OPTIONAL_SORT_JOINED};
            List<String> listValid = Arrays.asList(validArr);
            if (!listValid.contains(newDocumentDTO.getSort())) {
                throw new DemoNotFoundException("optional sort parameter with value as string only available for followers, repositories, joined");
            }
            sbUrl.append(newDocumentDTO.getSort());
        }

        sbUrl.append("&");
        sbUrl.append(DemoHttp.QS_ORDER);
        if (newDocumentDTO.getOrder() == null ) {
            sbUrl.append(DemoHttp.DEFAULT_ORDER);
        }
        if (newDocumentDTO.getOrder() != null ) {
            String[] validArr = new String[] {DemoHttp.OPTIONAL_ORDER_ASC, DemoHttp.OPTIONAL_ORDER_DESC};
            List<String> listValid = Arrays.asList(validArr);
            if (!listValid.contains(newDocumentDTO.getOrder())) {
                throw new DemoNotFoundException("optional order parameter with value as string only available for asc, desc joined");
            }

            sbUrl.append(newDocumentDTO.getOrder());
        }

        sbUrl.append("&");
        sbUrl.append(DemoHttp.QS_PER_PAGE);
        if (newDocumentDTO.getPer_page() == 0) {
            sbUrl.append(DemoHttp.DEFAULT_PER_PAGE);
        }
        if (newDocumentDTO.getPer_page() > 0) {
            if (newDocumentDTO.getPer_page() <= 100) {
                sbUrl.append(newDocumentDTO.getPer_page());
            }
            if (newDocumentDTO.getPer_page() > 100) {
                sbUrl.append(DemoHttp.DEFAULT_PER_PAGE);
            }
        }

        sbUrl.append("&");
        sbUrl.append(DemoHttp.QS_PAGE);
        if (newDocumentDTO.getPage() == 0) {
            sbUrl.append(DemoHttp.DEFAULT_PAGE);
        }
        if (newDocumentDTO.getPage() > 0) {
            sbUrl.append(newDocumentDTO.getPage());
        }

        if (newDocumentDTO.getDir() == null) {
            fileDir = System.getProperty(DemoCommon.DEFAULT_USER_DIR);

        }
        if (newDocumentDTO.getDir() != null) {
            fileDir = newDocumentDTO.getDir();

        }
        fileDir += (File.separator +DemoCommon.DEFAULT_DIR);
        File directory = new File(fileDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Instant now = Instant.now();
        long tempDocTime = now.getEpochSecond();
        String tempResTime = DateTimeFormatter.ofPattern(DemoCommon.CREATES_AT_PATTERN_FORMAT).withZone(ZoneId.of(DemoCommon.ZONE_ID))
                .format(now);
        if (newDocumentDTO.getAuthor() == null) {
            author = DemoCommon.DEFAULT_AUTHOR;
        }
        if (newDocumentDTO.getAuthor() != null) {
            author = newDocumentDTO.getAuthor().toUpperCase();
        }

        String tempDocName = tempDocTime + "_"  + author  + "_" + newDocumentDTO.getQuery() + DemoCommon.FILE_EXTENSION;
        String filePath = fileDir + File.separator + tempDocName;
        logger.info("request API to URL : {}",sbUrl);
        StringBuilder response = HttpUtil.get(sbUrl);
        if (response == null) {
            throw new DemoNotFoundException("There is something wrong with the server during request to Github API.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        GithubUsers githubUsers = objectMapper.readValue(response.toString(), GithubUsers.class);

        documentResponse.setAuthor(author);
        documentResponse.setTimestamp(tempResTime);
        documentResponse.setDocument(tempDocName);

        if (githubUsers.getItems().size() == 0) {
            documentResponse.setStatus(DemoCommon.ERROR);
        }

        if (githubUsers.getItems().size() > 0) {
            logger.warn("Number of extracted github users : {}",githubUsers.getItems().size());

            logger.warn("Create PDF at {}", filePath);
            boolean successPdf = PdfUtil.writePdf(filePath, githubUsers);

            logger.warn("Successfully PDF created.");

            if (successPdf) {


                byte[] byteArray = PdfUtil.readPdf(filePath);
                logger.warn("Successfully PDF converted to byte array");

                Blob blob = PdfUtil.convertToBlob(byteArray);
                History history = new History(
                        author,
                        tempDocName,
                        blob
                );
                boolean hasSuccess = demoDAO.save(history);

                if (hasSuccess){
                    documentResponse.setBlob(byteArray);
                    documentResponse.setStatus(DemoCommon.SUCCESS);
                }
                if (!hasSuccess){
                    documentResponse.setStatus(DemoCommon.ERROR);
                }
            } else {
                documentResponse.setStatus(DemoCommon.ERROR);
            }
        }

        return documentResponse;
    }

    @Override
    public List<DocumentResponse> returnOldPDF(OldDocumentDTO oldDocumentDTO) throws SQLException, IOException {
        String fileDir = null;

        List<DocumentResponse> documentResponseList = new ArrayList<>();
        List<History> histories =  demoDAO.findAll();

        for (History history : histories) {

            DocumentResponse documentResponse = new DocumentResponse();
            documentResponse.setAuthor(history.getDocAuthor());
            documentResponse.setDocument(history.getDocName());
            Instant tempCreatedAt =  history.getCreatedAt().toInstant();
            String tempResTime = DateTimeFormatter.ofPattern(DemoCommon.CREATES_AT_PATTERN_FORMAT).withZone(ZoneId.of(DemoCommon.ZONE_ID))
                    .format(tempCreatedAt);
            documentResponse.setTimestamp(tempResTime);

            if (history.getDocBin() == null) {
                documentResponse.setStatus(DemoCommon.ERROR);
                documentResponseList.add(documentResponse);
                continue;
            }
            if (oldDocumentDTO.getDir() == null) {
                fileDir = System.getProperty(DemoCommon.DEFAULT_USER_DIR);

            }
            if (oldDocumentDTO.getDir() != null) {
                fileDir = oldDocumentDTO.getDir();

            }
            fileDir += (File.separator + DemoCommon.DEFAULT_DIR);
            File directory = new File(fileDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = fileDir + File.separator+ history.getDocName();

            byte[] tempByteArray = PdfUtil.convertBlobToPDF(history.getDocBin(), filePath );

            if (tempByteArray != null && tempByteArray.length > 0) {

                byte[] byteArray = PdfUtil.readPdf(filePath);
                documentResponse.setStatus(DemoCommon.SUCCESS);
                documentResponse.setBlob(byteArray);
            } else {
                documentResponse.setStatus(DemoCommon.ERROR);
            }

            documentResponseList.add(documentResponse);
        }

        return documentResponseList;
    }
}
