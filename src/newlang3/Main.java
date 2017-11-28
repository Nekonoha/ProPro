package newlang3;

public class Main {
    public static void main(String[] args) {
        String fileName = "src/txt/BASIC";
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl_(fileName);
        LexicalUnit lexicalUnit;
        while (true) {
            try {
                lexicalUnit = lexicalAnalyzer.get();
                if (lexicalUnit != null) {
                    System.out.println(lexicalUnit);
                    if (lexicalUnit.getType() == LexicalType.EOF) break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

    }
}

