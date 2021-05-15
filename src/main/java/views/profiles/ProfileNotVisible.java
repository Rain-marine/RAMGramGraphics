package views.profiles;

import models.User;
import views.ExplorerMenu;
import views.MainMenu;
import views.Menu;

public class ProfileNotVisible extends Menu {
    //username either does not exist or the account is deActive
    //back: search/explorer

    public ProfileNotVisible() {
    }

    @Override
    public void run() {
        System.out.println("Profile Not found. the account either does not exist or you don't have access to it.\n" +
                "press enter to go back");
        scanner.nextLine();
        getMenu(1).run();
    }

    @Override
    public Menu getMenu(int option) {
        if (option == 1)
            return new ExplorerMenu();
        return new MainMenu();
    }
}
