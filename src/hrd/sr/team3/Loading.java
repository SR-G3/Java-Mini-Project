package hrd.sr.team3;

public class Loading{
	Thread load=new Thread(new Runnable() {
		public void run() {
			String loading = "................................................";
			System.out.print("Loading");
			
			
			for (int i = 0; i < loading.length(); i++) {
		        System.out.print(loading.charAt(i));
		        try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		    }
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
		}
	});
	
}
