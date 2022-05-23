package scraper;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SteamAchievementScraper {
	String url;
	String saveDirectory;
	String title;
	String filename;
	
	public SteamAchievementScraper() {
		this.url = "";
		this.title = "";
		this.filename = "Checklist.xlsx";
		this.saveDirectory = System.getProperty("user.home") + "/Desktop/";
		
	}
	
	public String getURL() {
		return this.url;
	}
	
	public void setURL(String s) {
		this.url = s;
	}
	
	public String getSaveDirectory() {
		return this.saveDirectory;
	}
	
	public void setSaveDirectory(String s) {
		this.saveDirectory = s;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String s) {
		this.title = s;
	}
	
	public void setFilename(String s) {
		this.filename = s;
	}
	
	@SuppressWarnings("unchecked")
	public void getGameTitle() {
		WebClient client = new WebClient(BrowserVersion.FIREFOX_38);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		String title = "";
		
		HtmlPage page;
		try {
			page = client.getPage(this.url);
			ArrayList<HtmlElement> gameTitle = (ArrayList<HtmlElement>) page.getByXPath("//h1");
			title = gameTitle.get(0).getTextContent();
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.title = title.substring(0, title.lastIndexOf(' '));
		this.title = this.title.replaceAll("[\\\\/:*?\"<>|]", "");
		this.filename = this.title + " Checklist.xlsx";
		client.close();
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getAchievementTitles(){
		WebClient client = new WebClient(BrowserVersion.FIREFOX_38);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		
		ArrayList<String> titles = new ArrayList<String>();
		
		try {
			//TrueSteamAchievements URL
			HtmlPage page = client.getPage(this.url);
			
			//Get achievement titles HTML
			ArrayList<HtmlElement> htmlTitles = (ArrayList<HtmlElement>) page.getByXPath("//ul/li/a[@class='title']");
			
			//Convert achievement title HTML to String
			for (HtmlElement element:htmlTitles) {
				titles.add(element.getTextContent());
			}
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.close();
		return titles;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getAchievementConditions(){
		WebClient client = new WebClient(BrowserVersion.FIREFOX_38);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		
		ArrayList<String> subTexts = new ArrayList<String>();
		
		try {
			//TrueSteamAchievements URL
			HtmlPage page = client.getPage(this.url);
			
			//Get achievement condition HTML
			ArrayList<HtmlElement> htmlSubtext = (ArrayList<HtmlElement>) page.getByXPath("//ul/li/p");
			
			//Convert achievement condition HTML to String
			for (HtmlElement element:htmlSubtext) {
				subTexts.add(element.getTextContent());
			}
			
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.close();
		return subTexts;
	}
	
	public ArrayList<Checklist> compileData(ArrayList<String> titles, ArrayList<String> subtexts){
		//Iterate over both ArrayList<String> to create Checklist objects and add them to an ArrayList<Checklist>
		ArrayList<Checklist> data = new ArrayList<Checklist>();
		for (int i=0;i<titles.size();i++) {
			Checklist c = new Checklist(titles.get(i), subtexts.get(i));
			data.add(c);
		}
		return data;
	}
	
	public void createSheet(ArrayList<Checklist> data) {
		//Create excel spreadsheet with formatting
		try {
			Workbook workbook = new XSSFWorkbook();
			
			//Create sheet name
			Sheet sh = workbook.createSheet("Checklist");
			
			//Create column names
			String[] columnHeadings = {"Achievement", "Check", "Condition"};
			
			//Create header properties
			Header header = sh.getHeader();
			header.setCenter(HSSFHeader.font("Calibri(Body)", "Bold") + HSSFHeader.fontSize((short) 18)  + (this.title + " Achievement Checklist"));
			
			//Header font format
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short)8);
			headerFont.setColor(IndexedColors.BLACK.index);
			
			//Header cell format
			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFont(headerFont);
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
			headerStyle.setBorderBottom(BorderStyle.MEDIUM);
			headerStyle.setBorderLeft(BorderStyle.MEDIUM);
			headerStyle.setBorderRight(BorderStyle.MEDIUM);
			headerStyle.setBorderTop(BorderStyle.MEDIUM);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			
			Row headerRow = sh.createRow(0);
			
			for (int i = 0; i < columnHeadings.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnHeadings[i]);
				cell.setCellStyle(headerStyle);
			
			}
			
			//Data font format
			Font contentFont = workbook.createFont();
			contentFont.setFontHeightInPoints((short)8);
			contentFont.setColor(IndexedColors.BLACK.index);
			
			//Left-most column style format
			CellStyle leftcontentStyle = workbook.createCellStyle();
			leftcontentStyle.setFont(contentFont);
			leftcontentStyle.setBorderBottom(BorderStyle.THIN);
			leftcontentStyle.setBorderLeft(BorderStyle.MEDIUM);
			leftcontentStyle.setBorderRight(BorderStyle.THIN);
			
			//Middle column style format
			CellStyle middlecontentStyle = workbook.createCellStyle();
			middlecontentStyle.setFont(contentFont);
			middlecontentStyle.setBorderBottom(BorderStyle.THIN);
			middlecontentStyle.setBorderLeft(BorderStyle.THIN);
			middlecontentStyle.setBorderRight(BorderStyle.THIN);
			
			//Right-most column style format
			CellStyle rightcontentStyle = workbook.createCellStyle();
			rightcontentStyle.setFont(contentFont);
			rightcontentStyle.setBorderBottom(BorderStyle.THIN);
			rightcontentStyle.setBorderLeft(BorderStyle.THIN);
			rightcontentStyle.setBorderRight(BorderStyle.MEDIUM);
			
			//Style format for the last left-most cell
			CellStyle leftLastStyle = workbook.createCellStyle();
			leftLastStyle.setFont(contentFont);
			leftLastStyle.setBorderBottom(BorderStyle.MEDIUM);
			leftLastStyle.setBorderLeft(BorderStyle.MEDIUM);
			leftLastStyle.setBorderRight(BorderStyle.THIN);
			
			//Style format for the last middle cell
			CellStyle middleLastStyle = workbook.createCellStyle();
			middleLastStyle.setFont(contentFont);
			middleLastStyle.setBorderLeft(BorderStyle.THIN);
			middleLastStyle.setBorderRight(BorderStyle.THIN);
			middleLastStyle.setBorderBottom(BorderStyle.MEDIUM);
			
			//Style format for the right-most cell
			CellStyle rightLastStyle = workbook.createCellStyle();
			rightLastStyle.setFont(contentFont);
			rightLastStyle.setBorderLeft(BorderStyle.THIN);
			rightLastStyle.setBorderBottom(BorderStyle.MEDIUM);
			rightLastStyle.setBorderRight(BorderStyle.MEDIUM);
			
			//Entering data and formatting for rows
			int rownum = 1;
			for (int i = 0; i < data.size() - 1; i++) {
				Row row = sh.createRow(rownum++);
				Cell a = row.createCell(0);
				a.setCellStyle(leftcontentStyle);
				a.setCellValue(data.get(i).getAchievement());
				Cell check = row.createCell(1);
				check.setCellStyle(middlecontentStyle);
				Cell c = row.createCell(2);
				c.setCellStyle(rightcontentStyle);
				c.setCellValue(data.get(i).getCondition());
			}
			
			//Entering data and formatting for the last row of cells
			Row row = sh.createRow(rownum);
			Cell last = row.createCell(0);
			last.setCellStyle(leftLastStyle);
			last.setCellValue(data.get(data.size()-1).getAchievement());
			last = row.createCell(1);
			last.setCellStyle(middleLastStyle);
			last = row.createCell(2);
			last.setCellStyle(rightLastStyle);
			last.setCellValue(data.get(data.size()-1).getCondition());
			
			//Resizing data columns to size and setting size for check column
			sh.autoSizeColumn(0);
			sh.setColumnWidth(1, 6 * 256);
			sh.autoSizeColumn(2);
			
			//Saving excel spreadsheet at given directory
			FileOutputStream fileOut = new FileOutputStream(this.saveDirectory);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
