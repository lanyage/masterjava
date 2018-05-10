package Register;

import java.util.HashMap;
import java.util.Map;

public enum Spicy {
    HOT("hot"),MIDDLE("middle"),NORMAL("normal");
    private final String detail;
    private static final Map<String, Spicy> spicyLookUp = new HashMap<>();
    Spicy(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
    public static Spicy forDetail(String detail) {
        if(detail == null || detail.equals("") || detail.trim().equals(""))
            return NORMAL;
        return spicyLookUp.get(detail);
    }

    static {
        Spicy[] arr$ = values();
        int len$ = arr$.length;
        for (int i = 0; i < len$; i++) {
            spicyLookUp.put(arr$[i].getDetail(),arr$[i]);
        }
    }
}

