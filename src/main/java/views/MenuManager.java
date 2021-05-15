package views;

import views.Welcome.WelcomeMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuManager {
    private final Scanner scanner;
    private final List<Menu> allMenus;

    public MenuManager(Scanner scanner) {
        allMenus = new ArrayList<>();
        this.scanner = scanner;
    }

    public void run() {
        allMenus.add(new WelcomeMenu());
        while(true) {
            Menu recent = allMenus.get(allMenus.size() - 1);
            //recent.setScanner(scanner);

            recent.run();

            System.out.print(recent);

            if(recent.getOptions() == null) {
                allMenus.add(recent.getMenu(0));
                continue;
            }

            for(int i = 0; i < recent.getOptions().size(); i++) {
                System.out.println((i + 1) + " - " + recent.getOptions().get(i));
            }
            System.out.println("0 - back");
            int selected = Integer.parseInt(scanner.nextLine());

            if(selected == 0) {
                allMenus.remove(allMenus.size() - 1);
            } else {
                allMenus.add(recent.getMenu(selected));
            }
        }
    }
}