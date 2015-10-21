/**
 * Contains a key - code pair for the Dictionary class. The key is of type
 * string and the code is of type int.
 *
 * @author Robert Meagher   2502749364
 * @since October 6, 2015
 */


public class DictEntry {
    private String key; // The key for this entry
    private int code;   // The code for this entry

    /**
     * Creates a DictEntry with the specified key and code
     * @param key The key for the entry
     * @param code The code for the entry
     */
    public DictEntry(String key, int code) {
        this.key = key;
        this.code = code;
    }

    /**
     *
     * @return The entry's key
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @return The entry's code
     */
    public int getCode() {
        return code;
    }
}
