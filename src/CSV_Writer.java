import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CSV_Writer {
	/*
	 * CSV Writer class creates a .csv file and writes information into it
	 */
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private String fileName;
	//CSV file header
	private static final String FILE_HEADER = "Genetic Tic Tac Toe Experiment";

	public CSV_Writer(String fileName){
		this.fileName = System.getProperty("user.dir") + "\\reports\\" + fileName;
	}

	public static void main(String[] args) {
		CSV_Writer writer = new CSV_Writer("test.csv");
		writer.createCsvFile();
		writer.appendCsvFile(new String[]{"Some new line","in seperated columns!"});
	}

	public void createCsvFile() {
		/*
		 * create a .csv file in reports directory
		 * and write initial information
		 */
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			// write the timestamp of the experiment
			fileWriter.append("Experiment Time: " + getCurrentTimeStamp());
			// add a new line seperator after the timestamp
			fileWriter.append(NEW_LINE_SEPARATOR);

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	public void appendCsvFile(String[] newLineArgs) {
		/*
		 * appends a new line to the file
		 */
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName, true);

			// write the new information to the file
			for(String s: newLineArgs){
				fileWriter.append(s);
				fileWriter.append(COMMA_DELIMITER);
			}

			//Add a new line separator after the information
			fileWriter.append(NEW_LINE_SEPARATOR);

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	public static String getCurrentTimeStamp() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
	}
}


