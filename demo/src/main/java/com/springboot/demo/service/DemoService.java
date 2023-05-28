package com.springboot.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.demo.dto.NewDocumentDTO;
import com.springboot.demo.dto.OldDocumentDTO;
import com.springboot.demo.response.DocumentResponse;
import com.springboot.demo.response.HistoryResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface DemoService {
    public List<HistoryResponse> findHistories();
    public DocumentResponse saveNewPDF(NewDocumentDTO newDocumentDTO) throws JsonProcessingException, SQLException;
    public List<DocumentResponse> returnOldPDF(OldDocumentDTO oldDocumentDTO) throws SQLException, IOException;

}
