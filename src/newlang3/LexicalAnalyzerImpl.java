package newlang3;

import sun.security.krb5.internal.PAData;

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
        Map<LexicalType, Pattern> sym_map = new LinkedHashMap<>();
        Map<LexicalType, String> str_map = new LinkedHashMap<>();
        Map<LexicalType, Pattern> num_map = new LinkedHashMap<>();
        Map<String, Pattern> map_block = new LinkedHashMap<>();

        //文字列、数値、記号かどうかでまず塊を切り出す
        map_block.put("NUM",Pattern.compile("^(([0-9]+(\\.[0-9]+)?))"));
        map_block.put("STR_NUM", Pattern.compile("^([a-zA-Z]+([a-zA-z0-9]*))"));
        map_block.put("SYMBOL", Pattern.compile("^(\"(.*)\"|[=+\\-/*<>]+)"));

        //予約語
        str_map.put(LexicalType.EOF, "EOF");
        str_map.put(LexicalType.DIM,"DIM");
        str_map.put(LexicalType.FOR, "FOR");
        str_map.put(LexicalType.TO, "TO");
        str_map.put(LexicalType.NEXT, "NEXT");
        str_map.put(LexicalType.ELSEIF, "ELSEIF");
        str_map.put(LexicalType.IF, "IF");
        str_map.put(LexicalType.ELSE, "ELSE");
        str_map.put(LexicalType.THEN, "THEN");


        //num
        num_map.put(LexicalType.DOUBLEVAL, Pattern.compile("^(([0-9]+(\\.[0-9]+)))"));
        num_map.put(LexicalType.INTVAL, Pattern.compile("^(([0-9]+))"));

        //Symbol
        sym_map.put(LexicalType.LE, Pattern.compile("^(<=)|(=<)"));
        sym_map.put(LexicalType.GE, Pattern.compile("^(>=)|(=>)"));
        sym_map.put(LexicalType.NE, Pattern.compile("^(<>)"));

        sym_map.put(LexicalType.LT, Pattern.compile("^(<)"));
        sym_map.put(LexicalType.GT, Pattern.compile("^(>)"));

        sym_map.put(LexicalType.EQ, Pattern.compile("^([=])"));
        sym_map.put(LexicalType.ADD, Pattern.compile("^([+])"));
        sym_map.put(LexicalType.SUB, Pattern.compile("^([\\-])"));
        sym_map.put(LexicalType.MUL, Pattern.compile("^([*])"));
        sym_map.put(LexicalType.DIV, Pattern.compile("^([/])"));

        sym_map.put(LexicalType.LITERAL, Pattern.compile("^\"(.*)\""));


        //無視するパターン
        Pattern p_ignore = Pattern.compile("^([ \t\r\n]+)");

        for (Map.Entry<String, Pattern> patternEntry : map_block.entrySet()) {
            Matcher matcher = patternEntry.getValue().matcher(code);
            Matcher m_ignore = p_ignore.matcher(code);

            if (matcher.find()) {
                //文字列の塊を切り出して、予約語と一致するか調べる
                if(patternEntry.getKey().equals("STR_NUM")){
                    for (Map.Entry<LexicalType, String> strPatternEntry : str_map.entrySet()) {
                        if(matcher.group().equals(strPatternEntry.getValue())){
                            code = code.substring(matcher.end());
                            System.out.println(strPatternEntry.getKey() + ":" + matcher.group());
                            return new LexicalUnit(strPatternEntry.getKey(), null);
                        }
                    }
                    code = code.substring(matcher.end());
                    System.out.println(patternEntry.getKey() + ":" + matcher.group());
                    return new LexicalUnit(LexicalType.NAME, null);
                }else if(patternEntry.getKey().equals("NUM")){
                    for (Map.Entry<LexicalType, Pattern> numPatternEntry : num_map.entrySet()) {
                        Matcher num_matcher = numPatternEntry.getValue().matcher(matcher.group());
                        if(num_matcher.find()){
                            code = code.substring(matcher.end());
                            System.out.println(numPatternEntry.getKey() + ":" + matcher.group());
                            return new LexicalUnit(numPatternEntry.getKey(), null);
                        }
                    }
                    code = code.substring(matcher.end());
                    return null;
                }else if(patternEntry.getKey().equals("SYMBOL")){
                    for (Map.Entry<LexicalType, Pattern> symPatternEntry : sym_map.entrySet()) {
                        Matcher sym_matcher = symPatternEntry.getValue().matcher(matcher.group());
                        if(sym_matcher.find()){
                            code = code.substring(matcher.end());
                            System.out.println(symPatternEntry.getKey() + ":" + matcher.group());
                            return new LexicalUnit(symPatternEntry.getKey(), null);
                        }
                    }
                    code = code.substring(matcher.end());
                    return null;
                }

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
