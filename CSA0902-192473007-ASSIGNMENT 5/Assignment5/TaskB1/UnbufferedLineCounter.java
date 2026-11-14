import java.io.*;
import java.nio.file.*;

public class UnbufferedLineCounter {
    public static long count(Path path) throws IOException {
        long lines=0; boolean hasChars=false,lastNewline=true;
        try(InputStream in=Files.newInputStream(path)){
            int b; while((b=in.read())!=-1){hasChars=true;if(b=='\\n')lines++;lastNewline=b=='\\n';}
        }
        return lines+(hasChars&&!lastNewline?1:0);
    }
    public static void main(String[] a)throws Exception{
        if(a.length!=1){System.out.println("Usage: java UnbufferedLineCounter <file>");return;}
        long t=System.nanoTime();System.out.println("Lines: "+count(Path.of(a[0])));
        System.out.println("Elapsed ms: "+((System.nanoTime()-t)/1_000_000.0));
    }
}
