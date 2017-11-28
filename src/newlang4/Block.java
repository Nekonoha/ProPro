package newlang4;

public class Block extends Node{
    public static Node isMatch(Environment env, LexicalUnit first){
        Node ret = DoBlock.isMatch(env, first);
        if(ret != null) return ret;

        ret = IfBlock.isMatch(env, first);
        if(ret != null) return ret;

        ret = WhileBlock.isMatch(env, first);
        if(ret != null) return ret;
        return null;
    }

}
