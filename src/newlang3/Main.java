package newlang3;

public class Main {
    public static void main(String[] args) {
        String fileName = "src/txt/BASIC";
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl(fileName);
        LexicalUnit lexicalUnit;
        while (true) {
            try {
                lexicalUnit = lexicalAnalyzer.get();
                if (lexicalUnit != null) {
                    if (lexicalUnit.getType() == LexicalType.EOF) break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
