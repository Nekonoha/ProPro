package newlang3;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {
    private String filename;
    private InputStreamReader isr;
    private PushbackReader pbr;
    ArrayList<String> unit = new ArrayList<>();
    StringBuilder code_s = new StringBuilder();
    String code;

    public LexicalAnalyzerImpl(String filename) {
        this.filename = filename;
        try {
            InputStream in = new FileInputStream(this.filename);
            this.isr = new InputStreamReader(in);
            this.pbr = new PushbackReader(isr);
            //切り出し
            while (true) {
                int c = pbr.read();
                if (c == -1) break;
                code_s.append((char) c);
            }

            code = code_s.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LexicalUnit get() throws Exception {
        //ほしいパターン
        Map<LexicalType, Pattern> map = new LinkedHashMap<>();

        //予約語
        map.put(LexicalType.NL, Pattern.compile("^(\r\n+)"));
        map.put(LexicalType.EOF, Pattern.compile("^(EOF)"));
        map.put(LexicalType.DIM, Pattern.compile("^(DIM)"));
        map.put(LexicalType.FOR, Pattern.compile("^(FOR)"));
        map.put(LexicalType.TO, Pattern.compile("^(TO)"));
        map.put(LexicalType.NEXT, Pattern.compile("^(NEXT)"));
        map.put(LexicalType.ELSEIF, Pattern.compile("^(ELSEIF)"));
        map.put(LexicalType.IF, Pattern.compile("^(IF)"));
        map.put(LexicalType.ELSE, Pattern.compile("^(ELSE)"));
        map.put(LexicalType.THEN, Pattern.compile("^(THEN)"));


        //num
        map.put(LexicalType.DOUBLEVAL, Pattern.compile("^(([0-9]+(\\.[0-9]+)))"));
        map.put(LexicalType.INTVAL, Pattern.compile("^(([0-9]+))"));

        //Symbol
        map.put(LexicalType.LE, Pattern.compile("^(<=)|(=<)"));
        map.put(LexicalType.GE, Pattern.compile("^(>=)|(=>)"));
        map.put(LexicalType.NE, Pattern.compile("^(<>)"));

        map.put(LexicalType.LT, Pattern.compile("^(<)"));
        map.put(LexicalType.GT, Pattern.compile("^(>)"));

        map.put(LexicalType.EQ, Pattern.compile("^([=])"));
        map.put(LexicalType.ADD, Pattern.compile("^([+])"));
        map.put(LexicalType.SUB, Pattern.compile("^([\\-])"));
        map.put(LexicalType.MUL, Pattern.compile("^([*])"));
        map.put(LexicalType.DIV, Pattern.compile("^([/])"));

        map.put(LexicalType.NAME, Pattern.compile("^([a-zA-Z]+([a-zA-z0-9]*))"));
        map.put(LexicalType.LITERAL, Pattern.compile("^\"(.*)\""));
        //無視するパターン
        Pattern p_ignore = Pattern.compile("^[ \t\r\n]+");

        for (Map.Entry<LexicalType, Pattern> patternEntry : map.entrySet()) {
            Matcher matcher = patternEntry.getValue().matcher(code);
            Matcher m_ignore = p_ignore.matcher(code);
            if (matcher.find()) {
                unit.add(matcher.group());
                code = code.substring(matcher.end());
                System.out.println(patternEntry.getKey());
                return new LexicalUnit(patternEntry.getKey(), null);
            } else if (m_ignore.find()) {
                code = code.substring(m_ignore.end());
            }
        }

        // TODO: 切り出したやつの表示　あとで消す
        return null;
    }

    @Override
    public boolean expect(LexicalType type) throws Exception {
        return false;
    }

    @Override
    public void unget(LexicalUnit token) throws Exception {

    }


}
