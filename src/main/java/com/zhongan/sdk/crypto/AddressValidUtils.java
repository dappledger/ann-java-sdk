package com.zhongan.sdk.crypto;

import com.zhongan.sdk.utils.utils.Numeric;
import static com.zhongan.sdk.crypto.Keys.ADDRESS_LENGTH_IN_HEX;

/**
 * address valid utils
 */
public class AddressValidUtils {

    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == ADDRESS_LENGTH_IN_HEX;
    }
}
