package mg.itu.display;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import mg.itu.database.*;
import mg.itu.generator.Scaffolding;
import mg.itu.utils.File;
import mg.itu.utils.Format;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Console {
    public final static String COLOR_RED = "\u001B[31m";
    public final static String COLOR_GREEN = "\u001B[32m";
    public final static String COLOR_YELLOW = "\u001B[33m";
    public final static String COLOR_BLUE = "\u001B[34m";
    public final static String COLOR_PURPLE = "\u001B[35m";
    public final static String COLOR_CYAN = "\u001B[36m";
    public final static String COLOR_WHITE = "\u001B[37m";
    public final static String COLOR_BLACK = "\u001B[30m";
    public final static String COLOR_RESET = "\u001B[0m";
    public static final Logger LOGGER = LogManager.getLogger(Console.class);

    public static void main(String[] args) {
        printLogo();

        String configName = "config.json";
        if(args.length != 0)
            configName = args[0];

        final Scanner scanner = new Scanner(System.in);
        final String content = File.getFileContentAsString(configName);
        final Config config = Format.fromJson(content, Config.class);

        init(config);

        final Database database = askForDatabase(config, scanner);

        final List<Table> tables = askForTables(database, scanner);

        // eg: spring, angular ...
        final String app = askToGenerate(config, scanner);
        final HashMap<String, HashMap<String, String>> components = config.getGenerations().get(app);

        // eg: entity, repository ...
        final String component = askToGenerateType(components, scanner);

        final String packageName = askString("📂 Enter package name (eg: mg.itu): ", scanner);

        final String path = askPath(scanner, packageName);

        if (component.equals("** All **"))
            generateAll(config, app, packageName, tables, database, path);
        else
            generate(config, app, component, packageName, tables, database, path);

        scanner.close();
    }

    // Main methods
    private static void init(Config config) {
        Scaffolding.setTemplatePath(config.getTemplatePath());
    }

    private static void printLogo() {
        System.out.println(COLOR_BLUE +
                """
                  __  __ _       _  _____            __  __      _     _ _            \s
                 |  \\/  (_)     (_)/ ____|          / _|/ _|    | |   | (_)           \s
                 | \\  / |_ _ __  _| (___   ___ __ _| |_| |_ ___ | | __| |_ _ __   __ _\s
                 | |\\/| | | '_ \\| |\\___ \\ / __/ _` |  _|  _/ _ \\| |/ _` | | '_ \\ / _` |
                 | |  | | | | | | |____) | (_| (_| | | | || (_) | | (_| | | | | | (_| |
                 |_|  |_|_|_| |_|_|_____/ \\___\\__,_|_| |_| \\___/|_|\\__,_|_|_| |_|\\__, |
                                                                                  __/ |
                                                                                 |___/\s
                                       \s"""
        + COLOR_RESET);
    }

    private static Database askForDatabase(Config config, Scanner scanner) {
        final Database database = askChoice("📦 Choose a database: ", config.getDatabases(), scanner);
        database.init(config);
        return database;
    }

    private static List<Table> askForTables(Database database, Scanner scanner) {
        final List<Table> tables = database.getTables();
        final TreeMap<String, Table> tableMap = new TreeMap<>();
        for (Table table : tables) {
            tableMap.put(table.getName(), table);
        }
        tableMap.put("** All **", null);
        return askChoices("ℹ️ Choose tables: ", tableMap, scanner);
    }

    private static String askToGenerate(Config config, Scanner scanner) {
        return askChoiceKey("🛠️ What to generate?", config.getGenerations(), scanner);
    }

    private static String askToGenerateType(HashMap<String, HashMap<String, String>> generation,
                                                             Scanner scanner) {
        HashMap<String, HashMap<String, String>> generationMap = new HashMap<>(generation);
        generationMap.put("** All **", null);
        return askChoiceKey("🧩 Which component? ", generationMap, scanner);
    }

    private static String askPath(Scanner scanner, String packageName) {
        final String packageNamePath = Format.packageToPath(packageName);
        return Format.toPathNameWithTrail(
                askString("🗃️ Enter the path to save the file(s) (eg: entity/): ", scanner))
                + packageNamePath;
    }

    private static void generate(Config config, HashMap<String, String> params, String packageName, Table table,
                                 Database database, String path) {
        final String engine = params.get("engine");

        Scaffolding scaffolding;
        try {
            Class<?> scaffoldingClass = Class.forName(engine);
            scaffolding = (Scaffolding) scaffoldingClass
                    .getDeclaredConstructor(HashMap.class, HashMap.class, HashMap.class,
                            String.class, Table.class, Provider.class, Database.class)
                    .newInstance(
                            params,
                            config.getLanguages().get(params.get("language")),
                            config.getMappings().get(params.get("language")),
                            packageName,
                            table,
                            config.getProviders().get(database.getProvider()),database
                    );
            scaffolding.generate(path);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            LOGGER.error("Error while generating scaffolding", e);
        }
    }

    private static void generate(Config config, String app, String component, String packageName, List<Table> table,
                                 Database database, String path) {
        for (Table t : table) {
            generate(config, config.getGenerations().get(app).get(component), packageName, t, database, path);
        }
    }

    private static void generateAll(Config config, String app, String packageName, List<Table> tables,
                                    Database database, String path) {
        System.out.println();
        System.out.println(COLOR_YELLOW + "Generating all components..." + COLOR_RESET);
        for (String component : config.getGenerations().get(app).keySet()) {
            System.out.println("✅" + component);
            generate(config, app, component, packageName, tables, database, path);
        }
        System.out.println(COLOR_GREEN + "All components generated successfully!" + COLOR_RESET);
    }

    // Helper methods
    private static void printQuestion(String question) {
        System.out.println(COLOR_CYAN + question + COLOR_RESET);
    }

    private static void printError(String error) {
        System.out.println(COLOR_RED + error + COLOR_RESET);
    }

    private static int askInt(String question, int min, int max, Scanner scanner) {
        printQuestion(question);
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().strip());
            if (choice < min || choice > max) {
                printError("Please enter a number between " + min + " and " + max);
                return askInt(question, min, max, scanner);
            }
        } catch (NumberFormatException e) {
            printError("Please enter a number");
            return askInt(question, min, max, scanner);
        }
        return choice;
    }

    private static String askString(String question, Scanner scanner) {
        printQuestion(question);
        return scanner.nextLine().strip();
    }

    private static void printChoices(String[] choices) {
        for (int i = 0; i < choices.length; i++) {
            System.out.println(i + ". " + choices[i]);
        }
    }

    private static <T> T askChoice(String question, HashMap<String, T> choices, Scanner scanner) {
        printQuestion(question);

        final String[] keys = choices.keySet().toArray(new String[0]);
        printChoices(keys);

        final int maxChoice = choices.size() - 1;
        final int choiceIndex = askInt("Enter the index of your choice: ", 0, maxChoice, scanner);

        return choices.get(keys[choiceIndex]);
    }

    private static String askChoiceKey(String question,
                                       HashMap<String, ?> choices, Scanner scanner) {
        printQuestion(question);

        final String[] keys = choices.keySet().toArray(new String[0]);
        printChoices(keys);

        final int maxChoice = choices.size() - 1;
        final int choiceIndex = askInt("Enter the index of your choice: ", 0, maxChoice, scanner);

        return keys[choiceIndex];
    }

    private static int[] askMultipleAnswers(String question, int min, int maxChoice, Scanner scanner) {
        final String answer = askString(question, scanner);
        final String[] answers = answer.split(",");
        final int[] choices = new int[answers.length];
        for (int i = 0; i < answers.length; i++) {
            try {
                choices[i] = Integer.parseInt(answers[i].strip());
                if (choices[i] < min || choices[i] > maxChoice) {
                    printError("Please enter a number between " + min + " and " + maxChoice);
                    return askMultipleAnswers(question, min, maxChoice, scanner);
                }
            } catch (NumberFormatException e) {
                printError("Please enter a number (separate multiple choices with a comma)");
                return askMultipleAnswers(question, min, maxChoice, scanner);
            }
        }
        return choices;
    }

    private static <T> List<T> askChoices(String question, TreeMap<String, T> choices, Scanner scanner) {
        printQuestion(question);

        final String[] keys = choices.keySet().toArray(new String[0]);
        printChoices(keys);

        final int maxChoice = choices.size() - 1;
        final int[] choiceIndex = askMultipleAnswers("Enter your choices (separate with a comma):",
                0, maxChoice, scanner);

        final List<T> result = new ArrayList<>();
        final List<T> values = choices.values().stream().toList();
        for (int index : choiceIndex) {
            result.add(values.get(index));
        }

        if (result.contains(null))
            return values.subList(1, values.size());
        return result;
    }
}
