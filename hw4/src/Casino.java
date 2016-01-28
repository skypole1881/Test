package foop;
import java.util.Random;
import java.util.Scanner;
import java.io.*;


public class Casino {
	public static void main (String[] argv) {
		System.out.println("**** Welcome to POOCasino ****");
		System.out.println("**** Let's play BlackJack ****");
		int nRound = Integer.valueOf(argv[0]);

		int nChips = Integer.valueOf(argv[1]);
		BlackJack game = new BlackJack(nRound,nChips,argv[2],argv[3],argv[4],argv[5]);
		game.run();
	}
}
