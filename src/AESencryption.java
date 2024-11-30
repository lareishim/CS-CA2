import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Scanner;

public class AESencryption {

    // REFERENCES ( I RESEARCHED ONLINE TO AID MY PROJECT )
    // https://www.baeldung.com/java-aes-encryption-decryption
    // https://www.javacodegeeks.com/
    // https://www.geeksforgeeks.org/what-is-java-aes-encryption-and-decryption/

    static Scanner sc = new Scanner(System.in);

    // Main method to start the application ( got from previous CA )
    public static void main(String[] args) {

        Menu();

    }

    // Menu system that allows user to choose encryption, decryption or exit
    public static void Menu() {
        boolean exit = false;

        // Loop until the user chooses to exit the program
        while (!exit) {
            // Display the menu
            System.out.println("\nMenu:");
            System.out.println("1. Encrypt a file");
            System.out.println("2. Decrypt a file");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());  // Get user's choice and parse it to an integer

                // Perform actions based on user's choice
                switch (choice) {
                    case 1:
                        //encryptFile(sc);  // Call the encryption method if user chooses 1
                        break;
                    case 2:
                        //decryptFile(sc);  // Call the decryption method if user chooses 2
                        break;
                    case 3:
                        exit = true;  // Exit the program if user chooses 3
                        System.out.println("Exiting program. Goodbye!");
                        break;
                    default:
                        // If user enters an invalid option, display an error message
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                // Handle invalid number input
                System.out.println("Error: Invalid input. Please enter a valid number.");
            } catch (Exception e) {
                // Catch any unexpected errors
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    // Method to encrypt the file data using the provided AES key
    private static byte[] encryptData(byte[] data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");  // Create an AES cipher instance
        cipher.init(Cipher.ENCRYPT_MODE, key);  // Initialize the cipher in encryption mode
        return cipher.doFinal(data);  // Perform the encryption and return the encrypted data
    }

    // Method to decrypt the file data using the provided AES key
    private static byte[] decryptData(byte[] data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");  // Create an AES cipher instance
        cipher.init(Cipher.DECRYPT_MODE, key);  // Initialize the cipher in decryption mode
        return cipher.doFinal(data);  // Perform the decryption and return the decrypted data
    }
}