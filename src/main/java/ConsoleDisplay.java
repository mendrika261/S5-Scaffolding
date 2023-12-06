import mg.core.Generation;
import mg.core.Utils;
import mg.database.Database;
import mg.database.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleDisplay {
  public final static String COLOR_RED = "\u001B[31m";
  public final static String COLOR_GREEN = "\u001B[32m";
  public final static String COLOR_YELLOW = "\u001B[33m";
  public final static String COLOR_BLUE = "\u001B[34m";
  public final static String COLOR_PURPLE = "\u001B[35m";
  public final static String COLOR_CYAN = "\u001B[36m";
  public final static String COLOR_WHITE = "\u001B[37m";
  public final static String COLOR_BLACK = "\u001B[30m";
  public final static String COLOR_RESET = "\u001B[0m";

  public static void main(String[] args) throws SQLException {
    clearScreen();

    Scanner scanner = new Scanner(System.in);

    Database database = new Database();
    Connection connection = null;
    try {
      connection = database.getConnection();
    } catch (Exception e) {
      System.out.println(COLOR_RED + "Connection to database failed 😕: " + e.getMessage() + COLOR_RESET);
      System.exit(1);
    }

    try {
      String langage = askForLangage(scanner);

      while(true) {
        String template = askForTemplate(scanner);
        String path = askForPath(scanner);
        String packageName = askForPackageName(scanner, path);
        Table table = askForTable(scanner, database, connection);

        System.out.println(COLOR_YELLOW + "Processing..." + COLOR_RESET);
        Generation generation = new Generation(langage, path, packageName, template, database, true);
        generation.generate(connection);
        System.out.println(COLOR_GREEN + "Done!" + COLOR_RESET);
      }
    } catch (Exception e) {
      System.out.println(COLOR_RED + "Error 😕: " + e.getMessage() + COLOR_RESET);
    }

    connection.close();
    scanner.close();
  }

  private static void printLogo() {
    /*
      ┳┳┓•  •┏┓   ┏┏  ┓ ┓•
      ┃┃┃┓┏┓┓┗┓┏┏┓╋╋┏┓┃┏┫┓┏┓┏┓
      ┛ ┗┗┛┗┗┗┛┗┗┻┛┛┗┛┗┗┻┗┛┗┗┫
                             ┛
     */
    System.out.print(COLOR_RED);
    System.out.println("┳┳┓•  •┏┓   ┏┏  ┓ ┓•    ");
    System.out.println("┃┃┃┓┏┓┓┗┓┏┏┓╋╋┏┓┃┏┫┓┏┓┏┓");
    System.out.println("┛ ┗┗┛┗┗┗┛┗┗┻┛┛┗┛┗┗┻┗┛┗┗┫");
    System.out.print(COLOR_RESET);
  }

  private static void clearScreen() {
    System.out.flush();
    System.out.print("\033[H\033[2J");
    System.out.flush();
    printLogo();
  }

  private static String askForLangage(Scanner scanner) {
    System.out.println(COLOR_GREEN + "*".repeat(50));
    System.out.println("What language do you want to generate the class in?");
    System.out.println(COLOR_RESET);

    String[] availableLanguages = Utils.getAvailableLanguages();
    for (int i = 0; i < availableLanguages.length; i++) {
      System.out.println((i + 1) + ". " + Utils.toCamelCase(availableLanguages[i], true));
    }

    System.out.print(COLOR_CYAN + "Enter the number: "+ COLOR_RESET);
    try {
      int languageNumber = Integer.parseInt(scanner.nextLine());
      System.out.println();
      return availableLanguages[languageNumber - 1];
    } catch (Exception e) {
      clearScreen();
      System.out.println(COLOR_RED);
      System.out.println("Please enter a valid number" + COLOR_RESET);
      return askForLangage(scanner);
    }
  }

  private static String askForPath(Scanner scanner) {
    System.out.println(COLOR_GREEN + "*".repeat(50));
    System.out.println("Where do you want to generate the class?");
    System.out.println(COLOR_RESET);

    System.out.print(COLOR_CYAN + "Enter the path (leave blank for current directory): "+ COLOR_RESET);
    String path = scanner.nextLine();
    try {
      System.out.println(COLOR_YELLOW + "Checking if path is writable..." + COLOR_RESET);
      Utils.checkIfPathIsWritable(path);
    } catch (Exception e) {
      clearScreen();
      System.out.println(COLOR_RED);
      System.out.println(e.getMessage() + COLOR_RESET);
      return askForPath(scanner);
    }
    System.out.println();
    return path;
  }

  private static String askForPackageName(Scanner scanner, String path) {
    System.out.println(COLOR_GREEN + "*".repeat(50));
    System.out.println("What is the package name?");
    System.out.println(COLOR_RESET);

    System.out.print(COLOR_CYAN + "Enter the package name (leave blank if you want to make package same as path): "+ COLOR_RESET);
    String packageName = scanner.nextLine();
    try {
      if(packageName.isEmpty())
        packageName = Utils.getPackageFromPath(path);
      System.out.println(COLOR_YELLOW + "Checking if package name is valid..." + COLOR_RESET);
      Utils.checkIfPackageNameIsValid(packageName);
    } catch (Exception e) {
      clearScreen();
      System.out.println(COLOR_RED);
      System.out.println(e.getMessage() + COLOR_RESET);
      return askForPackageName(scanner, path);
    }
    System.out.println();
    return packageName;
  }

  private static String askForTemplate(Scanner scanner) {
    System.out.println(COLOR_GREEN + "*".repeat(50));
    System.out.println("What template do you want to use?");
    System.out.println(COLOR_RESET);

    String[] availableTemplates = Utils.getAvailableTemplates();
    for (int i = 0; i < availableTemplates.length; i++) {
      System.out.println((i + 1) + ". " + availableTemplates[i]);
    }

    try {
      System.out.print(COLOR_CYAN + "Enter the number: "+ COLOR_RESET);
      int templateNumber = Integer.parseInt(scanner.nextLine());
      System.out.println();
      return availableTemplates[templateNumber - 1];
    } catch (Exception e) {
      clearScreen();
      System.out.println(COLOR_RED);
      System.out.println("Please enter a valid number" + COLOR_RESET);
      return askForTemplate(scanner);
    }
  }

  private static Table askForTable(Scanner scanner, Database database, Connection connection) {
    System.out.println(COLOR_GREEN + "*".repeat(50));
    System.out.println("What table do you want to generate the class for?");
    System.out.println(COLOR_RESET);

    List<Table> availableTable = database.getTables(connection);
    for (int i=0; i<availableTable.size(); i++) {
      System.out.println((i + 1) + ". " + availableTable.get(i).getName());
    }

    System.out.print(COLOR_CYAN + "Enter the table name or number: "+ COLOR_RESET);
    Table tableTarget = null;
    try {
      String table = scanner.nextLine();
      System.out.println(COLOR_YELLOW + "Checking if table exists..." + COLOR_RESET);

      if(Utils.isInteger(table)) {
        int tableNumber = Integer.parseInt(table);
        if(tableNumber > availableTable.size() | tableNumber < 1)
          throw new Exception("Table number '" + table + "' does not exist");
        tableTarget = availableTable.get(tableNumber - 1);
      } else {
        Optional<Table> tableTargetInput = availableTable.stream().filter(t -> t.getName().equals(table)).findFirst();
        if(tableTargetInput.isEmpty())
          throw new Exception("Table with name '" + table + "' does not exist");
        tableTarget = tableTargetInput.get();
      }
    } catch (Exception e) {
      clearScreen();
      System.out.println(COLOR_RED);
      System.out.println(e.getMessage() + COLOR_RESET);
      return askForTable(scanner, database, connection);
    }
    System.out.println();
    return tableTarget;
  }
}
