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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class Stock {
	//Declaration part
	int currentID;
	static Scanner sc = new Scanner(System.in);
	public static ArrayList<Product> table = null;
	static //public ArrayList<Product> table = null;
	int page;
	static int row;
	static int setRow;
	static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	static Date date = new Date();
	static String fdate = dateFormat.format(date).toString();
	private static final String validLetter = "[*wWrRdDuUfFlLnNpPsSgGEe]|(se|Se)|(sa|Sa)", validNumber = "-?\\d+(\\.\\d+)?";
	static String error_al = "You have entered the invalid command."; 
	static CharSequence Search;
	
	
	// method for performing insertion task
	public void insertProductToDB(int data) {
		ArrayList<Product> list = new ArrayList<>();

		// write data to file
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream("file/db.g3")))) {
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	// returning total of page
	public static int totalPages(int totalRows, int row) {
		double page;
		if(totalRows%row == 0){
		page = Math.ceil(totalRows / row);
		}else{
		page = Math.ceil(totalRows / row) + 1;
		}
		
		return (int) page;
	}

	
	
	//Selecting record to show based on starting page
	private static int selectRecordByStartPage(int startPage, int row) {
		return (startPage - 1) * row;
	}

	public static void chooseOption() {
		System.out.print("Option > ");
		shortCutCommands();
	}
	
	//shortCutCommands using in this System
	public static void shortCutCommands() {
			@SuppressWarnings("resource")
			//Scanner scan = new Scanner(System.in);
			String str = sc.next();
			if (ifletterX(str)) {
				char x = 0;
				if (str.length() > 1) {
					if (str.equals("Se")||str.equals("se")){
					System.out.print("Set showing record: ");
					setShowingRecord();
					}else if(str.equals("Sa")||str.equals("sa")){
						
						saveConfirmation();
					}
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
					case 's': case 'S': search((String) Search); break;
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
	public static void inputM(String Option) {
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
	public static String getText() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in).useDelimiter(";");
		String readString = "";
		readString = scanner.next();
	    scanner.close();
	    return readString;
	}
	
	//method check for number when input for menu
			public static boolean ifnumber(String str) {
				boolean p = Pattern.compile(validNumber).matcher(str).matches();
				return p;
			}
			
	//method check for letter when input for menu
	public static boolean ifletterX(String str) {
		boolean p = Pattern.compile(validLetter).matcher(str).matches();
		return p;
	}
	
	
	static int id;
	static String name;
	static double unitPrice;
	static int sQty;
	//method switch to method responder
		public void indexer(){
			System.out.println("INDEXER !");
		}
		
		// For Displaying Product
		public static void displayingProduct(int page, int row) {
			int col = 5;
			if(setRow>row)
				row = setRow;
			int startPage = selectRecordByStartPage(page, row);
			int pages = totalPages(table.size(), row);
			
			Table t = new Table(5,BorderStyle.UNICODE_BOX_DOUBLE_BORDER,ShownBorders.ALL);
			CellStyle numbers = new CellStyle(HorizontalAlign.center);
			
		// Header of Table
			t.setColumnWidth(0, 15, 30);
		    t.setColumnWidth(1, 15, 30);
		    t.setColumnWidth(2, 15, 30);
		    t.setColumnWidth(3, 15, 30);
		    t.setColumnWidth(4, 15, 30);
		    t.addCell("ID", numbers);
		    t.addCell("name", numbers);
		    t.addCell("Unit Price", numbers);
		    t.addCell("Quantity", numbers);
		    t.addCell("Imported Date",numbers);
		    
		 // Body of Table
		 			for (int i = startPage; i < startPage + row; i++) {
		 				if (i < table.size()) {
		 					id = table.get(i).getId();
		 					name = table.get(i).getName();
		 					unitPrice = table.get(i).getUnitPrice();
		 					sQty = table.get(i).getsQty();
		 					String date = table.get(i).getiDate();

		 			    	t.addCell(""+id, numbers);
		 			    	t.addCell(""+name, numbers);
		 			    	t.addCell(""+unitPrice, numbers);
		 			    	t.addCell(""+sQty, numbers);
		 			    	t.addCell(""+date, numbers);
		 				}
		 			}
		 		System.out.println(t.render());
		 		
		 // Footer of Table
				System.out.println("\n\t\t\tPage: " + page + "/" + pages + " =||= " + "Total: " + table.size() + "\t\t\n");
			tableMeu();
			
			
		}
		
		
		//Table Menu
				public static void tableMeu(){
					Table t = new Table(1,BorderStyle.UNICODE_BOX_DOUBLE_BORDER,  
							ShownBorders.SURROUND);
					CellStyle row = new CellStyle(HorizontalAlign.center);
					t.setColumnWidth(0, 79, 120);
					t.addCell("*)Display | W)rite | R)ead | U)pdate | D)elete | F)irst | P)revious | "
							+ "L)ast",row);
					t.addCell(" ");
					t.addCell("S)earch | G)o to | Se)t row | Sa)ve | B)ack up | Re)store | H)elp |"
							+ " E)xit",row);		
					System.out.println(t.render());
					chooseOption();
				}
				//end menu
		
		

		//method write
		public static void write() {
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
				System.out.println(error_al);
				System.out.print("Are you sure to save this new record? [Y/N] > ");
				check = sc.next();
				break;
			}
			
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			displayingProduct(page, row);
			
		}
		
		//Product Deltail
		public static void showProductDetail(int id){
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
		public static void read() {
			reverseTable();
			System.out.print("Product ID > ");
			int ProID = sc.nextInt();
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			if(ProID > table.size()){
				System.out.println("Could not be found the product that is matched to your input Product ID!");
			}else{
				System.out.println("==========Detail information of Product with ID: " + ProID + "==========");
				showProductDetail(ProID);
			}
			Collections.reverse(table);
			tableMeu();
			
			
			
		}
		
		//method update
		public static void update() {
			reverseTable();
			System.out.print("Product ID > ");
			int ProID = sc.nextInt();
			int index = ProID - 1;
			
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			if(ProID > table.size()){
				System.out.println("Could not be found the product that is matched to your input Product ID!");
				update();
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
					System.out.println(error_al);
					System.out.print("What do you want to update?\n (Al)All\t(N)Name\t(Up)Unit Price\t(Q)Stock Quantity\t(E)Exit > ");
					choose = sc.next();
					break;
				}
			}

		}
		
		//Update All data with the selected Product ID
		public static void updateAll(int id){
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
			case "y" : case "Y" : table.set(index, new Product(id, name, unitPrice, sQty, fdate)); Collections.reverse(table); 
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println(error_al);
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}
			displayingProduct(page, row);
		}
		
		//Update Name of Product with the selected ProID
		public static void updateName(int id){
			int index = id - 1;
			System.out.print("Name > ");
			name = sc.next();
			System.out.print("Are you sure to update this record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : table.set(index, new Product(id, name, unitPrice, sQty, fdate)); Collections.reverse(table); 
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println(error_al);
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}
			displayingProduct(page, row);
		}
		
		//Update unit price of product with the selected ProID
		public static void updateUnitPrice(int id){
			int index = id - 1;
			System.out.print("Unit Price > ");
			unitPrice = sc.nextDouble();
			System.out.print("Are you sure to update this record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : table.set(index, new Product(id, name, unitPrice, sQty, fdate)); Collections.reverse(table); 
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println(error_al);
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}
			displayingProduct(page, row);
		}
		
		//Update stock qty of product with the selected ProID
		public static void updateStockQty(int id){
			int index = id - 1;
			System.out.print("Stock Quantity > ");
			sQty = sc.nextInt();
			System.out.print("Are you sure to update this record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : table.set(index, new Product(id, name, unitPrice, sQty, fdate)); Collections.reverse(table); 
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;
			case "n" : case "N" : Collections.reverse(table); break;
			default:
				System.out.println(error_al);
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}
			displayingProduct(page, row);
		}
		
		//Delete one data from database with its ID
		public static void delete() {
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
				case "y" : case "Y" : table.remove(index); Collections.reverse(table); 
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); break;
				case "n" : case "N" : Collections.reverse(table); break;
				default:
					System.out.println(error_al);
					System.out.print("Are you sure to update this record? [Y/N] > ");
					check = sc.next();
					break;
				}
			}
			displayingProduct(page, row);
		}
		
		
		//Pagination and search not yet done
		//method toFirst
		public static void tofirst() {
			paginationToFirst();
		}
		
		//method toLast
		public static void tolast() {
			paginationToLast();
		}
		
		//method moveNext
		public static void tonext() {
			System.out.println("TO NEXT !");
		}
		
		//method movePrevious
		public static void toprevious() {
			System.out.println("TO PREVIOUS !");
		}
		
		//method search
		//search all name in table
		public static int[] searchArr(String search) {
			
			ArrayList<Integer> found = new ArrayList<>();
			for (int i = 0; i < table.size(); i++) {
				if (table.get(i).getName().equals(search.trim())) found.add(i);
			}
			int []foundIndex = found.stream().mapToInt(i -> i).toArray();
			return foundIndex;
		} 
		public static void search(String search) {
			Scanner sc = new Scanner(System.in);
			
			System.out.print("Enter Product Name > ");
			Search = sc.nextLine();
			search = Search.toString();
			int inter = 0;
			
			Iterator itr = table.iterator();
			Product P= (Product) itr.next();
			ArrayList aSearch = new ArrayList<>();
			
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			if ((inter = table.size()) > 0) {
				
				int col = 5;
				int startPage = selectRecordByStartPage(page, row);
				int pages = totalPages(inter, row);
				
				
				
				Table t = new Table(5,BorderStyle.UNICODE_BOX_DOUBLE_BORDER,ShownBorders.ALL);
				CellStyle numbers = new CellStyle(HorizontalAlign.center);
				
			// Header of Table
				t.setColumnWidth(0, 15, 30);
			    t.setColumnWidth(1, 15, 30);
			    t.setColumnWidth(2, 15, 30);
			    t.setColumnWidth(3, 15, 30);
			    t.setColumnWidth(4, 15, 30);
			    t.addCell("ID", numbers);
			    t.addCell("name", numbers);
			    t.addCell("Unit Price", numbers);
			    t.addCell("Quantity", numbers);
			    t.addCell("Imported Date",numbers);
			    
			 // Body of Table
			 			for (int i = 0; i < inter; i++) {
			 				if(((Product)table.get(i)).getName().contains(search) || ((Product)table.get(i)).getName().equalsIgnoreCase((String)search))
							{
								aSearch.add(table.get(i));
							}
			 			}
			 				Iterator ii = aSearch.iterator();
							while (ii.hasNext()) {
								//if (i < table.size()) {
									Product PP =(Product)ii.next();
									id = PP.getId();
				 					name = PP.getName();
				 					unitPrice = PP.getUnitPrice();
				 					sQty = PP.getsQty();
				 					
									
				 			    	t.addCell(""+id, numbers);
				 			    	t.addCell(""+name, numbers);
				 			    	t.addCell(""+unitPrice, numbers);
				 			    	t.addCell(""+sQty, numbers);
				 			    	t.addCell(""+dateFormat.format(date).toString(), numbers);
								//}
							}
//			 				if (i < table.size()) {
//			 					id = table.get((searchArr(search)[i])).getId();
//			 					name = table.get((searchArr(search)[i])).getName();
//			 					unitPrice = table.get((searchArr(search)[i])).getUnitPrice();
//			 					sQty = table.get((searchArr(search)[i])).getsQty();
//			 					String date = table.get((searchArr(search)[i])).getiDate();
//
//			 			    	t.addCell(""+id, numbers);
//			 			    	t.addCell(""+name, numbers);
//			 			    	t.addCell(""+unitPrice, numbers);
//			 			    	t.addCell(""+sQty, numbers);
//			 			    	t.addCell(""+date, numbers);
//			 				}
			 			//}
			 		System.out.println(t.render());
			 		
			 // Footer of Table
				System.out.println("\n\t\t\tPage: " + page + "/" + pages + " =||= " + "Total: " + aSearch.size() + "\t\t\n");
				tableMeu();
			}else{
				System.out.println(Search);
				System.out.println("Not found !");
				search((String) Search);
			}
		}
		
		//Going to Specific page
		public static void goTo(){
			int goPage = sc.nextInt();
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			displayingProduct(goPage, row);
			
		}
		//End description of Pagination and Search 
		
		//Saving data to Database
		public static void save(){
			try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new  FileOutputStream("file/db.tmp")))){
				reverseTable();
				oos.writeObject(table);
				Collections.reverse(table);
				tableMeu();
			} catch (FileNotFoundException e) {
				System.err.println("File Not Found!");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Save confirmation
		public static void saveConfirmation(){
			System.out.print("Are you sure to update this record? [Y/N] > ");
			String check = sc.next();
			switch (check) {
			case "y" : case "Y" : System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
								  System.out.println("Your data had been successfully saved to database."); save(); break;
			case "n" : case "N" : System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"); tableMeu(); break;
			default:
				System.out.println(error_al);
				System.out.print("Are you sure to update this record? [Y/N] > ");
				check = sc.next();
				break;
			}	
		}
		 
		
		
		//Set Showing record
		public static void setShowingRecord(){
			setRow = sc.nextInt();
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			displayingProduct(page, setRow);
			
		}
		
		//Reversing table 
		public static void reverseTable(){
			if(table.get(0).getId() != 1)
				
				Collections.reverse(table);
		}
		
		//Reversing Page 
				public static void paginationToFirst(){
					if(table.get(0).getId() == 1){
						Collections.reverse(table);
					}
					System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					displayingProduct(page, row);
				}
				public static void paginationToLast(){
					int last = totalPages(table.size(), row);
					System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					displayingProduct(last, row);
				}
		
		
		
		

}
