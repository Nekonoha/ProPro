package newlang3;

import newlang4.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {
    private String filename;
    private InputStreamReader isr;
    private PushbackReader pbr;
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

    public LexicalAnalyzerImpl(FileInputStream fin) {
        try {
            InputStream in = fin;
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
        Map<LexicalType, Pattern> num_map = new LinkedHashMap<>();
        Map<String, Pattern> map_block = new LinkedHashMap<>();

        //文字列、数値、記号かどうかでまず塊を切り出す
        map_block.put("DOUBLEVAL", Pattern.compile("^(([0-9]+(\\.[0-9]+)))"));
        map_block.put("INTVAL", Pattern.compile("^(([0-9]+))"));
        map_block.put("STR_NUM", Pattern.compile("^([a-zA-Z]+([a-zA-z0-9]*))"));
        map_block.put("SYMBOL", Pattern.compile("^(\"(.*)\"|[=+\\-/*<>.,]+)"));
        map_block.put("LITERAL", Pattern.compile("^\"(.*)\""));
        map_block.put("NL", Pattern.compile("^[\n]"));


        LexicalType[] rws = {
                LexicalType.EOF,
                LexicalType.DIM,
                LexicalType.FOR,
                LexicalType.FORALL,
                LexicalType.TO,
                LexicalType.NEXT,
                LexicalType.ELSEIF,
                LexicalType.IF,
                LexicalType.ELSE,
                LexicalType.END,
                LexicalType.WHILE,
                LexicalType.WEND,
                LexicalType.UNTIL,
                LexicalType.LOOP
        };

        //Symbol
        sym_map.put(LexicalType.LE, Pattern.compile("^(<=)|(=<)"));
        sym_map.put(LexicalType.GE, Pattern.compile("^(>=)|(=>)"));
        sym_map.put(LexicalType.NE, Pattern.compile("^<>"));
        sym_map.put(LexicalType.LT, Pattern.compile("^<"));
        sym_map.put(LexicalType.GT, Pattern.compile("^>"));
        sym_map.put(LexicalType.EQ, Pattern.compile("^="));
        sym_map.put(LexicalType.ADD, Pattern.compile("^\\+"));
        sym_map.put(LexicalType.SUB, Pattern.compile("^-"));
        sym_map.put(LexicalType.MUL, Pattern.compile("^\\*"));
        sym_map.put(LexicalType.DIV, Pattern.compile("^/"));
        sym_map.put(LexicalType.COMMA, Pattern.compile("^,"));
        sym_map.put(LexicalType.DOT, Pattern.compile("^\\."));

        for (Map.Entry<String, Pattern> patternEntry : map_block.entrySet()) {
            Matcher matcher = patternEntry.getValue().matcher(code);
            code = code.substring(matcher.end());
            if (matcher.find()) {
                //文字列の塊を切り出して、予約語と一致するか調べる
                if (patternEntry.getKey().equals("STR_NUM")) {
                    for (LexicalType rw : rws) {
                        if (matcher.group().equals(rw.name())) return new LexicalUnit(rw, null);
                    }
                    return new LexicalUnit(LexicalType.NAME, new ValueImpl(matcher.group()));
                } else if (patternEntry.getKey().equals("SYMBOL")) {
                    for (Map.Entry<LexicalType, Pattern> symPatternEntry : sym_map.entrySet()) {
                        Matcher sym_matcher = symPatternEntry.getValue().matcher(matcher.group());
                        if (sym_matcher.find()) {
                            return new LexicalUnit(symPatternEntry.getKey(), null);
                        }
                    }
                } else if (patternEntry.getKey().equals("DOUBLEVAL")) {
                    return new LexicalUnit(LexicalType.DOUBLEVAL, new ValueImpl(Double.parseDouble(matcher.group())));
                } else if (patternEntry.getKey().equals("INTVAL")) {
                    return new LexicalUnit(LexicalType.INTVAL, new ValueImpl(Integer.parseInt(matcher.group())));
                } else if (patternEntry.getKey().equals("LITERAL")) {
                    return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(matcher.group().replaceAll("\"", "")));
                } else if (patternEntry.getKey().equals("NL")) {
                    return new LexicalUnit(LexicalType.NL, null);
                }
            }
        }

            // TODO: 切り出したやつの表示　あとで消す
            return null;
        }

        @Override
        public boolean expect (LexicalType type) throws Exception {
            return false;
        }

        @Override
        public void unget (LexicalUnit token) throws Exception {

        }

    }
