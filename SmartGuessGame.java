import java.util.Random;
import java.util.Scanner;

public class SmartGuessGame {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int numberToGuess = random.nextInt(100) + 1;
        int attemptsLeft = 7;
        int previousGuess = -1;

        System.out.println("Welcome to Guess The Number!");
        System.out.println("I have chosen a number between 1 and 100.");
        System.out.println("You have " + attemptsLeft + " attempts.");
        System.out.println("Type 0 anytime to quit.\n");

        while (attemptsLeft > 0) {
            System.out.print("Enter your guess: ");
            int guess = sc.nextInt();

            if (guess == 0) {
                System.out.println("You quit the game.");
                break;
            }

            if (guess < 1 || guess > 100) {
                System.out.println("Guess must be between 1 and 100.");
                continue;
            }

            attemptsLeft--;

            if (guess == numberToGuess) {
                int score = attemptsLeft * 10 + 30;
                System.out.println("Correct! You guessed the number!");
                System.out.println("Your Score: " + score);
                break;
            }

            int difference = Math.abs(numberToGuess - guess);

            if (difference <= 5) {
                System.out.println("Very Hot!");
            } else if (difference <= 10) {
                System.out.println("Warm");
            } else if (difference <= 20) {
                System.out.println("Cold");
            } else {
                System.out.println("Very Cold");
            }

            if (previousGuess != -1) {
                if (difference < Math.abs(numberToGuess - previousGuess)) {
                    System.out.println("You're getting closer!");
                } else {
                    System.out.println("You're moving away!");
                }
            }

            previousGuess = guess;
            System.out.println("Attempts left: " + attemptsLeft + "\n");
        }

        if (attemptsLeft == 0) {
            System.out.println("Game Over!");
            System.out.println("The number was: " + numberToGuess);
        }

        sc.close();
    }
}
