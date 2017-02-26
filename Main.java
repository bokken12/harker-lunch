import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class Main {

	public static void main(String[] args) {

		try {
			File file = new File("/Users/patrickzhong/Downloads/LunchMenu.pdf");
			
			String[][] result = parse(file);
			int day = 0;
			for(String[] sA : result){
				System.out.println("----- "+CStripper.days[day]+" -----");
				System.out.println("");
				day++;
				
				for(String s : sA){
					System.out.println(s);
					System.out.println("");
				}
				
				System.out.println("");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String[][] parse(File file) throws IOException {
		PDDocument pdf = PDDocument.load(file);
		PDPage page = pdf.getPage(0);

		CStripper stripper = new CStripper();
		stripper.getText(pdf);

		PDFTextStripperByArea ts = new PDFTextStripperByArea();
		ts.setSortByPosition(true);

		for(int day = 0; day < stripper.days.length; day++){ // Just in case there are more than five school days a week... Hehe xd.
			for(int i = 0; i < stripper.types.length; i++){

				int startY = (int)stripper.startY[i] + 5;
				int endY = i == stripper.types.length-1 ? (int)stripper.getCurrentPage().getMediaBox().getHeight() : (int)stripper.startY[i+1];
	        	
	        	int startX = day == 0 ? 0 : (int)stripper.endX[day-1];
	        	int endX = (int)stripper.endX[day];
	        	
		        Rectangle rect = new Rectangle(startX, startY, endX-startX, endY-startY);
		        ts.addRegion("reg-"+day+"-"+i, rect);
	        }
		}
		
		ts.extractRegions(page);
		
		String[][] result = new String[stripper.days.length][stripper.types.length];
		
		for(int day = 0; day < stripper.days.length; day++)
			for(int i = 0; i < stripper.types.length; i++)
				result[day][i] = ts.getTextForRegion("reg-"+day+"-"+i).replace("\n", "");//.replaceFirst("  ", "\n");
		
		return result;
	}
	
	private static String format(){
		return "";
	}

}
