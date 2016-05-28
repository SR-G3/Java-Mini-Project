package hrd.sr.team3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Stock {
	//Declaration part
	int currentID;
	Scanner sc = new Scanner(System.in);
	public static ArrayList<Product> table = null;
	//public ArrayList<Product> table = null;
	int page, row;
	int setRow;
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	Date date = new Date();
	String fdate = dateFormat.format(date).toString();
	private static final String validLetter = "[*wWrRdDuUfFlLnNpPsSgGEe]|(se|Se)", validNumber = "-?\\d+(\\.\\d+)?";
	static String error_al = "You have type a wrong command!"; 
	
	
	// method for performing insertion task
	public void insertProductToDB(int data) {
		ArrayList<Product> list = new ArrayList<>();

		// write data to file
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream("file/db.g3")))) {
			// Product pro = new Product("Coca-Cola", 2.0, 5, fdate);
			long start = System.currentTimeMillis();
			for (int i = 0; i < data; i++) {
				currentID = i + 1;
				// Product pro = new Product(i+1, "Coca-Cola", 2.0, 5,
				list.add(new Product(currentID, "Coca-Cola", 2.0, 5, fdate));
			}
			oos.writeObject(list);
			long stop = System.currentTimeMillis();
			System.out.println("Finish Inserting product to database: " + (stop - start) / 1E3);

		} catch (FileNotFoundException e) {
			System.err.println("File Not Found!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	// method for performing selection task
	public void selectProductFromDB(int page, int row) {
		this.page = page;
		this.row = row;
		// read data from file
		try (ObjectInputStream ois = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream("file/db.g3")))) {
			long start = System.currentTimeMillis();
			// ArrayList<Product> table = null;
			table = (ArrayList<Product>) ois.readObject();
			long stop = System.currentTimeMillis();
			System.out.println("Finish Selecting data from database: " + (stop - start) / 1E3);
			Collections.reverse(table);	
			displayingProduct(page, row);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// returning total of page
	public int totalPages(int totalRows, int row) {
		double page;
		if(totalRows%row == 0){
		page = Math.ceil(totalRows / row);
		}else{
		page = Math.ceil(totalRows / row) + 1;
		}
		
		return (int) page;
	}

	
	
	//Selecting record to show based on starting page
	private int selectRecordByStartPage(int startPage, int row) {
		return (startPage - 1) * row;
	}

	public void chooseOption() {
		System.out.print("Option > ");
	}
	
	//shortCutCommands using in this System
	public void shortCutCommands() {
			@SuppressWarnings("resource")
			//Scanner scan = new Scanner(System.in);
			String str = sc.next();
			if (ifletterX(str)) {
				char x = 0;
				if (str.length() > 1) {
					if (str.equals("Se")||str.equals("se"))
					System.out.print("Set showing record: ");
					setShowingRecord();
				}else {
					x = str.charAt(0);
					switch (x) {
					case '*': displayingProduct(page, row); break;
					case 'w': case 'W': write(); break;
					case 'r': case 'R': read(); break;
					case 'd': case 'D': delete(); break;
					case 'u': case 'U': update(); break;
					case 'f': case 'F': tofirst(); break;
					case 'l': case 'L': tolast(); break;
					case 'n': case 'N': tonext(); break;
					case 'p': case 'P': toprevious(); break;
					case 's': case 'S': search(); break;
					case 'g': case 'G': System.out.print("Go to specific page: "); goTo(); break;
					case 'e': case 'E': System.out.println("GoodBye");System.exit(0); break;
					}
				}
			}else {
				System.out.println(error_al);
				chooseOption();inputM("M");
		}
	}
	
	
	//method for control input value M,m = get from menu, T,t = get as text input 
	public void inputM(String Option) {
		boolean p = Pattern.compile("[mMtT]").matcher(Option).matches();		
		if (p) {
			if (Option.equals("M")||Option.equals("m")) {
				shortCutCommands();
			}else if (Option.equals("T")||Option.equals("t")) {
				System.out.println(getText());
			}
		}
		else {
			System.out.println("\n** Help: you can use only M,m or T,t");
			inputM("T");
		}
	}
	
	//Get all text return string
	public String getText() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in).useDelimiter(";");
		String readString = "";
		readString = scanner.next();
	    scanner.close();
	    return readString;
	}
	
	//method check for number when input for menu
			public boolean ifnumber(String str) {
				boolean p = Pattern.compile(validNumber).matcher(str).matches();
				return p;
			}
			
	//method check for letter when input for menu
	public boolean ifletterX(String str) {
		boolean p = Pattern.compile(validLetter).matcher(str).matches();
		return p;
	}
	
	
	int id;
	String name;
	double unitPrice;
	int sQty;
	//method switch to method responder
		public void indexer(){
			System.out.println("INDEXER !");
		}
		
		// For Displaying Product
		public void displayingProduct(int page, int row) {
			//table = new ArrayList<>();
			//table.addAll(table);
			
			if(setRow>row)
				row = setRow;
			int startPage = selectRecordByStartPage(page, row);
			int pages = totalPages(table.size(), row);
			// Header of Table
			System.out.format(
					" +---------------+----------------------+-----------------------+-----------------------+-----------------------+%n");
			System.out.format("||\tID\t||\tNAME\t\t||\tUNIT PRICE($)\t||\tSTOCK QUANTITY\t||\tIMPORTED DATE\t||%n");
			System.out.format(
					" +---------------+----------------------+-----------------------+-----------------------+-----------------------+%n");

			// Body of Table
			for (int i = startPage; i < startPage + row; i++) {
				if (i < table.size()) {
					id = table.get(i).getId();
					name = table.get(i).getName();
					unitPrice = table.get(i).getUnitPrice();
					sQty = table.get(i).getsQty();
					String date = table.get(i).getiDate();
					System.out.format("|| " + id + "\t||\t" + name + "\t||\t" + unitPrice + "\t\t||\t" + sQty + "\t\t||\t"
							+ date + "\t||%n");
					System.out.format(
							" +---------------+----------------------+-----------------------+-----------------------+-----------------------+%n");
				}
			}
			// Footer of Table
			System.out.println("\n\t\t\t\t\tPage: " + page + "/" + pages + " =||= " + "Total: " + table.size() + "\t\t");
			chooseOption();
			shortCutCommands();
		}
		
		

		//method write
		public void write() {
			reverseTable();
			id = currentID + 1;
			System.out.println("Product ID: " + id);
			
			System.out.print("Name > ");
			name = sc.next();
			
			System.out.print("Unit Price > ");
			unitPrice = sc.nextDouble();
			
			System.out.print("Stock Quantity > ");
			sQty = sc.nextInt();
			
			System.out.println("Imported Date > " + fdate);
			table.add(new Product(id, name, unitPrice, sQty, fdate));
			Collections.reverse(table);
			
			
			displayingProduct(page, row);
			
		}
		
		//method read
		public void read() {
			System.out.println("READER !");
		}
		
		//method update
		public void update() {
			System.out.println("UPDATER !");
		}
		
		//method delete
		public void delete() {
			System.out.println("DELETER !");
		}
		
		//method toFirst
		public void tofirst() {
			System.out.println("TO FIRST !");
		}
		
		//method toLast
		public void tolast() {
			System.out.println("TO LAST !");
		}
		
		//method moveNext
		public void tonext() {
			System.out.println("TO NEXT !");
		}
		
		//method movePrevious
		public void toprevious() {
			System.out.println("TO PREVIOUS !");
		}
		
		//method search
		public void search() {
			System.out.println("SEARCH !");
		}
		
		//Going to Specific page
		public void goTo(){
			int goPage = sc.nextInt();
			displayingProduct(goPage, row);
		}
		
		//Set Showing record
		public void setShowingRecord(){
			setRow = sc.nextInt();
			displayingProduct(page, setRow);
		}
		
		//Reversing table 
		public void reverseTable(){
			if(table.get(0).getId() != 1)
				Collections.reverse(table);
		}
		

}
