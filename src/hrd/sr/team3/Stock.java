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
			System.out.println("==========Inserting new Product to Database==========");
			reverseTable();
			int ProID = table.size() + 1;
			System.out.println("New Product ID: " + ProID);
			
			System.out.print("Name > ");
			name = sc.next();
			
			System.out.print("Unit Price > ");
			unitPrice = sc.nextDouble();
			
			System.out.print("Stock Quantity > ");
			sQty = sc.nextInt();
			
			System.out.println("Imported Date > " + fdate);
			System.out.print("Are you sure to save this new record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : table.add(new Product(ProID, name, unitPrice, sQty, fdate)); Collections.reverse(table); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println("You have entered the invalid command.");
				System.out.print("Are you sure to save this new record? [Y/N] > ");
				check = sc.next();
				break;
			}
			
			
			displayingProduct(page, row);
			
		}
		
		//Product Deltail
		public void showProductDetail(int id){
			int index = id - 1;
			name = table.get(index).getName();
			unitPrice = table.get(index).getUnitPrice();
			sQty = table.get(index).getsQty();
			String date = table.get(index).getiDate();
			System.out.println("Last Updated: " + date);
			System.out.println("Product ID: " + id);
			System.out.println("Product Name: " + name);
			System.out.println("Unit Price: $" + unitPrice);
			System.out.println("Stocked Quantity: " + sQty);
		}
		
		//method read
		public void read() {
			reverseTable();
			System.out.print("Product ID > ");
			int ProID = sc.nextInt();
			if(ProID > table.size()){
				System.out.println("Could not be found the product that is matched to your input Product ID!");
			}else{
				System.out.println("==========Detail information of Product with ID: " + ProID + "==========");
				showProductDetail(ProID);
			}
			Collections.reverse(table);
			displayingProduct(page, row);
			
		}
		
		//method update
		public void update() {
			reverseTable();
			System.out.print("Product ID > ");
			int ProID = sc.nextInt();
			int index = ProID - 1;
			if(ProID > table.size()){
				System.out.println("Could not be found the product that is matched to your input Product ID!");
			}else{
			System.out.println("==========Update information of Product with ID: " + ProID + "==========");
			showProductDetail(ProID);
			
			System.out.print("What do you want to update?\n (Al)All\t(N)Name\t(Up)Unit Price\t(Q)Stock Quantity\t(E)Exit > ");
			String choose = sc.next();
				switch (choose) {
				case "Al" : case "al" : updateAll(ProID); break;
				case "N"  : case "n"  : updateName(ProID); break;
				case "Up" : case "up" : updateUnitPrice(ProID); break;
				case "Q"  : case "q"  : updateStockQty(ProID); break;
				case "E"  : case "e"  : Collections.reverse(table);displayingProduct(page, row); break;
				default:
					System.out.println("You have entered the invalid command.");
					System.out.print("What do you want to update?\n (Al)All\t(N)Name\t(Up)Unit Price\t(Q)Stock Quantity\t(E)Exit > ");
					choose = sc.next();
					break;
				}
			}

		}
		public void updateAll(int id){
			int index = id - 1;
			System.out.print("Name > ");
			name = sc.next();
			
			System.out.print("Unit Price > ");
			unitPrice = sc.nextDouble();
			
			System.out.print("Stock Quantity > ");
			sQty = sc.nextInt();
			
			System.out.print("Are you sure to update this record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : table.set(index, new Product(id, name, unitPrice, sQty, fdate)); Collections.reverse(table); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println("You have entered the invalid command.");
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}
			displayingProduct(page, row);
		}
		public void updateName(int id){
			int index = id - 1;
			System.out.print("Name > ");
			name = sc.next();
			System.out.print("Are you sure to update this record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : table.set(index, new Product(id, name, unitPrice, sQty, fdate)); Collections.reverse(table); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println("You have entered the invalid command.");
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}
			displayingProduct(page, row);
		}
		public void updateUnitPrice(int id){
			int index = id - 1;
			System.out.print("Unit Price > ");
			unitPrice = sc.nextDouble();
			System.out.print("Are you sure to update this record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : table.set(index, new Product(id, name, unitPrice, sQty, fdate)); Collections.reverse(table); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println("You have entered the invalid command.");
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}
			displayingProduct(page, row);
		}
		public void updateStockQty(int id){
			int index = id - 1;
			System.out.print("Stock Quantity > ");
			sQty = sc.nextInt();
			System.out.print("Are you sure to update this record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : table.set(index, new Product(id, name, unitPrice, sQty, fdate)); Collections.reverse(table); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println("You have entered the invalid command.");
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}
			displayingProduct(page, row);
		}
		
		//method delete
		public void delete() {
			reverseTable();
			System.out.print("Product ID > ");
			int ProID = sc.nextInt();
			int index = ProID - 1;
			if(ProID > table.size()){
				System.out.println("Could not be found the product that is matched to your input Product ID!");
			}else{
				System.out.println("==========Delete Product from database with ID: " + ProID + "==========");
				showProductDetail(ProID);
				
				System.out.print("Are you sure to delete this record? [Y/N] > ");
				String check = sc.next();
				switch (check) {
				case "y" : case "Y" : table.remove(index); Collections.reverse(table); break;
				case "n" : case "N" : Collections.reverse(table); break;
				default:
					System.out.println("You have entered the invalid command.");
					System.out.print("Are you sure to update this record? [Y/N] > ");
					check = sc.next();
					break;
				}
			}
			displayingProduct(page, row);
		}
		
		
		//Pagination and search not yet done
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
		//End description of Pagination and Search 
		 
		
		
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
