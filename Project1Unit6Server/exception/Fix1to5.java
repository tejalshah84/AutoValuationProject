/**
 * @author Tejal Shah
 *
 */
package exception;

import java.util.Scanner;
import exception.AutoException.AutomobileError;

public class Fix1to5 {
	
	private Scanner input;

	public String fix(int errno){
		
		AutomobileError err = AutomobileError.findErrorByValue(errno);
		input = new Scanner(System.in);
		
		System.out.println(err.getErrMsg());
		String fixResult = input.nextLine();
		
		do {
		
			if (((errno == 1) || (errno == 5)) && fixResult.matches("\\s*[+-]?[0-9]+[.]?[0-9]{0,2}\\s*")){
				return fixResult.trim();
			}
			
			if (((errno == 2) || (errno == 3)) && fixResult.matches("([a-zA-Z[/]\\s]+)")){
				return fixResult.trim();
			}
			
			if (errno == 4 && fixResult.matches("([\\s*a-zA-Z[-]]+[a-zA-Z0-9[-]\\s]+)")){
				return fixResult.trim();
			}
			
			System.out.println("Incorrect Entry, please reenter value:");
			fixResult = input.nextLine();
			
			
		} while (true);
	}

}
