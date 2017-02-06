import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.io.*;
import java.text.ParseException;

class ThreadRunner extends Exception implements Runnable{
  private Thread t;
  private String server;
  private long now;

  public ThreadRunner(String s, long str){
    server = s;
    now = str;
    System.out.println("Creating "+server);
  }

  public void run(){
    BufferedWriter bw = null;
    try{
      bw = new BufferedWriter(new FileWriter("out"+server+".txt"));
    }catch(Exception e){
      System.out.println(e);
    }
    int time = 0;
    int ser = 0;
    String serStr = "192.168."+server+".";
    while(time < 1440){
      ser = 0;
      while(ser < 125){
        try{
          bw.write(String.valueOf(now+time*60)+", "+serStr+String.valueOf(ser)+", 0, "+ String.valueOf(new Random().nextInt(100) + 1)+"\n");
          bw.write(String.valueOf(now+time*60)+", "+serStr+String.valueOf(ser)+", 1, "+ String.valueOf(new Random().nextInt(100) + 1)+"\n");
          ser++;
          bw.flush();
        }catch(Exception e){
          System.out.println(e);
        }

      }
      time++;
    }

    System.out.println("Done "+server);
  }

  public void start(){
    System.out.println("Starting " +  server );
      if (t == null) {
         t = new Thread (this, server);
         t.start();
      }
  }

}

public class Generate{
  public static void main(String[] args){
    System.out.println("Enter start date(yyyy-MM-dd HH:mm): ");
    Scanner s = new Scanner(System.in);
    long start = 0L;
    try{
      start = stringToEpoch(s.nextLine());
    }catch(Exception e){
      e.printStackTrace();
    }

    for(int i=0;i<8;i++)
      new ThreadRunner(String.valueOf(i), start).start();

    System.out.println("IP addresses generated:");
    System.out.println("192.168.[0-7].[0-124]");
  }

  public static long stringToEpoch(String start) throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date date = null;
    try{
      date = df.parse(start);
    }catch(Exception e){
      e.printStackTrace();
    }
		long epoch = (date.getTime()) / 1000;

		return epoch;
	}

}
