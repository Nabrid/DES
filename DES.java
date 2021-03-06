public class DESalgo {
	
	//Creating 16 subkeys, each of which is 48-bits long
	
	/**Permuted Choice-1(PC-1)
	 * The 64-bit key is permuted according to the
	 * permutation PC-1. Note only 56 bits of the
	 * original key appear in the permuted key. 
	 */
	
	private static final byte[] PC1 = {
		57, 49, 41, 33, 25, 17, 9,
        1,  58, 50, 42, 34, 26, 18,
        10, 2,  59, 51, 43, 35, 27,
        19, 11, 3,  60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7,  62, 54, 46, 38, 30, 22,
        14, 6,  61, 53, 45, 37, 29,
        21, 13, 5,  28, 20, 12, 4
	};
	
	/**Permuted Choice-2(PC-2)
	 * 56 bit keys generated from PC-1 is further reduced to
	 * 16 48-bit subkeys. 
	 */
	private static final byte[] PC2 = {
		14, 17, 11, 24, 1,  5,
        3,  28, 15, 6,  21, 10,
        23, 19, 12, 4,  26, 8,
        16, 7,  27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
	};
	
	/**Subkey Rotations
	 * Defines the number of left shifts either by 1 or 2
	 */

	private static final byte[] rotations =  {
    		1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
	};
	
	/**Initial Permutation(IP) reearranges the bits and 
	 * Shows the new arrangement of the bits from 
	 * their initial order
	 */
	
	private static final byte[] IP = { 
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9,  1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };
	
	/**Final Permutation(FP) 
	 * The order of the two blocks L16 and R16 obtained 
	 * at the end of the 16th round is reversed into the 
	 * 64 bit block.
	 */
	
	private static final byte[] FP = {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    };
	
	/**E bit selection table
	 * E(Rn-1) has a 32 bit input block, and a 48 bit
	 * "expanded" output block.  
	 */

	private static final byte[] E = {
        32, 1,  2,  3,  4,  5,
        4,  5,  6,  7,  8,  9,
        8,  9,  10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };
	
	/**Sustitution Boxes(S Boxes)
	 * 48 bits, or eight groups of six bits are used as
	 * addresses in tables called "S boxes". Each group 
	 * of six bits will give an address in a different
	 * S box. Located at that address will be a 4 bit 
	 * number. This 4 bit number will replace the 
	 * original 6 bits. The net result is that the eight
	 * groups of 6 bits are transformed into eight groups
	 * of 4 bits (the 4-bit outputs from the S boxes) for
	 * 32 bits total.
	 */
	
	private static final byte[][] SBox = { 
		//S1
		{ 14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,  
        0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
        4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
        15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13 },
        //S2
        { 15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,	
        3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
        0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
        13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9 },
        //S3
    	{ 10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,	
        13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
        13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
        1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12 },
        //S4
        { 7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,	
        13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
        10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
        3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14 },
        //S5
        { 2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,	
        14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
        4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
        11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3 },
        //S6
        { 12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,	
        10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
        9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
        4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13 },
        //S7
        { 4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,	
        13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
        1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
        6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12 },
        //S8
        { 13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,	
        1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
        7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
        2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11 }
	};
	
	/**P Permutation
	 * The final stage in the calculation of f is to 
	 * do a permutation P of the S-box output to obtain
	 * the final value of f. 
	 * f = P(S1(B1)S2(B2)...S8(B8)) 
	 * P yields a 32-bit output from a 32-bit input 
	 * by permuting the bits of the input block.  
	 */
	
	private static final byte[] P = {
        16, 7,  20, 21,
        29, 12, 28, 17,
        1,  15, 23, 26,
        5,  18, 31, 10,
        2,  8,  24, 14,
        32, 27, 3,  9,
        19, 13, 30, 6,
        22, 11, 4,  25
    };
    
  	public static byte[] msgInBin = new byte[64];
	public static byte[] keyInBin = new byte[64];
	public static byte[] alias = new byte[64];
	public static byte[] keyPC1 = new byte[56];
	public static byte[] msgIP = new byte[64];
	public static byte[][][] subKey = new byte[2][17][28]; 
	public static byte[][] keysCD = new byte[16][56];
	public static byte[][] keyPC2 = new byte[16][48];
	public static byte[] msgE = new byte[48];
	public static byte count=0;
		
	//Reduce the 64 bit key to 56 bit
	private static void permuteKey64(byte[] PC1, byte[] keyInBin) {
		for(int i = 0; i < 56; i++) {
			byte temp = PC1[i];
			keyPC1[i] = keyInBin[temp-1];
		}
	}
	
	//Reshuffle the bits in data block using Initial Permutation
	private static void permuteMsg64(byte[] IP, byte[] msgInBin) {
		for(int i = 0; i < 64; i++) {
			byte temp = IP[i];
			msgIP[i] = msgInBin[temp-1];
		}
	}
	
	//Creates an array for the binary values
	private static void assignValues(String bin) {
		int i = 0;
		char ch;
		String str;
		
		while(i < bin.length()) {
			ch = bin.charAt(i++);
			str = Character.toString(ch);
			byte dec = Byte.parseByte(str);
			alias[count++] = dec;
		} 
	}
	
	//Converts the hex characters to corresponding binary digits
	private static void toBinary(String hex) {
		int i = 0;
		char ch;
		String str;
		hex = hex.toLowerCase();
		
		while(i<hex.length()) {
			ch = hex.charAt(i++);
			str = Character.toString(ch);
			int decimal = Integer.parseInt(str,16);
			String binary = Integer.toBinaryString(0x10 | decimal).substring(1);
			assignValues(binary);
		}	
	}
	
	//Checks if the string consists ONLY of valid hex characters
	 
	private static boolean isHex(String hex) {
		int i = 0;
		char test;
		
		while(i < hex.length()){
			test=hex.charAt(i++);
			if(!((test >= '0' && test <= '9') || (test >= 'a' && test <= 'f') 
					||(test >= 'A'&& test <= 'F')))
				return false;
		}
		return true;
	} 
	
	//Gets an input from the user
	 
	private static String getInput(String a) {
		String b;
		boolean result;
		
		Scanner test = new Scanner(System.in);
		System.out.print("Enter the " + a + " (16 Hex digits)\t");
		b  = test.next();
		result = isHex(b);
		
		while(result == false || b.length()!=16) {
			System.out.print("Invalid !!! Re-enter the " + a + "\t");
			b  = test.next();
			result = isHex(b);			
		}
		return b;
	}
		
	//Rotations of the keys
	private static void rotKeys() {
		
		for(int i = 0; i < 2; i++) {
			for(int j = 1; j < 17; j++) {
				byte temp = 0, temp1 = 0, temp2 = 0;
				for(int k = 0; k < 27; k++) {
					if(rotations[j-1] == 1){
						if(k == 0) {
							temp = subKey[i][j-1][0];
						}
						subKey[i][j][k] = subKey[i][j-1][k+1];
						if(k == 26) {
							subKey[i][j][27] = temp;
						}
					}
					else{
						if(k == 0) {
							temp1 = subKey[i][j-1][0];
							temp2 = subKey[i][j-1][1];
						}
						if(k != 26)
							subKey[i][j][k] = subKey[i][j-1][k+2];
						else {
							subKey[i][j][26] = temp1;
							subKey[i][j][27] = temp2;
						}							
					}
				}
			}
		}
	}
	
	//Reduce the keys to 48 bits
	public static void permuteKey48() {
		for(int i = 0; i < 16; i++)
			for(int j = 0; j < 48; j++) {
				byte temp = PC2[j];
				keyPC2[i][j] = keysCD[i][temp-1];
			}
	}
	
	//S-boxes
	public static void S(int cnt, byte[] smp) {
		int row, cnt1 = 3;
		int col = 0;
		int x = smp[0]; 
		int y = smp[5];
		
		if(x == 0 && y == 0)
			row = 0;
		else if(x == 0 && y == 1)
			row = 1;
		else if(x == 1 && y == 0)
			row = 2;
		else 
			row = 3;
		
		for(int i = 1; i < 5; i++) {
			col = (int) (col + smp[i] * Math.pow(2,cnt1--));
		}
		
		for(int i = 0; i < 6; i++) {
			System.out.print(smp[i]);
		}
		System.out.print("\t" + row + "\t" + col + "\t");
		
		int temp = row * 16 + col;
		//System.out.print(temp + " ");
		int temp1;
		temp1 = SBox[cnt][temp]; 
		System.out.print(temp1 + "\t");
		
		String temp2 = Integer.toBinaryString(0x10 | temp1).substring(1);
		System.out.print(temp2);
		System.out.println();
		
		String var;
		char foo;
		while(++cnt1 < 4) {
			foo = temp2.charAt(cnt1);
			var = Character.toString(foo);
			byte dec = Byte.parseByte(var);
			msgIP[count+32] = dec;
			count++;
		}
	}
	
	//Permutation P table
	public static void permuteP() {
		for(int i = 0; i < 32; i++) {
			byte temp = P[i];
			alias[i+32] = msgIP[temp-1+32];	//Storing the permuted value temporarily in alias[]
			if(i % 4 == 0)
				System.out.print(" ");
			System.out.print(alias[i+32]);
		}
		System.arraycopy(alias, 32, msgIP, 32, 32);
	}
	
	//E bit selection table
	public static void selectE32(int inc) {
		//System.out.print("E ");
		for(int i = 0; i < 48; i++) {
			byte temp = E[i];
			msgE[i] = msgIP[temp-1+32];
			//System.out.print(msgE[i]);
		}
		xorMsgIP(inc);
	}
	
	//XOR key and bits obtained from E bit selection table
	public static void xorMsgIP(int inc) {
		byte[] samp = new byte[6];
		
		System.arraycopy(msgIP, 32, alias, 0, 32);
		
		//System.out.println();
		System.out.print("L[" + inc + "]\t");
		for(int i = 0; i < 32; i++) {
			if(i % 4 == 0)
				System.out.print(" ");
			System.out.print(msgIP[i]);
		}
		
		System.out.println();
		System.out.print("R[" + inc + "]\t");
		for(int i = 32; i < 64; i++) {
			if(i % 4 == 0)
				System.out.print(" ");
			System.out.print(msgIP[i]);
		}
		
		System.out.println();
		System.out.println();
		System.out.println("Round " + (inc+1));
		
		System.out.print("E\t");
		for(int i = 0; i < 48; i++) {
			if(i % 6 == 0)
				System.out.print(" ");
			System.out.print(msgE[i]);
		}
		
		System.out.println();
		System.out.print("K[" + (inc+1) + "]\t");
		for(int i = 0; i < 48; i++) {
			if(i % 6 == 0)
				System.out.print(" ");
			System.out.print(keyPC2[inc][i]);
		}
			
		System.out.println();
			count = 0;
			int cnt = 0;
			System.out.print("XOR\t");
			for(int j = 0; j < 48; j++){
				if((msgE[j] == 0 && keyPC2[inc][j] == 0) || (msgE[j] == 1 && keyPC2[inc][j] == 1))
					msgE[j] = 0;
				else 
					msgE[j] = 1;
				if(j % 6 == 0)
					System.out.print(" ");
				System.out.print(msgE[j]);
			}
			
			System.out.println();
			System.out.println();
			System.out.println("Sbox");
			while(cnt < 8) {
				System.arraycopy(msgE, cnt * 6, samp, 0, 6);
				S(cnt, samp);
				cnt++;
			}
			
			System.out.println();
			System.out.print("P\t");
			permuteP();
			for(int i = 0; i < 32; i++){
				if((msgIP[i] == 0 && msgIP[i+32] == 0) || (msgIP[i] == 1 && msgIP[i+32] == 1))
					msgIP[i+32] = 0;
				else 
					msgIP[i+32] = 1;
			}
			System.arraycopy(alias, 0, msgIP, 0, 32);
	}
	
	public static void permuteFP() {
		for(int i = 0; i < 64; i++) {
			byte temp = FP[i];
			msgInBin[i] = msgIP[temp-1];
			//System.out.print(msgInBin[i]);
		}
	}
	public static void main(String[] args) {
		
		System.out.println("DES Algorithm Implementation\n");
		
		String msg, key;
		msg = getInput("message");
		key = getInput("key");
		
		toBinary(msg);
		System.arraycopy(alias, 0, msgInBin, 0, alias.length);
		
		count = 0;
		toBinary(key);
		System.arraycopy(alias, 0, keyInBin, 0, alias.length);
		
		permuteKey64(PC1, keyInBin);
		permuteMsg64(IP, msgInBin);
		
		System.out.println();
		System.out.print("Input Bits : ");
		for(int i = 0; i < 64; i++) {
			if(i % 8 == 0)
				System.out.print(" ");
    		System.out.print(msgInBin[i]);
    	}
		
		System.out.println();
		System.out.print("Key Bits : ");
		for(int i = 0; i < 64; i++) {
			if(i % 8 == 0)
				System.out.print(" ");
    		System.out.print(keyInBin[i]);
    	}
		
		System.out.println();
		for(int i = 0; i < 28; i++) {
			subKey[0][0][i] = keyPC1[i];
			subKey[1][0][i] = keyPC1[i+28];
		}

		System.out.println();
		rotKeys();
		for(int j = 0; j < 17; j++) {
			System.out.print("CD[" + j + "]\t");
			for(int i = 0; i < 2; i++) {
				for(int k = 0; k < 28; k++) {
					if(k % 7 == 0)
						System.out.print(" ");
					System.out.print(subKey[i][j][k]);
				}
			}
			System.out.println();
		}
		
		for(int i = 0; i < 16; i++) {
			for(int k = 0; k < 56; k++) {
				int x;
				x = k % 28;
				if(k < 28)
					keysCD[i][k] = subKey[0][i+1][k];
				else 
					keysCD[i][k] = subKey[1][i+1][x];
			}
		}
		
		System.out.println();
		permuteKey48();
		for(int j = 0; j < 16; j++) {
			System.out.print("K" + (j+1) + "\t");
			for(int k = 0; k < 48; k++) {
				if(k % 6 == 0)
					System.out.print(" ");
				System.out.print(keyPC2[j][k]);
			}
			System.out.println();
		}
		
		System.out.println();
		//count = 0;
		for(int i = 0; i < 16; i++){
			selectE32(i);
			System.out.println();
			//xorMsgIP();
		}
		System.out.println();
		
    	System.arraycopy(msgIP, 32, msgIP, 0, 32);
    	System.arraycopy(alias, 0, msgIP, 32, 32);
    	
    	System.out.print("LR[16]\t");
    	for(int i = 0; i < 64; i++) {
    		if(i % 8 == 0)
    			System.out.print(" ");
    		System.out.print(msgIP[i]);
    	}
		
    	permuteFP();
    	System.out.println();
    	System.out.print("Output\t");
    	for(int i = 0; i < 64; i++) {
    		if(i % 8 == 0)
    			System.out.print(" ");
    		System.out.print(msgInBin[i]);
    	}	
		
	} //end of main
}
