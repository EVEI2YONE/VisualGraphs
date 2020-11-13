package dbBuilder;

import models.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBGrammarParser {
    private static Graph graph;
    private static StringBuilder content;
    private static String[] keywords =
            {"Table", "Enum", "ref"};
    private static String[] types =
            {"int", "varchar"};
    private static List<String> enums = new ArrayList<>();
    private static List<String> vars = new ArrayList<>();
    private static int lineNumber = 0;

    private static int getEndOfWord() {
        int space = content.indexOf(" ");
        int recent = (space < 0) ? 1 : space;
        //consider if EOF
        recent = (content.length() == 0) ? 0 : recent;
        return recent;
    }

    private static String peekToken(){
        int recent = getEndOfWord();
        return content.substring(0, recent);
    }

    private static String getToken() {
        String token = peekToken();
        int recent = getEndOfWord();
        content = content.delete(0, recent+1);
        content.trimToSize();
        return token;
    }

    private static int getClosest(String str, int current) {
        int nextIndex = content.indexOf(str);
        if(nextIndex == -1)
            return current;
        else
            return Math.min(current, nextIndex);
    }

    //consume up to next valid name, }, (Table, Enum, ref, or $ (EOF))
    private static String consume(String type) {
        int closest = content.length()-1; // $ (EOF)
        closest = getClosest("Table", closest);
        closest = getClosest("Enum", closest);
        closest = getClosest("ref", closest);
        switch(type.toLowerCase()) {
            case "row":
                closest = getClosest("}", closest);
                String nextWord = nextValidName(); //find next valid word
                closest = getClosest(nextWord, closest);
                break;
            case "table":
                break;
        }
        String str = content.substring(0, closest);
        content.delete(0, closest);
        return str;
    }

    private static String nextValidName() {
        String[] words = content.toString().split(" ");
        for(String word : words)
            if(validName(word))
                return word;
        return "$"; //return EOF
    }

    public static void testMethod() {
        content = new StringBuilder();
        content.append("Table orders {\n" +
                "    id int PK\n" +
                "    user_id int\n" +
                "    status varchar\n" +
                "    created_at varchar\n" +
                "}\n" +
                "\n" +
                "Table order_items {\n" +
                "    order_id int\n" +
                "    product_id int\n" +
                "    quantity int\n" +
                "}");
        content.trimToSize();
        content = new StringBuilder(content.toString().replaceAll("\\s+", " "));
        content.delete(0, 10);
        String table = consume("table");
        System.out.println(table);

        content.delete(0, 44);
        String row = consume("row");
        System.out.println(row);
    }

    private static void build(String filename) throws IOException{
        content = new StringBuilder();
        File file;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            file = new File(filename);
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String[] ext = filename.trim().split(".*\\.");
            String line;
            int count;
            int loops = 0;
            String[] temp;
            while ((line = br.readLine()) != null) {
                count = 0;
                line = line.trim();
                temp = line.split("(\\s)+");
                while(count < temp.length) {
                    if(count > 0 || loops > 0)
                        if(!temp[count].equals(""))
                            content.append(" ");
                    content.append(temp[count]);
                    count++;
                }
                loops++;
            }
            content.append(" $"); //EOF indicator
        }catch(Exception ex) {
            System.out.println("Error building from file");
        }
    }

    public static Graph parseDB(String filename) {
        graph = new Graph();
        try {
            build(filename);
            System.out.println("DONE");
        }catch(Exception ex) {
            System.out.println("Error opening file");
        }

        parseContainer();
        return graph;
    }

    //CONTAINERS -> CONTAINER | CONTAINER CONTAINERS
    //CONTAINER -> TABLE | ENUM | REF | EPSILON
    private static void parseContainer() {
        String token;
        boolean flag = false;
        do {
            token = getToken();
            switch(token) {
                case "Table": parseTable(); break;
                case "Enum": parseEnum(); break;
                case "ref": parseRef(); break;
                case "$": System.out.println("End of file"); break;
                default:
                    System.out.println("Expected 'Table', 'Enum' or 'ref'");
                    consume("table");
                    flag = true;
            }
        } while(!token.equals("$") && !flag);
    }

    //TABLE -> Table NAME { TABLE_CONTENT }
    private static void parseTable() {
        String name = peekToken();
        if(validName(name)) {
            getToken();
        }
        else {
            System.out.println("Invalid name");
        }
        String token = peekToken();
        switch(token) {
            case "{": getToken(); break;
            default:
                System.out.println("Expected {");
        }

        parseTableContent();

        token = peekToken();
        switch(token) {
            case "}": getToken(); break;
            default:
                System.out.println("Expected a }");
        }
    }

    //TABLE_CONTENT -> TABLE_ROW | TABLE_ROW TABLE_CONTENT
    private static void parseTableContent() {
        String token = peekToken();
        boolean flag = false;
        while(!token.equals("}") && !token.equals("") && !flag) {
            //TABLE_ROW
            if(validName(token))
                parseTableRow();
            else if(validKeyword(token)) {
                consume("row");
                flag = true;
            }
            else {
                if(validType(token) || validEnum(token)) {
                    System.out.println("Expected a variable name");
                }
                consume("row");
                flag = true;
            }
            token = peekToken();
        }
    }

    //TABLE_ROW -> EPSILON | NAME TYPE | NAME TYPE pk | NAME ENUM_TYPE
    private static void parseTableRow() {
        String name = getToken();
        String token = peekToken();
        //EPSILON
        if(name.equals("}")) {
            return;
        }
        //NAME TYPE
        else if(validType(token)) {
            getToken();
            String pk = peekToken();
            //NAME TYPE PK
            if(pk.toUpperCase().equals("PK")) {
                getToken();
                if(!pk.equals("PK")) {
                    System.out.println("Expected PK");
                }
            }
            else if(pk.equals("}")) {
                return;
            }
        }
        //NAME ENUM_TYPE
        else if(validEnum(token)) {

        }
        else {
            if(!validName(token))
                System.out.println("Row needs valid variable name");
            else if(!validType(token))
                System.out.println("Row needs valid type");
            else if(!validType(token))
                System.out.println("Row needs valid enum");
            else
                System.out.println("SOME OTHER ERROR PARSING ROW");
        }
    }

    //ENUM -> Enum ENUM_TYPE { ENUM_CONTENT }
    private static void parseEnum() {

    }

    //ENUM_CONTENT -> ENUM_ROW | ENUM_ROW ENUM_CONTENT
    private static void parseEnumContent() {

    }

    //ENUM_ROW -> NAME [ note: NAME ]
    private static void parseEnumRow() {

    }

    private static void parseRef() {

    }

    private static boolean validName(String name) {
        //TODO: use regex to validate name
        //TODO: make sure name isn't a keyword
        if(name.equals("$"))
            return false;
        if(validType(name))
            return false;
        if(validEnum(name))
            return false;
        if(validKeyword(name))
            return false;
        return true;
    }

    private static boolean  validEnum(String name) {
        //TODO: use regex to validate name
        //TODO: make sure name isn't a keyword
        if(name.equals("$"))
            return false;
        for(String tempEnum : enums)
            if(name.equals(tempEnum))
                return true;
        return false;
    }

    private static boolean validType(String name){
        for(String type : types)
            if(name.equals(type))
                return true;
        return false;
    }

    private static boolean validKeyword(String token) {
        for(String key : keywords)
            if(token.equals(key))
                return true;
        return false;
    }
}