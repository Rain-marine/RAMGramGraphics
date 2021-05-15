package views.Welcome;

import controllers.RegisterManager;
import views.ConsoleColors;
import views.Menu;
import views.profiles.FollowingProfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegisterMenu extends Menu {
    private final RegisterManager registerManager;

    public RegisterMenu() {
        this.registerManager = new RegisterManager();
    }

    @Override
    public void run() {
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT+ "Hi! to register complete this form. fields with * are necessary ");
        boolean registerSuccessful;
        do {
            System.out.println("if you want to go back to Welcome Menu enter 1, enter anything else to continue ");
            String firstInput = scanner.nextLine();
            if (firstInput.equals("1")){
                getMenu(1).run();
            }
            System.out.println("*Enter your full name:");
            String fullName = scanner.nextLine();
            System.out.println("*Enter your username:");
            String username = scanner.nextLine();
            System.out.println("*Enter your password:");
            String password = scanner.nextLine();
            System.out.println("*re-Enter your password:");
            String passwordRe = scanner.nextLine();
            System.out.println("*Enter your email:");
            String email = scanner.nextLine();
            System.out.println("Enter your phone number:");
            String phoneNumber = scanner.nextLine();
            System.out.println("Enter your Bio:");
            String bio = scanner.nextLine();
            System.out.println("Enter your birth date in yyyy/MM/dd format:");
            String birthDate = scanner.nextLine();
            registerSuccessful = checkRequiredInputs(fullName,username,password,passwordRe,email,phoneNumber,bio,birthDate);

            if(registerSuccessful){
                System.out.println("register successful!");
                registerManager.makeNewUser(fullName,username,password,email,phoneNumber,bio,birthDate);
                log.info("User " + username + " registered");
            }

        } while (!registerSuccessful);
        System.out.println("press enter to continue");
        scanner.nextLine();
        getMenu(1).run();
    }

    public boolean checkRequiredInputs(String fullName,String username ,String password,String passwordRe , String email ,String phoneNumber , String bio , String birthDate){
        if (fullName.equals("")){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"you must enter your fullName"+ConsoleColors.RESET);
            return false;
        }
        if (username.equals("")){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"you must enter your username"+ConsoleColors.RESET);
            return false;
        }
        if (password.equals("")){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"you must enter your password"+ConsoleColors.RESET);
            return false;
        }
        if (passwordRe.equals("")){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"you must Re-enter your password"+ConsoleColors.RESET);
            return false;
        }
        if (email.equals("")){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"you must enter your email"+ConsoleColors.RESET);
            return false;
        }

        if (!password.equals(passwordRe)){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"passwords don't match"+ConsoleColors.RESET);
            return false;
        }

        if (!registerManager.isUsernameAvailable(username)){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"username exists. please enter another username"+ConsoleColors.RESET);
            return false;
        }

        if(!email.contains("@") || !email.contains(".")){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"invalid email address"+ConsoleColors.RESET);
            return false;
        }

        if (!registerManager.isEmailAvailable(email)){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT+ "email exists"+ConsoleColors.RESET);
            return false;
        }
        if (!(phoneNumber.equals(""))){
            if(!registerManager.isPhoneNumberAvailable(phoneNumber)){
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT+"phone number exists"+ConsoleColors.RESET);
                return false;
            }
        }
        if(!birthDate.equals("")) {
            try {
                new SimpleDateFormat("yyyy/MM/dd").parse(birthDate);
            } catch (ParseException e) {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT+ "birthday format not valid"+ConsoleColors.RESET);
                return false;
            }
        }
        return true;

    }
    @Override
    public Menu getMenu(int option) {
        return new WelcomeMenu();
    }

    @Override
    public boolean checkValidation(String... input) {
        return false;
    }


}