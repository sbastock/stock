package com.stocksba.stock.apicontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stocksba.stock.client.CompanyClient;
import com.stocksba.stock.mapper.StockMapper;
import com.stocksba.stock.model.CompanyPrice;
import com.stocksba.stock.model.CompareResult;
import com.stocksba.stock.model.SerieDate;
import com.stocksba.stock.rspmodel.RspPriceModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/stock")
@Api(description = "Stock SBA Stock Interface")
public class StockApi {
	
	@Autowired
	private StockMapper stockmapper;
	
	@Autowired
	private CompanyClient companyclient;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	@ApiOperation(value = "SBA Compare Price List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "No Authroization"), @ApiResponse(code = 403, message = "No Permission"),
			@ApiResponse(code = 404, message = "No Mentors Found"),
			@ApiResponse(code = 500, message = "Internal Error") })
	public ResponseEntity<RspPriceModel> ComparePrice(
			@ApiParam(name = "companycode1", required = true) @RequestParam String companycode1,
			@ApiParam(name = "companycode2", required = true) @RequestParam String companycode2,
			@ApiParam(name = "fromDate", required = true) @RequestParam String fromDate,
			@ApiParam(name = "toDate", required = true) @RequestParam String toDate	
			) {

		try {
			CompareResult compareresult = new CompareResult();
			ArrayList<String> companycodes = new ArrayList<String>();
			companycodes.add(companycode1);
			companycodes.add(companycode2);
//			ArrayList<SeriesData> seriesdata = new ArrayList<SeriesData>();
			ArrayList<SerieDate> seriesdata = new ArrayList<SerieDate>();
			for (int i=0; i < companycodes.size(); i++){
				SerieDate sd = new SerieDate();
				List<CompanyPrice> prices = stockmapper.getCompanyPrice(companycodes.get(i), fromDate, toDate);
//				sd.setCompanycode(companycodes.get(i));
				ResponseEntity<Object> company = companyclient.getCompany(companycodes.get(i));
				JsonObject companyresult = getResult(company);
				sd.setName(companyresult.get("data").getAsString());
				
				if (prices.size() > 0 ) {
					ArrayList<Double> seriesprice = new ArrayList<Double>();
					ArrayList<String> seriesdate = new ArrayList<String>();
					for (int n= 0; n < prices.size(); n++) {
						seriesprice.add(prices.get(n).getPrice());
						seriesdate.add(prices.get(n).getPricedate());
					}
					sd.setData(seriesprice);
					sd.setType("line");
					compareresult.setS_date(seriesdate);
				}
				seriesdata.add(sd);
			}
			
			

			if (seriesdata.size() > 0) {					
				compareresult.setS_data(seriesdata);
				RspPriceModel rsp = new RspPriceModel();
				rsp.setCode(200);
				rsp.setMessage("Found Compare Price");
				rsp.setData(compareresult);
				return new ResponseEntity<RspPriceModel>(rsp, HttpStatus.OK);

			} else {
				RspPriceModel rsp = new RspPriceModel();
				rsp.setCode(404);
				rsp.setMessage("No Found Companys");
				return new ResponseEntity<RspPriceModel>(rsp, HttpStatus.OK);
			}

		} catch (Exception ex) {
			RspPriceModel rsp = new RspPriceModel();
			rsp.setCode(500);
			rsp.setMessage(ex.getMessage());
			return new ResponseEntity<RspPriceModel>(rsp, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	public JsonObject getResult(ResponseEntity<Object> result) {
		Gson gson = new Gson();
		String jsonResultStr = gson.toJson(result.getBody());
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(jsonResultStr);

		return object;

	}

}
