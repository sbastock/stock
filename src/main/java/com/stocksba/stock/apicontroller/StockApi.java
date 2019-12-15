package com.stocksba.stock.apicontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stocksba.stock.mapper.StockMapper;
import com.stocksba.stock.model.ComparePrice;
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

			List<ComparePrice> prices = stockmapper.getComparePrice(companycode1,companycode2,fromDate,toDate);

			if (prices.size() > 0) {

				RspPriceModel rsp = new RspPriceModel();
				rsp.setCode(200);
				rsp.setMessage("Found Compare Price");
				rsp.setData(prices);
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

}
