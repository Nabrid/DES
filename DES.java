//DES in JAVA

public class DESalgo {
	
	public static void main(String[] args) {
		
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
        
		
	} //end of main
	
}//end of class DESalgo
