import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
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

    // Method to handle file encryption
    private static void encryptFile(Scanner sc) {
        try {
            // Ask the user to provide a file to encrypt
            System.out.print("Enter the filename to encrypt: ");
            String filename = sc.nextLine().trim();

            File file = new File(filename);
            if (!file.exists()) {  // Check if the file exists
                System.out.println("Error: File does not exist.");
                return;
            }

            //Got AES Key code from online
            // Generate a random AES key for encryption
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);  // Use AES-128 encryption (can change to AES-192 or AES-256)
            SecretKey secretKey = keyGen.generateKey();
            // Encode the key in Base64 format
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            // Encrypt the file data using the generated AES key
            byte[] fileData = readFile(file);  // Read file data as a byte array
            byte[] encryptedData = encryptData(fileData, secretKey);  // Encrypt the data

            // Ask the user for the output filename for encrypted data
            System.out.print("Enter the output filename for encrypted data: ");
            String outputFileName = sc.nextLine().trim();

            // Write the encrypted data to the specified output file
            try (FileOutputStream fos = new FileOutputStream(outputFileName)) {
                fos.write(encryptedData);
            }

            // Save the encryption key to a separate file (key.txt)
            try (FileWriter keyFile = new FileWriter("key.txt")) {
                keyFile.write(encodedKey);
            }

            // Display success messages to the user
            System.out.println("\nEncryption complete.");
            System.out.println("Encryption Key: " + encodedKey);
            System.out.println("Encrypted data has been written to '" + outputFileName + "'.");
            System.out.println("Encryption key has been saved to 'key.txt'.");
        } catch (IOException e) {
            // Handle any input/output exceptions
            System.out.println("Error: Unable to read or write file. " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions during encryption
            System.out.println("Error during encryption: " + e.getMessage());
        }
    }

    // Method to handle file decryption
    private static void decryptFile(Scanner sc) {
        try {
            // Ask the user to provide a file to decrypt
            System.out.print("Enter the filename to decrypt: ");
            String filename = sc.nextLine().trim();

            File file = new File(filename);
            if (!file.exists()) {  // Check if the file exists
                System.out.println("Error: File does not exist.");
                return;
            }

            // Ask the user to enter the encryption key yh
            System.out.print("Enter the decryption key: ");
            String encodedKey = sc.nextLine().trim();

            // Decode the provided key from Base64 format
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            // Decrypt the file data using the provided AES key
            byte[] fileData = readFile(file);  // Read file data as a byte array
            byte[] decryptedData = decryptData(fileData, secretKey);  // Decrypt the data

            // Ask the user for the output filename for decrypted data
            System.out.print("Enter the output filename for decrypted data: ");
            String outputFileName = sc.nextLine().trim();

            // Write the decrypted data to the specified output file
            try (FileOutputStream fos = new FileOutputStream(outputFileName)) {
                fos.write(decryptedData);
            }

            // Display success message to the user
            System.out.println("\nDecryption complete.");
            System.out.println("Decrypted data has been written to '" + outputFileName + "'.");
        } catch (IllegalArgumentException e) {
            // Handle invalid key format during decryption
            System.out.println("Error: Invalid key format. Please ensure the key is correct.");
        } catch (IOException e) {
            // Handle input/output exceptions during decryption
            System.out.println("Error: Unable to read or write file. " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions during decryption
            System.out.println("Error during decryption: " + e.getMessage());
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

    // Method to read the file data as a byte array
    private static byte[] readFile(File file) throws IOException {
        try (FileInputStream f = new FileInputStream(file)) {
            // Create a buffer (4KB size) to read the file in chunks
            byte[] buffer = new byte[4096];  // Buffer size of 4KB
            int bytesRead;  // Variable to store the number of bytes read
            ByteArrayOutputStream b = new ByteArrayOutputStream();  // To accumulate the file content
            // Read the file in chunks until the end of the file (EOF) is reached
            while ((bytesRead = f.read(buffer)) != -1) {
                // Write the read bytes to the ByteArrayOutputStream
                b.write(buffer, 0, bytesRead);
            }
            // Convert the accumulated data in the ByteArrayOutputStream to a byte array
            return b.toByteArray();  // Return the file content as a byte array
        }
    }
}