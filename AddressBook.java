import java.io.*;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AddressBook {
    public static void main(String[] args) throws Exception{

        File file = new File("C:/Users/panos/IdeaProjects/ErgasiaJava/src/p18154/Contacts.txt");
        file.createNewFile();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to your personal Address Book!");

        int ans = 0;
        while (ans == 0){
            System.out.println("Press a number from 1-7 for the following choices:\n1. View all contacts\n2. New contact\n3. Search by name\n4. Search by phone\n5. Edit by name\n6. Delete by name\n7. Quit");

            do {//Ensuring the user enters valid input
                ans = scanner.nextInt();
                if (ans < 1 || ans > 7){
                    System.out.println("The number should not be smaller than 1 or greater than 7. Please enter an other number:");
                }
            }while (ans < 1 || ans > 7);

            if (ans == 1) { //1. View all contacts
                View(file);
                ans = 0;
            } else if (ans == 2){//2. New contact
                scanner.nextLine();
                System.out.println("Enter name:");
                String name = scanner.nextLine();
                System.out.println("Enter phone number:");
                long phone = scanner.nextLong();
                scanner.nextLine();
                System.out.println("Enter email:");
                String email = scanner.nextLine();
                System.out.println("Enter address:");
                String address = scanner.nextLine();

                Add(name, String.valueOf(phone), email, address, file);

                ans = 0;

            } else if (ans == 3){//3. Search by name
                scanner.nextLine();
                System.out.println("Enter the name of the contact you want to find:");
                String name = scanner.nextLine();
                SearchByName(name, file);
                ans = 0;
            } else if (ans == 4){//4. Search by phone
                scanner.nextLine();
                System.out.println("Enter the phone of the contact you want to find:");
                long phone = scanner.nextLong();
                SearchByPhone(phone, file);
                ans = 0;

            } else if (ans == 5){//5. Edit by name
                scanner.nextLine();
                System.out.println("Enter the name of the contact you want to edit: ");
                String name = scanner.nextLine();
                EditByName(name, file);
                System.out.println("The contact has been edited!");
                ans = 0;
            } else if (ans == 6){//6. Delete by name
                scanner.nextLine();
                System.out.println("Enter the name of the contact you want to delete: ");
                String name = scanner.nextLine();
                String res = DeleteByName(name, file);
                if(!res.equals("Contact not found")){
                    System.out.println("Contact " + name + " deleted successfully!");
                }else {
                    System.out.println(res);
                }

                ans = 0;
            } else if(ans == 7){//7. Quit
                scanner.nextLine();
                System.out.println("Do you want to quit AddressBook? (Yes/No)");
                String qans = scanner.nextLine();
                if (qans.equals("Yes")){
                    ans = -1;
                }else if (qans.equals("No")){
                    ans = 0;
                    System.out.println("Welcome Back!");
                }
            }
        }
        System.out.println("Exiting Addressbook...");//In any other case, the program is to be closed
    }

    public static void View(File file) throws Exception{
        Scanner viewfile = new Scanner(file);

        while (viewfile.hasNextLine()){
            System.out.println(viewfile.nextLine());
        }
    }

    public static void Add(String name, String phone, String email, String address, File file) throws Exception{//This method appends to the Contacts file a line consisted of the contact's name, phone, email, address which are the methods parameters
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.append(name + "-" + phone + "-" + email + "-" + address + "\n");
        writer.close();
    }

    public static void SearchByName(String name, File file) throws Exception{
        Scanner viewfile = new Scanner(file); //Scanner for the file
        boolean found = false;
        String line = "";

        while (viewfile.hasNextLine()){//Searches the whole file as long as it has a line to be searched
            line = viewfile.nextLine();
            Scanner viewline = new Scanner(line); //Scanner for a specific line of the file
            String foundtheName = viewline.findInLine(name); //If the name is found in a line, it is stored as a string
            if (foundtheName != null){
                found = true;
                break;//Found it, no need to search furthermore
            }
        }

        if (found){
            System.out.println("You searched for " + line); //The String line contains all the data needed
        }else{
            System.out.println("Did not find contact with this name: " + name);
        }
    }

    public static void SearchByPhone(long phone, File file) throws Exception{//Works in a similar way as the above method
        Scanner viewfile = new Scanner(file);
        boolean found = false;
        String line = "";

        while (viewfile.hasNextLine()){
            line = viewfile.nextLine();
            Scanner viewline = new Scanner(line);
            String foundthePhone = viewline.findInLine(String.valueOf(phone));//We search for the phone of the contact. But the parameter is stored in a long, while the file stores it as a String. So we search for the String value of the long phone
            if (foundthePhone != null){
                found = true;
                break;
            }
        }

        if (found){
            System.out.println("You searched for " + line);
        }else{
            System.out.println("Did not find contact with this phone: " + phone);
        }
    }

    public static void EditByName(String name, File file) throws Exception{

        String lineToRemove = DeleteByName(name, file);//The method DeleteByName is called to delete the line that contains the data of the contact we want to edit

        if (!lineToRemove.equals("Contact not found")){
            String[] contactData = lineToRemove.split("-"); /*The data aren't really lost, they are stored in the Array of Strings contactData. So, in case the user does not want to
                                                                    edit a specific field, its old value will be retrieved by this Array as shown below*/
            Scanner scanner = new Scanner(System.in);

            //Dialogs are following, allowing the program know which of the data will be edited

            System.out.println("Edit name. Enter a new name or 'S' to keep name as it is");
            String ans = scanner.nextLine();
            if (ans.equals("S")){
                name = contactData[0];
            }else{
                name = ans;
            }
            System.out.println("Do you want to Edit field phone? (Yes/No)");
            ans = scanner.nextLine();
            String phone;
            while (!(ans.equals("Yes") || ans.equals("No"))){
                System.out.println("Please type one of the answers: 'Yes' or 'No'");//In case the user wants to edit the phone, we need a long as input instead of a String (as above). So we ensure that he wants to edit it.
                ans = scanner.nextLine();
            }
            if (ans.equals("No")){
                phone = contactData[1];
            }else{// If he wants to do so, we ask the user for a long as input.
                System.out.println("Enter a new phone");
                long phoneAsLong = scanner.nextLong();
                phone = String.valueOf(phoneAsLong); //We store the String value of the user's input, due to the 'Add' method's parameters
                scanner.nextLine();
            }
            System.out.println("Edit email. Enter a new email or 'S' to keep email as it is");
            ans = scanner.nextLine();
            String email;
            if (ans.equals("S")){
                email = contactData[2];
            }else{
                email = ans;
            }
            System.out.println("Edit address. Enter a new address or 'S' to keep address as it is");
            ans = scanner.nextLine();
            String address;
            if (ans.equals("S")){
                address = contactData[3];
            }else{
                address = ans;
            }


            Add(name, phone, email, address, file);//Finally, despite the fact that the old Contact is deleted and a new one is created, the user does not need to edit all of the Contact's data
        }
    }

    public static String DeleteByName(String name, File file) throws Exception{
        //This is a file where all the contacts will be stored, except the ona that we want to delete. The file is temporary and will be deleted by the end of the method
        File tempFile = new File("C:/Users/panos/IdeaProjects/ErgasiaJava/src/p18154/TempFile.txt");

        //A similar to the algorithm of method SearchByName is used to find the line that has the data of the Contact the user entered. If it is found, it returns the whole line. If not, it returns a String to be printed for the user's information
        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        Scanner viewfile = new Scanner(file);
        String line = "";

        while (viewfile.hasNextLine()){
            line = viewfile.nextLine();
            Scanner viewline = new Scanner(line);
            String foundtheName = viewline.findInLine(name);
            if (foundtheName != null){
                break;
            }else{
                line = "Contact not found";
            }
        }

        if (!line.equals("Contact not found")) {//If the String is not "Contact not found" we have a match

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {//All the lines of the file are copied to the temporary one
                String trimmedLine = currentLine.trim();
                if (trimmedLine.equals(line)) continue; //EXCEPT the one that has the data of the Contact that we want to delete
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            FileInputStream instream = null;
            FileOutputStream outstream = null;

            instream = new FileInputStream(tempFile);
            outstream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = instream.read(buffer)) > 0) {
                outstream.write(buffer, 0, length);
            }
            instream.close();
            outstream.close();
            tempFile.delete();//The original file is updated so the temporary one is deleted
        }else{
            System.out.println(line);//If the String is "Contact not found" we inform the user by printing it
        }
        return line;
    }
}
