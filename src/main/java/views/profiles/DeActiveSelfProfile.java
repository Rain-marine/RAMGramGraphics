package views.profiles;

import controllers.SettingController;
import views.MainMenu;
import views.Menu;
import views.Welcome.LoginMenu;

public class DeActiveSelfProfile extends Menu {
    private final SettingController settingController;
    private String username;

    public DeActiveSelfProfile(String username) {
        this.settingController = new SettingController();
        this.username = username;
    }

    @Override
    public void run() {
        System.out.println("your account is deActive.\n  active it now by typing \"activate\"." +
                "\n enter anything else to go back.");
        String input = scanner.nextLine();
        if (input.equals("activate")){
            settingController.activateAccount(username);
            System.out.println("Account Activated! press enter to continue");
            scanner.nextLine();
            getMenu(2).run();
        }
        else
            getMenu(1).run();

    }

    @Override
    public Menu getMenu(int option) {
        if(option==1)
            return new LoginMenu();
        return new MainMenu();
    }
}
