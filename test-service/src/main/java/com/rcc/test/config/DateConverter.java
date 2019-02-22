package com.rcc.test.config;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import org.springframework.core.convert.converter.Converter;
 
 
/**
 * 自定义 日期转换器
 *
 * @author: rencc
 * @date: 2019年2月22日
 */
public class DateConverter implements Converter<String, Date>  {
 
	/**
	 * 可格式化 的日期 字串
	 */
	private static final List<String> formarts = new ArrayList();
 
	static{
		formarts.add("yyyy-MM");
		formarts.add("yyyy-MM-dd");
		formarts.add("yyyy-MM-dd hh:mm");
		formarts.add("yyyy-MM-dd hh:mm:ss");
	}
 
	@Override
	public Date convert(String source) {
		String value = source.trim();
		if ("".equals(value)) {
			return null;
		}
		if(source.matches("^\\d{4}-\\d{1,2}$")){ 
			return parseDate(source, formarts.get(0));
		}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
			return parseDate(source, formarts.get(1));
		}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
			return parseDate(source, formarts.get(2));
		}else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
			return parseDate(source, formarts.get(3));
		}else {
			throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
		}
	}
 
	/**
	 * 功能描述：格式化日期
	 *
	 * @param dateStr String 字符型日期
	 * @param format  String 格式
	 * @return Date 日期
	 */
	public  Date parseDate(String dateStr, String format) {
		Date date=null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			date = (Date) dateFormat.parse(dateStr);
		} catch (Exception e) {
		}
		return date;
	}
 
	public static void main(String[] args) {
		System.err.println(new DateConverter().convert("2014-04"));
	}
}
