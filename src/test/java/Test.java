import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.mbedsys.jvar.Variant;


public class Test {
    public static void main(String[] args) {
		try {
			Variant value = Variant.newParser(new FileInputStream("rs/test.json"), Variant.Format.JSON).next();
//			Variant value = Variant.newParser(new FileInputStream("rs/test.bson"), Variant.Format.BSON).next();
//			Variant value = Variant.newParser(new FileInputStream("rs/test.bcon"), Variant.Format.BCON).next();
			Variant.serializeBCON(new FileOutputStream("rs/test_out.bcon"), value);
			Variant.serializeBSON(new FileOutputStream("rs/test_out.bson"), value);
			System.out.println(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
