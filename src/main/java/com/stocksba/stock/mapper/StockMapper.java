package com.stocksba.stock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.stocksba.stock.model.CompanyPrice;
import com.stocksba.stock.model.ComparePrice;
import com.stocksba.stock.model.StockPrice;

@Mapper
public interface StockMapper {
	
	@Insert("insert into sba_stock.stockprice(companycode,stockexchange,ppr,stockdate,stocktime) values(#{companycode},#{stockexchange},#{ppr},#{stockdate},#{stocktime})")
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	void addStockPrice(StockPrice stockprice);
	
	@Select("SELECT companycode,round(avg(ppr),2) as price,  stockdate FROM sba_stock.stockprice where stockdate >= #{fromDate} and stockdate <= #{toDate}  and companycode in (#{companycode1}, #{companycode2}) group by companycode, stockdate")
	List<ComparePrice> getComparePrice(@Param("companycode1") String companycod1,@Param("companycode2") String companycod2,@Param("fromDate") String fromDate,@Param("toDate") String toDate);
	
	@Select("SELECT round(avg(ppr),2) as price,  stockdate as pricedate FROM sba_stock.stockprice where stockdate >= #{fromDate} and stockdate <= #{toDate}  and companycode =#{companycode} group by stockdate")
	List<CompanyPrice> getCompanyPrice(@Param("companycode") String companycode,@Param("fromDate") String fromDate,@Param("toDate") String toDate);

}
