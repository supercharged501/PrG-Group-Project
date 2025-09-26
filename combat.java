import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
public class combat {

    public static void main(String[] args) {

        int p1Health = 100;
        int p2Health = 100;

        boolean game = true;

        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        while (game) {
            try {
                int num1 = sc.nextInt(); // Player 1 input
                int num2 = sc.nextInt(); // Player 2 input

                int p1Damage = 0;
                int p2Damage = 0;

            // Player 1 combat actions
                //Player 1 punch code
                if (num1 == 1) {
                    System.out.println("You landed a hit");

                    int attackPunch = rnd.nextInt(9) + 1;

                    switch (attackPunch) {
                        case 1: for (int a = 1; a <= 15; a++) {
                            p1Damage++;
                        }
                        break;

                        case 2: for (int a = 1; a <= 15; a++) {
                            p1Damage++;
                        }
                        break;

                        case 3: for (int a = 1; a <= 15; a++) {
                            p1Damage++;
                        }
                        break;

                        case 4: for (int a = 1; a <= 15; a++) {
                            p1Damage++;
                        }
                        break;

                        case 5: for (int a = 1; a <= 15; a++) {
                            p1Damage++;
                        }
                        break;

                        case 6: for (int a = 1; a <= 15; a++) {
                            p1Damage++;
                        }
                        break;

                        case 7: for (int a = 1; a <= 28; a++) {
                            p1Damage++;
                        }
                        System.out.println("Player 1 landed a critical hit");
                        break;

                        case 8: for (int a = 1; a <= 30; a++) {
                            p1Damage++;
                        }
                        System.out.println("Player 1 landed a critical hit");
                        break;

                        case 9: for (int a = 1; a <= 45; a++) {
                            p1Damage++;
                        }
                        System.out.println("Player 1 landed a Black flash!!");
                        break;

                        default:
                            break;
                    }
                // Player 1 kick code
                } else if (num1 == 2) {
                    System.out.println("You kicked your enemy");

                    int attackKick = rnd.nextInt(5) + 1;

                    switch (attackKick) {
                        case 1: for (int a = 1; a <= 15; a++) {
                            p1Damage++;
                        }
                        break;

                        case 2: for (int a = 1; a <= 15; a++) {
                            p1Damage++;
                        }
                        break;

                        case 3: for (int a = 1; a <= 28; a++) {
                            p1Damage++;
                        }
                        break;

                        case 4: for (int a = 1; a <= 30; a++) {
                            p1Damage++; 
                        }
                        break;

                        case 5: for (int a = 1; a <= 35; a++) { 
                            p1Damage++;
                        }
                        break;

                        default:
                            break;
                    }

                //Player 1 block code
                } else if (num1 == 3) {
                    System.out.println("You blocked");
                }


            // Player 2 combat actions
                //Player 2 punch code
                if (num2 == 8) {
                    System.out.println("You landed a hit");

                    int attackPunch = rnd.nextInt(9) + 1;

                    switch (attackPunch) {
                        case 1: for (int a = 1; a <= 15; a++) {
                            p2Damage++;
                        }
                        break;

                        case 2: for (int a = 1; a <= 15; a++) {
                            p2Damage++;
                        }
                        break;

                        case 3: for (int a = 1; a <= 15; a++) { 
                            p2Damage++;
                        }
                        break;

                        case 4: for (int a = 1; a <= 15; a++) {
                            p2Damage++;
                        }
                        break;

                        case 5: for (int a = 1; a <= 15; a++) {
                            p2Damage++;
                        }
                        break;

                        case 6: for (int a = 1; a <= 15; a++) {
                            p2Damage++;
                        }
                        break;

                        case 7: for (int a = 1; a <= 28; a++) {
                            p2Damage++;
                        }
                        System.out.println("Critical hit landed"); 
                        break;

                        case 8: for (int a = 1; a <= 30; a++) {
                            p2Damage++;
                        }
                        System.out.println("Critical hit landed"); 
                        break;

                        case 9: for (int a = 1; a <= 45; a++) {
                            p2Damage++; 
                        }
                        System.out.println("Black flash landed!!"); 
                        break;

                        default:
                            break;
                    }

                //Player 2 kick code
                } else if (num2 == 9) {
                    System.out.println("You kicked your enemy");

                    int attackKick = rnd.nextInt(5) + 1;

                    switch (attackKick) {
                        case 1: for (int a = 1; a <= 15; a++) {
                            p2Damage++;
                        }
                        break;

                        case 2: for (int a = 1; a <= 15; a++) {
                            p2Damage++;
                        }
                        break;

                        case 3: for (int a = 1; a <= 28; a++) {
                            p2Damage++;
                        }
                        break;

                        case 4: for (int a = 1; a <= 30; a++) {
                            p2Damage++;
                        }
                        break;

                        case 5: for (int a = 1; a <= 35; a++) {
                            p2Damage++;
                         }
                         break;

                        default:
                            break;
                    }
                //Player 2 block code
                } else if (num2 == 0) {
                    System.out.println("You blocked");
                }

                // Code for reducing the impact damage when blocking
                if (num1 == 0 && p2Damage > 0) {
                    p2Damage = p2Damage / 2;
                    System.out.println("Player 1 blocked! Damage reduced.");
                }

                if (num2 == 0 && p1Damage > 0) {
                    p1Damage = p1Damage / 2;
                    System.out.println("Player 2 blocked! Damage reduced.");
                }

                // Apply damage after blocking
                p2Health -= p1Damage;
                p1Health -= p2Damage;

                // Print results of the round
                if (p1Damage > 0) System.out.println("Player 1 dealt " + p1Damage + " damage");
                if (p2Damage > 0) System.out.println("Player 2 dealt " + p2Damage + " damage");

                System.out.println("Player 2s health is " + p2Health);
                System.out.println("Player 1s health is " + p1Health);

                if (p1Health <= 0 && p2Health <= 0) {
                    System.out.println("DRAW");
                    game = false;

                } else if (p2Health <= 0) {
                    System.out.println("Player 2 loses, Player 1 Wins!!");
                    game = false;
                    
                } else if (p1Health <= 0) {
                    System.out.println("Player 1 loses, Player 2 Wins!!!");
                    game = false;
                }

            } catch (InputMismatchException e) {
                System.err.println("Wrong Input, Please Try Again");
                sc.next();
            }
        }
    }

}
