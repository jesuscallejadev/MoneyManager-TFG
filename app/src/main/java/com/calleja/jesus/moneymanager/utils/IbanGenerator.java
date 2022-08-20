package com.calleja.jesus.moneymanager.utils;

    import java.math.BigInteger;

   public class IbanGenerator{

        /**
         * Constructor for generator
         */
        public IbanGenerator(){
        }

        /**
         * This function takes 2 params and generates a valid IBAN representation.
         * A random number is used to generate this IBAN.
         *
         * The function will return NULL when the lengths of countryCode or
         * bankCode are not 2 or 4 respectively.
         *
         * @param countryCode A countryCode with a prefix of length 2
         * @param bankCode A bankCode with a prefix of lenght 4
         * @return A valid IBAN representation as a String
         */
        public static String generateIban(String countryCode, String bankCode) {
            if(countryCode.length() != 2){
                System.out.println("Failed to generate IBAN with invalid country prefix length (should be 2, got " + countryCode.length() + ")");
                return null;
            }
            if(bankCode.length() != 4){
                System.out.println("Failed to generate IBAN with invalid bankCode prefix length (should be 4, got " + countryCode.length() + ")");
                return null;
            }
            countryCode = countryCode.toUpperCase();
            bankCode = bankCode.toUpperCase();
            String randomBankPassNumber = String.valueOf(Math.round(100000 + Math.random() * 100000));
            String countryPrefixControlVersion = ibanUpRot(countryCode);
            String bankCodeControlVersion = ibanUpRot(bankCode);

            // BigInteger is used to store a number with the lenght of 20, because you cant store this is normal variable.
            String controlNumber = String.valueOf(new BigInteger("98").subtract(new BigInteger(bankCodeControlVersion + randomBankPassNumber + countryPrefixControlVersion + "00")).mod(new BigInteger("97")));

            if(controlNumber.length() < 2){
                controlNumber = "0" + controlNumber;
            }
            return countryCode + controlNumber + bankCode + randomBankPassNumber;
        }

        /**
         * According to the IBAN specification
         * a special rotational algorithm is used
         * to generate numerical values specific
         * to a give string (str).
         *
         * This algorithm is used for generating
         * and validating iban numbers.
         *
         * This functions takes a String as param and
         * returns a String with the numerical value
         * of the chars in the string
         *
         * @param str input string which is either the bankcode or the countrycode
         * @return String numerical value of the input
         */
        private static String ibanUpRot(String str){
            String ret = "";
            for(char c : str.toCharArray()){
                ret += c - 64 + 9;
            }
            return ret;
        }
}
