import java.io.FileInputStream;
import java.io.IOException;

import org.mbedsys.jvar.Variant;


public class Test {
    public static void main(String[] args) {
		try {
//			Variant value = Variant.newParser(new FileInputStream("rs/test.json"), Variant.Format.JSON).next();
			Variant value = Variant.newParser(new FileInputStream("rs/test.bson"), Variant.Format.BSON).next();
//			Variant value = Variant.newParser(new FileInputStream("rs/test.bcon"), Variant.Format.BCON).next();
			System.out.println(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
