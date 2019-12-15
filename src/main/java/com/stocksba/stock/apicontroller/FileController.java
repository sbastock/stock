package com.stocksba.stock.apicontroller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stocksba.stock.client.CompanyClient;
import com.stocksba.stock.mapper.StockMapper;
import com.stocksba.stock.model.StockPrice;
import com.stocksba.stock.payload.UploadFileResponse;
import com.stocksba.stock.property.FileStorageProperties;
import com.stocksba.stock.rspmodel.RspModel;
import com.stocksba.stock.service.FileStorageService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v1/file")
@Api(description = "Stock SBA Upload File Interface")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private CompanyClient companyclient;

	@Autowired
	private StockMapper stockmapper;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@PostMapping("/upload")
	public ResponseEntity<RspModel> uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileName)
				.toUriString();

		String companycode = fileName.split("_")[0];
		String exchange = fileName.split("_")[1];
		String fromDate = fileName.split("_")[2];
		String toDate = fileName.split("_")[3].substring(0, 10);

		ResponseEntity<Object> company = companyclient.getCompany(companycode);
		JsonObject companyresult = getResult(company);

		if (company.getStatusCodeValue() == 200) {

			try {
				FileInputStream uploadfile = new FileInputStream(new File(fileStorageProperties.getUploadDir()+fileName));

				// Create Workbook instance holding reference to .xlsx file
				XSSFWorkbook workbook = new XSSFWorkbook(uploadfile);

				// Get first/desired sheet from the workbook
				XSSFSheet sheet = workbook.getSheetAt(0);

				// Iterate through each rows one by one
				Iterator<Row> rowIterator = sheet.iterator();
				// create a ArrayList String type 
		        @SuppressWarnings("rawtypes")
				ArrayList<List> rowdata = new ArrayList<List>(); 
				while (rowIterator.hasNext()) {
					ArrayList<Object> celldata = new ArrayList<Object>(); 
					Row row = rowIterator.next();
					// For each row, iterate through all the columns
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						 switch (cell.getCellType()) { 
		                    case Cell.CELL_TYPE_NUMERIC:
		                    	DataFormatter formatter = new DataFormatter(); 
								celldata.add(String.valueOf(formatter.formatCellValue(cell)));
		                        break;
		                    case Cell.CELL_TYPE_STRING: 
		                    	celldata.add(cell.getStringCellValue()); 
		                        break;
		                    }
					}
					rowdata.add(celldata);
				}
				uploadfile.close();
				StockPrice sp = new StockPrice();
				for (int i =0; i < rowdata.size(); i++) {
					sp.setCompanycode(companycode);
					sp.setStockexchange(exchange);
					sp.setPpr(Double.parseDouble(rowdata.get(i).get(0).toString()));
					sp.setStockdate(rowdata.get(i).get(1).toString());
					sp.setStocktime(rowdata.get(i).get(2).toString());
					stockmapper.addStockPrice(sp);
				}
				RspModel rsp = new RspModel();
				UploadFileResponse uploadrsp = new UploadFileResponse();
				uploadrsp.setCompanyName(companyresult.get("data").getAsString());
				uploadrsp.setStockExchange(exchange);
				uploadrsp.setRecords(rowdata.size());
				uploadrsp.setFromDate(fromDate);
				uploadrsp.setToDate(toDate);
				uploadrsp.setFileName(fileName);
				uploadrsp.setFileDownloadUri(fileDownloadUri);
				uploadrsp.setSize(file.getSize());
				uploadrsp.setFileType(file.getContentType());
				rsp.setCode(200);
				rsp.setMessage("Import Successful");
				rsp.setData(uploadrsp);
				return new ResponseEntity<RspModel>(rsp, HttpStatus.OK);
			} catch (Exception ex) {
				ex.printStackTrace();
				RspModel rsp = new RspModel();
				rsp.setCode(500);
				rsp.setMessage(ex.getMessage());
				return new ResponseEntity<RspModel>(rsp, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			

			// stockmapper.addStockPrice(stockprice);

		} else {
			RspModel rsp = new RspModel();
			rsp.setCode(404);
			rsp.setMessage("Account No Found");
			return new ResponseEntity<RspModel>(rsp, HttpStatus.NOT_FOUND);	
		}
	}

	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	public JsonObject getResult(ResponseEntity<Object> result) {
		Gson gson = new Gson();
		String jsonResultStr = gson.toJson(result.getBody());
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(jsonResultStr);

		return object;

	}
}
