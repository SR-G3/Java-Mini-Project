package hrd.sr.team3;

public class Run {
	public static void main(String[] args) {

		Stock wr = new Stock();
		Loading l = new Loading();
		l.load.start();
		try {
			l.load.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//wr.insertProductToDB((int) 1E5);
		wr.selectProductFromDB(1, 5);
	}

}
