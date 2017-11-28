package newlang4;

public class DoBlock extends Node {
    Environment env;

    public DoBlock(Environment env) {
        this.env = env;
    }

    public static Node isMatch(Environment env, LexicalUnit first) {
        if (first.getType() != LexicalType.DO) return null;
        return (Node) new DoBlock(env);
    }

    @Override
    public boolean Parse() throws Exception {
        LexicalUnit lu = env.getInput().get();
        //最初に来るのは絶対<DO>なので
        if (lu.getType() != LexicalType.DO) return false;
        //<DO>の次をとる
        lu = env.getInput().get();
        switch (lu.getType()) {
            case WHILE:
            case UNTIL:
                return parseWHILE();
            case NL:
                return parseNL();
        }
    }

    private boolean parseNL() throws Exception {
        LexicalUnit lu = env.getInput().get();
        Node body = StmtList.isMatch(env, lu);
        if (body == null) return false;
        return body.Parse();
    }

    private boolean parseWHILE() throws Exception {
        LexicalUnit lu = env.getInput().get();
        Node body = Cond.isMatch(env, lu);
        if (body == null) return false;
        return body.Parse();
    }
}
