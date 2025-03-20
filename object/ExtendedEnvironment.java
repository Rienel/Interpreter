package object;

public class ExtendedEnvironment {

    public static Environment newEnclosedEnvironment(Environment outer){
        Environment env = new Environment();
        env.setOuter(outer);
        return env;
        
    }
}
