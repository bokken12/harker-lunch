import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class CStripper extends PDFTextStripper {
	
	String[] types = {
			"INTERNATIONAL GRILL",
			"VEGGIE CAFÉ",
			"CARVING",
			"FARMERS",
			"CHOWDA HOUSE",
			"REGIONAL ITALIAN",
			"FRESH MEX",
			"HEALTHY CHOICE",
			"CREATIVE DELI SPECIAL"};
	
	static String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
	
	double[] endX = new double[5];
	double[] startY = new double[types.length];
	double[] endY = new double[types.length];
	
	double current = 0;
	String word = "";
	
	int dayIndex = 0;
	boolean foundDay = false;
	
	int typeIndex = 0;
	boolean foundType = false;
	

	public CStripper() throws IOException {
		super();
	}
	
	@Override
	public void processTextPosition(TextPosition text){
		
		if(text.getY() != current){
			current = text.getY();
			
			if(foundDay){
				endX[dayIndex] = this.getCurrentPage().getMediaBox().getWidth();
				foundDay = false;
				dayIndex++;
			}
			
			word = "";
		}
		
		String s = text.toString();
		
		word += s;
		
		if(!s.equals(" ")){
			if(dayIndex < endX.length){
				if(foundDay){
					foundDay = false;
					endX[dayIndex] = text.getX();
					dayIndex++;
				}
				else if(word.contains(days[dayIndex])){
					foundDay = true;
					word = "";
				}
			}
			else if(typeIndex < types.length && word.contains(types[typeIndex]) || (typeIndex == 2 && word.contains("BISTRO"))){
				startY[typeIndex] = text.getY();
				typeIndex++;
				word = "";
			}
		}
	}

}
