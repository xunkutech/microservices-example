package com.xunkutech.tuturial.springcloud.service.book;

import java.util.Arrays;
import java.util.List;

public class BookService {	
	private static List<String> bookList = Arrays.asList("Book1","Book2","Book3");

	public static List<String> getBookList() {
		return bookList;
	}

}
