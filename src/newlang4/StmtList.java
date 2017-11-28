package newlang4;

import java.util.ArrayList;
import java.util.List;

public class StmtList extends Node {
    Environment env;
    List<Node> stmt_list;

    private StmtList(Environment env){
        this.env = env;
        stmt_list = new ArrayList<Node>();
    }

    public static Node isMatch(Environment env, LexicalUnit first){
        return (Node)new StmtList(env);
    }

    @Override
    public boolean Parse() throws Exception {
        while(true) {
            LexicalUnit first = env.getInput().get();
            env.getInput().unget(first);
            //stmtなら
            Node child = Stmt.isMatch(env, first);
            if (child != null) {
                switch (first.getType()) {
                    case END:
                    case EOF:
                        return true;
                    case NL:
                        continue;
                    default:
                        break;
                }
                if(child.Parse()){
                    stmt_list.add(child);
                    continue;
                }else{
                    return false;
                }
            } else {
                //stmtじゃないならblockだろう
                child = Block.isMatch(env, first);
                if (child == null) {
                    return false;
                }
                if(child.Parse()){
                    stmt_list.add(child);
                    continue;
                }else{
                    return true;
                }
            }
        }
        return false;
    }

}
