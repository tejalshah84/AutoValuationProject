/**
 * @author Tejal Shah
 *
 */
package exception;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AutoException extends Exception {

	private static final long serialVersionUID = 1L;

	public enum AutomobileError {
		InvalidErrorCode(0, "Invalid Error Code:"),
		AutoBasePriceMissing(1, "AutoBasePriceMissing- please provide base price for Automobile "),
		NoOptionSetsDefined(2, "NoOptionSetsDefined- please provide option category for Automobile "),
		OptionSetNameMissing(3, "OptionSetNameMissing- please provide option category name for options such as "),
		NoOptionsDefined(4, "NoOptionsDefined- please provide option name for OptionSet "),
		OptionPriceMissing(5, "OptionPriceMissing- please provide price for option "),
		InvalidFileHeader(6, "InvalidFileHeader- please provide Automobile name for file ");
		
		private int errNum;
		private String errMsg;
		
		private AutomobileError(final int errNm, String errMg){
		    errNum = errNm;
		    errMsg = errMg; 
		}
		   
		public int getErrNum() {
		    return errNum;
		} 
		
		public String getErrMsg() {
		    return errMsg;
		}
		
		public void setErrMsg(String ctxt) {
		    this.errMsg = errMsg + ctxt + ":";
		}
		
		public static AutomobileError findErrorByValue(int errNm){
			for (AutomobileError a: AutomobileError.values()){
				if(a.getErrNum()==errNm){
					return a;
				}
			}
			return null;
		}
	}
	
	public AutomobileError error;
	public int errorNumber;
	public String errorContext = "";

	public AutoException(){
		super();
		error = AutomobileError.findErrorByValue(0);
		errorNumber = 0;
		logAutoError();
	}

	public AutoException(int errno){
		super();
		if (errno<0 || errno>6) errno = 0;
		error = AutomobileError.findErrorByValue(errno);
		errorNumber = errno;
		logAutoError();
	}
	
	public AutoException(int errno, String ctxt){
		super();
		if (errno<0 || errno>6) errno = 0;
		error = AutomobileError.findErrorByValue(errno);
		errorNumber = errno;
		this.errorContext = (ctxt!=null?ctxt:"");
		error.setErrMsg(this.errorContext);
		logAutoError();
	}

	public String fix(int errno) {
	
		if (0<=errno && errno<=5){
			Fix1to5 fixer = new Fix1to5();
	
			switch(errno){
				case 1: return fixer.fix(1);
				case 2: return fixer.fix(2);
				case 3: return fixer.fix(3);
				case 4: return fixer.fix(4);
				case 5: return fixer.fix(5);
				default: return "Incorrect Errorcode--cannot resolve error";
			}
		}
	
		if (6<=errno){
			Fix6andHigher fixFile = new Fix6andHigher();
			
			switch(errno){
				case 6: return fixFile.fix(6);
				default: return "Incorrect Errorcode--cannot resolve error";
			}
			
		}
		
		return "Incorrect Errorcode--cannot resolve error";
	}
	
	public void logAutoError() {
        
		File f = new File("AutoErrorLog.txt");
		try {
			
			if(!f.exists() || f.isDirectory()){
				f.createNewFile();
			}
			
			FileWriter fw = new FileWriter(f.getAbsoluteFile(), true);
			BufferedWriter buff = new BufferedWriter(fw);
			buff.write(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			buff.newLine();
			buff.write(error.getErrNum() + ": " + error.getErrMsg());
			buff.newLine();
			buff.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
