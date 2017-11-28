package newlang4;

public class Program extends Node{
    Environment env;

    public Program(Environment env) {
        this.env = env;
    }

    public static Node isMatch(Environment env, LexicalUnit first){
        return (Node)new Program(env);
    }

    @Override
    public boolean Parse() throws Exception {
        LexicalUnit first = env.getInput().get();
        Node child = StmtList.isMatch(env, first);
        return child.Parse();
    }
}
