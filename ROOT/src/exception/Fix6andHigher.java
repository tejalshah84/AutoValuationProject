/**
 * @author Tejal Shah
 *
 */
package exception;

import java.util.Scanner;

import exception.AutoException.AutomobileError;

public class Fix6andHigher {
	private Scanner input;

	public String fix(int errno){
		
		AutomobileError err = AutomobileError.findErrorByValue(errno);
		input = new Scanner(System.in);
		
		System.out.println(err.getErrMsg());
		String fixResult = input.nextLine();
		
		do {
		
			if ((errno == 6) && fixResult.matches("([\\s*a-zA-Z[-]]+[a-zA-Z0-9[-]\\s]+)")){
				return fixResult.trim();
			}
			
			System.out.println("Incorrect Entry, please reenter value:");
			fixResult = input.nextLine();
		} while (true);
	}

}
