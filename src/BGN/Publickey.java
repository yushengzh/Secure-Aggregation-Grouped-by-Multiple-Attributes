package BGN;

import java.math.BigInteger;
import it.unisa.dia.gas.jpbc.*;


public class Publickey {
	
		private BigInteger n;
		private Field<Element> Field_G, Field_GT;
		private Pairing pairing;
		private Element g, h;
		
		public Publickey(BigInteger n, Field<Element> G,Field<Element> GT, Pairing pairing, Element g,Element h) {
			this.n = n;
			this.Field_G = G;
			this.Field_GT = GT;
			this.pairing = pairing;
			this.g = g;
			this.h = h;
		}
		
		public Element getG() {
			return g;
		}
		public Element getH() {
			return h;
		}
		public BigInteger getN() {
			return n;
		}
		public Pairing getPairing() {
			return pairing;
		}
		public Field<Element> getField_G() {
			return Field_G;
		}
		public Field<Element> getField_GT() {
			return Field_GT;
		}
}
