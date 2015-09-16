I'm always frustrated when I don't have a quick command line parser handy -- Argh! I probably haven't looked hard 
enough, so here's a stupid simple command line argument parsing library for Java.  For a more robust solution, take 
a look at [commons-cli](https://commons.apache.org/proper/commons-cli/) or [argsparse4j](https://github.com/tatsuhiro-t/argparse4j).
This library does _NOT_:
    
+ Support required arguments
+ Have a fancy help message
+ Let you use multiple aliases for a single argument (like `-v` being the same as `--verbose`)
+ Do most of the things the aforementioned libraries do

Just specify an argument with some dashes in front, and then the value of the argument right after, like so


    java -jar Test.jar --api-key testing123 --debug --threads 16
    
And then grab it like

    public static void main(String[] args) {
        Arghs arghs = new Arghs(args);
        
        String arg1 = arghs.getString("api-key");
        boolean debug = arghs.getBooleanOrDefault("debug", false);
        int threads = arghs.getInt("threads");
    
        ...
    }

Hopefully this works for someone else!